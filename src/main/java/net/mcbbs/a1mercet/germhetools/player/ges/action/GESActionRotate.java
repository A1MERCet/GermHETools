package net.mcbbs.a1mercet.germhetools.player.ges.action;

import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import net.mcbbs.a1mercet.germhetools.player.ges.builder.SampleBuilderVector;
import net.mcbbs.a1mercet.germhetools.player.ges.target.IGESLocation;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class GESActionRotate extends GESActionVector
{
    public GESActionRotate(GES ges)
    {
        super(ges,"ges_rotate", "GES-旋转", "ROTATE");
    }

    @Override
    public void onApplyTargetData()
    {
        super.onApplyTargetData();

        IGESLocation target = (IGESLocation)getTarget();
        target.setRotate(calTargetVector());

        target.updateLocation();
    }

    @Override public Location getEffectLocation() {return ((IGESLocation)getTarget()).getLocation();}

    @Override
    public Vector calTargetVector()
    {
        IGESLocation target = (IGESLocation)getTarget();
        if(target==null)return value;

        Vector rotate = target.getRotate();
        if(overwrite)   return new Vector(value.getX(), value.getY(), value.getZ());
        else            return new Vector(rotate.getX()+ value.getX(),rotate.getY()+ value.getY(),rotate.getZ()+ value.getZ());
    }

    @Override public IGESAction createInstance(GES ges) {return new GESActionRotate(ges);}
    @Override public SampleBuilderVector<? extends IGESAction> createBuilder() {return new SampleBuilderVector<GESActionRotate>(this);}
    @Override public int[] getKey() {return new int[]{19,-1};}

}
