package com.omega.command.impl;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.omega.BotManager;
import com.omega.MusicModule;
import com.omega.MusicPermissionSupplier;
import com.omega.audio.GuildAudioPlayer;
import com.omega.audio.callback.SearchAudioLoadCallback;
import com.omega.command.*;
import com.omega.guild.GuildContext;
import com.omega.guild.GuildManager;
import com.omega.property.MusicBotPropertySupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.impl.obj.Embed;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.io.IOException;
import java.util.List;

@Command(name = "search")
public class SearchCommand extends AbstractCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchCommand.class);

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final YouTube YOUTUBE = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, request -> {
    }).setApplicationName("discord-bot").build();

    public SearchCommand(IUser by, IMessage message) {
        super(by, message);
    }

    @Permission(permission = MusicPermissionSupplier.COMMAND_SEARCH)
    @Signature(help = "Search on youtube for the given keywords and add to queue the first result")
    public void searchCommand(@Parameter(name = "keywords") String keywords) {
        String apiKey = BotManager.getInstance().getBotProperties()
            .getProperty(MusicBotPropertySupplier.YT_API_KEY, String.class);

        if (apiKey == null) {
            sendErrorMessage("No Youtube Data API key set");
        } else {
            try {
                sendStateMessage(
                    new EmbedBuilder()
                        .withTitle("YouTube search")
                        .withDescription("Searching, please wait ...")
                        .build(),
                    Color.BLUE
                );

                YouTube.Search.List search = YOUTUBE.search().list("id,snippet");
                search.setKey(apiKey);
                search.setQ(keywords);
                search.setType("video");
                search.setFields("items(id/videoId)");
                search.setMaxResults(1L);

                SearchListResponse searchResponse = search.execute();
                List<SearchResult> searchResultList = searchResponse.getItems();

                if (searchResultList != null && !searchResultList.isEmpty()) {
                    SearchResult searchResult = searchResultList.get(0);
                    String videoId = searchResult.getId().getVideoId();

                    addToQueue(videoId, keywords);
                } else {
                    EmbedBuilder embedBuilder = new EmbedBuilder();

                    embedBuilder
                        .withTitle("YouTube search")
                        .withDescription("Nothing found")
                        .appendField("Keywords", keywords, true);

                    sendStateMessage(embedBuilder.build());
                }
            } catch (GoogleJsonResponseException e) {
                LOGGER.warn("Youtube service error", e);

                sendExceptionMessage("YouTube search", "Youtube service error : " + e.getDetails().getCode() +
                        " : " + e.getDetails().getMessage(), e,
                    new Embed.EmbedField("Keywords", keywords, true)
                );
            } catch (IOException e) {
                LOGGER.warn("Error occurred", e);

                sendExceptionMessage("YouTube search", "Error occurred while searching on youtube", e,
                    new Embed.EmbedField("Keywords", keywords, true)
                );
            }
        }
    }

    private void addToQueue(String identifier, String keywords) {
        GuildContext guildContext = GuildManager.getInstance().getContext(message.getGuild());
        GuildAudioPlayer audioPlayer = (GuildAudioPlayer) guildContext.getModuleComponent(MusicModule.AUDIO_PLAYER_COMPONENT);

        audioPlayer.queue(identifier, false, new SearchAudioLoadCallback(this, keywords));
    }
}
