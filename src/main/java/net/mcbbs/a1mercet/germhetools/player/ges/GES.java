package net.mcbbs.a1mercet.germhetools.player.ges;

import net.mcbbs.a1mercet.germhetools.api.BlockManager;
import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import net.mcbbs.a1mercet.germhetools.player.ges.action.ActionHistory;
import net.mcbbs.a1mercet.germhetools.player.ges.action.GESActionType;
import net.mcbbs.a1mercet.germhetools.player.ges.action.IGESAction;
import net.mcbbs.a1mercet.germhetools.player.ges.builder.SampleBuilder;
import net.mcbbs.a1mercet.germhetools.util.UtilPlayer;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class GES
{
    public boolean enable = false;
    public final PlayerState ps;
    public final ActionHistory history = new ActionHistory(this);
    public final PresetLibrary library;

    protected SampleBuilder<? extends IGESAction> builder = null;
    public HEState current = null;

    public GES(PlayerState ps)
    {
        this.ps             = ps;
        this.enable         = true;
        this.library        = new PresetLibrary(ps);
    }

    public boolean setBuilder(String type)
    {
        IGESAction action = GESActionType.create(type,this);
        if(action!=null)return setBuilder(action);
        else ps.player.sendMessage("编辑器类型 ["+type+"] 不存在");
        return false;
    }
    public boolean setBuilder(IGESAction action)
    {
        removeBuilder();

        builder=action.createBuilder();
        builder.onStateReSelect();
        ps.player.sendMessage("已切换至编辑器 "+action.getName());
        return true;
    }
    public void removeBuilder()
    {
        if(builder!=null){
            clearBuilder();
            builder=null;
        }
    }
    public void clearBuilder()
    {
        if(builder!=null)
            builder.clear();
    }
    public void keyHandle(int key , int assist)
    {
        if(builder == null) return;

        if(key==28){
            builder.apply();
            builder.onStateReSelect();
            ps.player.sendMessage("已重置 ["+builder.sample.getName()+"] 操作状态");
        }else if(key==14){
            builder.clear();
        }else if(key==44){
            IGESAction action = history.recovery();
            if(action!=null)ps.player.sendMessage("已撤销操作 ["+action.getName()+"]");
            return;
        }

        builder.keyHandle(key , assist);
    }

    public void removeHEState()
    {
        ps.player.sendMessage("已取消目标HEState");
        current=null;
    }

    public void setHEState(HEState state)
    {
        if(current != null)     ps.player.sendMessage("已重设目标HEState");
        else                    ps.player.sendMessage("已设置目标HEState");
        clearBuilder();
        current=state;
        if(builder!=null) builder.sample.onStateReSelect();
    }

    public boolean setHEState(Block block)
    {
        if(block!=null)
        {
            HEState pointer = new HEState().parse(block);
            if(pointer==null)return false;
            setHEState(pointer);
            return true;
        }
        return false;
    }

    public boolean setHEStatePointer()
    {
        Location location = ps.player.getLocation();
        Block block = UtilPlayer.rayTraceBlock(location.add(0D,ps.player.getEyeHeight(),0D),100D);
        if(block!=null&&BlockManager.material.name().equals(block.getType().name()))
        {
            HEState pointer = new HEState().parse(block);
            if(pointer==null)return false;
            setHEState(pointer);
            return true;
        }
        return false;
    }

    public GES setEnable(){return setEnable(!enable);}
    public GES setEnable(boolean enable)
    {
        this.enable = enable;
        if(!enable) removeBuilder();
        return this;
    }
}
