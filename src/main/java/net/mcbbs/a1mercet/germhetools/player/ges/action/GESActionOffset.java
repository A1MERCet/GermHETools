package net.mcbbs.a1mercet.germhetools.player.ges.action;

import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import net.mcbbs.a1mercet.germhetools.player.ges.builder.SampleBuilderVector;
import net.mcbbs.a1mercet.germhetools.player.ges.target.IGESLocation;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class GESActionOffset extends GESActionVector
{
    public GESActionOffset(GES ges)
    {
        super(ges, "ges_offset", "GES-偏移", "OFFSET");
    }


    @Override
    public void onApplyTargetData()
    {
        super.onApplyTargetData();

        IGESLocation target = (IGESLocation)getTarget();
        target.setOffset(calTargetVector());

        target.updateLocation();
    }

    @Override public Location getEffectLocation() {
        Location source = ((IGESLocation)getTarget()).getLocation();
        return new Location(source.getWorld(),source.getX(),source.getY(),source.getZ()).add(calTargetVector());
    }

    @Override
    public Vector calTargetVector()
    {
        IGESLocation target = (IGESLocation)getTarget();
        if(target==null)return value;

        Vector offset = target.getOffset();
        if(overwrite)   return new Vector(value.getX(), value.getY(), value.getZ());
        else            return new Vector(offset.getX()+ value.getX(),offset.getY()+ value.getY(),offset.getZ()+ value.getZ());
    }

    @Override public IGESAction createInstance(GES ges) {return new GESActionOffset(ges);}
    @Override public SampleBuilderVector<? extends IGESAction> createBuilder() {return new SampleBuilderVector<GESActionOffset>(this);}
    @Override public int[] getKey() {return new int[]{34,-1};}

}
