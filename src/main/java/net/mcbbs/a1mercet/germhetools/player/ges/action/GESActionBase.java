package net.mcbbs.a1mercet.germhetools.player.ges.action;

import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import net.mcbbs.a1mercet.germhetools.util.Options;

public abstract class GESActionBase implements IGESAction
{
    public final GES ges;
    public final Options<String> data = new Options<>();
    public final String id,name,type;

    public GESActionBase(GES ges, String id, String name , String type)
    {
        this.ges   = ges;
        this.id    = id;
        this.name  = name;
        this.type  = type;
    }

    @Override public Options<?> getData()           {return data;}
    @Override public String getID()                 {return id;}
    @Override public String getName()               {return name;}
    @Override public String getType()               {return type;}
    @Override public HEState getHEState()           {return ges.current;}

    @Override public GES getGES() {return ges;}
}
