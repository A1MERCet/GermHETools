package net.mcbbs.a1mercet.germhetools.api.event;

import net.mcbbs.a1mercet.germhetools.player.ges.preset.IPreset;
import net.mcbbs.a1mercet.germhetools.player.ges.preset.PresetLibrary;
import org.bukkit.entity.Player;

public class AddPresetEvent extends PresetLibraryEvent
{
    public final PresetLibrary.PresetList<? extends IPreset<?>> list;
    public final IPreset<?> state;

    public AddPresetEvent(Player who , PresetLibrary.PresetList<? extends IPreset<?>> list , IPreset<?> state)
    {
        super(who);
        this.list=list;
        this.state=state;
    }
}
