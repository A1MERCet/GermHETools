package net.mcbbs.a1mercet.germhetools.api.event;

import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerEventBase extends org.bukkit.event.player.PlayerEvent implements Cancellable
{
    public final PlayerState ps;
    private static final HandlerList handlers = new HandlerList();

    public PlayerEventBase(Player who)
    {
        super(who);
        this.ps = PlayerState.get(who);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private boolean cancelled = false;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled=b;
    }
}
