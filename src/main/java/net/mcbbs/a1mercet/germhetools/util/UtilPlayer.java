package net.mcbbs.a1mercet.germhetools.util;

import com.germ.germplugin.api.dynamic.gui.GermGuiScreen;
import com.germ.germplugin.api.dynamic.gui.GuiManager;
import net.minecraft.server.v1_12_R1.PacketPlayOutMapChunk;
import net.minecraft.server.v1_12_R1.PacketPlayOutUnloadChunk;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UtilPlayer
{
    public static void updateBlockData(Player p ,Block block)
    {
        if(block==null)return;
        Chunk chunk = block.getChunk();
        PacketPlayOutUnloadChunk packet1 = new PacketPlayOutUnloadChunk(chunk.getX(),chunk.getZ());
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet1);

        PacketPlayOutMapChunk packet2 = new PacketPlayOutMapChunk(((CraftChunk)chunk).getHandle(),65535);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet2);
    }

    public static List<GermGuiScreen> getOpenedGuiAll(Player p)
    {
        List<GermGuiScreen> l = new ArrayList<>();

        for(GermGuiScreen g : GuiManager.getOpenedAllGui(p))
        {
            l.add(g);
            l.addAll(g.getChildGuiScreens());
        }

        return l;
    }

    public static Block rayTraceBlock(LivingEntity e, double range) {return rayTraceBlock(e.getEyeLocation().add(0D,e.getEyeHeight(),0D),range);}
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
