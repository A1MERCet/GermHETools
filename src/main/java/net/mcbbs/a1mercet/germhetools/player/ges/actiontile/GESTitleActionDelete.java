package net.mcbbs.a1mercet.germhetools.player.ges.actiontile;

import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import net.mcbbs.a1mercet.germhetools.player.ges.target.IGESLocation;

public class GESTitleActionDelete extends GESTitleActionBase
{
    public GESTitleActionDelete()
    {
        super("delete", "删除");
    }

    @Override
    public boolean execute(GES ges)
    {
        if(!super.execute(ges))return false;

        if(ges.target instanceof IGESLocation) {
            ((IGESLocation)ges.target).remove();
        }else{
            ges.warn("操作无效 - 目标类型错误");
        }

        return true;
    }

    @Override public int[] getKey() {return new int[]{211,-1};}
}
