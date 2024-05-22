package net.mcbbs.a1mercet.germhetools.player.ges.actiontile;

import net.mcbbs.a1mercet.germhetools.gui.germ.GPresetLibrary;
import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import net.mcbbs.a1mercet.germhetools.player.ges.action.GESActionCreate;
import net.mcbbs.a1mercet.germhetools.player.ges.action.GESActionType;
import net.mcbbs.a1mercet.germhetools.player.ges.preset.IPreset;
import net.mcbbs.a1mercet.germhetools.player.ges.target.IGESBlock;
import net.mcbbs.a1mercet.germhetools.util.UtilPlayer;
import org.bukkit.block.Block;

public class GESTitleActionLibrary extends GESTitleActionBase
{
    public GESTitleActionLibrary()
    {
        super("open_library", "预设库");
    }

    @Override
    public boolean execute(GES ges)
    {
        if(!super.execute(ges))return false;

        GPresetLibrary g = new GPresetLibrary(ges.ps);

        g.registerCallbackSelect((p,gprset)->{
            if(gprset.preset instanceof IGESBlock)
            {
                GESActionCreate builder = (GESActionCreate)GESActionType.create("CREATE",ges);

                Block ray = UtilPlayer.rayTraceBlock(ges.ps.player,100D);
                if(ray==null){ges.warn("无法在目标方向放置方块");return;}

                IPreset<?> copy = gprset.preset.createInstance();
                copy.copy(gprset.preset);
                ((IGESBlock)copy).setLocation(ray.getLocation());

                builder.setBlock((IGESBlock) copy);

                ges.setBuilder(builder);
                g.close();
            }
        });

        g.openGuiTo(ges.ps.player);

        return true;
    }

    @Override public int[] getKey() {return new int[]{58,-1};}
}
