package net.mcbbs.a1mercet.germhetools.util;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.TileEntity;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

public class UtilNBT
{
    public static NBTTagCompound getNBTBlock(Block bukkitBlock)
    {
        try {
            CraftWorld world = (CraftWorld) bukkitBlock.getWorld();
            TileEntity tile = world.getTileEntityAt(bukkitBlock.getX(),bukkitBlock.getY(),bukkitBlock.getZ());
            NBTTagCompound tag = tile.save(new NBTTagCompound());
            Bukkit.getLogger().warning(tile.getBlock().getName());

            Bukkit.getLogger().warning("TTTTTTTTTTTTTTTTTTTTTTT");
            printNBT(tag);
            Bukkit.getLogger().warning("TTTTTTTTTTTTTTTTTTTTTTT");
            tag.getCompound("CustomBlock").getCompound("scale").setDouble("width",0.1F);
            tag.getCompound("CustomBlock").getCompound("scale").setDouble("length",0.1F);
            tag.getCompound("CustomBlock").getCompound("scale").setDouble("height",0.1F);

            tile.load(tag);
        }catch (Exception e){e.printStackTrace();}
        return null;
    }

    public static void printNBT(String preFix , NBTTagCompound nbt)
    {
        preFix+="  ";
        for(String str : nbt.c())
        {
            if(nbt.get(str)==null)continue;
            if(nbt.get(str).getTypeId()==10)
            {
                Bukkit.getLogger().info(preFix+str+": ");
                printNBT(preFix,(NBTTagCompound) nbt.get(str));
            } else {
                Bukkit.getLogger().info(preFix+str+": "+nbt.get(str).toString());
            }
        }

    }
    public static void printNBT(NBTTagCompound nbt)
    {
        Bukkit.getLogger().info("NBTTagCompound");
        printNBT("",nbt);
    }
}
