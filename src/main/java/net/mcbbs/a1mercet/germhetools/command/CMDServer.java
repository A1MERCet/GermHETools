package net.mcbbs.a1mercet.germhetools.command;

import com.germ.germplugin.api.dynamic.effect.GermEffectEntity;
import net.mcbbs.a1mercet.germhetools.api.BlockManager;
import net.mcbbs.a1mercet.germhetools.gui.HEGuiManager;
import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import net.mcbbs.a1mercet.germhetools.player.ges.gui.germ.GSquareBox;
import net.mcbbs.a1mercet.germhetools.util.UtilNBT;
import net.mcbbs.a1mercet.germhetools.util.UtilPlayer;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CMDServer extends CMDBase
{
    public CMDServer()
    {
        super("ghe", CMDServer.class);
    }

    @CommandArgs(
            describe    = "test",
            args        = {"test"} ,
            types       = {ArgType.DEPEND},
            playerOnly = true
    )
    public void test(CommandSender sender)
    {
        Player p = (Player)sender;
        PlayerState ps = PlayerState.get(p);
        UtilNBT.getNBTBlock(UtilPlayer.rayTraceBlock(p.getLocation().add(0D,p.getEyeHeight(),0D),50D));
    }

    @CommandArgs(
            describe    = "[GES]启用/关闭GES",
            args        = {"ges"} ,
            types       = {ArgType.DEPEND},
            playerOnly = true
    )
    public void gesEnable(CommandSender sender)
    {
        Player p = (Player)sender;
        PlayerState ps = PlayerState.get(p);
        if(ps.ges!=null)ps.ges.setEnable();
    }

    @CommandArgs(
            describe    = "[GES]编辑准星指向的方块",
            args        = {"ges","pointer"} ,
            types       = {ArgType.DEPEND,ArgType.DEPEND},
            playerOnly = true
    )
    public void gesPointer(CommandSender sender)
    {
        Player p = (Player)sender;
        PlayerState ps = PlayerState.get(p);
        if(!ps.isGESEnable()){sender.sendMessage("GES未开启");return;}

        if(!ps.ges.setHEStatePointer())
            sender.sendMessage("HEState不存在");
    }

    @CommandArgs(
            describe    = "testx",
            args        = {"testx"} ,
            types       = {ArgType.DEPEND},
            playerOnly = true
    )
    public void testX(CommandSender sender)
    {
        Player p = (Player)sender;
        Block block = UtilPlayer.rayTraceBlock(p.getLocation(),100D);
        if(block==null){sender.sendMessage("Block not found");return;}

        Vector direction = p.getEyeLocation().getDirection();
        Vector v = new Vector(Math.abs(direction.getX()),Math.abs(direction.getY()),Math.abs(direction.getZ()));

        if(v.getX()>v.getZ()) {
            new GSquareBox().spawnToLocation(p,new Location(block.getWorld(),block.getX(),block.getY(),block.getZ()+0D));
            new GSquareBox().spawnToLocation(p,new Location(block.getWorld(),block.getX(),block.getY()+1D,block.getZ()+0D));
            new GSquareBox().spawnToLocation(p,new Location(block.getWorld(),block.getX(),block.getY(),block.getZ()+1D));
            new GSquareBox().spawnToLocation(p,new Location(block.getWorld(),block.getX(),block.getY(),block.getZ()+2D));
            new GSquareBox().spawnToLocation(p,new Location(block.getWorld(),block.getX(),block.getY(),block.getZ()+3D));
            new GSquareBox().spawnToLocation(p,new Location(block.getWorld(),block.getX(),block.getY(),block.getZ()+4D));
        }else {
            new GSquareBox().spawnToLocation(p,new Location(block.getWorld(),block.getX(),block.getY(),block.getZ()));
            new GSquareBox().spawnToLocation(p,new Location(block.getWorld(),block.getX(),block.getY()+1D,block.getZ()));
            new GSquareBox().spawnToLocation(p,new Location(block.getWorld(),block.getX()+1D,block.getY(),block.getZ()));
            new GSquareBox().spawnToLocation(p,new Location(block.getWorld(),block.getX()+2D,block.getY(),block.getZ()));
            new GSquareBox().spawnToLocation(p,new Location(block.getWorld(),block.getX()+3D,block.getY(),block.getZ()));
            new GSquareBox().spawnToLocation(p,new Location(block.getWorld(),block.getX()+4D,block.getY(),block.getZ()));
        }
    }

    @CommandArgs(
            describe    = "传送至目标方块",
            args        = {"tp","block"} ,
            types       = {ArgType.DEPEND,ArgType.DEPEND},
            playerOnly = true
    )
    public void tpPointer(CommandSender sender)
    {
        Player p = (Player) sender;
        Block b = BlockManager.rayTraceBlock(p.getLocation().add(0,2,0),500);
        if(b==null){sender.sendMessage("方块不存在");return;}
        p.teleport(b.getLocation());
    }

    @CommandArgs(
            describe    = "编辑目视方块",
            args        = {"edit"} ,
            types       = {ArgType.DEPEND},
            playerOnly = true
    )
    public void editPointer(CommandSender sender)
    {
        Player p = (Player) sender;
        Block b = BlockManager.rayTraceBlock(p.getLocation().add(0,2,0),20);
        if(b==null){sender.sendMessage("方块不存在");return;}
        HEGuiManager.createHEStateGui(p,new HEState().parse(b),false).openGuiTo(p);
    }

    @CommandArgs(
            describe    = "编辑手持方块",
            args        = {"edit","hand"} ,
            types       = {ArgType.DEPEND,ArgType.DEPEND},
            playerOnly = true
    )
    public void editHand(CommandSender sender)
    {
        Player p = (Player) sender;
        Block b = BlockManager.rayTraceBlock(p.getLocation().add(0,2,0),20);
        if(b==null){sender.sendMessage("方块不存在");return;}
        HEGuiManager.createHEStateGui(p,new HEState().parse(b),true).openGuiTo(p);
    }

}
