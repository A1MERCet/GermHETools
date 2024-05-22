package net.mcbbs.a1mercet.germhetools.player.ges.actiontile;

import net.mcbbs.a1mercet.germhetools.player.ges.GES;

public class GESTitleActionTargetSelect extends GESTitleActionBase
{
    public GESTitleActionTargetSelect()
    {
        super("select", "选取目标");
    }

    @Override
    public boolean execute(GES ges)
    {
        if(!super.execute(ges))return false;

        ges.setTargetPointer(true);

        return true;
    }

    @Override public int[] getKey() {return new int[]{33,-1};}
}
