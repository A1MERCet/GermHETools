package net.mcbbs.a1mercet.germhetools.gui.germ.ges;

import net.mcbbs.a1mercet.germhetools.gui.germ.GermGuiBase;
import net.mcbbs.a1mercet.germhetools.gui.germ.ges.action.GGESAction;
import net.mcbbs.a1mercet.germhetools.gui.germ.ges.samplerbuilder.GSamplerBuilder;
import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;

public class GGES extends GermGuiBase
{
    public final GES ges;
    public GGESTitleBar titleBar;
    public GGESActionBar actionBar;
    public @Nullable GSamplerBuilder builder;

    public GMSGContainer msgContainer;

    public GGES(GES ges)
    {
        super("ges");
        this.ges = ges;
    }

    public void build()
    {
        this.msgContainer = new GMSGContainer(this);

        this.titleBar     = new GGESTitleBar(this);
        this.titleBar.build();
        addGuiPart(titleBar);

        this.actionBar    = new GGESActionBar(this);
        this.actionBar.build();
        addGuiPart(actionBar);

        update();
    }
    public void update()
    {
        titleBar.update();
        actionBar.update();
        if(builder!=null)builder.update();
    }


    public void onSetBuilder()
    {
        if(ges.getBuilder()!=null)
        {
            this.builder = ges.getBuilder().getHud();
            this.builder.openHUDTo(ges.ps.player);
        }

        actionBar.update();
    }
    public void onRemoveBuilder()
    {
        if(this.builder!=null)
            this.builder.closeFrom(ges.ps.player);
        this.builder=null;

        actionBar.update();
    }
    public void onClearBuilder()
    {

    }
}
