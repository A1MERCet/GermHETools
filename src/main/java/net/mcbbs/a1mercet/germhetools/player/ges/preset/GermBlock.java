package net.mcbbs.a1mercet.germhetools.player.ges.preset;

import com.germ.germplugin.api.GermBlockAPI;
import com.germ.germplugin.api.dynamic.item.GermItemBlock;
import net.mcbbs.a1mercet.germhetools.gui.germ.GActionPanel;
import net.mcbbs.a1mercet.germhetools.gui.germ.GPreset;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import net.mcbbs.a1mercet.germhetools.player.ges.target.IGESBlock;
import net.mcbbs.a1mercet.germhetools.util.Options;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

import java.util.UUID;

public class GermBlock implements IGESBlock , IPreset<GermBlock>
{

    @Override public String getDefaultPath() {return id;}

    @Override
    public void load(ConfigurationSection section)
    {
        this.id         = section.getString("ID");
        this.name       = section.getString("Name");
        this.offset.copy(section.getVector("Offset", new Vector()));
        this.rotate.copy(section.getVector("Rotate", new Vector()));
        this.scale.copy(section.getVector("Scale",   new Vector()));

        if(section.getConfigurationSection("GermBlock")!=null)
            this.block.loadSrc(section.getConfigurationSection("GermBlock"));

        if(section.getConfigurationSection("Data")!=null)
            data.load(section.getConfigurationSection("Data"));
    }
    @Override
    public void save(ConfigurationSection section)
    {
        IPreset.super.save(section);
        section.set("ID",id);
        section.set("Name",name);
        section.set("Offset",offset);
        section.set("Rotate",rotate);
        section.set("Scale",scale);

        section.set("GermBlock",block.toString());

        section.set("Data",null);
        data.save(section.createSection("Data"));
    }

    public String id;
    public String name;
    public GermItemBlock block          = new GermItemBlock(UUID.randomUUID().toString());
    public final Location location      = new Location(null,0D,0D,0D);
    public final Vector offset          = new Vector();
    public final Vector rotate          = new Vector();
    public final Vector scale           = new Vector();
    public Options<String> data         = new Options<>();
    public Options<String> targetData   = new Options<>();

    public GermBlock(GermItemBlock block)
    {
        this.block.copyFrom(block);
    }
    public GermBlock()
    {

    }

    public GermBlock setID(String id)            {this.id = id;block.setIndexName(id);return this;}
    public GermBlock setName(String name)        {this.name = name;block.setName(name);return this;}

    @Override
    public boolean place(Location loc)
    {
        GermItemBlock b = this.block;
        return true;
    }

    @Override public Location getLocation() {return location;}

    @Override
    public void setLocation(Location loc)
    {
        this.location.setWorld(loc.getWorld());
        this.location.setX(loc.getX());
        this.location.setY(loc.getY());
        this.location.setZ(loc.getZ());
    }

    @Override public void setOffset(Vector vector)  {offset.copy(vector);}
    @Override public Vector getOffset()             {return offset;}
    @Override public void setRotate(Vector vector)  {rotate.copy(vector);}
    @Override public Vector getRotate()             {return rotate;}
    @Override public void setScale(Vector vector)   {scale.copy(vector);}
    @Override public Vector getScale()              {return scale;}
    @Override public Options<String> getTargetData(){return targetData;}

    @Override public String getID()                 {return id;}
    @Override public String getType()               {return "BLOCK";}
    @Override public String getName()               {return name;}

    @Override public IPreset<GermBlock> createInstance() {return new GermBlock();}
    @Override public GPreset createGPreset(PlayerState ps, int size)    {return new GPreset(ps,this,size);}
    @Override public Options<String> getData()                          {return data;}
    @Override public GActionPanel createGActionPanel(GPreset gpreset)   {return IPreset.super.createGActionPanel(gpreset);}

    @Override
    public void copy(IPreset<?> t)
    {
        GermBlock b = (GermBlock)t;
        this.id         = b.id;
        this.name       = b.name;
        this.block      = (GermItemBlock) new GermItemBlock(b.block.getIndexName()).copyFrom(b.block);
        this.block.setIndexName(b.block.getIndexName());
        this.block.setName(b.block.getName());

        setLocation(b.location);
        this.offset.copy(b.offset);
        this.rotate.copy(b.rotate);
        this.scale .copy(b.scale);
        this.data = new Options<>(b.data);
    }
}
