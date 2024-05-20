package net.mcbbs.a1mercet.germhetools.player;

import com.germ.germplugin.api.GermPacketAPI;
import com.germ.germplugin.api.KeyType;
import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;

public class PlayerState
{
    public static HashMap<String,PlayerState> players = new HashMap<>();
    public static Collection<PlayerState> values()  {return players.values();}
    public static void remove(Player p)             {players.remove(p.getName());}
    public static void remove(String p)             {players.remove(p);}
    public static void remove(PlayerState p)        {players.remove(p.name);}
    public static PlayerState get(String p)         {return players.get(p);}
    public static PlayerState get(Player p)         {return players.get(p.getName());}
    public static PlayerState get(PlayerState p)    {return players.get(p.name);}
    public static PlayerState create(Player p)
    {
        PlayerState ps = new PlayerState(p);
        players.put(p.getName(),ps);
        return ps;
    }

    public final Player player;
    public final String name;
    public GES ges;

    public PlayerState(Player player)
    {
        this.player = player;
        this.name = player.getName();
        this.ges = new GES(this);

        GermPacketAPI.sendKeyRegister(player, KeyType.KEY_UP.getKeyId());
        GermPacketAPI.sendKeyRegister(player, KeyType.KEY_DOWN.getKeyId());
        GermPacketAPI.sendKeyRegister(player, KeyType.KEY_LEFT.getKeyId());
        GermPacketAPI.sendKeyRegister(player, KeyType.KEY_RIGHT.getKeyId());
        GermPacketAPI.sendKeyRegister(player, KeyType.KEY_EQUALS.getKeyId());
        GermPacketAPI.sendKeyRegister(player, KeyType.KEY_RETURN.getKeyId());
        GermPacketAPI.sendKeyRegister(player, KeyType.KEY_BACK.getKeyId());
    }

    public boolean isGESEnable(){return ges!=null&&ges.enable;}
}
