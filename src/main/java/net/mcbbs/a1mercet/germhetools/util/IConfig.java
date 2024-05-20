package net.mcbbs.a1mercet.germhetools.util;

import org.bukkit.configuration.ConfigurationSection;

public interface IConfig
{
    String getDefaultPath();
    void save(ConfigurationSection section);
    void load(ConfigurationSection section);
}
