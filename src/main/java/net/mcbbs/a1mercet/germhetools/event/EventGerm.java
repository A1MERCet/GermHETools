package net.mcbbs.a1mercet.germhetools.event;

import com.germ.germplugin.api.GermPacketAPI;
import com.germ.germplugin.api.dynamic.gui.GermGuiScreen;
import com.germ.germplugin.api.dynamic.gui.GuiManager;
import com.germ.germplugin.api.event.GermKeyDownEvent;
import com.germ.germplugin.api.event.GermKeyUpEvent;
import net.mcbbs.a1mercet.germhetools.api.event.AddPresetEvent;
import net.mcbbs.a1mercet.germhetools.api.event.DeletePresetEvent;
import net.mcbbs.a1mercet.germhetools.api.event.HEStateSaveEvent;
import net.mcbbs.a1mercet.germhetools.gui.germ.GPresetLibrary;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import net.mcbbs.a1mercet.germhetools.player.ges.preset.IPreset;
import net.mcbbs.a1mercet.germhetools.player.ges.preset.PresetList;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EventGerm implements Listener
{
    @EventHandler
    public void confirmState(HEStateSaveEvent evt)
    {
        PlayerState ps = evt.ps;
        if(ps.ges==null)return;
        PresetList<IPreset<?>> list = ps.ges.library.getFrom(evt.state.id);

        if(list!=null)
            for(GermGuiScreen g : GuiManager.getOpenedAllGui(ps.player))
                if(g instanceof GPresetLibrary)
                {
                    ((GPresetLibrary) g).update(list.category,evt.state);
                }
    }

    @EventHandler
    public void addPreset(AddPresetEvent evt)
    {
        PlayerState ps = evt.ps;
        for(GermGuiScreen g : GuiManager.getOpenedAllGui(ps.player))
            if(g instanceof GPresetLibrary)
            {
                ((GPresetLibrary) g).add(evt.list.category,evt.state);
            }
    }
    @EventHandler
    public void removePreset(DeletePresetEvent evt)
    {
        PlayerState ps = evt.ps;
        for(GermGuiScreen g : GuiManager.getOpenedAllGui(ps.player))
            if(g instanceof GPresetLibrary)
            {
                ((GPresetLibrary) g).remove(evt.list.category,evt.preset);
            }
    }

    @EventHandler
    public void onKeyHandle(GermKeyDownEvent evt)
    {
        if(evt.getKeyType().getKeyId()==56)
            GermPacketAPI.setPlayerFocus(evt.getPlayer(),false);

        for(PlayerState ps : PlayerState.values())
            if(ps.isGESEnable())
                ps.setKey(evt.getKeyType().getKeyId());
    }
    @EventHandler
    public void onKeyHandle(GermKeyUpEvent evt)
    {
        for(PlayerState ps : PlayerState.values())
            if(ps.isGESEnable())
                ps.removeKey(evt.getKeyType().getKeyId());
    }
}
