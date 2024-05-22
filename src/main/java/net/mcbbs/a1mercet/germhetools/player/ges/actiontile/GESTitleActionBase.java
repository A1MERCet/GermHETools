package net.mcbbs.a1mercet.germhetools.player.ges.actiontile;

import net.mcbbs.a1mercet.germhetools.player.ges.GES;

public abstract class GESTitleActionBase implements IGESTitleAction
{
    public final String id,name;

    public GESTitleActionBase(String id, String name)
    {
        this.id = id;
        this.name = name;
    }

    @Override public boolean execute(GES ges)
    {
        return true;
    }
    @Override public String getID() {return id;}
    @Override public String getName() {return name;}
}
