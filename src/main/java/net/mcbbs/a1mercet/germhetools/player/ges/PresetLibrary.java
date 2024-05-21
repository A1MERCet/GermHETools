package net.mcbbs.a1mercet.germhetools.player.ges;

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
            PresetList presetList = list.get(i);
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
                PresetList list = new PresetList("NaN");
                list.load(section.getConfigurationSection("Fold."+i));
                this.list.add(list);
            }else {break;}

        if(getList("收藏夹")==null)      createList("收藏夹").color="orange";
        if(getList("默认目录")==null)    createList("默认目录");
    }

    public class PresetList extends ArrayList<HEState> implements IConfig
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
                        HEState s = new HEState();
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
        public boolean add(HEState state)
        {
            lock.lock();
            HEState copy = new HEState(state);

            try {
                if(bypass) return super.add(copy);

                AddPresetEvent evt = new AddPresetEvent(PresetLibrary.this.owner.player,this,copy);
                Bukkit.getServer().getPluginManager().callEvent(evt);
                if(evt.isCancelled())return false;
                copy.setLocation(null,0D,0D,0D);

                return super.add(copy);
            }finally {lock.unlock();}
        }

        @Override
        public HEState remove(int index) {
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

        public void sortID()    {lock.lock();try {this.sort(Comparator.comparing(o -> o.id));}finally {lock.unlock();}}
        public void sortName()  {lock.lock();try {this.sort(Comparator.comparing(o -> o.name));;}finally {lock.unlock();}}
        public void sortData()  {lock.lock();try {this.sort(Comparator.comparingLong(o -> o.data.getLong("date")));}finally {lock.unlock();}}

        public HEState get(String id)
        {
            lock.lock();
            try {
                for(HEState s : this)
                    if(id.equals(s.id))
                        return s;
                return null;
            }finally {lock.unlock();}
        }

        public void swap(int i , int j)
        {
            lock.lock();
            try {
                HEState is = get(i);
                HEState js = get(j);

                remove(j);
                add(j,is);
                remove(i);
                add(i,js);
            }finally {lock.unlock();}
        }
    }

    protected final List<PresetList> list = new ArrayList<>();
    public final List<String> favorites = new ArrayList<>();

    public PresetLibrary(PlayerState owner)
    {
        this.owner = owner;

    }

    public void setFavorite(String id)
    {
        HEState state = get(id);
        if(state!=null && !favorites.contains(id))
            favorites.add(id);
    }
    public List<HEState> getFavorites()
    {
        List<HEState> list = new ArrayList<>();

        for(String id : favorites)
        {
            HEState state = get(id);
            if(state!=null)list.add(state);
        }

        return list;
    }

    public PresetList getFrom(String id)
    {
        for (PresetList presetList : list)
        {
            HEState state = presetList.get(id);
            if(state!=null)return presetList;
        }
        return null;
    }

    public HEState get(String id)
    {
        for (PresetList presetList : list)
        {
            HEState state = presetList.get(id);
            if(state!=null)return state;
        }
        return null;
    }

    public PresetList getList(String category)
    {
        for (PresetList heStates : list)
            if (category.equals(heStates.category))
                return heStates;
        return null;
    }
    public PresetList deleteList(String category)
    {
        if("favorites".equals(category))return null;

        for(int i = 0;i<list.size();i++)
            if(category.equals(list.get(i).category))
                return list.remove(i);
        return null;
    }
    public PresetList createList(String category)
    {
        for(PresetList l : list)
            if(category.equals(l.category))
                return null;
        PresetList presetList = new PresetList(category);
        list.add(presetList);
        return presetList;
    }
    public List<PresetList> getList() {return list;}
}
