package net.mcbbs.a1mercet.germhetools.player.ges.preset;

import net.mcbbs.a1mercet.germhetools.gui.HEGuiManager;
import net.mcbbs.a1mercet.germhetools.gui.germ.GActionPanel;
import net.mcbbs.a1mercet.germhetools.gui.germ.GPreset;
import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import net.mcbbs.a1mercet.germhetools.util.IConfig;
import net.mcbbs.a1mercet.germhetools.util.Options;
import org.bukkit.configuration.ConfigurationSection;

public interface IPreset<T> extends IConfig {
    /**
     * <br>BLOCK
     * <br>ENTITY
     */
    String getType();

    String getID();

    String getName();

    long getAddDate();

    T getObject();

    default IPreset<T> createInstance() {return (IPreset<T>) PresetType.create(getType());}

    void copy(IPreset<T> t);

    GPreset createGPreset(PlayerState ps , IPreset<?> preset , int size);

    Options<String> getData();

    default GActionPanel createGActionPanel(GPreset gpreset)
    {
        PlayerState ps = gpreset.ps;
        GActionPanel g = new GActionPanel(ps.player);

        PresetLibrary.PresetList<IPreset<?>> list = ps.ges.library.getFrom(getID());

        g.addAction(new GActionPanel.Action("id", getID()));
        g.addAction(new GActionPanel.Action("fave","收藏"){
            @Override public boolean callback(GActionPanel parent) {
                if(!super.callback(parent))return false;


                PresetLibrary.PresetList<IPreset<?>> fav = ps.ges.library.getList("收藏夹");
                if(fav!=null){
                    IPreset<T> copy = createInstance();
                    copy.copy(IPreset.this);
                    fav.add(copy);
                }

                parent.close();

                return true;
            }});
        g.addAction(new GActionPanel.Action("enable_img",(getData().containsKey("enable_img")?"关闭":"打开")+"自定义图片"){
            @Override public boolean callback(GActionPanel parent) {
                if(!super.callback(parent))return false;

                if(getData().containsKey("enable_img")) getData().remove("enable_img");
                else getData().putDefault("enable_img",true);

                gpreset.update();
                parent.close();

                return true;
            }});
        if(list!=null)
            g.addAction(new GActionPanel.Action("delete","#FFE96363从收藏夹中删除"){
                @Override public boolean callback(GActionPanel parent) {
                    if(!super.callback(parent))return false;

                    list.remove(IPreset.this);

                    parent.close();

                    return true;
                }});
        return g;
    }

    @Override default void save(ConfigurationSection section)
    {
        section.set("Type", getType());
    }
}
