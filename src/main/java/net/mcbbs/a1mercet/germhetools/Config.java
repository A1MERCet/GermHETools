package net.mcbbs.a1mercet.germhetools;

public class Config
{
    public enum GUIType
    {
        GERM,DRAGON,INVENTORY
    }
    public static GUIType guiType = GUIType.GERM;
    public static int historySize = 50;
    public static String path = null;
}
