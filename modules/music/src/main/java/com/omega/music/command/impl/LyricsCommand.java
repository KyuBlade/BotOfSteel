package com.omega.music.command.impl;

import com.omega.core.command.*;
import com.omega.core.util.MessageWrapper;
import com.omega.music.MusicPermissionSupplier;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MessageBuilder;

import java.awt.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@CommandInfo(name = "lyrics")
public class LyricsCommand extends AbstractCommand {

    private static final Color EMBED_COLOR = new Color(0, 153, 255);

    public LyricsCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = MusicPermissionSupplier.COMMAND_LYRICS)
    @Signature(help = "Find the lyrics of a song")
    public void lyricsCommand(@Parameter(name = "keywords") String keywords) throws IOException {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder
            .withDescription("Searching for lyrics, please wait ...")
            .appendField("Keywords", keywords, true);

        sendStateMessage(embedBuilder.build(), Color.BLUE);

        SearchResult searchResult = searchLyrics(keywords);
        if (searchResult == null) {
            sendErrorMessage("Nothing found for song " + keywords);
        } else if (searchResult instanceof SongLyricsWithHint) {
            SongLyricsWithHint lyricsWithHint = (SongLyricsWithHint) searchResult;
            MessageWrapper messageWrapper = sendLyrics(lyricsWithHint.songLyrics);

            while (!messageWrapper.isSent()) {
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException ignored) {
                }
            }

            stateMessage = null;
            sendHints(lyricsWithHint.hints, false);
        } else if (searchResult instanceof HintResult) {
            sendHints((HintResult) searchResult, true);
        } else {
            sendLyrics((SongLyrics) searchResult);
        }
    }

    private SearchResult searchLyrics(String songName) throws IOException {
        String baseUrl = "http://search.azlyrics.com/search.php?q=";
        String finalUrl = baseUrl + URLEncoder.encode(songName, "UTF-8");

        Document searchListSource = Jsoup.connect(finalUrl).get();

        // Search song result panel
        Elements panels = searchListSource.select(".panel");
        Element songPanel = panels.select("div.panel:contains(Song results)").first();
        Element artistPanel = panels.select("div.panel:contains(Artist results)").first();

        if (songPanel == null) { // Get artist hints
            if (artistPanel != null) {
                return new HintResult(null, getArtistHints(artistPanel));
            } else {
                return null;
            }
        } else { // Get song lyrics entries
            Elements entries = songPanel.select("td[class=text-left visitedlyr]");

            List<Element> finalEntryList;
            int entrySize = entries.size();

            if (entrySize < 1) { // Nothing to filter
                finalEntryList = new ArrayList<>(0);
            } else if (entrySize == 1) { // No need to filter
                finalEntryList = new ArrayList<>(1);
                finalEntryList.add(entries.first());
            } else { // Filter covers
                finalEntryList = entries.stream().filter(entry -> {
                    Element child = entry.select(":nth-child(1)").first();
                    boolean cover = child.text().contains("originally by");

                    return !cover;
                }).collect(Collectors.toList());
            }

            SongHint songHint = null;
            ArtistHint artistHint = null;
            int finalEntrySize = finalEntryList.size();

            if (finalEntrySize > 1) {
                List<String> songHints = new ArrayList<>(finalEntrySize);
                finalEntryList.forEach(entry -> {
                    Element linkElement = entry.select("a").first();
                    String title = linkElement.text();
                    String author = entry.select("b").get(1).text();

                    songHints.add(author + " - " + title);
                });

                songHint = new SongHint(songHints);
                artistHint = getArtistHints(artistPanel);
            }

            SongLyrics songLyrics = null;
            if (finalEntrySize > 0) {
                Element firstEntry = finalEntryList.get(0);
                Element linkElement = firstEntry.select("a").first();

                String link = linkElement.attr("href");
                String title = linkElement.text();
                String author = firstEntry.select("b").get(1).text();

                // Get the lyrics from the entry link
                Document songLyricsSource = Jsoup.connect(link).get();
                Elements lyricsBody = songLyricsSource.select("div[class=col-xs-12 col-lg-8 text-center] div:nth-of-type(5)");

                String lyrics = lyricsBody.html();
                lyrics = Jsoup.clean(lyrics, "", Whitelist.none(),
                    new Document.OutputSettings().prettyPrint(false));

                songLyrics = new SongLyrics(author, title, lyrics, link);
            }

            if (songHint == null) {
                return songLyrics;
            } else {
                return new SongLyricsWithHint(songLyrics, songHint, artistHint);
            }
        }
    }

    private ArtistHint getArtistHints(Element artistPanel) {
        if (artistPanel == null) {
            return null;
        }

        Elements entries = artistPanel.select("td[class=text-left visitedlyr]");
        List<String> artistHints = new ArrayList<>(entries.size());
        entries.forEach(entry -> {
            Element artistElement = entry.select("b").first();
            if (artistElement != null) {
                artistHints.add(artistElement.text());
            }
        });

        return new ArtistHint(artistHints);
    }

    private MessageWrapper sendHints(HintResult hints, boolean notFound) {
        List<String> songHints = (hints.songHints != null) ? hints.songHints.hints : null;
        List<String> artistHints = (hints.artistHints != null) ? hints.artistHints.hints : null;

        StringBuilder sBuilder = new StringBuilder();
        String description;
        if (notFound) {
            description = "Lyrics not found, look below for hints on what you possibly wanted to search.";
        } else {
            description = "Other possibilities listed below.";
        }
        sBuilder.append(description).append("\n\n");

        // Print song hints
        if (songHints != null) {
            int hintSize = songHints.size();
            sBuilder.append(MessageBuilder.Styles.BOLD.getMarkdown())
                .append("Songs : ").append("\n\n")
                .append(MessageBuilder.Styles.BOLD.getReverseMarkdown());
            IntStream.range(0, hintSize).forEach(i -> {
                sBuilder.append(songHints.get(i));

                if (i + 1 < hintSize) {
                    sBuilder.append('\n');
                }
            });
        }

        // Print artist hints
        if (artistHints != null) {
            if (songHints != null) {
                sBuilder.append("\n\n");
            }

            int hintSize = artistHints.size();
            sBuilder.append(MessageBuilder.Styles.BOLD.getMarkdown())
                .append("Artists : ").append("\n\n")
                .append(MessageBuilder.Styles.BOLD.getReverseMarkdown());
            IntStream.range(0, hintSize).forEach(i -> {
                sBuilder.append(artistHints.get(i));

                if (i + 1 < hintSize) {
                    sBuilder.append('\n');
                }
            });
        }

        return sendStateMessage(sBuilder.toString());
    }

    private MessageWrapper sendLyrics(SongLyrics songLyrics) {
        String lyrics = songLyrics.lyrics;
        if (lyrics.length() <= EmbedBuilder.DESCRIPTION_CONTENT_LIMIT) {
            EmbedBuilder builder = new EmbedBuilder();
            builder
                .withColor(EMBED_COLOR)
                .withTitle(songLyrics.title)
                .withUrl(songLyrics.url)
                .withAuthorName(songLyrics.artist)
                .withAuthorUrl("")
                .withDescription(lyrics);
            return sendStateMessage(builder.build());
        } else {
            return sendStateMessage("Lyrics were too long, here the link instead : <" + songLyrics.url + '>');
        }
    }

    class SearchResult {
    }

    class SongLyrics extends SearchResult {

        String artist;
        String title;
        String lyrics;
        String url;

        private SongLyrics(String artist, String title, String lyrics, String url) {
            this.artist = artist;
            this.title = title;
            this.lyrics = lyrics;
            this.url = url;
        }
    }

    private class SongLyricsWithHint extends SearchResult {

        private SongLyrics songLyrics;
        private HintResult hints;

        private SongLyricsWithHint(SongLyrics songLyrics, SongHint songHint, ArtistHint artistHint) {
            this.songLyrics = songLyrics;
            this.hints = new HintResult(songHint, artistHint);
        }
    }

    class Hint {
        List<String> hints;

        private Hint(List<String> hints) {
            this.hints = hints;
        }
    }

    private class SongHint extends Hint {

        private SongHint(List<String> songs) {
            super(songs);
        }
    }

    private class ArtistHint extends Hint {

        private ArtistHint(List<String> hints) {
            super(hints);
        }
    }

    class HintResult extends SearchResult {

        SongHint songHints;
        ArtistHint artistHints;

        private HintResult(SongHint songHint, ArtistHint artistHint) {
            this.songHints = songHint;
            this.artistHints = artistHint;
        }
    }
}
