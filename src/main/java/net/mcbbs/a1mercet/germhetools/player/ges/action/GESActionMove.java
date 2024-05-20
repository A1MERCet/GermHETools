package net.mcbbs.a1mercet.germhetools.player.ges.action;

import com.germ.germplugin.api.dynamic.effect.GermEffectPart;
import net.mcbbs.a1mercet.germhetools.GermHETools;
import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import net.mcbbs.a1mercet.germhetools.player.ges.gui.germ.GSquareBox;
import net.mcbbs.a1mercet.germhetools.util.Options;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class GESActionMove extends GESActionBase implements IGESSample
{
    public final double sourceX,sourceY,sourceZ;
    public double x=0D,y=0D,z=0D;
    public boolean overwrite = false;

    public GESActionMove(GES actioner,HEState state)
    {
        super(actioner,"ges_action_move", "GES-移动", "MOVE",state);
        sourceX=state.location.getX();
        sourceY=state.location.getY();
        sourceZ=state.location.getZ();
    }

    public Location calLocation()
    {
        if(overwrite)   return new Location(state.location.getWorld(), x,y,z);
        else            return new Location(state.location.getWorld(), sourceX-x,sourceY-y,sourceZ-z);
    }

    @Override
    public boolean onRevoke(GES ges)
    {
        if(!super.onRevoke(ges))return false;

        Location cal = calLocation();
        state.location.getBlock().setType(Material.AIR);
        state.place(cal);

        ges.ps.player.sendMessage("     恢复至>"+ cal.getX()+","+cal.getY()+","+cal.getZ());

        return true;
    }

    @Override
    public boolean onApply(GES ges)
    {
        if(!super.onApply(ges))return false;

        Location cal = calLocation();
        Chunk chunk = cal.getChunk();

        state.location.getBlock().setType(Material.AIR);
        state.place(cal);

        Location playerLocation = ges.ps.player.getLocation();
        ges.ps.player.teleport(new Location(ges.ps.player.getWorld(),10000D,10000D,10000D));

        Bukkit.getScheduler().runTaskLater(GermHETools.getInstance(), chunk::unload,5);
        Bukkit.getScheduler().runTaskLater(GermHETools.getInstance(), chunk::load,15);
        Bukkit.getScheduler().runTaskLater(GermHETools.getInstance(),()->ges.ps.player.teleport(playerLocation),30);

        ges.ps.player.sendMessage("     移动至>"+ cal.getX()+","+cal.getY()+","+cal.getZ());

        return true;
    }

    @Override
    public void onShowSample(IGESAction action , GES ges)
    {
        IGESSample.super.onShowSample(action, ges);
        GSquareBox e = new GSquareBox();
        e.spawnToLocation(ges.ps.player, calLocation());
        data.putDefault("effect",e);
    }

    @Override
    public void onUnshowSample(IGESAction action, GES ges)
    {
        IGESSample.super.onUnshowSample(action, ges);
        Options.IValue ivalue = data.get("effect");
        if(ivalue!=null&&ivalue.getValue()!=null)
            ((GermEffectPart<?>)ivalue.getValue()).despawn(ges.ps.player);
    }

    public GESActionMove setMoveX(double x) {this.x = x;return this;}
    public GESActionMove setMoveY(double y) {this.y = y;return this;}
    public GESActionMove setMoveZ(double z) {this.z = z;return this;}
    public GESActionMove setMove(double x,double y,double z) {this.z = z;this.x = x;this.y = y;return this;}
    public GESActionMove setMove(Vector v)  {this.x = v.getX();this.y = v.getY();this.z = v.getZ();return this;}
    public GESActionMove add(Vector v)      {this.x += v.getX();this.y += v.getY();this.z += v.getZ();return this;}
    public GESActionMove add(double x,double y,double z){this.x += x;this.y += y;this.z += z;return this;}
    public GESActionMove setOverwrite(boolean overwrite) {this.overwrite = overwrite;return this;}
    public static Vector getVector(Vector direction)
    {
        Vector v = new Vector(Math.abs(direction.getX()),Math.abs(direction.getY()),Math.abs(direction.getZ()));
        Bukkit.getLogger().warning("V:"+v.toString());

        return v;
    }
}
