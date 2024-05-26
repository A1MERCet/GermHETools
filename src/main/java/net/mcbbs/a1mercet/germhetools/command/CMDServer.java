package net.mcbbs.a1mercet.germhetools.command;

import net.mcbbs.a1mercet.germhetools.api.BlockManager;
import net.mcbbs.a1mercet.germhetools.gui.HEGuiManager;
import net.mcbbs.a1mercet.germhetools.gui.germ.GPresetLibrary;
import net.mcbbs.a1mercet.germhetools.he.HEManager;
import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import net.mcbbs.a1mercet.germhetools.player.ges.action.GESActionType;
import net.mcbbs.a1mercet.germhetools.util.Options;
import net.mcbbs.a1mercet.germhetools.util.UtilNBT;
import net.mcbbs.a1mercet.germhetools.util.UtilPlayer;
import net.mcbbs.a1mercet.germhetools.util.UtilWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CMDServer extends CMDBase
{
    public CMDServer()
    {
        super("ghe", CMDServer.class);
    }

    @CommandArgs(
            describe    = "test",
            args        = {"test","input"} ,
            types       = {ArgType.DEPEND,ArgType.STRING}
    )
    public void test(CommandSender sender,String input)
    {
        sender.sendMessage(HEManager.getFileNameOnly(input));
    }

    @CommandArgs(
            describe    =
            "\n             读取同名的MTL文件生成材质(默认true)"+
            "\n             [Boolean/String]..READ_MTL"+
            "\n"+
            "\n             参考格式: CREATE_EMPTY_TEXTURE,0,MODEL_PATH,D:/xx,TEXTURE_PATH,D:/xx,IMAGE_TYPED,png" +
            "\n             生成路径内所有模型文件至预设文件" +
            "\n             生成的HEState默认带多少个空材质槽"+
            "\n             [int/String]......CREATE_EMPTY_TEXTURE"+
            "\n"+
            "\n             重定向模型路径"+
            "\n             -留空按文件所在路径"+
            "\n             [String]..........MODEL_PATH"+
            "\n"+
            "\n             重定向纹理路径"+
            "\n             -留空按文件所在路径"+
            "\n             -NONE不生成纹理路径"+
            "\n             [String]..........TEXTURE_PATH"+
            "\n"+
            "\n             纹理文件格式 默认png"+
            "\n             [STRING]..........IMAGE_TYPE",
            args        = {"he","gen","模型路径","输出文件","参数"} ,
            types       = {ArgType.DEPEND,ArgType.DEPEND,ArgType.STRING,ArgType.STRING,ArgType.STRINGARY}
    )
    public void modelToPreset(CommandSender sender,String input,String output,String[] parameters)
    {
        Options<String> p = new Options<>();
        for(int i = 0;i<parameters.length;i+=2)
            if(i+1<=parameters.length-1) 
                p.putDefault(parameters[i],parameters[i+1]);
        HEManager.modelToPreset(input,output,p);
    }

    @CommandArgs(
            describe    =
                    "\n             生成路径内所有模型文件至预设文件",
            args        = {"he","gen","模型路径","输出文件"} ,
            types       = {ArgType.DEPEND,ArgType.DEPEND,ArgType.STRING,ArgType.STRING}
    )
    public void modelToPreset(CommandSender sender,String input,String output)
    {
        modelToPreset(sender,input,output,new String[]{});
    }
    
    @CommandArgs(
            describe    = "打印手中物品/指针方块NBT",
            args        = {"nbt"} ,
            types       = {ArgType.DEPEND},
            playerOnly = true
    )
    public void printNBT(CommandSender sender)
    {
        Player p = (Player)sender;

        if(p.getInventory().getItemInMainHand()!=null&&p.getInventory().getItemInMainHand().getType()!=Material.AIR)
            UtilNBT.printNBT(UtilNBT.getItemNBT(p.getInventory().getItemInMainHand()));

        Block b = UtilPlayer.rayTraceBlock(p,1000D);
        if(b==null)return;
        UtilNBT.printNBT(UtilNBT.getBlockNBT(b));
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
            net.mcbbs.a1mercet.germhetools.player.ges.action.IGESAction a = GESActionType.types.get(str);
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
        ps.ges.removeTarget();
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

        if(!ps.ges.setTargetPointer())
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
