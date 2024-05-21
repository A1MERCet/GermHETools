package net.mcbbs.a1mercet.germhetools.player.ges;

import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import net.mcbbs.a1mercet.germhetools.util.IConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PresetLibrary implements IConfig
{
    public final PlayerState owner;

    @Override public String getDefaultPath() {return "AssetsLibrary";}

    @Override
    public void save(ConfigurationSection section)
    {
        section.set("",null);

        section.set("Favorites",favorites);

        for(int i = 0;i<list.size();i++)
        {
            PresetList presetList = list.get(i);
            presetList.save(section.createSection(i+""));
        }
    }

    @Override
    public void load(ConfigurationSection section)
    {
        favorites.clear();
        favorites.addAll(section.getStringList("Favorites"));

        for(int i = 0;;i++)
            if(section.getConfigurationSection(i+"")!=null){
                PresetList list = new PresetList("NaN");
                list.load(section.getConfigurationSection(i+""));
                this.list.add(list);
            }else {break;}
    }

    public static class PresetList extends ArrayList<HEState> implements IConfig
    {
        @Override public String getDefaultPath() {return category;}

        @Override
        public void save(ConfigurationSection section)
        {
            section.set("",null);
            section.set("Category",category);
            for(int i = 0;i<size();i++)
                get(i).save(section.createSection(i+""));
        }

        @Override
        public void load(ConfigurationSection section)
        {
            category=section.getString("Category");
            for(int i = 0;;i++)
                if(section.getConfigurationSection(i+"")!=null) {
                    HEState s = new HEState();
                    s.load(section.getConfigurationSection(i+""));
                    add(s);
                } else {break;}
        }

        public String category;

        public PresetList(String category)
        {
            this.category = category;
        }


        public void sortID()    {this.sort(Comparator.comparing(o -> o.id));}
        public void sortName()  {this.sort(Comparator.comparing(o -> o.name));}
        public void sortData()
        {
            this.sort((o1,o2)->{
                long d1 = o1.data.getLong("date");
                long d2 = o2.data.getLong("date");
                return Long.compare(d1,d2);
            });
        }

        public HEState get(String id)
        {
            for(HEState s : this)
                if(id.equals(s.id))
                    return s;
            return null;
        }

        public void swap(int i , int j)
        {
            HEState is = get(i);
            HEState js = get(j);

            remove(j);
            add(j,is);
            remove(i);
            add(i,js);
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
    public PresetList removeList(String category)
    {
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
