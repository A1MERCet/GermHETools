package net.mcbbs.a1mercet.germhetools.player.ges.action;

import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.ges.GES;

public class GESActionRotate extends GESActionBase
{
    public final double sourceX,sourceY,sourceZ;
    public double x=0D,y=0D,z=0D;
    public boolean overwrite = false;

    public GESActionRotate(GES actioner,HEState state)
    {
        super(actioner,"ges_action_rotate", "GES-旋转", "ROTATE",state);
        sourceX=state.rotate.getX();
        sourceY=state.rotate.getY();
        sourceZ=state.rotate.getZ();
    }

    @Override
    public boolean onRevoke(GES ges)
    {
        if(!super.onRevoke(ges))return false;

        if(overwrite)   state.setRotate(x,y,z);
        else            state.setRotate(sourceX+x,sourceY+y,sourceZ+z);

        state.place();

        return true;
    }

    @Override
    public boolean onApply(GES ges)
    {
        if(!super.onApply(ges))return false;

        if(overwrite)   state.setRotate(x,y,z);
        else            state.setRotate(sourceX+x,sourceY+y,sourceZ+z);

        state.place();

        return true;
    }

    public GESActionRotate setRotateX(double x) {this.x = x;return this;}
    public GESActionRotate setRotateY(double y) {this.y = y;return this;}
    public GESActionRotate setRotateZ(double z) {this.z = z;return this;}
    public GESActionRotate setRotate(double x, double y, double z) {this.z = z;this.x = x;this.y = y;return this;}
    public GESActionRotate setOverwrite(boolean overwrite) {this.overwrite = overwrite;return this;}
}
