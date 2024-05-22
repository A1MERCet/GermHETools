package net.mcbbs.a1mercet.germhetools;

import com.germ.germplugin.api.GermKeyAPI;
import com.germ.germplugin.api.KeyType;
import net.mcbbs.a1mercet.germhetools.api.BlockManager;
import net.mcbbs.a1mercet.germhetools.command.CMDServer;
import net.mcbbs.a1mercet.germhetools.event.Event;
import net.mcbbs.a1mercet.germhetools.event.EventGerm;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.*;

public final class GermHETools extends JavaPlugin {

    static GermHETools instance;

    @Override
    public void onEnable()
    {
        instance=this;

        Bukkit.getPluginCommand("ghe").setExecutor(new CMDServer());
        Bukkit.getServer().getPluginManager().registerEvents(new Event(),this);
        Bukkit.getServer().getPluginManager().registerEvents(new EventGerm(),this);

        BlockManager.init();
        Bukkit.getLogger().info("§6[HETools]已成功加载");

        GermKeyAPI.registerKey(KeyType.KEY_UP);
        GermKeyAPI.registerKey(KeyType.KEY_LEFT);
        GermKeyAPI.registerKey(KeyType.KEY_RIGHT);
        GermKeyAPI.registerKey(KeyType.KEY_DOWN);
        GermKeyAPI.registerKey(KeyType.KEY_EQUALS);
        GermKeyAPI.registerKey(KeyType.KEY_RETURN);
        GermKeyAPI.sendRegisteredKeysToAllPlayer();

        saveDefaultConfig();
        Config.path=getConfig().getString("Path");
        if(Config.path==null || "".equals(Config.path))
            Config.path=getDataFolder().getAbsolutePath()+"/";

        for(Player p : Bukkit.getOnlinePlayers())
            Event.joinHandle(p);
    }

    @Override
    public void onDisable()
    {
        for(PlayerState ps : PlayerState.values())
            try {
                ps.save();
            }catch (Exception e){e.printStackTrace();}
        PlayerState.players.clear();
    }
    public static GermHETools getInstance(){return instance;}
}
