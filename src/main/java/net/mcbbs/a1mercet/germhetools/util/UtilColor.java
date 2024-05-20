package net.mcbbs.a1mercet.germhetools.util;

public class UtilColor
{
    public static final long WHITE_ALP = 0x44FFFFFF;
    public static final long WHITE = 0xFFFFFFFF;

    public static final long GRAY_ALP = 0x44888888;
    public static final long GRAY = 0xFF888888;

    public static final long BLACK_ALP = 0x44000000;
    public static final long BLACK = 0xFF000000;

    public static final long YELLOW_ALP = 0x44FFFF00;
    public static final long YELLOW = 0xFFFFFF00;

    public static final long BROWN_ALP = 0x4491890A;
    public static final long BROWN = 0xFF91890A;

    public static final long RED_ALP = 0x449B0000;
    public static final long RED = 0xFF9B0000;
    public static final long PINK_ALP = 0x44FF4444;
    public static final long PINK = 0xFFFF4444;

    public static final long PURPLE_ALP = 0x2292167F;
    public static final long PURPLE = 0xFF92167F;

    public static final long BLUE_ALP = 0x443387f0;
    public static final long BLUE = 0xFF3387f0;

    public static final long GREEN_ALP = 0x4433BB33;
    public static final long GREEN = 0xFF00FF00;

    public static final long ORANGE_ALP = 0x44FFAA00;
    public static final long ORANGE = 0xFFFFAA00;

    public static String getColorReserve(float cur , float max)
    {
        float v = cur/max;

        if(v<=0.0001F)         return "§4";
        else if(v<=0.2F)    return "§6";
        else if(v<=0.4F)    return "§e";
        else return "§f";
    }
    public static String getColor(float cur , float max)
    {
        float v = cur/max;

        if(v<=0.0001F)      return "§f";
        else if(v<=0.4F)    return "§e";
        else if(v<=0.8F)    return "§6";
        else return "§4";
    }
}
