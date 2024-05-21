package net.mcbbs.a1mercet.germhetools.command;

import io.netty.buffer.Unpooled;
import net.mcbbs.a1mercet.germhetools.GermHETools;
import net.mcbbs.a1mercet.germhetools.api.BlockManager;
import net.mcbbs.a1mercet.germhetools.gui.HEGuiManager;
import net.mcbbs.a1mercet.germhetools.gui.germ.GPresetLibrary;
import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import net.mcbbs.a1mercet.germhetools.player.ges.action.GESActionType;
import net.mcbbs.a1mercet.germhetools.player.ges.action.IGESAction;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;


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

        CraftPlayer craftPlayer = (CraftPlayer) p;
        Bukkit.getScheduler().runTaskLater(GermHETools.getInstance(),()->{
            PacketPlayInBlockPlace packet = new PacketPlayInBlockPlace(EnumHand.MAIN_HAND);
            craftPlayer.getHandle().playerConnection.sendPacket(packet);
        },1);
        Bukkit.getScheduler().runTaskLater(GermHETools.getInstance(),()->{
            CraftWorld craftWorld = (CraftWorld)p.getWorld();
            BlockPosition blockPosition = new BlockPosition(0,0,0);
            PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(craftWorld.getHandle(),blockPosition);
            craftPlayer.getHandle().playerConnection.sendPacket(packet);
        },1);
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

    public boolean gesCheck(PlayerState ps)
    {
        if(!ps.isGESEnable()){ps.player.sendMessage("GES未开启");return false;}
        return true;
    }

    @CommandArgs(
            describe    = "[GES]查看所有编辑器种类",
            args        = {"ges","types"} ,
            types       = {ArgType.DEPEND,ArgType.DEPEND},
            playerOnly = true
    )
    public void gesTypeList(CommandSender sender)
    {
        int i = 0;
        for(String str : GESActionType.types.keySet())
        {
            IGESAction a = GESActionType.types.get(str);
            sender.sendMessage("["+i+"] "+a.getType()+"(类型)"+" - "+a.getName()+"["+a.getID()+"]");
            i++;
        }
    }

    @CommandArgs(
            describe    = "[GES]切换坐标编辑器" +
                    "\n移动 - MOVE" +
                    "\n旋转 - ROTATE" +
                    "\n缩放 - SCALE"
            ,
            args        = {"ges","builder","类型"} ,
            types       = {ArgType.DEPEND,ArgType.DEPEND,ArgType.STRING}
    )
    public void gesBuilderMove(CommandSender sender,String type)
    {
        Player p = (Player)sender;
        PlayerState ps = PlayerState.get(p);
        if(!gesCheck(ps))return;
        ps.ges.setBuilder(type);
    }

    @CommandArgs(
            describe    = "[GES]取消编辑当前的方块",
            args        = {"ges","remove"} ,
            types       = {ArgType.DEPEND,ArgType.DEPEND},
            playerOnly = true
    )
    public void gesRemove(CommandSender sender)
    {
        Player p = (Player)sender;
        PlayerState ps = PlayerState.get(p);
        if(!gesCheck(ps))return;
        ps.ges.removeHEState();
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
        if(!gesCheck(ps))return;

        if(!ps.ges.setHEStatePointer())
            sender.sendMessage("HEState不存在");
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

    @CommandArgs(
            describe    = "打开预设界面",
            args        = {"gui","preset"} ,
            types       = {ArgType.DEPEND,ArgType.DEPEND},
            playerOnly = true
    )
    public void openPreset(CommandSender sender)
    {
        Player p = (Player) sender;
        new GPresetLibrary(PlayerState.get(p)).openGuiTo(p);
    }

    @CommandArgs(
            describe    = "保存玩家数据",
            args        = {"player","save","id"} ,
            types       = {ArgType.DEPEND,ArgType.DEPEND,ArgType.STRING}
    )
    public void savePlayer(CommandSender sender,String p)
    {
        PlayerState ps = PlayerState.get(p);
        if(ps==null){sender.sendMessage("玩家不存在");return;}
        ps.save();
    }
    @CommandArgs(
            describe    = "重载玩家数据",
            args        = {"player","load","id"} ,
            types       = {ArgType.DEPEND,ArgType.DEPEND,ArgType.STRING}
    )
    public void loadPlayer(CommandSender sender,String p)
    {
        PlayerState ps = PlayerState.get(p);
        if(ps==null){sender.sendMessage("玩家不存在");return;}

        PlayerState.remove(ps);

        ps = PlayerState.create(Bukkit.getPlayerExact(p));

        ps.load();
    }
}
