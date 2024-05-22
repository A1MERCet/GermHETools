package net.mcbbs.a1mercet.germhetools.api.event;

import net.mcbbs.a1mercet.germhetools.player.ges.preset.IPreset;
import net.mcbbs.a1mercet.germhetools.player.ges.preset.PresetList;
import org.bukkit.entity.Player;

public class DeletePresetEvent extends PresetLibraryEvent
{
    public final PresetList<? extends IPreset<?>> list;
    public final IPreset<?> preset;

    public DeletePresetEvent(Player who , PresetList<? extends IPreset<?>> list , IPreset<?> preset)
    {
        super(who);
        this.list=list;
        this.preset = preset;
    }
}
