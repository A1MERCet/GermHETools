package net.mcbbs.a1mercet.germhetools.gui;

import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.gui.germ.GHEState;
import org.bukkit.entity.Player;

public class HEGuiManager
{
    public static IGui createHEStateGui(Player player , HEState state , boolean hand)
    {
        if(state==null)return null;
        GHEState g = new GHEState(player,state,hand);
        return g;
    }
}
