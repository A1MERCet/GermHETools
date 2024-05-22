package net.mcbbs.a1mercet.germhetools.player.ges.action;

import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import net.mcbbs.a1mercet.germhetools.player.ges.builder.SampleBuilderVector;
import net.mcbbs.a1mercet.germhetools.player.ges.target.IGESBlock;
import net.mcbbs.a1mercet.germhetools.player.ges.target.IGESLocation;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class GESActionScale extends GESActionVector
{
    public GESActionScale(GES ges)
    {
        super(ges,"ges_scale", "GES-缩放", "SCALE");
    }

    @Override
    public void onApplyTargetData()
    {
        super.onApplyTargetData();

        IGESBlock target = (IGESBlock)getTarget();
        target.setScale(calTargetVector());

        target.updateLocation();
    }

    @Override public Location getEffectLocation() {return ((IGESLocation)getTarget()).getLocation();}

    @Override
    public Vector calTargetVector()
    {
        IGESLocation state = (IGESLocation)getTarget();
        if(state==null)return value;

        Vector rotate = state.getScale();
        if(overwrite)   return new Vector(value.getX(), value.getY(), value.getZ());
        else            return new Vector(rotate.getX()+ value.getX(),rotate.getY()+ value.getY(),rotate.getZ()+ value.getZ());
    }

    @Override public IGESAction createInstance(GES ges) {return new GESActionScale(ges);}
    @Override public SampleBuilderVector<? extends IGESAction> createBuilder() {return new SampleBuilderVector<GESActionScale>(this);}
    @Override public int[] getKey() {return new int[]{45,-1};}

}
