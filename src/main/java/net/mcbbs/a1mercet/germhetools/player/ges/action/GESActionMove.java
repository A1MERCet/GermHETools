package net.mcbbs.a1mercet.germhetools.player.ges.action;

import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import net.mcbbs.a1mercet.germhetools.player.ges.builder.SampleBuilderVector;
import net.mcbbs.a1mercet.germhetools.player.ges.target.IGESLocation;
import org.bukkit.util.Vector;

public class GESActionMove extends GESActionVector
{
    public GESActionMove(GES ges)
    {
        super(ges, "ges_move", "GES-移动", "MOVE");
    }


    @Override
    public void onApplyTargetData()
    {
        super.onApplyTargetData();

        Vector cal = calTargetVector();
        IGESLocation target = (IGESLocation)getTarget();
        target.updateLocation(cal.toLocation(target.getLocation().getWorld()));
    }


    @Override public IGESAction createInstance(GES ges) {return new GESActionMove(ges);}
    @Override public SampleBuilderVector<? extends IGESAction> createBuilder() {return new SampleBuilderVector<GESActionMove>(this);}
    @Override public int[] getKey() {return new int[]{46,-1};}
}
