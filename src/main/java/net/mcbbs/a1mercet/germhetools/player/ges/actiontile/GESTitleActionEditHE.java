package net.mcbbs.a1mercet.germhetools.player.ges.actiontile;

import net.mcbbs.a1mercet.germhetools.gui.HEGuiManager;
import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import net.mcbbs.a1mercet.germhetools.player.ges.target.IGESTarget;

public class GESTitleActionEditHE extends GESTitleActionBase
{
    public GESTitleActionEditHE()
    {
        super("edit_heblock", "编辑目标");
    }

    @Override
    public boolean execute(GES ges)
    {
        if(!super.execute(ges))return false;

        IGESTarget target = ges.target;
        if(target instanceof HEState) {

            HEState state = (HEState) target;
            HEGuiManager.createHEStateGui(ges.ps.player,state,false).openGuiTo(ges.ps.player);

        }else {
            ges.warn("目标类型错误");
        }

        return true;
    }

    @Override public int[] getKey() {return new int[]{210,-1};}
}
