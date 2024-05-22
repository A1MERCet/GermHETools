package net.mcbbs.a1mercet.germhetools.player;

import com.germ.germplugin.api.GermPacketAPI;
import com.germ.germplugin.api.KeyType;
import net.mcbbs.a1mercet.germhetools.Config;
import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import net.mcbbs.a1mercet.germhetools.util.IConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

public class PlayerState implements IConfig
{
    public static final HashMap<Integer,Integer> assistKey = new HashMap<>();
    static {
        assistKey.put(42,42);
        assistKey.put(54,54);
        assistKey.put(29,29);
        assistKey.put(157,157);
        assistKey.put(56,56);
        assistKey.put(184,184);
    }

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

    public final int[] keyPreseed                   = new int[]{-1,-1};
    public boolean allowGES                         = false;
    public GES ges                                  = null;

    public PlayerState(Player player)
    {
        this.player = player;
        this.name = player.getName();
    }

    public void clearKey()
    {
        keyPreseed[0]=-1;
        keyPreseed[1]=-1;
    }
    public void removeKey(int k)
    {
        boolean assist = assistKey.containsKey(k);
        if(assist)  clearKey();
        else        keyPreseed[0]=-1;
    }
    public void setKey(int k)
    {
        if(isGESEnable()){
            boolean assist = assistKey.containsKey(k);
            if(assist)  keyPreseed[1]=k;
            else        keyPreseed[0]=k;

            ges.keyHandle(keyPreseed[0],keyPreseed[1]);
        }
    }

    public void initGES()
    {
        if(!allowGES)return;

        if(player!=null)
        {
            ges = new GES(this);
            GermPacketAPI.sendKeyRegister(player, KeyType.KEY_UP.getKeyId());
            GermPacketAPI.sendKeyRegister(player, KeyType.KEY_DOWN.getKeyId());
            GermPacketAPI.sendKeyRegister(player, KeyType.KEY_LEFT.getKeyId());
            GermPacketAPI.sendKeyRegister(player, KeyType.KEY_RIGHT.getKeyId());
            GermPacketAPI.sendKeyRegister(player, KeyType.KEY_EQUALS.getKeyId());
            GermPacketAPI.sendKeyRegister(player, KeyType.KEY_RETURN.getKeyId());
            GermPacketAPI.sendKeyRegister(player, KeyType.KEY_BACK.getKeyId());
            GermPacketAPI.sendKeyRegister(player, KeyType.KEY_Z.getKeyId());
            GermPacketAPI.sendKeyRegister(player, KeyType.KEY_RMENU.getKeyId());
            GermPacketAPI.sendKeyRegister(player, KeyType.KEY_LMENU.getKeyId());
            GermPacketAPI.sendKeyRegister(player, KeyType.KEY_CAPITAL.getKeyId());
            GermPacketAPI.sendKeyRegister(player, KeyType.KEY_GRAVE.getKeyId());
            GermPacketAPI.sendKeyRegister(player, KeyType.KEY_RCONTROL.getKeyId());
            GermPacketAPI.sendKeyRegister(player, KeyType.KEY_LCONTROL.getKeyId());
            GermPacketAPI.sendKeyRegister(player, KeyType.KEY_X.getKeyId());
            GermPacketAPI.sendKeyRegister(player, KeyType.KEY_F.getKeyId());
            GermPacketAPI.sendKeyRegister(player, KeyType.KEY_D.getKeyId());
            GermPacketAPI.sendKeyRegister(player, KeyType.KEY_N.getKeyId());
            GermPacketAPI.sendKeyRegister(player, KeyType.KEY_R.getKeyId());
            GermPacketAPI.sendKeyRegister(player, KeyType.KEY_C.getKeyId());
            GermPacketAPI.sendKeyRegister(player, KeyType.KEY_G.getKeyId());
            GermPacketAPI.sendKeyRegister(player, KeyType.KEY_MINUS.getKeyId());
            GermPacketAPI.sendKeyRegister(player, KeyType.KEY_EQUALS.getKeyId());
            GermPacketAPI.sendKeyRegister(player, KeyType.KEY_DELETE.getKeyId());
            GermPacketAPI.sendKeyRegister(player, KeyType.KEY_INSERT.getKeyId());
        }
    }

    public boolean isGESEnable(){return ges!=null&&ges.enable;}


    @Override public String getDefaultPath() {return name;}

    public void save()
    {
        String path = Config.path+"\\player\\"+name+".yml";
        File file = new File(path);
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        save(cfg.createSection(getDefaultPath()));

        try {cfg.save(file);}catch (Exception e){e.printStackTrace();}
    }

    public void load()
    {
        String path = Config.path+"\\player\\"+name+".yml";
        File file = new File(path);
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        load(cfg.getConfigurationSection(getDefaultPath()) == null?cfg.createSection(getDefaultPath()):cfg.getConfigurationSection(getDefaultPath()));
    }

    @Override
    public void save(ConfigurationSection section)
    {
        section.set("AllowGES",allowGES);
        if(ges!=null)
        {
            section.set(ges.library.getDefaultPath(),null);
            ges.library.save(section.createSection(ges.library.getDefaultPath()));
        }
    }

    @Override
    public void load(ConfigurationSection section)
    {
        allowGES = section.getBoolean("AllowGES");
        initGES();

        if(ges!=null && section.getConfigurationSection(ges.library.getDefaultPath())!=null)
        {
            ges.library.load(section.getConfigurationSection(ges.library.getDefaultPath()));
        }
    }
}
