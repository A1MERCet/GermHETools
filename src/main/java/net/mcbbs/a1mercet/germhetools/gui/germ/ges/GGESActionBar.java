package net.mcbbs.a1mercet.germhetools.gui.germ.ges;

import com.germ.germplugin.api.dynamic.gui.GermGuiCanvas;
import com.germ.germplugin.api.dynamic.gui.GermGuiColor;
import com.germ.germplugin.api.dynamic.gui.GermGuiScroll;
import net.mcbbs.a1mercet.germhetools.gui.germ.ges.action.GGESAction;
import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import net.mcbbs.a1mercet.germhetools.player.ges.action.GESActionType;
import net.mcbbs.a1mercet.germhetools.player.ges.action.IGESAction;
import net.mcbbs.a1mercet.germhetools.util.UtilGerm2K;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class GGESActionBar extends GermGuiCanvas
{
    public final GES ges;
    public final GGES parent;

    public final GermGuiScroll scroll = new GermGuiScroll("scroll")
            .setWidth("252/2560*w").setHeight("1040/1440*h")
            .setLocationX("(2308-20)/2560*w").setLocationY("200/1440*h")
            .setRelative(true).setScrollDraggable(true)
            .setInvalidV(false).setInvalidH(true)
            .setScrollableV("1000/1440*h")
            .setSliderV(new GermGuiColor("sliderV").setEndColor(0xFF232323).setColor(0xFF232323).setWidth("10/2560*w").setHeight("200/1440*h"))
            .setSliderH(new GermGuiColor("sliderH").setEndColor(0xAA000000).setColor(0xAA000000).setWidth("0").setHeight("0"))
            .setLocationVX("(252+10)/2560*w").setWidthV("10/2560*w").setHeightV("(1040-200)/1440*h");
    public final List<GGESAction> actions = new ArrayList<>();

    public GGESActionBar(GGES parent)
    {
        super("builder_bar");
        this.parent=parent;
        this.ges=parent.ges;
    }

    public void build()
    {
        addGuiPart(UtilGerm2K.createTexture("main","he/ges/builder/main.png",2406,0));

        for(String str : GESActionType.types.keySet())
        {
            IGESAction a = GESActionType.create(str, ges);
            if(a==null || !a.showInHUD()){continue;}

            GGESAction g = new GGESAction(this,a);
            g.build();
            actions.add(g);
        }

        updateLayout();

        addGuiPart(scroll);
        update();
    }
    public void updateLayout()
    {
        int size = actions.size();

        for(int i = 0;i<size;i++)
        {
            GGESAction g = actions.get(i);

            UtilGerm2K.setLocation(g,0,i*105);
            Bukkit.getLogger().warning("Height: "+(i*105));

            if(scroll.getGuiPart(g.getIndexName())==null)scroll.addGuiPart(g);
        }
    }
    public void update()
    {
        actions.forEach(GGESAction::update);
    }
}
