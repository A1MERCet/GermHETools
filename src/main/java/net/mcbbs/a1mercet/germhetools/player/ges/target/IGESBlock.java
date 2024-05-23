package net.mcbbs.a1mercet.germhetools.player.ges.target;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public interface IGESBlock extends IGESLocation
{
    default String getType(){return "方块";}
    default String getName(){Location l = getLocation();return l==null?"-":l.toVector().toString();}

    default Block getBlock(){Location loc = getLocation();return loc==null?null:getLocation().getBlock();}
    @Override
    default void remove()
    {
        Location l = getLocation();
        if(l!=null&&l.getWorld()!=null)
            l.getBlock().setType(Material.AIR);
    }
    default boolean place(){return place(getLocation());}
    boolean place(Location loc);
    @Override
    default void updateLocation(Location location)
    {
        Location source = getLocation();
        if(source!=null&&source.getWorld()!=null)
            source.getBlock().setType(Material.AIR);
        place(location);
    }
}
