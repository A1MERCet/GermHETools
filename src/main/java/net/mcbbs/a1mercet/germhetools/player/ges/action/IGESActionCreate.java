package net.mcbbs.a1mercet.germhetools.player.ges.action;

import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import net.mcbbs.a1mercet.germhetools.player.ges.builder.SampleBuilder;
import net.mcbbs.a1mercet.germhetools.player.ges.builder.SampleBuilderVector;
import net.mcbbs.a1mercet.germhetools.util.UtilPlayer;
import org.bukkit.block.Block;

public class IGESActionCreate extends GESActionVector
{
    public HEState state;
    public IGESActionCreate(GES ges)
    {
        super(ges, "ges_create", "GES-创建", "CREATE");
    }
    public IGESActionCreate setState(HEState state) {
        this.state = state;
        Block ray = UtilPlayer.rayTraceBlock(ges.ps.player.getLocation().add(0D,ges.ps.player.getEyeHeight(),0D),100D);
        if(ray==null){
            state.setLocation(ges.ps.player.getLocation());
        }else {
            state.setLocation(ray.getLocation().add(0D,1D,0D));
        }
        return this;
    }

    @Override public HEState getHEState() {return state;}

    @Override public IGESAction createInstance(GES ges) {return new IGESActionCreate(ges);}
    @Override public SampleBuilder<? extends IGESAction> createBuilder() {return new SampleBuilderVector<IGESActionCreate>(this);}
}
