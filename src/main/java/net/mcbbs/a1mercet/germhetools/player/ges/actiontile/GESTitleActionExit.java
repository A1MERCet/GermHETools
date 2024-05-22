package net.mcbbs.a1mercet.germhetools.player.ges.actiontile;

import net.mcbbs.a1mercet.germhetools.player.ges.GES;

public class GESTitleActionExit extends GESTitleActionBase
{
    public GESTitleActionExit()
    {
        super("exit", "退出GES");
    }

    @Override
    public boolean execute(GES ges)
    {
        if(!super.execute(ges))return false;

        ges.setEnable(false);

        return true;
    }

    @Override public boolean showInTitle() {return false;}
}
