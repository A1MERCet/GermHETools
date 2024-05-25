package net.mcbbs.a1mercet.germhetools.gui.germ.ges;

import com.germ.germplugin.api.dynamic.gui.GermGuiButton;
import com.germ.germplugin.api.dynamic.gui.GermGuiCanvas;
import com.germ.germplugin.api.dynamic.gui.GermGuiColor;
import com.germ.germplugin.api.dynamic.gui.GermGuiScroll;
import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import net.mcbbs.a1mercet.germhetools.player.ges.actiontile.GESTitleActionManager;
import net.mcbbs.a1mercet.germhetools.player.ges.actiontile.IGESTitleAction;
import net.mcbbs.a1mercet.germhetools.util.UtilGerm2K;

import java.util.ArrayList;
import java.util.List;

public class GGESTitleBar extends GermGuiCanvas
{
    public static class GGESTitleAction extends GermGuiCanvas
    {
        public final IGESTitleAction action;
        public final GGESTitleBar parent;

        public GGESTitleAction(GGESTitleBar parent, IGESTitleAction action)
        {
            super(action.getID());
            this.action=action;
            this.parent=parent;
        }

        public void build()
        {
            addGuiPart(UtilGerm2K.createButton("button","he/ges/action/"+action.getID()+".png",0,0)
                    .registerCallbackHandler((p,b)->callback(), GermGuiButton.EventType.LEFT_CLICK));
            update();
        }
        public void update()
        {

        }
        public void callback()
        {
            action.execute(parent.ges);
        }
    }

    public final GES ges;
    public final GGES parent;
    public final GermGuiScroll scroll = new GermGuiScroll("scroll")
            .setWidth("752/2560*w").setHeight("92/1440*h")
            .setLocationX("24/2560*w").setLocationY("4/1440*h")
            .setRelative(true).setScrollDraggable(true)
            .setInvalidV(true).setInvalidH(false)
            .setScrollableH("1000/2560*w")
            .setSliderV(new GermGuiColor("sliderV").setEndColor(0xFF232323).setColor(0xFF232323).setWidth("0").setHeight("0"))
            .setSliderH(new GermGuiColor("sliderH").setEndColor(0xFF232323).setColor(0xFF232323).setWidth("60/2560*w").setHeight("8/1440*h"))
            .setLocationHY("102/2560*w").setHeightH("10/1440*h").setWidthH("(752-60)/2560*w");
    public final List<GGESTitleAction> actions = new ArrayList<>();
    public GGESTitleAction exit;

    public GGESTitleBar(GGES parent)
    {
        super("action_bar");
        this.parent=parent;
        this.ges=parent.ges;
    }

    public void build()
    {
        UtilGerm2K.setLocation(this,880,0);
        addGuiPart(UtilGerm2K.createTexture("main","he/ges/action/main.png"));
        addGuiPart(scroll);

        for(IGESTitleAction a : ges.titleActions)
        {
            if(!a.showInTitle())continue;
            GGESTitleAction g = new GGESTitleAction(this,a);
            g.build();
            actions.add(g);
        }

        exit = new GGESTitleAction(this, GESTitleActionManager.create("exit"));
        exit.build();

        UtilGerm2K.setLocation(exit,804,4);
        addGuiPart(exit);

        updateLayout();

        update();
    }
    public void update()
    {
        actions.forEach(GGESTitleAction::update);
    }
    public void updateLayout()
    {
        int size = actions.size();

        for(int i = 0;i<size;i++)
        {
            GGESTitleAction a = actions.get(i);

            UtilGerm2K.setLocation(a,i*94,0);

            if(scroll.getGuiPart(a.getIndexName())==null)scroll.addGuiPart(a);
        }
    }
}
