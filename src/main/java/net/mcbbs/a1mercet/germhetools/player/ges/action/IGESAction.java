package net.mcbbs.a1mercet.germhetools.player.ges.action;

import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import net.mcbbs.a1mercet.germhetools.util.Options;

public interface IGESAction
{
    String getID();
    String getName();
    String getType();
    Options<?> getData();
    HEState getHEState();

    default boolean onApply(GES ges)
    {
        ges.ps.player.sendMessage("已应用操作["+getName()+"]");
        return true;
    }
    default boolean onRevoke(GES ges)
    {
        ges.ps.player.sendMessage("已撤回操作["+getName()+"]");
        return true;
    }
}
