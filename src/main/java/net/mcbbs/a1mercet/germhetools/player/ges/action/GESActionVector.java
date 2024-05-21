package net.mcbbs.a1mercet.germhetools.player.ges.action;

import com.germ.germplugin.api.dynamic.effect.GermEffectPart;
import net.mcbbs.a1mercet.germhetools.GermHETools;
import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import net.mcbbs.a1mercet.germhetools.gui.germ.effect.GSquareBox;
import net.mcbbs.a1mercet.germhetools.util.Options;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public abstract class GESActionVector extends GESActionBase
{
    public boolean chunkReload = true;
    public final Vector offset = new Vector();
    public boolean overwrite = false;

    public GESActionVector(GES ges , String id, String name , String type)
    {
        super(ges,id, name, type);
    }

    public Vector calVector()
    {
        HEState state = getHEState();
        if(state==null)return offset;
        Location location = state.location;
        if(overwrite)   return new Vector(offset.getX(),offset.getY(),offset.getZ());
        else            return new Vector(location.getX()-offset.getX(),location.getY()-offset.getY(),location.getZ()-offset.getZ());
    }

    @Override
    public void onStateReSelect()
    {
        offset.zero();
        onShowSample();
    }

    @Override
    public boolean onRevoke(GES ges)
    {
        if(!super.onRevoke(ges))return false;

        clear();
        Vector cal = calVector();
        ges.ps.player.sendMessage("     恢复至>"+ cal.getX()+","+cal.getY()+","+cal.getZ());

        return true;
    }

    @Override
    public boolean onApply(GES ges)
    {
        if(!super.onApply(ges))return false;

        Vector cal = calVector();
        ges.ps.player.sendMessage("     移动至>"+ cal.toString());

        if(chunkReload)
            try {
                Chunk chunk = new Location(ges.ps.player.getWorld(),cal.getX(),cal.getY(),cal.getZ()).getChunk();
                Location playerLocation = ges.ps.player.getLocation();
                ges.ps.player.teleport(new Location(ges.ps.player.getWorld(),10000D,10000D,10000D));

                chunk.unload();
                chunk.load();
                Bukkit.getScheduler().runTaskLater(GermHETools.getInstance(),()->ges.ps.player.teleport(playerLocation),4);
            }catch (Exception e){e.printStackTrace();}


        return true;
    }

    @Override
    public void onShowSample()
    {
        super.onShowSample();
        GermEffectPart<?> e = createEffect();
        Vector cal = calVector();
        e.spawnToLocation(ges.ps.player, cal.getX(),cal.getY(),cal.getZ());
        data.putDefault("effect",e);
    }

    public GermEffectPart<?> createEffect(){return new GSquareBox();}

    @Override
    public void onUnshowSample()
    {
        super.onUnshowSample();
        HEState state = getHEState();   if(state==null)return;
        
        Options.IValue ivalue = data.get("effect");
        if(ivalue!=null&&ivalue.getValue()!=null)
            ((GermEffectPart<?>)ivalue.getValue()).despawn(ges.ps.player);
    }

    public GESActionVector set(double x, double y, double z)    {this.offset.setX(x).setY(y).setZ(z);return this;}
    public GESActionVector clear()                              {return set(0D,0D,0D);}
    public GESActionVector setX(double x)                       {return set(x,offset.getY(),offset.getZ());}
    public GESActionVector setY(double y)                       {return set(offset.getX(),y,offset.getZ());}
    public GESActionVector setZ(double z)                       {return set(offset.getX(),offset.getY(),z);}
    public GESActionVector set(Vector v)                        {return set(v.getX(),v.getY(),v.getZ());}
    public GESActionVector add(Vector v)                        {return set(offset.getX()+v.getX(),offset.getY()+v.getY(),offset.getZ()+v.getZ());}
    public GESActionVector add(double x, double y, double z)    {return set(offset.getX()+x,offset.getY()+y,offset.getZ()+z);}
    public GESActionVector setOverwrite(boolean overwrite)      {this.overwrite = overwrite;return this;}
    public GESActionVector setChunkReload(boolean chunkReload)  {this.chunkReload = chunkReload;return this;}
}
