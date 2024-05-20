package net.mcbbs.a1mercet.germhetools.api;

import net.mcbbs.a1mercet.germhetools.he.HEState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class BlockManager
{
    public static Material material = Material.valueOf("HUEIHUEAENGINE_CUSTOMBLOCK");

    public static HashMap<Location, HEState> cache = new HashMap<>();

    public static void init()
    {
    }

    public static Block rayTraceBlock(Location loc, double range){
        Vector vec = loc.getDirection().multiply(0.15);
        while((range-=0.1)>0){
            Block b = loc.getBlock();
            if(b!=null&&b.getType()!= Material.AIR)
                return b;
            loc.add(vec);
        }
        return null;
    }

    public static String getModelName(HEState s)
    {
        String[] v1 = s.model.split("/");
        String[] v2 = s.model.split(":");
        StringBuilder name = new StringBuilder();
        if(v2.length>0)name.append(v2[0]).append(":");
        if(v1.length>0)name.append(v1[v1.length-1]);
        return name.toString();
    }
    public static String getOBJName(HEState s)
    {
        String[] v1 = s.model.split("/");
        if(v1.length>0)return v1[v1.length-1];
        return "";
    }
    public static String getNameSpace(HEState s)
    {
        String[] v2 = s.model.split(":");
        if(v2.length>0)return v2[0];
        return "";
    }
}
