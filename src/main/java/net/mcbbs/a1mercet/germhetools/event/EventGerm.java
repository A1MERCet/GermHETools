package net.mcbbs.a1mercet.germhetools.event;

import com.germ.germplugin.api.event.GermKeyDownEvent;
import com.germ.germplugin.api.event.GermKeyUpEvent;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class EventGerm implements Listener
{
    @EventHandler
    public void onKeyHandle(GermKeyDownEvent evt)
    {
        for(PlayerState ps : PlayerState.values())
            if(ps.isGESEnable())
                ps.addKey(evt.getKeyType().getKeyId());
    }
    @EventHandler
    public void onKeyHandle(GermKeyUpEvent evt)
    {
        for(PlayerState ps : PlayerState.values())
            if(ps.isGESEnable())
                ps.removeKey(evt.getKeyType().getKeyId());
    }
}
