package net.mcbbs.a1mercet.germhetools.player.ges.action;

import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import org.bukkit.Bukkit;

import java.util.HashMap;

public class GESActionType
{
    public static final HashMap<String,IGESAction> types = new HashMap<>();
    public static void registerType(String str , IGESAction instance)
    {
        types.put(str,instance);
    }
    public static IGESAction create(String type , GES ges)
    {
        try {
            return types.get(type).createInstance(ges);
        }catch (Exception e){Bukkit.getLogger().warning("创建类型 ["+type+"] 出错");e.printStackTrace();}
        return null;
    }
    static {
        registerType("MOVE",        new GESActionMove(null));
        registerType("SCALE",       new GESActionScale(null));
        registerType("ROTATE",      new GESActionRotate(null));
        registerType("OFFSET",      new GESActionOffset(null));
        registerType("CREATE",      new GESActionCreate(null));
    }
}
