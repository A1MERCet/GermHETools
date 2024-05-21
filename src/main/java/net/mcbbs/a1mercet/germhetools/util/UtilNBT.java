package net.mcbbs.a1mercet.germhetools.util;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.TileEntity;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;

public class UtilNBT
{
    public static org.bukkit.inventory.ItemStack saveItemNBT(org.bukkit.inventory.ItemStack bukkitItemStack , NBTTagCompound nbt)
    {
        ItemStack isk = CraftItemStack.asNMSCopy(bukkitItemStack);
        isk.load(nbt);
        return CraftItemStack.asBukkitCopy(isk);
    }

    public static NBTTagCompound getItemNBT(org.bukkit.inventory.ItemStack bukkitItemStack)
    {
        ItemStack isk = CraftItemStack.asNMSCopy(bukkitItemStack);
        return isk.save(new NBTTagCompound());
    }

    public static NBTTagCompound getBlockNBT(Block bukkitBlock)
    {
        TileEntity tile = ((CraftWorld) bukkitBlock.getWorld()).getTileEntityAt(bukkitBlock.getX(),bukkitBlock.getY(),bukkitBlock.getZ());
        return tile.save(new NBTTagCompound());
    }

    public static void saveBlockNBT(Block bukkitBlock,NBTTagCompound nbt)
    {
        TileEntity tile = ((CraftWorld) bukkitBlock.getWorld()).getTileEntityAt(bukkitBlock.getX(),bukkitBlock.getY(),bukkitBlock.getZ());
        tile.load(nbt);
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
