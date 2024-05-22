package net.mcbbs.a1mercet.germhetools.player.ges.action;

import net.mcbbs.a1mercet.germhetools.GermHETools;
import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import net.mcbbs.a1mercet.germhetools.player.ges.target.IGESBlock;
import net.mcbbs.a1mercet.germhetools.player.ges.target.IGESLocation;
import net.mcbbs.a1mercet.germhetools.util.UtilPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public abstract class GESActionVector extends GESActionBase
{
    public boolean chunkReload = true;
    public final Vector value = new Vector();
    public boolean overwrite = false;

    public GESActionVector(GES ges , String id, String name , String type)
    {
        super(ges,id, name, type);
    }

    public Vector calTargetVector()
    {
        IGESLocation state = (IGESLocation) getTarget();
        if(state==null)return value;
        Location location = state.getLocation();
        if(overwrite)   return new Vector(value.getX(), value.getY(), value.getZ());
        else            return new Vector(location.getX()+value.getX(),location.getY()+value.getY(),location.getZ()+value.getZ());
    }

    @Override
    public boolean onTargetReSelect()
    {
        value.zero();
        if(overwrite && getTarget()!=null)value.add(((IGESLocation)getTarget()).getLocation().toVector());
        onShowSample();
        return true;
    }

    @Override
    public boolean onRevoke(GES ges)
    {
        if(!super.onRevoke(ges))return false;

        clear();
        onApply(ges);

        return true;
    }

    public void onApplyTargetData()
    {

    }

    @Override
    public boolean onApply(GES ges)
    {
        if(!super.onApply(ges))return false;

        onApplyTargetData();

        updateBlockData();

        return true;
    }
    public void updateBlockData()
    {
        onUnshowSample();
        if(getTarget() instanceof IGESBlock) {
            ges.setTargetBlock(((IGESBlock) getTarget()).getBlock());
            Bukkit.getScheduler().runTaskLater(GermHETools.getInstance(),()->UtilPlayer.updateBlockData(ges.ps.player, ((IGESBlock)getTarget()).getLocation().getBlock()),2);
        }
        onShowSample();
    }

    @Override
    public Location getEffectLocation() {
        Vector cal = calTargetVector();
        return new Location(ges.ps.player.getWorld(),cal.getX(),cal.getY(),cal.getZ());
    }

    public GESActionVector clear()
    {
        if(overwrite){
            IGESLocation target = (IGESLocation)getTarget();
            set(target.getLocation().toVector());
        }else {
            set(0D,0D,0D);
        }
        return this;
    }


    public GESActionVector set(double x, double y, double z)    {this.value.setX(x).setY(y).setZ(z);return this;}
    public GESActionVector setX(double x)                       {return set(x, value.getY(), value.getZ());}
    public GESActionVector setY(double y)                       {return set(value.getX(),y, value.getZ());}
    public GESActionVector setZ(double z)                       {return set(value.getX(), value.getY(),z);}
    public GESActionVector set(Vector v)                        {return set(v.getX(),v.getY(),v.getZ());}
    public GESActionVector add(Vector v)                        {return set(value.getX()+v.getX(), value.getY()+v.getY(), value.getZ()+v.getZ());}
    public GESActionVector add(double x, double y, double z)    {return set(value.getX()+x, value.getY()+y, value.getZ()+z);}
    public GESActionVector setChunkReload(boolean chunkReload)  {this.chunkReload = chunkReload;return this;}
    public GESActionVector setOverwrite(boolean overwrite)
    {
        this.overwrite = overwrite;return this;
    }
}
