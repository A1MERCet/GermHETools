package net.mcbbs.a1mercet.germhetools.gui.germ.ges.samplerbuilder;

import com.germ.germplugin.api.dynamic.gui.GermGuiLabel;
import com.germ.germplugin.api.dynamic.gui.GermGuiTexture;
import net.mcbbs.a1mercet.germhetools.gui.germ.GermGuiBase;
import net.mcbbs.a1mercet.germhetools.player.ges.action.IGESAction;
import net.mcbbs.a1mercet.germhetools.player.ges.builder.SampleBuilder;
import net.mcbbs.a1mercet.germhetools.util.Options;
import net.mcbbs.a1mercet.germhetools.util.UtilGerm2K;

public class GSamplerBuilder extends GermGuiBase
{
    public final GermGuiTexture icon = UtilGerm2K.createTexture("icon","icon/setting.png",6,-21,18,18);
    public final GermGuiLabel name   = UtilGerm2K.createLabel("action_name","",29,-19,2.25F);

    public final SampleBuilder<? extends IGESAction> builder;
    public GSamplerBuilder(SampleBuilder<? extends IGESAction> builder)
    {
        super("builder_"+builder.action.getID());
        this.builder=builder;
    }
    public void build()
    {
        addGuiPart(icon);
        addGuiPart(name);
    }
    public void update()
    {
        name.setText(builder.action.getName());
    }
    public void onKeyHandle(int key , int assist)
    {
        update();
    }
    public void onStateReSelect()
    {
        update();
    }
    public void onDataHandle(String key , Options.IValue value)
    {
        update();
    }
    public void onClear()
    {
        update();
    }
    public void onApply()
    {
        update();
    }
}
