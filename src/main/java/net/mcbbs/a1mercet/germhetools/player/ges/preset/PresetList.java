package net.mcbbs.a1mercet.germhetools.player.ges.preset;

import net.mcbbs.a1mercet.germhetools.api.event.AddPresetEvent;
import net.mcbbs.a1mercet.germhetools.api.event.DeletePresetEvent;
import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.util.IConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.locks.ReentrantLock;

public class PresetList<E extends IPreset<?>> extends ArrayList<E> implements IConfig {
    public final PresetLibrary parent;
    protected boolean bypass = false;

    @Override
    public String getDefaultPath() {
        return category;
    }

    @Override
    public void save(ConfigurationSection section) {
        lock.lock();
        try {
            section.set("Preset", null);

            section.set("Category", category);
            section.set("Color", color);
            for (int i = 0; i < size(); i++)
                get(i).save(section.createSection("Preset." + i));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bypass = false;
            lock.unlock();
        }
    }

    @Override
    public void load(ConfigurationSection section) {
        bypass = true;
        category = section.getString("Category");
        color = section.getString("Color");

        lock.lock();
        try {
            for (int i = 0; ; i++)
                if (section.getConfigurationSection("Preset." + i) != null) {
                    String type = section.getString("Preset." + i + ".Type");
                    E s = (E) PresetType.create(type);
                    if (s == null) {
                        Bukkit.getLogger().warning("预设类型 [" + type + "] 创建实例败");
                        continue;
                    }
                    s.load(section.getConfigurationSection("Preset." + i));
                    add(s);
                } else {
                    break;
                }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bypass = false;
            lock.unlock();
        }
    }

    public String category;
    public String color = "blue";
    public final ReentrantLock lock = new ReentrantLock();

    public PresetList(PresetLibrary parent, String category) {
        this.parent = parent;
        this.category = category;
    }


    @Override
    public boolean add(E preset) {
        lock.lock();
        E copy = (E) preset.createInstance();
        copy.copy((IPreset) preset);

        try {
            if (bypass) return super.add(copy);

            AddPresetEvent evt = new AddPresetEvent(parent.owner.player, this, copy);
            Bukkit.getServer().getPluginManager().callEvent(evt);
            if (evt.isCancelled()) return false;

            return super.add(copy);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E remove(int index) {
        lock.lock();
        try {
            if (bypass) return super.remove(index);

            DeletePresetEvent evt = new DeletePresetEvent(parent.owner.player, this, get(index));
            Bukkit.getServer().getPluginManager().callEvent(evt);
            if (evt.isCancelled()) return null;

            return super.remove(index);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean remove(Object o) {
        lock.lock();
        try {
            if (bypass) return super.remove(o);

            DeletePresetEvent evt = new DeletePresetEvent(parent.owner.player, this, o instanceof HEState ? (HEState) o : null);
            Bukkit.getServer().getPluginManager().callEvent(evt);
            if (evt.isCancelled()) return false;

            return super.remove(o);
        } finally {
            lock.unlock();
        }
    }

    public void sortID() {
        lock.lock();
        try {
            this.sort(Comparator.comparing(o -> o.getID()));
        } finally {
            lock.unlock();
        }
    }

    public void sortName() {
        lock.lock();
        try {
            this.sort(Comparator.comparing(o -> o.getName()));
        } finally {
            lock.unlock();
        }
    }

    public void sortData() {
        lock.lock();
        try {
            this.sort(Comparator.comparingLong(o -> o.getAddDate()));
        } finally {
            lock.unlock();
        }
    }

    public E get(String id) {
        lock.lock();
        try {
            for (E e : this)
                if (id.equals(e.getID()))
                    return e;
            return null;
        } finally {
            lock.unlock();
        }
    }

    public void swap(int i, int j) {
        lock.lock();
        try {
            E is = get(i);
            E js = get(j);

            remove(j);
            add(j, is);
            remove(i);
            add(i, js);
        } finally {
            lock.unlock();
        }
    }
}
