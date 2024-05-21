package net.mcbbs.a1mercet.germhetools.util;

import org.bukkit.util.Vector;

public class UtilConfig
{
    public static Vector toVector(String str)
    {
        String[] args = str.split(",");
        return new Vector(Double.parseDouble(args[0]),Double.parseDouble(args[0]),Double.parseDouble(args[0]));
    }
    public static Vector toVector(Vector vector , String str)
    {
        String[] args = str.split(",");
        if(args.length!=3)return vector;
        vector.setX(Double.parseDouble(args[0]));
        vector.setY(Double.parseDouble(args[1]));
        vector.setZ(Double.parseDouble(args[2]));
        return vector;
    }
}
