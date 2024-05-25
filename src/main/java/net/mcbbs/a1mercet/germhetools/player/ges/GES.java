package net.mcbbs.a1mercet.germhetools.player.ges;

import com.germ.germplugin.api.util.EntityUtil;
import com.germ.germplugin.api.util.RayTrackResult;
import net.mcbbs.a1mercet.germhetools.api.BlockManager;
import net.mcbbs.a1mercet.germhetools.gui.germ.ges.GGES;
import net.mcbbs.a1mercet.germhetools.gui.germ.ges.GMSGInfo;
import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import net.mcbbs.a1mercet.germhetools.player.ges.action.ActionHistory;
import net.mcbbs.a1mercet.germhetools.player.ges.action.GESActionType;
import net.mcbbs.a1mercet.germhetools.player.ges.action.IGESAction;
import net.mcbbs.a1mercet.germhetools.player.ges.actiontile.GESTitleActionManager;
import net.mcbbs.a1mercet.germhetools.player.ges.actiontile.IGESTitleAction;
import net.mcbbs.a1mercet.germhetools.player.ges.builder.SampleBuilder;
import net.mcbbs.a1mercet.germhetools.player.ges.preset.PresetLibrary;
import net.mcbbs.a1mercet.germhetools.player.ges.target.IGESTarget;
import net.mcbbs.a1mercet.germhetools.util.UtilPlayer;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class GES
{
    public GGES gui;
    public boolean enable = false;
    public final PlayerState ps;
    public final ActionHistory history = new ActionHistory(this);
    public final PresetLibrary library;

    public final List<IGESTitleAction> titleActions       = new ArrayList<>();
    public final List<IGESAction> actions                 = new ArrayList<>();
    protected SampleBuilder<? extends IGESAction> builder = null;
    public IGESTarget target = null;

    public GES(PlayerState ps)
    {
        this.ps             = ps;
        this.library        = new PresetLibrary(ps);
    }

    public void init()
    {
        titleActions.clear();
        actions.clear();

        for(String s : GESTitleActionManager.registerList.keySet())
        {
            IGESTitleAction a = GESTitleActionManager.create(s);
            if(a==null)continue;
            titleActions.add(a);
        }
        for(String s : GESActionType.types.keySet())
        {
            IGESAction a = GESActionType.create(s,this);
            if(a==null)continue;
            actions.add(a);
        }

    }

    public boolean setBuilder(String type)
    {
        IGESAction action = GESActionType.create(type,this);
        if(action!=null)return setBuilder(action);
        else warn("编辑器类型 ["+type+"] 不存在");

        return false;
    }
    public boolean setBuilder(IGESAction action){return setBuilder(action,false);}
    public boolean setBuilder(IGESAction action , boolean tip)
    {
        removeBuilder();

        builder=action.createBuilder();
        builder.onActive();
        if(builder!=null)
        {
            builder.onTargetReSelect();
            if(gui!=null)gui.onSetBuilder();
            if(tip)msg("已切换至编辑器 ["+action.getName()+"]");
        }

        return true;
    }
    public void removeBuilder()
    {
        if(builder!=null){
            builder=null;
            if(gui!=null)gui.onRemoveBuilder();
        }
    }
    public void clearBuilder()
    {
        if(builder!=null)
        {
            builder.clear();
            if(gui!=null)gui.onClearBuilder();
        }
    }
    public void keyHandle(int key , int assist)
    {

        if(builder!=null)
        {
            if(key==28){
                builder.apply();
                builder.onTargetReSelect();
            }else if(key==14){
                builder.clear();
            }else if(key==44){
                IGESAction action = history.revoke();
                if(action!=null)msg("已撤销操作 ["+action.getName()+"]");
                return;
            }

            builder.keyHandle(key , assist);
        }

        for(IGESTitleAction a : titleActions)
        {
            int[] k = a.getKey();
            if( (k[0] == key && k[1]==assist) )
                a.execute(this);

        }
        for(IGESAction a : actions)
        {
            int[] k = a.getKey();
            if( (k[0] == key && k[1]==assist) )
                setBuilder(a);
        }

        if(gui!=null) gui.update();
    }

    public void setTarget(IGESTarget target){setTarget(target,false);}
    public void setTarget(IGESTarget target , boolean tip)
    {
        clearBuilder();
        this.target=target;
        if(tip)msg("已设置目标 "+target.getType()+"["+target.getName()+"]");
        if(builder!=null) builder.action.onTargetReSelect();
    }
    public void removeTarget(){removeTarget(false);}
    public void removeTarget(boolean tip)
    {
        if(this.target!=null)
        {
            if(tip)msg("已清除目标 "+target.getName());
            removeBuilder();
            this.target=null;
        }
    }

    public boolean setTargetBlock(Block block){return setTargetBlock(block,false);}
    public boolean setTargetBlock(Block block,boolean tip)
    {
        if(BlockManager.isHEBlock(block))
        {
            HEState pointer = new HEState().parse(block);
            if(pointer==null){warn("不受支持的方块类型["+block.getType().name()+"]");return false;}
            setTarget(pointer,tip);
            return true;
        }
        return false;
    }
    public boolean setTargetPointer(){return setTargetPointer(false);}
    public boolean setTargetPointer(boolean tip)
    {
        Location location = ps.player.getLocation();
        RayTrackResult ray = EntityUtil.getRayTrace(ps.player,100D,0D);
        if(ray!=null && ray.getEntityHit()!=null){

            //todo
        }

        Block block = UtilPlayer.rayTraceBlock(location.add(0D,ps.player.getEyeHeight(),0D),100D);
        if(block!=null)
            return setTargetBlock(block,tip);

        return false;
    }

    public GES setEnable(){return setEnable(!enable);}
    public GES setEnable(boolean enable)
    {
        this.enable = enable;
        if(!enable) {
            if(this.gui!=null){this.gui.closeFrom(ps.player);this.gui=null;}
            removeBuilder();
        }else {
            this.init();
            this.gui = new GGES(this);
            this.gui.build();
            this.gui.openHUDTo(ps.player);
        }
        return this;
    }

    public void msg(GMSGInfo v){
        if(gui!=null){
            gui.msgContainer.sendMessage(v);
        }else {
            ps.player.sendMessage("["+v.type.name()+"]"+v.message);
        }
    }
    public void msg(String v){msg(GMSGInfo.Type.INFO,v);}
    public void warn(String v){msg(GMSGInfo.Type.WARN,v);}
    public void error(String v){msg(GMSGInfo.Type.ERROR,v);}
    public void msg(GMSGInfo.Type type , String v)
    {
        if(gui!=null){
            gui.msgContainer.sendMessage(new GMSGInfo(type,v));
        }else {
            ps.player.sendMessage("["+type.name()+"]"+v);
        }
    }

    public SampleBuilder<? extends IGESAction> getBuilder() {return builder;}
}
