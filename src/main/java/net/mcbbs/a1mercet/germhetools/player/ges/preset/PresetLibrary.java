package net.mcbbs.a1mercet.germhetools.player.ges.preset;

import net.mcbbs.a1mercet.germhetools.api.event.AddPresetEvent;
import net.mcbbs.a1mercet.germhetools.api.event.DeletePresetEvent;
import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import net.mcbbs.a1mercet.germhetools.util.IConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class PresetLibrary implements IConfig
{
    public final PlayerState owner;

    @Override public String getDefaultPath() {return "AssetsLibrary";}

    @Override
    public void save(ConfigurationSection section)
    {
        section.set("Favorites",favorites);
        section.set("Preset",null);

        for(int i = 0;i<list.size();i++)
        {
            PresetList<IPreset<?>> presetList = list.get(i);
            presetList.save(section.createSection("Fold."+i));
        }
    }

    @Override
    public void load(ConfigurationSection section)
    {
        favorites.clear();
        favorites.addAll(section.getStringList("Favorites"));

        for(int i = 0;;i++)
            if(section.getConfigurationSection("Fold."+i)!=null){
                PresetList<IPreset<?>> list = new PresetList<>("NaN");
                list.load(section.getConfigurationSection("Fold."+i));
                this.list.add(list);
            }else {break;}

        if(getList("收藏夹")==null)      createList("收藏夹").color="orange";
        if(getList("默认目录")==null)    createList("默认目录");
    }

    public class PresetList<E extends IPreset<?>> extends ArrayList<E> implements IConfig
    {
        protected boolean bypass = false;
        @Override public String getDefaultPath() {return category;}

        @Override
        public void save(ConfigurationSection section)
        {
            lock.lock();
            try {
                section.set("Preset",null);

                section.set("Category",category);
                section.set("Color",color);
                for(int i = 0;i<size();i++)
                    get(i).save(section.createSection("Preset."+i));
            }catch (Exception e){e.printStackTrace();}finally {bypass=false;lock.unlock();}
        }

        @Override
        public void load(ConfigurationSection section)
        {
            bypass = true;
            category        = section.getString("Category");
            color           = section.getString("Color");

            lock.lock();
            try {
                for(int i = 0;;i++)
                    if(section.getConfigurationSection("Preset."+i)!=null) {
                        String type = section.getString("Preset."+i+".Type");
                        E s = (E) PresetType.create(type);
                        if(s==null){Bukkit.getLogger().warning("预设类型 ["+type+"] 创建失实例败");continue;}
                        s.load(section.getConfigurationSection("Preset."+i));
                        add(s);
                    } else {break;}
            }catch (Exception e){e.printStackTrace();}finally {bypass=false;lock.unlock();}
        }

        public String category;
        public String color = "blue";
        public final ReentrantLock lock = new ReentrantLock();

        public PresetList(String category)
        {
            this.category = category;
        }


        @Override
        public boolean add(E preset)
        {
            lock.lock();
            E copy = (E) preset.createInstance();
            copy.copy((IPreset)preset);

            try {
                if(bypass) return super.add(copy);

                AddPresetEvent evt = new AddPresetEvent(PresetLibrary.this.owner.player,this,copy);
                Bukkit.getServer().getPluginManager().callEvent(evt);
                if(evt.isCancelled())return false;

                return super.add(copy);
            }finally {lock.unlock();}
        }

        @Override
        public E remove(int index) {
            lock.lock();
            try {
                if(bypass) return super.remove(index);

                DeletePresetEvent evt = new DeletePresetEvent(PresetLibrary.this.owner.player,this,get(index));
                Bukkit.getServer().getPluginManager().callEvent(evt);
                if(evt.isCancelled())return null;

                return super.remove(index);
            }finally {lock.unlock();}
        }

        @Override
        public boolean remove(Object o) {
            lock.lock();
            try {
                if(bypass) return super.remove(o);

                DeletePresetEvent evt = new DeletePresetEvent(PresetLibrary.this.owner.player,this,o instanceof HEState?(HEState) o:null);
                Bukkit.getServer().getPluginManager().callEvent(evt);
                if(evt.isCancelled())return false;

                return super.remove(o);
            }finally {lock.unlock();}
        }

        public void sortID()    {lock.lock();try {this.sort(Comparator.comparing(o -> o.getID()));}finally {lock.unlock();}}
        public void sortName()  {lock.lock();try {this.sort(Comparator.comparing(o -> o.getName()));}finally {lock.unlock();}}
        public void sortData()  {lock.lock();try {this.sort(Comparator.comparingLong(o -> o.getAddDate()));}finally {lock.unlock();}}

        public E get(String id)
        {
            lock.lock();
            try {
                for(E e : this)
                    if(id.equals(e.getID()))
                        return e;
                return null;
            }finally {lock.unlock();}
        }

        public void swap(int i , int j)
        {
            lock.lock();
            try {
                E is = get(i);
                E js = get(j);

                remove(j);
                add(j,is);
                remove(i);
                add(i,js);
            }finally {lock.unlock();}
        }
    }

    protected final List<PresetList<IPreset<?>>> list = new ArrayList<>();
    public final List<String> favorites = new ArrayList<>();

    public PresetLibrary(PlayerState owner)
    {
        this.owner = owner;

    }

    public void setFavorite(String id)
    {
        IPreset<?> preset = get(id);
        if(preset!=null && !favorites.contains(id))
            favorites.add(id);
    }
    public List<IPreset<?>> getFavorites()
    {
        List<IPreset<?>> list = new ArrayList<>();

        for(String id : favorites)
        {
            IPreset<?> preset = get(id);
            if(preset!=null)list.add(preset);
        }

        return list;
    }

    public PresetList<IPreset<?>> getFrom(String id)
    {
        for (PresetList<IPreset<?>> presetList : list)
        {
            IPreset<?> preset = presetList.get(id);
            if(preset!=null)return presetList;
        }
        return null;
    }

    public IPreset<?> get(String id)
    {
        for (PresetList<IPreset<?>> presetList : list)
        {
            IPreset<?> preset = presetList.get(id);
            if(preset!=null)return preset;
        }
        return null;
    }

    public PresetList<IPreset<?>> getList(String category)
    {
        for (PresetList<IPreset<?>> heStates : list)
            if (category.equals(heStates.category))
                return heStates;
        return null;
    }
    public PresetList<IPreset<?>> deleteList(String category)
    {
        if("favorites".equals(category))return null;

        for(int i = 0;i<list.size();i++)
            if(category.equals(list.get(i).category))
                return list.remove(i);
        return null;
    }
    public PresetList<IPreset<?>> createList(String category)
    {
        for(PresetList<IPreset<?>> l : list)
            if(category.equals(l.category))
                return null;
        PresetList<IPreset<?>> presetList = new PresetList<>(category);
        list.add(presetList);
        return presetList;
    }
    public List<PresetList<IPreset<?>>> getList() {return list;}
}
