package net.mcbbs.a1mercet.germhetools.player.ges.builder;

import net.mcbbs.a1mercet.germhetools.gui.germ.ges.samplerbuilder.GSamplerBuilder;
import net.mcbbs.a1mercet.germhetools.gui.germ.ges.samplerbuilder.GSamplerBuilderVector;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import net.mcbbs.a1mercet.germhetools.player.ges.action.GESActionRotate;
import net.mcbbs.a1mercet.germhetools.player.ges.action.GESActionVector;
import net.mcbbs.a1mercet.germhetools.player.ges.target.IGESLocation;
import net.mcbbs.a1mercet.germhetools.player.ges.target.IGESTarget;
import net.mcbbs.a1mercet.germhetools.util.UtilPlayer;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class SampleBuilderVector<T extends GESActionVector> extends SampleBuilder<T>
{
    public T action;
    protected double subdivision = 1D;

    public SampleBuilderVector(T t)
    {
        super(t);
        action = t;
    }

    @Override
    public void onTargetReSelect()
    {
        if(!action.onTargetReSelect()){exit();return;}
        super.onTargetReSelect();
    }

    @Override
    public boolean keyHandle(int key , int assist)
    {
        if(!super.keyHandle(key , assist))return false;
        if(action instanceof GESActionRotate){
            if(key==203){
                action.add(0D,0D,subdivision*-1D);
            }else if(key==205){
                action.add(0D,0D,subdivision);
            }else if(key==200){
                action.add(0D,subdivision,0D);
            }else if(key==208){
                action.add(0D,subdivision*-1D,0D);
            }
        }else {
            if(key==203){
                keyMove(0,subdivision);
            }else if(key==205){
                keyMove(0,subdivision*-1D);
            }else if(key==200){
                keyMove(1,subdivision*-1D);
            }else if(key==208){
                keyMove(1,subdivision);
            }
        }

        if(key==12)      subdivision-=0.1F;
        else if(key==13) subdivision+=0.1F;

        action.onShowSample();

        if(hud!=null)hud.update();
        return true;
    }

    @Override public void clear() {super.clear();clearKeyMove();}

    public void clearKeyMove()
    {
        action.clear();
        action.onShowSample();
    }

    /**
     *  0=side 1=height 2=forword
     */
    public void keyMove(int axis , double moveLength)
    {
        IGESTarget target = ges.target;
        if(target==null)return;

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
    }


    public Vector axisPlayerY(double moveLength)
    {
        PlayerState ps = ges.ps;
        Block block = UtilPlayer.rayTraceBlock(ps.player.getLocation(),100D);
        if(block==null){ges.warn("方块不存在");return new Vector();}
        return new Vector(0D,moveLength*-1D,0D);
    }
    public Vector axisPlayerSide(double moveLength)
    {
        PlayerState ps = ges.ps;
        Block block = UtilPlayer.rayTraceBlock(ps.player.getLocation(),100D);
        if(block==null){ges.warn("方块不存在");return new Vector();}

        Vector direction = ps.player.getEyeLocation().getDirection();

        double x=direction.getX(),y=direction.getY(),z=direction.getZ();

        if(x > 0.5D && z < 0.5D) {
            return new Vector(0D,0D,moveLength*-1D);
        }else if(x < 0.5D & z > 0.5D) {
            return new Vector(moveLength,0D,0D);
        }else if(x < -0.5D && z < 0.5D) {
            return new Vector(0D,0D,moveLength);
        }else {
            return new Vector(moveLength*-1D,0D,0D);
        }
    }
    public Vector axisPlayerForward(double moveLength)
    {
        PlayerState ps = ges.ps;
        Block block = UtilPlayer.rayTraceBlock(ps.player.getLocation(),100D);
        if(block==null){ges.warn("方块不存在");return new Vector();}

        Vector direction = ps.player.getEyeLocation().getDirection();

        if(Math.abs(direction.getX())>Math.abs(direction.getZ())) {
            return new Vector(moveLength*-1D,0D,0D);
        }else {
            return new Vector(0D,0D,moveLength*-1D);
        }
    }

    public double getSubdivision() {return subdivision;}
    public SampleBuilderVector<? extends GESActionVector> setSubdivision(double subdivision) {this.subdivision = subdivision;return this;}

    @Override public GSamplerBuilder createHUD() {GSamplerBuilderVector g = new GSamplerBuilderVector(this);g.build();return g;}
}
