package net.mcbbs.a1mercet.germhetools.player.ges.actiontile;

import net.mcbbs.a1mercet.germhetools.player.ges.GES;

public class GESTitleActionClearSelect extends GESTitleActionBase
{
    public GESTitleActionClearSelect()
    {
        super("remove_select", "取消选择");
    }

    @Override
    public boolean execute(GES ges)
    {
        if(!super.execute(ges))return false;

        ges.removeTarget(true);

        return true;
    }

    @Override public int[] getKey() {return new int[]{32,29};}
}
