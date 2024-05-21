package net.mcbbs.a1mercet.germhetools.player.ges.action;

import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import net.mcbbs.a1mercet.germhetools.player.ges.builder.SampleBuilder;
import net.mcbbs.a1mercet.germhetools.util.IConfig;
import net.mcbbs.a1mercet.germhetools.util.Options;
import org.bukkit.configuration.ConfigurationSection;

public interface IGESAction extends IConfig
{
    @Override
    default String getDefaultPath(){return getID();}
    @Override
    default void save(ConfigurationSection section)
    {
        section.set("Type",getType());
        getData().save(section.createSection("Data"));
    }

    @Override
    default void load(ConfigurationSection section)
    {
        if(section.getConfigurationSection("Data")!=null)
            getData().load(section.getConfigurationSection("Data"));
    }

    String getID();
    String getName();
    String getType();
    Options<?> getData();
    HEState getHEState();
    GES getGES();

    IGESAction createInstance(GES ges);

    void onStateReSelect();

    default boolean onApply(GES ges)
    {
        ges.ps.player.sendMessage("已应用操作["+getName()+"]");
        return true;
    }
    default boolean onRevoke(GES ges)
    {
        ges.ps.player.sendMessage("已撤回操作["+getName()+"]");
        return true;
    }
    SampleBuilder<? extends IGESAction> createBuilder();
    default void onShowSample()
    {
    }
    default void onUnshowSample()
    {
    }
}
