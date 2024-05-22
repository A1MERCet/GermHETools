package net.mcbbs.a1mercet.germhetools.api.event;

import net.mcbbs.a1mercet.germhetools.he.HEState;
import org.bukkit.entity.Player;

public class HEStateSaveEvent extends PlayerEventBase
{
    public final HEState state;
    public HEStateSaveEvent(Player who, HEState state)
    {
        super(who);
        this.state=state;
    }
}
