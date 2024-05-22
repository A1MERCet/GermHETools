package net.mcbbs.a1mercet.germhetools.player.ges.preset;

import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import net.mcbbs.a1mercet.germhetools.util.IConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.ArrayList;
import java.util.List;

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
                PresetList<IPreset<?>> list = new PresetList<>(this,"NaN");
                list.load(section.getConfigurationSection("Fold."+i));
                this.list.add(list);
            }else {break;}

        if(getList("收藏夹")==null)      createList("收藏夹").color="orange";
        if(getList("默认目录")==null)    createList("默认目录");
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
        PresetList<IPreset<?>> presetList = new PresetList<>(this,category);
        list.add(presetList);
        return presetList;
    }
    public List<PresetList<IPreset<?>>> getList() {return list;}
}
