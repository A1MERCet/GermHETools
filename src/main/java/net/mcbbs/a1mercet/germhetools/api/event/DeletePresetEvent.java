package net.mcbbs.a1mercet.germhetools.api.event;

import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.ges.PresetLibrary;
import org.bukkit.entity.Player;

public class DeletePresetEvent extends PresetLibraryEvent
{
    public final PresetLibrary.PresetList list;
    public final HEState state;

    public DeletePresetEvent(Player who , PresetLibrary.PresetList list , HEState state)
    {
        super(who);
        this.list=list;
        this.state=state;
    }
}