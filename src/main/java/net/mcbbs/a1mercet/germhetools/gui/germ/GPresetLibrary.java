package net.mcbbs.a1mercet.germhetools.gui.germ;

import net.mcbbs.a1mercet.germhetools.gui.germ.GermGuiBase;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import net.mcbbs.a1mercet.germhetools.player.ges.PresetLibrary;

public class GPresetLibrary extends GermGuiBase
{

    public final PlayerState ps;
    public final PresetLibrary library;
    
    public GPresetLibrary(PlayerState ps)
    {
        super("library");
        this.ps=ps;
        this.library=ps.ges.library;
    }
}
