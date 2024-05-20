package net.mcbbs.a1mercet.germhetools;

import com.germ.germplugin.api.GermKeyAPI;
import com.germ.germplugin.api.KeyType;
import com.tuershen.nbtlibrary.CompoundLibraryManager;
import com.tuershen.nbtlibrary.api.CompoundLibraryApi;
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
    public static CompoundLibraryApi libraryApi;

    @Override
    public void onEnable()
    {
        instance=this;

        Bukkit.getPluginCommand("ghe").setExecutor(new CMDServer());
        Bukkit.getServer().getPluginManager().registerEvents(new Event(),this);
        Bukkit.getServer().getPluginManager().registerEvents(new EventGerm(),this);

        BlockManager.init();
        libraryApi = CompoundLibraryManager.getPluginManager(this);
        Bukkit.getLogger().info("§6[HETools]已成功加载");

        JFrame frame = new JFrame("TEST");
        frame.setSize(1200,600);
        frame.setVisible(true);

        for(Player p : Bukkit.getOnlinePlayers())
            Event.joinHandle(p);

        GermKeyAPI.registerKey(KeyType.KEY_UP);
        GermKeyAPI.registerKey(KeyType.KEY_LEFT);
        GermKeyAPI.registerKey(KeyType.KEY_RIGHT);
        GermKeyAPI.registerKey(KeyType.KEY_DOWN);
        GermKeyAPI.registerKey(KeyType.KEY_EQUALS);
        GermKeyAPI.registerKey(KeyType.KEY_RETURN);
        GermKeyAPI.sendRegisteredKeysToAllPlayer();
    }

    @Override
    public void onDisable()
    {
        Bukkit.getOnlinePlayers().forEach(PlayerState::remove);
    }
    public static GermHETools getInstance(){return instance;}
}
