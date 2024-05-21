package net.mcbbs.a1mercet.germhetools.player.ges.action;

import com.germ.germplugin.api.dynamic.effect.GermEffectPart;
import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import net.mcbbs.a1mercet.germhetools.player.ges.builder.SampleBuilderVector;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class GESActionScale extends GESActionVector
{
    public GESActionScale(GES ges)
    {
        super(ges,"ges_rotate", "GES-旋转", "ROTATE");
    }

    @Override
    public boolean onApply(GES ges)
    {
        if(!super.onApply(ges))return false;
        HEState state = getHEState();
        Vector cal = calVector();

        state.location.getBlock().setType(Material.AIR);
        state.setScale(cal.getX(),cal.getY(),cal.getZ());
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

        state.setScale(cal.getX(),cal.getY(),cal.getZ());
        state.place();
        ges.setHEState(state.location.getBlock());
        return true;
    }

    @Override
    public Vector calVector()
    {
        HEState state = getHEState();
        if(state==null)return offset;

        Vector rotate = state.scale;
        if(overwrite)   return new Vector(offset.getX(),offset.getY(),offset.getZ());
        else            return new Vector(rotate.getX()+offset.getX(),rotate.getY()+offset.getY(),rotate.getZ()+offset.getZ());
    }

    @Override
    public void onShowSample()
    {
        HEState state = getHEState();   if(state==null)return;
        Location location = state.location;
        GermEffectPart<?> e = createEffect();
        Vector cal = calVector();

        e.spawnToLocation(ges.ps.player, location.getX(),location.getY(),location.getZ());
        data.putDefault("effect",e);
        ges.ps.player.sendMessage("缩放["+cal.toString()+"]");
    }

    @Override public IGESAction createInstance(GES ges) {return new GESActionScale(ges);}

    @Override public SampleBuilderVector<? extends IGESAction> createBuilder() {return new SampleBuilderVector<GESActionScale>(this);}
}
