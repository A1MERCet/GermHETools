package net.mcbbs.a1mercet.germhetools.player.ges.builder;

import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import net.mcbbs.a1mercet.germhetools.player.ges.action.GESActionRotate;
import net.mcbbs.a1mercet.germhetools.player.ges.action.GESActionVector;
import net.mcbbs.a1mercet.germhetools.util.UtilPlayer;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class SampleBuilderVector<T extends GESActionVector> extends SampleBuilder<T>
{
    public T action;

    public SampleBuilderVector(T t)
    {
        super(t);
        action = t;
    }

    @Override
    public boolean keyHandle(int key , int assist)
    {
        if(!super.keyHandle(key , assist))return false;
        if(action instanceof GESActionRotate){
            if(key==203){
                action.add(0D,0D,-1D);
            }else if(key==205){
                action.add(0D,0D,1D);
            }else if(key==200){
                action.add(0D,1D,0D);
            }else if(key==208){
                action.add(-0D,-1D,0D);
            }

            action.onShowSample();
        }else {
            if(key==203){
                keyMove(0,1D);
            }else if(key==205){
                keyMove(0,-1D);
            }else if(key==200){
                keyMove(1,-1D);
            }else if(key==208){
                keyMove(1,1D);
            }
        }
        return true;
    }

    @Override public void clear() {super.clear();clearKeyMove();}

    public void clearKeyMove()
    {
        action.onUnshowSample();
        action.clear();
    }

    /**
     *  0=side 1=height 2=forword
     */
    public void keyMove(int axis , double moveLength)
    {
        HEState current = ges.current;
        if(current==null)return;
        action.onUnshowSample();

        switch (axis){
            case 0:{
                action.add(axisPlayerSide(moveLength));
                break;}
            case 1:{
                action.add(axisPlayerY(moveLength));
                break;}
            case 2:{
                action.add(axisPlayerForward(moveLength));
                break;}
        }

        action.onShowSample();
    }


    public Vector axisPlayerY(double moveLength)
    {
        PlayerState ps = ges.ps;
        Block block = UtilPlayer.rayTraceBlock(ps.player.getLocation(),100D);
        if(block==null){return null;}
        return new Vector(0D,moveLength,0D);
    }
    public Vector axisPlayerSide(double moveLength)
    {
        PlayerState ps = ges.ps;
        Block block = UtilPlayer.rayTraceBlock(ps.player.getLocation(),100D);
        if(block==null){return null;}

        Vector direction = ps.player.getEyeLocation().getDirection();

        double x=direction.getX(),y=direction.getY(),z=direction.getZ();

        if(x > 0.5D && z < 0.5D) {
            return new Vector(0D,0D,moveLength);
        }else if(x < 0.5D & z > 0.5D) {
            return new Vector(moveLength*-1D,0D,0D);
        }else if(x < -0.5D && z < 0.5D) {
            return new Vector(0D,0D,moveLength*-1D);
        }else {
            return new Vector(moveLength,0D,0D);
        }
    }
    public Vector axisPlayerForward(double moveLength)
    {
        PlayerState ps = ges.ps;
        Block block = UtilPlayer.rayTraceBlock(ps.player.getLocation(),100D);
        if(block==null){return null;}

        Vector direction = ps.player.getEyeLocation().getDirection();

        if(Math.abs(direction.getX())>Math.abs(direction.getZ())) {
            return new Vector(moveLength,0D,0D);
        }else {
            return new Vector(0D,0D,moveLength);
        }
    }
}
