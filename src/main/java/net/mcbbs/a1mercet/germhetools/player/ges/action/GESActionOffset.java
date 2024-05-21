package net.mcbbs.a1mercet.germhetools.player.ges.action;

import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import net.mcbbs.a1mercet.germhetools.player.ges.builder.SampleBuilderVector;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class GESActionOffset extends GESActionVector
{
    public GESActionOffset(GES ges)
    {
        super(ges, "ges_offset", "GES-偏移", "OFFSET");
    }

    @Override
    public boolean onApply(GES ges)
    {
        if(!super.onApply(ges))return false;
        HEState state = getHEState();
        Vector cal = calVector();

        state.location.getBlock().setType(Material.AIR);
        state.setTransform(cal.getX(),cal.getY(),cal.getZ());
        state.place();
        ges.setHEState(state.location.getBlock());
        return true;
    }

    @Override
    public boolean onRevoke(GES ges)
    {
        if(!super.onRevoke(ges))return false;
        HEState state = getHEState();
        Vector cal = calVector();

        state.setTransform(cal.getX(),cal.getY(),cal.getZ());
        state.place();
        ges.setHEState(state.location.getBlock());
        return true;
    }

    @Override public IGESAction createInstance(GES ges) {return new GESActionOffset(ges);}
    @Override public SampleBuilderVector<? extends IGESAction> createBuilder() {return new SampleBuilderVector<GESActionOffset>(this);}
}
