package net.mcbbs.a1mercet.germhetools.gui;

import org.bukkit.entity.Player;

public interface IGui
{
    void openGuiTo(Player p);
    void openChildGuiTo(Player p);
    void openHUDTo(Player p);
    void closeFrom(Player p);
}
