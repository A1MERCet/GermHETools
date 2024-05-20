package net.mcbbs.a1mercet.germhetools.player.ges;

import net.mcbbs.a1mercet.germhetools.api.BlockManager;
import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import net.mcbbs.a1mercet.germhetools.player.ges.action.ActionHistory;
import net.mcbbs.a1mercet.germhetools.player.ges.action.GESActionMove;
import net.mcbbs.a1mercet.germhetools.player.ges.action.IGESAction;
import net.mcbbs.a1mercet.germhetools.player.ges.action.IGESSample;
import net.mcbbs.a1mercet.germhetools.util.UtilPlayer;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class GES
{
    public boolean enable = false;
    public final PlayerState ps;
    public final ActionHistory history = new ActionHistory(this);
    public final List<IGESSample> sampleList = new ArrayList<>();
    public GESActionMove keymoveAction = null;
    public HEState current;

    public GES(PlayerState ps)
    {
        this.ps=ps;
        this.enable=true;
    }

    public void setHEState(HEState state)
    {
        this.current=state;
        if(keymoveAction!=null)
        {
            keymoveAction.onUnshowSample(keymoveAction,this);
            sampleList.remove(keymoveAction);
            keymoveAction=null;
            ps.player.sendMessage("已重设目标HEState");
        }else {
            ps.player.sendMessage("已设置目标HEState");
        }
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

    public void clearKeyMove()
    {
        if(keymoveAction!=null){
            keymoveAction.onUnshowSample(keymoveAction,this);
            keymoveAction=null;
            sampleList.remove(keymoveAction);
        }
    }

    /**
     * -1=apply 0=side 1=height 2=forword
     */
    public void keyMove(int axis , double moveLength)
    {
        if(current==null)return;
        if(keymoveAction!=null)
        {
            keymoveAction.onUnshowSample(keymoveAction,this);
            sampleList.remove(keymoveAction);
        }

        if(axis==-1) {
            if(keymoveAction!=null)
                keymoveAction.onApply(this);
        }else {

            GESActionMove move = new GESActionMove(this,current);

            double sourceX=0D,sourceY=0D,sourceZ=0D;
            if(keymoveAction!=null){
                sourceX=keymoveAction.x;
                sourceY=keymoveAction.y;
                sourceZ=keymoveAction.z;
            }

            switch (axis){
                case 0:{
                    move.setMove(axisPlayerSide(moveLength));
                    break;}
                case 1:{
                    move.setMove(axisPlayerY(moveLength));
                    break;}
                case 2:{
                    move.setMove(axisPlayerForward(moveLength));
                    break;}
            }

            move.add(sourceX,sourceY,sourceZ);
            move.data.putDefault("keymove","true");

            move.onShowSample(move,this);
            sampleList.add(move);
            keymoveAction=move;
        }
    }

    public void keyHandle(int key)
    {
        if(current==null)return;
        if(key==203){
            keyMove(0,1D);
        }else if(key==205){
            keyMove(0,-1D);
        }else if(key==200){
            keyMove(1,-1D);
        }else if(key==208){
            keyMove(1,1D);
        }else if(key==28){
            keyMove(-1,0D);
        }else if(key==14){
            clearKeyMove();
        }
    }

    public Vector axisPlayerY(double moveLength)
    {
        Block block = UtilPlayer.rayTraceBlock(ps.player.getLocation(),100D);
        if(block==null){return null;}
        return new Vector(0D,moveLength,0D);
    }
    public Vector axisPlayerSide(double moveLength)
    {
        Block block = UtilPlayer.rayTraceBlock(ps.player.getLocation(),100D);
        if(block==null){return null;}

        Vector direction = ps.player.getEyeLocation().getDirection();

        Bukkit.getLogger().warning(direction.getX()+" "+direction.getY()+" "+direction.getZ());

        double x=direction.getX(),y=direction.getY(),z=direction.getZ();

        if(x > 0.5D && z < 0.5D) {
            Bukkit.getLogger().warning("1");
            return new Vector(0D,0D,moveLength);
        }else if(x < 0.5D & z > 0.5D) {
            Bukkit.getLogger().warning("2");
            return new Vector(moveLength*-1D,0D,0D);
        }else if(x < -0.5D && z < 0.5D) {
            Bukkit.getLogger().warning("3");
            return new Vector(0D,0D,moveLength*-1D);
        }else {
            Bukkit.getLogger().warning("4");
            return new Vector(moveLength,0D,0D);
        }

//        if(Math.abs(direction.getX())>Math.abs(direction.getZ())) {
//            return new Vector(0D,0D,moveLength);
//        }else {
//            return new Vector(moveLength*-1,0D,0D);
//        }
    }
    public Vector axisPlayerForward(double moveLength)
    {
        Block block = UtilPlayer.rayTraceBlock(ps.player.getLocation(),100D);
        if(block==null){return null;}

        Vector direction = ps.player.getEyeLocation().getDirection();

        if(Math.abs(direction.getX())>Math.abs(direction.getZ())) {
            return new Vector(moveLength,0D,0D);
        }else {
            return new Vector(0D,0D,moveLength);
        }
    }

    public GES setEnable(){return setEnable(!enable);}
    public GES setEnable(boolean enable)
    {
        this.enable = enable;
        if(!enable){
            clearKeyMove();
        }
        return this;
    }
}
