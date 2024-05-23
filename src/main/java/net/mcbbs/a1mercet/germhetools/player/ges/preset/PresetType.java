package net.mcbbs.a1mercet.germhetools.player.ges.preset;

import net.mcbbs.a1mercet.germhetools.he.HEState;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public class PresetType {
    public static HashMap<String, Class<? extends IPreset<?>>> register = new HashMap<>();

    public static void register(String str, Class<? extends IPreset<?>> clz) {
        register.put(str, clz);
    }

    public static IPreset<?> create(String type) {
        Class<? extends IPreset<?>> clz = register.get(type);
        try {
            Constructor<? extends IPreset<?>> constructor = clz.getConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static {
        register("BLOCK", HEState.class);
        register("SCENE", Scene.class);
    }
}
