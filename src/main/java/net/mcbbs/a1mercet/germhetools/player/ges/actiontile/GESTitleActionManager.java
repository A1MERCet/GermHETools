package net.mcbbs.a1mercet.germhetools.player.ges.actiontile;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public class GESTitleActionManager
{
    public static final HashMap<String,Class<? extends IGESTitleAction>> registerList = new HashMap<>();
    public static void register(String id , Class<? extends IGESTitleAction> clz){registerList.put(id,clz);}
    public static IGESTitleAction create(String id)
    {
        Class<? extends IGESTitleAction> clz = registerList.get(id);
        if(clz==null){return null;}
        try {
            Constructor<? extends IGESTitleAction> constructor = clz.getConstructor();
            return constructor.newInstance();
        }catch (Exception e){e.printStackTrace();}
        return null;
    }
    static
    {
        register("open_library",        GESTitleActionLibrary.class);
        register("select",              GESTitleActionTargetSelect.class);
        register("remove_select",       GESTitleActionClearSelect.class);
        register("create_heblock",      GESTitleActionCreateHEBlock.class);
        register("edit_heblock",        GESTitleActionEditHE.class);
        register("copy_block",          GESTitleActionCopyBlock.class);
        register("delete",              GESTitleActionDelete.class);
        register("exit",                GESTitleActionExit.class);
    }
}
