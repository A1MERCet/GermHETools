package net.mcbbs.a1mercet.germhetools.player.ges.actiontile;

import net.mcbbs.a1mercet.germhetools.player.ges.GES;

public interface IGESTitleAction
{
    String getID();
    String getName();
    default int[] getKey(){return new int[]{-1,-1};}
    default boolean allowExecute(GES ges){return true;}
    default boolean showInTitle(){return true;}
    boolean execute(GES ges);
}
