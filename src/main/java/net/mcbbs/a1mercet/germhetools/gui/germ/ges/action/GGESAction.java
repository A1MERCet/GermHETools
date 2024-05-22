package net.mcbbs.a1mercet.germhetools.gui.germ.ges.action;

import com.germ.germplugin.api.dynamic.gui.GermGuiButton;
import com.germ.germplugin.api.dynamic.gui.GermGuiCanvas;
import com.germ.germplugin.api.dynamic.gui.GermGuiTexture;
import net.mcbbs.a1mercet.germhetools.gui.germ.ges.GGESActionBar;
import net.mcbbs.a1mercet.germhetools.player.ges.action.IGESAction;
import net.mcbbs.a1mercet.germhetools.player.ges.builder.SampleBuilder;
import net.mcbbs.a1mercet.germhetools.util.UtilGerm2K;

public class GGESAction extends GermGuiCanvas
{
    public final GGESActionBar parent;
    public final IGESAction action;
    public final GermGuiButton button = UtilGerm2K.createButton("button","he/ges/action/",0,0)
            .registerCallbackHandler((p,b)->callback(), GermGuiButton.EventType.LEFT_CLICK);
    public final GermGuiTexture select = UtilGerm2K.createTexture("select","he/ges/action/current.png",-30,0).setEnable(false);

    public GGESAction(GGESActionBar parent,IGESAction action)
    {
        super(action.getID());
        this.parent=parent;
        this.action=action;
    }

    public void build()
    {
        addGuiPart(button);
        addGuiPart(select);
        update();
    }
    public void update()
    {
        button.setDefaultPath("he/ges/builder/"+action.getID()+".png");
        SampleBuilder<? extends IGESAction> current = parent.ges.getBuilder();
        select.setEnable(action.getID().equals(current==null?false:current.action.getID()));
    }

    public void callback()
    {
        parent.ges.setBuilder(action,true);

        parent.actions.forEach(GGESAction::update);
        update();
    }
}
