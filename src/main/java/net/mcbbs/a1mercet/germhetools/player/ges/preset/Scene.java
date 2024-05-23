package net.mcbbs.a1mercet.germhetools.player.ges.preset;

import net.mcbbs.a1mercet.germhetools.gui.germ.GPreset;
import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import net.mcbbs.a1mercet.germhetools.player.ges.target.IGESScene;
import net.mcbbs.a1mercet.germhetools.util.Options;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Scene implements IPreset<Scene> , IGESScene
{
    @Override public String getDefaultPath() {return id;}

    @Override
    public void load(ConfigurationSection section)
    {
        this.id             = section.getString("ID");
        this.name           = section.getString("Name");
        if(section.getConfigurationSection("Data")!=null)this.data.load(section.getConfigurationSection("Data"));
        if(section.getConfigurationSection("Blocks")!=null)
            for(int i = 0;;i++)
                if(section.getConfigurationSection("Blocks."+i)!=null){

                    String type = section.getString("Blocks." + i + ".Type");
                    IPreset<?> s = PresetType.create(type);

                    if (s == null) {Bukkit.getLogger().warning("预设类型 [" + type + "] 创建实例败");continue;}

                    s.load(section.getConfigurationSection("Preset." + i));
                    blocks.add((HEState) s);
                }else {break;}
    }

    @Override
    public void save(ConfigurationSection section)
    {
        IPreset.super.save(section);
        section.set("ID",           id);
        section.set("Name",         name);
        section.set("Data",         null);
        data.save(section.createSection("Data"));
        section.set("Blocks",null);
        for(int i = 0;i<blocks.size();i++)
            blocks.get(i).save(section.createSection("Blocks."+i));
    }

    public String id,name;
    public final String type                    = "SCENE";

    public Options<String> data           = new Options<>();
    public Options<String> targetData     = new Options<>();
    protected final List<HEState> blocks  = new ArrayList<>();
    public final Location location        = new Location(null,0D,0D,0D);
    public final Vector rotate            = new Vector();
    public final Vector scale             = new Vector();
    public final Vector offset            = new Vector();

    public Scene(String id, String name)
    {
        this.id = id;
        this.name = name;
        this.data.putDefault("date",System.currentTimeMillis());
    }

    @Override public String getID() {return id;}
    @Override public String getName() {return name;}

    @Override
    public void copy(IPreset<?> t)
    {
        Scene s = (Scene)t;

        this.id             = s.id;
        this.name           = s.name;
        this.data           = new Options<>(s.data);

        blocks.clear();
        for(HEState block : s.blocks)
        {
            HEState copy = (HEState) block.createInstance();
            copy.copy(block);
            addBlock(copy);
        }
    }

    public void resetLoaction(HEState block)
    {

    }

    public Scene addBlock(HEState block)
    {
        blocks.add(block);
        Location source = block.getLocation();
        Location locCopy = new Location(source.getWorld(), source.getX(), source.getY(),source.getZ());
        locCopy.subtract(getLocation());

        block.id="scene_"+id+"_"+UUID.randomUUID().toString();

        block.getData().putDefault("offset",new Vector());
        block.getData().putDefault("location",locCopy.toVector());
        block.getData().putDefault("rotate",new Vector());
        block.getData().putDefault("scale",new Vector());
        block.getData().putDefault("scene",this);
        return this;
    }

    @Override public GPreset createGPreset(PlayerState ps, int size) {return new GPreset(ps,this,size);}
    @Override public Options<String> getData() {return data;}
    @Override public String getType() {return type;}
    @Override public Options<String> getTargetData() {return targetData;}

    @Override public Location getLocation() {return location;}

    @Override
    public void setLocation(Location loc)
    {
        location.setWorld(loc.getWorld());
        loc.setX(loc.getX());
        loc.setY(loc.getY());
        loc.setZ(loc.getZ());
    }

    @Override
    public boolean place(Location loc)
    {
        World world = loc.getWorld();
        this.location.setWorld(world);
        this.location.setX(loc.getX());
        this.location.setY(loc.getY());
        this.location.setZ(loc.getZ());

        for(HEState block : blocks)
        {
            HEState copy = (HEState)block.createInstance();
            copy.copy(block);

            Vector location = block.getData().getVector("location");
            Vector offset = block.getData().getVector("offset" , new Vector());
            Vector rotate = block.getData().getVector("rotate" , new Vector());
            Vector scale = block.getData().getVector("scale" , new Vector());
            if(location==null){Bukkit.getLogger().warning(block.name+"["+block.id+"] 未找到其坐标");continue;}

            Location placeLoc = location.toLocation(world).add(loc);
            block.getOffset().add(offset).add(this.offset);
            block.getRotate().add(rotate).add(this.rotate);
            block.getScale().add(scale).add(this.scale);
            block.place(placeLoc);
        }
        return false;
    }
    @Override
    public void remove()
    {
        for(HEState state : blocks)
        {
            Location loc = state.getLocation();
            if(loc!=null&&loc.getWorld()!=null)
            {
                HEState real = new HEState().parse(loc.getBlock());
                if(real!=null && !real.id.equals(state.id))
                    continue;
            }

            state.remove();
        }
    }

    @Override public void setOffset(Vector vector)      {offset.copy(vector);}
    @Override public Vector getOffset()                 {return offset;}
    @Override public void setRotate(Vector vector)      {rotate.copy(vector);}
    @Override public Vector getRotate()                 {return rotate;}
    @Override public void setScale(Vector vector)       {scale.copy(vector);}
    @Override public Vector getScale()                  {return scale;}
}
