package net.mcbbs.a1mercet.germhetools.player.ges.action;

import net.mcbbs.a1mercet.germhetools.player.ges.GES;

public interface IGESSample
{
    default void onShowSample(IGESAction action , GES ges)
    {
    }
    default void onUnshowSample(IGESAction action , GES ges)
    {
    }
}
