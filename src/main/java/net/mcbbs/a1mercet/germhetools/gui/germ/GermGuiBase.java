package net.mcbbs.a1mercet.germhetools.gui.germ;

import com.germ.germplugin.api.dynamic.gui.GermGuiScreen;
import com.germ.germplugin.api.dynamic.gui.GuiManager;
import net.mcbbs.a1mercet.germhetools.gui.IGui;
import org.bukkit.entity.Player;

public class GermGuiBase extends GermGuiScreen implements IGui
{
    public GermGuiBase(String guiName)
    {
        super(guiName, true);
    }

    @Override public void openGuiTo(Player p)
    {
        for(GermGuiScreen g : GuiManager.getOpenedAllGui(p))
            if(g.equals(this))
                return;
        openGui(p);
    }
    @Override public void openChildGuiTo(Player p) {openChildGui(p);}
    @Override public void openHUDTo(Player p) {openHud(p);}
    @Override
    public void closeFrom(Player p) {
        close();
        for(GermGuiScreen g : GuiManager.getOpenedAllGui(p))
            if(g.equals(this))
                g.close();
    }
}
