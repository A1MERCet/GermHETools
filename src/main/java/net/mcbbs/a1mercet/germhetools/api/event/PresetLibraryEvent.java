package net.mcbbs.a1mercet.germhetools.api.event;

import net.mcbbs.a1mercet.germhetools.player.ges.PresetLibrary;
import org.bukkit.entity.Player;

public class PresetLibraryEvent extends PlayerEventBase
{
    public final PresetLibrary library;
    public PresetLibraryEvent(Player who)
    {
        super(who);
        this.library=ps.ges.library;
    }
}
