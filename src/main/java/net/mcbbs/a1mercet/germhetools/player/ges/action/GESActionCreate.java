package net.mcbbs.a1mercet.germhetools.player.ges.action;

import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import net.mcbbs.a1mercet.germhetools.player.ges.builder.SampleBuilder;
import net.mcbbs.a1mercet.germhetools.player.ges.builder.SampleBuilderVector;
import net.mcbbs.a1mercet.germhetools.player.ges.target.IGESBlock;
import net.mcbbs.a1mercet.germhetools.player.ges.target.IGESLocation;
import net.mcbbs.a1mercet.germhetools.player.ges.target.IGESTarget;
import org.bukkit.util.Vector;

public class GESActionCreate extends GESActionVector
{
    public IGESBlock block;

    public GESActionCreate(GES ges)
    {
        super(ges, "ges_create", "GES-创建", "CREATE");
    }

    @Override
    public void onApplyTargetData()
    {
        IGESBlock target = (IGESBlock)getTarget();
        Vector vector = calTargetVector();
        target.place(vector.toLocation(ges.ps.player.getWorld()));
    }

    public GESActionCreate setBlock(IGESBlock block) {ges.setTarget(block);this.block = block;return this;}

    @Override public IGESTarget getTarget() {return block;}
    @Override public IGESAction createInstance(GES ges) {return new GESActionCreate(ges);}
    @Override public SampleBuilder<? extends IGESAction> createBuilder() {return new SampleBuilderVector<GESActionCreate>(this);}
    @Override public boolean showInHUD() {return false;}
}
