package net.mcbbs.a1mercet.germhetools.player.ges.action;

import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import net.mcbbs.a1mercet.germhetools.util.Options;

public abstract class GESActionBase implements IGESAction
{
    public final GES actioner;
    public final Options<String> data = new Options<>();
    public final String id,name,type;
    public final HEState state;

    public GESActionBase(GES actioner , String id, String name , String type , HEState state)
    {
        this.actioner=actioner;
        this.id    = id;
        this.name  = name;
        this.type  = type;
        this.state = state;
    }

    @Override public Options<?> getData()           {return data;}
    @Override public String getID()                 {return id;}
    @Override public String getName()               {return name;}
    @Override public String getType()               {return type;}
    @Override public HEState getHEState()           {return state;}
}
