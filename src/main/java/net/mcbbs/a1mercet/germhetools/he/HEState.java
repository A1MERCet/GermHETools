package net.mcbbs.a1mercet.germhetools.he;

import com.tuershen.nbtlibrary.api.NBTTagCompoundApi;
import com.tuershen.nbtlibrary.api.TileEntityCompoundApi;
import com.tuershen.nbtlibrary.minecraft.nbt.*;
import net.mcbbs.a1mercet.germhetools.GermHETools;
import net.mcbbs.a1mercet.germhetools.api.BlockManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class HEState
{
    public final Location location = new Location(null,0,0,0);
    public final Vector transform = new Vector();
    public final Vector scale = new Vector();
    public final Vector rotate = new Vector();
    public final Vector aabbMax = new Vector();
    public final Vector aabbMin = new Vector();
    public String followStyle = "static";
    public double size;
    public boolean enableGlowTexture = false;
    public boolean passable = false;

    public String glowTexture = "";
    public String model = "";
    public String texture = "";
    public final List<String> material = new ArrayList<>();

    public HEState()
    {
    }
    public HEState(HEState s)
    {
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
    }

    public ItemStack createItemStack()
    {
        ItemStack i = new ItemStack(BlockManager.material);

        NBTTagCompoundApi tagApi = GermHETools.libraryApi.getCompound(i);
        TagCompound<TagBase> tagBase = tagApi.getNBTTagCompoundApi();
        tagBase.put("CustomBlock",createTag());
        tagApi.setCompoundMap(tagBase);
        return GermHETools.libraryApi.setCompound(i,tagApi);
    }
    public boolean place(){return place(null);}
    public boolean place(Location loc)
    {
        if(loc!=null)setLocation(loc);

        Block b = location.getBlock();
        if(!(BlockManager.material.name().equals(b.getType().name()))){
            b.setType(Material.valueOf(BlockManager.material.name()));
            Bukkit.getLogger().warning("placed");
        }

        TileEntityCompoundApi api = GermHETools.libraryApi.getTileEntityCompoundApi(b);
        NBTTagCompoundApi tagApi = api.getNBTTagCompound();
        TagCompound<TagBase> tagBase = tagApi.getNBTTagCompoundApi();
        tagBase.put("CustomBlock",createTag());

        api.saveNBTTag(tagApi);

        return true;
    }
    public boolean updateToBlock()
    {
        Block b = location.getBlock();
        TileEntityCompoundApi api = GermHETools.libraryApi.getTileEntityCompoundApi(b);
        NBTTagCompoundApi tagApi = api.getNBTTagCompound();
        TagCompound<TagBase> tagBase = tagApi.getNBTTagCompoundApi();
        tagBase.put("CustomBlock",createTag());

        api.saveNBTTag(tagApi);
        return true;
    }

    public TagCompound<? extends TagBase> createTag()
    {
        TagCompound<TagBase> layer_CustomBlock = new TagCompound<>();
        layer_CustomBlock.put("enableGlowTexture",new TagByte((byte) (glowTexture!=null&&!glowTexture.equals("")?1:0)));
        layer_CustomBlock.put("passable",new TagByte((byte) (passable?1:0)));
        layer_CustomBlock.put("size",new TagDouble(size));
        layer_CustomBlock.put("followStyle",new TagString(followStyle));
        layer_CustomBlock.put("glowTexture",new TagString(glowTexture));
        layer_CustomBlock.put("model",new TagString(model));
        layer_CustomBlock.put("texture",new TagString(texture));

        TagCompound<TagBase> layer_aabb = new TagCompound<>();
        layer_aabb.put("maxx",new TagDouble((float) aabbMax.getX()));
        layer_aabb.put("maxy",new TagDouble((float) aabbMax.getY()));
        layer_aabb.put("maxz",new TagDouble((float) aabbMax.getZ()));
        layer_aabb.put("minx",new TagDouble((float) aabbMin.getX()));
        layer_aabb.put("miny",new TagDouble((float) aabbMin.getY()));
        layer_aabb.put("minz",new TagDouble((float) aabbMin.getZ()));
        layer_CustomBlock.put("aabb",layer_aabb);

        TagCompound<TagBase> layer_rotate = new TagCompound<>();
        layer_rotate.put("pitch",new TagDouble((float) rotate.getX()));
        layer_rotate.put("roll",new TagDouble((float) rotate.getY()));
        layer_rotate.put("yaw",new TagDouble((float) rotate.getZ()));
        layer_CustomBlock.put("rotate",layer_rotate);

        TagCompound<TagBase> layer_scale = new TagCompound<>();
        layer_scale.put("length",new TagDouble((float) scale.getX()));
        layer_scale.put("height",new TagDouble((float) scale.getY()));
        layer_scale.put("width",new TagDouble((float) scale.getZ()));
        layer_CustomBlock.put("scale",layer_scale);

        TagCompound<TagBase> layer_transform = new TagCompound<>();
        layer_transform.put("x",new TagDouble((float) transform.getX()));
        layer_transform.put("y",new TagDouble((float) transform.getY()));
        layer_transform.put("z",new TagDouble((float) transform.getZ()));
        layer_CustomBlock.put("translate",layer_transform);

        TagCompound<TagBase> layer_material = new TagCompound<>();
        for(String m : material)
        {
            String name = m.split(":")[0];
            String path = m.replace(name+":","");
            layer_material.put(name,new TagString(path));
        }
        layer_CustomBlock.put("materials",layer_material);

        return layer_CustomBlock;
    }

    public HEState parse(ItemStack isk)
    {
        NBTTagCompoundApi tagApi = GermHETools.libraryApi.getCompound(isk);
        TagCompound<TagBase> tagBase = tagApi.getNBTTagCompoundApi();

//        if(tagBase.hasKey("tag")){
//            if(tagBase.getCompound("tag").hasKey("CustomBlock")){
//                return parse(tagBase.getCompound("tag").getCompound("CustomBlock"));
//            }else {Bukkit.getLogger().warning("sss2");}
//        }else {Bukkit.getLogger().warning("sss");}

        if(tagBase.hasKey("CustomBlock")){
            return parse(tagBase.getCompound("CustomBlock"));
        }else {Bukkit.getLogger().warning("tag \"CustomBlock\" not find");}

        return this;
    }
    public HEState parse(Block b)
    {
        if(!BlockManager.material.name().equals(b.getType().name()))return null;

        setLocation(b.getLocation());
        TileEntityCompoundApi api = GermHETools.libraryApi.getTileEntityCompoundApi(b);
        NBTTagCompoundApi tagApi = api.getNBTTagCompound();
        TagCompound<TagBase> tagBase = tagApi.getNBTTagCompoundApi();
        if(tagBase.hasKey("CustomBlock"))
            return parse(tagBase.getCompound("CustomBlock"));
        return this;
    }
    public HEState parse(TagCompound<? extends TagBase> tag)
    {
        if(tag.hasKey("enableGlowTexture")) {enableGlowTexture= tag.getByte("enableGlowTexture").getData() == 1;}
        if(tag.hasKey("passable"))          {passable= tag.getByte("passable").getData() == 1;}
        if(tag.hasKey("size"))              {size= tag.getDouble("size").getData();}
        if(tag.hasKey("followStyle"))       {followStyle= tag.getString("followStyle").getData();}
        if(tag.hasKey("glowTexture"))       {glowTexture= tag.getString("glowTexture").getData();}
        if(tag.hasKey("model"))             {model= tag.getString("model").getData();}
        if(tag.hasKey("texture"))           {texture= tag.getString("texture").getData();}

        if(tag.hasKey("aabb"))
        {
            TagCompound<TagBase> layer = tag.getCompound("aabb");
            setAABB(
                    layer.getDouble("minx").getData(),
                    layer.getDouble("miny").getData(),
                    layer.getDouble("minz").getData(),
                    layer.getDouble("maxx").getData(),
                    layer.getDouble("maxy").getData(),
                    layer.getDouble("maxz").getData()
            );
        }

        if(tag.hasKey("rotate"))
        {
            TagCompound<TagBase> layer = tag.getCompound("rotate");
            setRotate(
                    layer.getDouble("pitch").getData(),
                    layer.getDouble("roll").getData(),
                    layer.getDouble("yaw").getData()
            );
        }

        if(tag.hasKey("scale"))
        {
            TagCompound<TagBase> layer = tag.getCompound("scale");
            setScale(
                    layer.getDouble("length").getData(),
                    layer.getDouble("height").getData(),
                    layer.getDouble("width").getData()
            );
        }

        if(tag.hasKey("translate"))
        {
            TagCompound<TagBase> layer = tag.getCompound("translate");
            setTransform(
                    layer.getDouble("x").getData(),
                    layer.getDouble("y").getData(),
                    layer.getDouble("z").getData()
            );
        }

        if(tag.hasKey("materials"))
        {
            TagCompound<TagBase> layer = tag.getCompound("materials");
            material.clear();
            for(String s : layer.getMap().keySet())
                material.add(s+":"+layer.getString(s).getData());
        }
        return this;
    }

    public HEState setLocation(World w , double x , double y , double z){location.setWorld(w);location.setX(x);location.setY(y);location.setZ(z);return this;}
    public HEState setLocation(Location l){location.setWorld(l.getWorld());location.setX(l.getX());location.setY(l.getY());location.setZ(l.getZ());return this;}
    public HEState setSize(double size) {this.size = size;return this;}
    public HEState setTransform(double x, double y,double z){transform.setX(x).setY(y).setZ(z);return this;}
    public HEState setRotate(double x, double y,double z){rotate.setX(x).setY(y).setZ(z);return this;}
    public HEState setScale(double x, double y,double z){scale.setX(x).setY(y).setZ(z);return this;}
    public HEState setEnableGlowTexture(boolean enableGlowTexture) {this.enableGlowTexture = enableGlowTexture;return this;}
    public HEState setPassable(boolean passable) {this.passable = passable;return this;}
    public HEState setGlowTexture(String glowTexture) {this.glowTexture = glowTexture;return this;}
    public HEState setModel(String model) {this.model = model;return this;}
    public HEState setTexture(String texture) {this.texture = texture;return this;}
    public HEState setAABB(double minx,double miny,double minz,double maxx, double maxy,double maxz){aabbMin.setX(minx).setY(miny).setZ(minz);aabbMax.setX(maxx).setY(maxy).setZ(maxz);return this;}
    public HEState setAABBMin(double minx,double miny,double minz){aabbMin.setX(minx).setY(miny).setZ(minz);return this;}
    public HEState setAABBMax(double maxx, double maxy,double maxz){aabbMax.setX(maxx).setY(maxy).setZ(maxz);return this;}
}
