package net.mcbbs.a1mercet.germhetools.util;

import com.germ.germplugin.api.dynamic.gui.GermGuiScreen;
import com.germ.germplugin.api.dynamic.gui.GuiManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UtilPlayer
{
    public List<GermGuiScreen> getOpenedGuiAll(Player p)
    {
        List<GermGuiScreen> l = new ArrayList<>();

        for(GermGuiScreen g : GuiManager.getOpenedAllGui(p))
        {
            l.add(g);
            l.addAll(g.getChildGuiScreens());
        }

        return l;
    }

    public static Block rayTraceBlock(LivingEntity e, double range) {return rayTraceBlock(e.getEyeLocation(),range);}
    public static Block rayTraceBlock(Location loc, double range)
    {
        Vector vec = loc.getDirection().multiply(0.15);
        while((range-=0.1)>0){
            Block b = loc.getBlock();
            if(b!=null&&b.getType()!= Material.AIR)
                return b;
            loc.add(vec);
        }
        return null;
    }

    public static Entity rayTraceEntity(LivingEntity e, double range){
        Entity target;
        Iterator<Entity> entities;
        Location loc = e.getEyeLocation();
        Vector vec = loc.getDirection().multiply(0.15);
        while((range-=0.1)>0){
            entities = loc.getWorld().getNearbyEntities(loc.add(vec), 0.001, 0.001, 0.001).iterator();
            while(entities.hasNext()){
                if((target = entities.next()) != e){
                    return target;
                }
            }
        }
        return null;
    }
}
