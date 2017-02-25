import com.omega.util.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

    @Test
    public void stringUtilTest() {
        String normalString = "test";
        String normalLongString = "11";
        String negativeLongString = "-11";
        String positiveLongString = "+11";
        String normalDoubleString = "15.345";
        String negativeDoubleString = "-15.345";
        String positiveDoubleString = "+15.345";

        Assert.assertEquals("Should return string " + normalString, "test", StringUtils.parse(normalString));
        Assert.assertEquals("Should return long " + normalLongString, 11L, StringUtils.parse(normalLongString));
        Assert.assertEquals("Should return long " + negativeLongString, -11L, StringUtils.parse(negativeLongString));
        Assert.assertEquals("Should return long " + positiveLongString, 11L, StringUtils.parse(positiveLongString));
        Assert.assertEquals("Should return double " + normalDoubleString, 15.345d, StringUtils.parse(normalDoubleString));
        Assert.assertEquals("Should return double " + negativeDoubleString, -15.345d, StringUtils.parse(negativeDoubleString));
        Assert.assertEquals("Should return double " + positiveDoubleString, 15.345d, StringUtils.parse(positiveDoubleString));
    }
}
