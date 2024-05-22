package net.mcbbs.a1mercet.germhetools.he;

import net.mcbbs.a1mercet.germhetools.api.BlockManager;
import net.mcbbs.a1mercet.germhetools.gui.HEGuiManager;
import net.mcbbs.a1mercet.germhetools.gui.germ.GActionPanel;
import net.mcbbs.a1mercet.germhetools.gui.germ.GPreset;
import net.mcbbs.a1mercet.germhetools.gui.germ.GPresetHEBlock;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import net.mcbbs.a1mercet.germhetools.player.ges.target.IGESBlock;
import net.mcbbs.a1mercet.germhetools.player.ges.preset.IPreset;
import net.mcbbs.a1mercet.germhetools.util.Options;
import net.mcbbs.a1mercet.germhetools.util.UtilConfig;
import net.mcbbs.a1mercet.germhetools.util.UtilNBT;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class HEState implements IPreset<HEState> , IGESBlock
{

    @Override public String getDefaultPath() {return id;}

    @Override
    public void save(ConfigurationSection section)
    {
        IPreset.super.save(section);

        section.set("ID",               id);
        section.set("Name",             name);
        section.set("Model",            model);
        section.set("Texture",          texture);

        section.set("Size",             size);
        section.set("FollowStyle",      followStyle);
        section.set("Passable",         passable);
        section.set("EnableGlowTexture",enableGlowTexture);
        section.set("GlowTexture",      glowTexture);

        section.set("Transform",    transform.toString());
        section.set("Scale",        scale.toString());
        section.set("Rotate",       rotate.toString());
        section.set("AABBMax",      aabbMax.toString());
        section.set("AABBMin",      aabbMin.toString());

        data.save(section.createSection("data"));

        section.set("Material",     material);
    }

    @Override
    public void load(ConfigurationSection section)
    {
        id                  = section.getString("ID");
        name                = section.getString("Name");
        model               = section.getString("Model");
        texture             = section.getString("Texture");

        followStyle         = section.getString("FollowStyle");
        size                = section.getDouble("Size");
        passable            = section.getBoolean("Passable");
        enableGlowTexture   = section.getBoolean("EnableGlowTexture");
        glowTexture         = section.getString("GlowTexture");

        UtilConfig.toVector(transform,section.getString("Transform"));
        UtilConfig.toVector(scale,section.getString("Scale"));
        UtilConfig.toVector(rotate,section.getString("Rotate"));
        UtilConfig.toVector(aabbMax,section.getString("AABBMax"));
        UtilConfig.toVector(aabbMin,section.getString("AABBMin"));

        if(section.getConfigurationSection("data")!=null)
            data.load(section.getConfigurationSection("data"));

        material.clear();
        material.addAll(section.getStringList("Material"));
    }

    public String id = "hestate";
    public String name = "HEState";
    public String model = "";
    public String texture = "";

    public final Location location = new Location(null,0,0,0);
    public final Vector transform = new Vector();
    public final Vector scale = new Vector();
    public final Vector rotate = new Vector();
    public final Vector aabbMax = new Vector();
    public final Vector aabbMin = new Vector();
    public double size;
    public String followStyle = "static";
    public boolean passable = false;
    public boolean enableGlowTexture = false;
    public String glowTexture = "";
    public Options<String> data = new Options<>();
    public Options<String> targetData = new Options<>();

    public final List<String> material = new ArrayList<>();

    public HEState()
    {
    }
    public HEState(HEState s)
    {
        copy(s);
    }

    public ItemStack createItemStack()
    {
        ItemStack i = new ItemStack(BlockManager.material);

        NBTTagCompound tag = UtilNBT.getItemNBT(i);
        NBTTagCompound cutomsBlock = new NBTTagCompound();
        cutomsBlock.set("CustomBlock",createTag());
        tag.set("tag",cutomsBlock);
        return UtilNBT.saveItemNBT(i,tag);
    }

    @Override
    public boolean place(Location loc)
    {
        setLocation(loc);

        Block b = location.getBlock();
        if(!(BlockManager.material.name().equals(b.getType().name()))){
            b.setType(Material.valueOf(BlockManager.material.name()));
        }

        NBTTagCompound tag = UtilNBT.getBlockNBT(b);
        tag.set("CustomBlock",createTag());

        UtilNBT.saveBlockNBT(b,tag);
        return true;
    }
    public boolean updateToBlock()
    {
        Block b = location.getBlock();
        NBTTagCompound tag = UtilNBT.getBlockNBT(b);
        tag.set("CustomBlock",createTag());

        UtilNBT.saveBlockNBT(b,tag);

        return true;
    }

    public NBTTagCompound createTag()
    {
        NBTTagCompound layer_CustomBlock = new NBTTagCompound();
        layer_CustomBlock.setByte("enableGlowTexture",(byte) (glowTexture!=null&&!glowTexture.equals("")?1:0));
        layer_CustomBlock.setByte("passable",(byte) (passable?1:0));
        layer_CustomBlock.setDouble("size",size);
        layer_CustomBlock.setString("followStyle",followStyle);
        layer_CustomBlock.setString("glowTexture",glowTexture);
        layer_CustomBlock.setString("model",model);
        layer_CustomBlock.setString("texture",texture);

        NBTTagCompound layer_aabb = new NBTTagCompound();
        layer_aabb.setDouble("maxx",(float) aabbMax.getX());
        layer_aabb.setDouble("maxy",(float) aabbMax.getY());
        layer_aabb.setDouble("maxz",(float) aabbMax.getZ());
        layer_aabb.setDouble("minx",(float) aabbMin.getX());
        layer_aabb.setDouble("miny",(float) aabbMin.getY());
        layer_aabb.setDouble("minz",(float) aabbMin.getZ());
        layer_CustomBlock.set("aabb",layer_aabb);

        NBTTagCompound layer_rotate = new NBTTagCompound();
        layer_rotate.setFloat("pitch",(float) rotate.getX());
        layer_rotate.setFloat("roll",(float) rotate.getY());
        layer_rotate.setFloat("yaw",(float) rotate.getZ());
        layer_CustomBlock.set("rotate",layer_rotate);

        NBTTagCompound layer_scale = new NBTTagCompound();
        layer_scale.setDouble("length",(float) scale.getX());
        layer_scale.setDouble("height",(float) scale.getY());
        layer_scale.setDouble("width",(float) scale.getZ());
        layer_CustomBlock.set("scale",layer_scale);

        NBTTagCompound layer_transform = new NBTTagCompound();
        layer_transform.setDouble("x",(float) transform.getX());
        layer_transform.setDouble("y",(float) transform.getY());
        layer_transform.setDouble("z",(float) transform.getZ());
        layer_CustomBlock.set("translate",layer_transform);

        NBTTagCompound layer_material = new NBTTagCompound();
        for(String m : material)
        {
            String name = m.split(":")[0];
            String path = m.replace(name+":","");
            layer_material.setString(name,path);
        }
        layer_CustomBlock.set("materials",layer_material);

        return layer_CustomBlock;
    }

    public HEState parse(ItemStack isk)
    {
        NBTTagCompound tag = UtilNBT.getItemNBT(isk);

        if(tag.hasKey("tag")){
            return parse(tag.getCompound("tag").getCompound("CustomBlock"));
        }else {Bukkit.getLogger().warning("tag \"Tag \\ CustomBlock\" not find");}

        return this;
    }
    public HEState parse(Block b)
    {
        if(!BlockManager.material.name().equals(b.getType().name()))return null;

        setLocation(b.getLocation());

        NBTTagCompound tag = UtilNBT.getBlockNBT(b);

        if(tag.hasKey("CustomBlock"))
            return parse(tag.getCompound("CustomBlock"));
        return this;
    }
    public HEState parse(NBTTagCompound tag)
    {
        if(tag.hasKey("enableGlowTexture")) {enableGlowTexture= tag.getByte("enableGlowTexture") == 1;}
        if(tag.hasKey("passable"))          {passable= tag.getByte("passable") == 1;}
        if(tag.hasKey("size"))              {size= tag.getDouble("size");}
        if(tag.hasKey("followStyle"))       {followStyle= tag.getString("followStyle");}
        if(tag.hasKey("glowTexture"))       {glowTexture= tag.getString("glowTexture");}
        if(tag.hasKey("model"))             {model= tag.getString("model");}
        if(tag.hasKey("texture"))           {texture= tag.getString("texture");}

        if(tag.hasKey("aabb"))
        {
            NBTTagCompound layer = tag.getCompound("aabb");
            setAABB(
                    layer.getDouble("minx"),
                    layer.getDouble("miny"),
                    layer.getDouble("minz"),
                    layer.getDouble("maxx"),
                    layer.getDouble("maxy"),
                    layer.getDouble("maxz")
            );
        }

        if(tag.hasKey("rotate"))
        {
            NBTTagCompound layer = tag.getCompound("rotate");
            setRotate(
                    layer.getDouble("pitch"),
                    layer.getDouble("roll"),
                    layer.getDouble("yaw")
            );
        }

        if(tag.hasKey("scale"))
        {
            NBTTagCompound layer = tag.getCompound("scale");
            setScale(
                    layer.getDouble("length"),
                    layer.getDouble("height"),
                    layer.getDouble("width")
            );
        }

        if(tag.hasKey("translate"))
        {
            NBTTagCompound layer = tag.getCompound("translate");
            setTransform(
                    layer.getDouble("x"),
                    layer.getDouble("y"),
                    layer.getDouble("z")
            );
        }

        if(tag.hasKey("materials"))
        {
            NBTTagCompound layer = tag.getCompound("materials");
            material.clear();
            for(String s : layer.c())
                material.add(s+":"+layer.getString(s));
        }
        return this;
    }

    public HEState setLocation(World w , double x , double y , double z){location.setWorld(w);location.setX(x);location.setY(y);location.setZ(z);return this;}
    public void setLocation(Location l){location.setWorld(l.getWorld());location.setX(l.getX());location.setY(l.getY());location.setZ(l.getZ());}
    public HEState setSize(double size) {this.size = size;return this;}
    public HEState setTransform(double x, double y, double z){transform.setX(x).setY(y).setZ(z);return this;}
    public HEState setRotate(double x, double y, double z){rotate.setX(x).setY(y).setZ(z);return this;}
    public HEState setScale(double x, double y, double z){scale.setX(x).setY(y).setZ(z);return this;}
    public HEState setEnableGlowTexture(boolean enableGlowTexture) {this.enableGlowTexture = enableGlowTexture;return this;}
    public HEState setPassable(boolean passable) {this.passable = passable;return this;}
    public HEState setGlowTexture(String glowTexture) {this.glowTexture = glowTexture;return this;}
    public HEState setModel(String model) {this.model = model;return this;}
    public HEState setTexture(String texture) {this.texture = texture;return this;}
    public HEState setAABB(double minx, double miny, double minz, double maxx, double maxy, double maxz){aabbMin.setX(minx).setY(miny).setZ(minz);aabbMax.setX(maxx).setY(maxy).setZ(maxz);return this;}
    public HEState setAABBMin(double minx, double miny, double minz){aabbMin.setX(minx).setY(miny).setZ(minz);return this;}
    public HEState setAABBMax(double maxx, double maxy, double maxz){aabbMax.setX(maxx).setY(maxy).setZ(maxz);return this;}
    public HEState setId(String id) {this.id = id;return this;}
    public HEState setName(String name) {this.name = name;return this;}

    @Override public GPreset createGPreset(PlayerState ps , int size) {GPreset g = new GPresetHEBlock(ps, this,size);g.build();return g;}
    @Override public String getType() {return "BLOCK";}
    @Override public String getID() {return id;}
    @Override public String getName() {return name;}
    @Override public long getAddDate() {return data.getLong("date");}
    @Override public HEState getObject() {return this;}
    @Override public Options<String> getData() {return data;}
    @Override public void copy(IPreset<?> preset) {
        HEState s = (HEState) preset.getObject();

        this.id=s.id;
        this.name=s.name;
        setLocation(s.location);
        setTransform(s.transform.getX(),s.transform.getY(),s.transform.getZ());
        setScale(s.scale.getX(),s.scale.getY(),s.scale.getZ());
        setRotate(s.rotate.getX(),s.rotate.getY(),s.rotate.getZ());
        setAABB(s.aabbMin.getX(),s.aabbMin.getY(),s.aabbMin.getZ(),s.aabbMax.getX(),s.aabbMax.getY(),s.aabbMax.getZ());
        this.followStyle=s.followStyle;
        this.size=s.size;
        this.enableGlowTexture=s.enableGlowTexture;
        this.passable=s.passable;
        this.glowTexture=s.glowTexture;
        this.model=s.model;
        this.texture=s.texture;
        this.material.addAll(s.material);
        this.data=new Options<>(s.data);
    }

    @Override
    public GActionPanel createGActionPanel(GPreset gPreset) {
        PlayerState ps = gPreset.ps;
        GActionPanel g =  IPreset.super.createGActionPanel(gPreset);

        g.addAction(new GActionPanel.Action("create","创建物品"){
            @Override public boolean callback(GActionPanel parent) {
                if(!super.callback(parent))return false;

                parent.ps.player.getInventory().addItem(createItemStack());
                parent.close();

                return true;
            }},3);
        g.addAction(new GActionPanel.Action("edit","编辑"){
            @Override public boolean callback(GActionPanel parent) {
                if(!super.callback(parent))return false;

                HEGuiManager.createHEStateGui(ps.player, HEState.this,false).openGuiTo(ps.player);
                parent.close();

                return true;
            }},3);

        return g;
    }


    @Override public Location getLocation()                     {return location;}
    @Override public void remove()                              {if(location.getWorld()!=null)location.getBlock().setType(Material.AIR);}
    @Override public void setOffset(Vector vector)              {transform.copy(vector);}
    @Override public Vector getOffset()                         {return transform;}
    @Override public void setRotate(Vector vector)              {rotate.copy(vector);}
    @Override public Vector getRotate()                         {return rotate;}
    @Override public void setScale(Vector vector)               {scale.copy(vector);}
    @Override public Vector getScale()                          {return scale;}

    @Override public Object getTarget() {return this;}
    @Override public Options<String> getTargetData() {return targetData;}
}
