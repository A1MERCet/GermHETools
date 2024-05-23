package net.mcbbs.a1mercet.germhetools.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class UtilWorld
{
    public interface ICallbackBlock
    {
        void handle(Location location);
    }
    public static void areaBlockHandle(World w , Vector l1, Vector l2, ICallbackBlock callback) {areaBlockHandle(l1.toLocation(w),l2.toLocation(w),callback);}
    public static void areaBlockHandle(Location l1, Location l2 , ICallbackBlock callback)
    {
        Vector max = new Vector();
        Vector min = new Vector();
        max.setX(Math.max(l1.getBlockX(),l2.getBlockX()));
        max.setY(Math.max(l1.getBlockY(),l2.getBlockY()));
        max.setZ(Math.max(l1.getBlockZ(),l2.getBlockZ()));

        min.setX(Math.min(l1.getBlockX(),l2.getBlockX()));
        min.setY(Math.min(l1.getBlockY(),l2.getBlockY()));
        min.setZ(Math.min(l1.getBlockZ(),l2.getBlockZ()));

        Location temp = new Location(l1.getWorld(),0,0,0);

        int cycle = 0;

        for(int x = min.getBlockX();x<max.getBlockX();x++)
            for(int z = min.getBlockZ();z<max.getBlockZ();z++)
                for(int y = min.getBlockY();y<max.getBlockY();y++)
                {
                    temp.setX(x);
                    temp.setY(y);
                    temp.setZ(z);

                    callback.handle(temp);

                    cycle++;
                    if(cycle>5000)return;
                }
    }
}
