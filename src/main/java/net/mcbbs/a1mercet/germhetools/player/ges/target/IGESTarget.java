package net.mcbbs.a1mercet.germhetools.player.ges.target;

import net.mcbbs.a1mercet.germhetools.util.Options;

public interface IGESTarget
{
    String getName();
    String getType();
    Options<String> getTargetData();
}
