package net.mcbbs.a1mercet.germhetools.player.ges.actiontile;

import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import net.mcbbs.a1mercet.germhetools.player.ges.action.GESActionCreate;
import net.mcbbs.a1mercet.germhetools.util.UtilPlayer;
import org.bukkit.block.Block;

public class GESTitleActionCreateHEBlock extends GESTitleActionBase
{
    public GESTitleActionCreateHEBlock()
    {
        super("create_heblock", "创建(HE方块)");
    }

    @Override
    public boolean execute(GES ges)
    {
        if(!super.execute(ges))return false;

        Block ray = UtilPlayer.rayTraceBlock(ges.ps.player.getLocation().add(0D,ges.ps.player.getEyeHeight(),0D),100D);

        ges.removeBuilder();

        HEState state = new HEState();
        if(ray!=null) state.setLocation(ray.getLocation().add(0D,1D,0D));
        ges.setTarget(state);

        GESActionCreate action = new GESActionCreate(ges).setBlock(state);
        ges.setBuilder(action);

        return true;
    }

    @Override public int[] getKey() {return new int[]{49,-1};}
}
