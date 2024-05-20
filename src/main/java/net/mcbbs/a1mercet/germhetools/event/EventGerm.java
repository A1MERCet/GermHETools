package net.mcbbs.a1mercet.germhetools.event;

import com.germ.germplugin.api.event.GermKeyDownEvent;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EventGerm implements Listener
{
    @EventHandler
    public void onKeyHandle(GermKeyDownEvent evt)
    {
        for(PlayerState ps : PlayerState.values())
            if(ps.isGESEnable())
                ps.ges.keyHandle(evt.getKeyType().getKeyId());
    }
}
