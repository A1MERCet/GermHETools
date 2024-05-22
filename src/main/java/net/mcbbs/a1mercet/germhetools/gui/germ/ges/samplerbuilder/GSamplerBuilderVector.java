package net.mcbbs.a1mercet.germhetools.gui.germ.ges.samplerbuilder;

import com.germ.germplugin.api.dynamic.gui.GermGuiButton;
import com.germ.germplugin.api.dynamic.gui.GermGuiInput;
import com.germ.germplugin.api.dynamic.gui.GermGuiLabel;
import net.mcbbs.a1mercet.germhetools.player.ges.action.GESActionVector;
import net.mcbbs.a1mercet.germhetools.player.ges.builder.SampleBuilderVector;
import net.mcbbs.a1mercet.germhetools.util.UtilGerm2K;
import org.bukkit.util.Vector;

public class GSamplerBuilderVector extends GSamplerBuilder
{
    public final SampleBuilderVector<? extends GESActionVector> builder;
    public final GermGuiButton overwrite = UtilGerm2K.createButton("overwrity","he/ges/builder/vector/false.png",217,48)
            .registerCallbackHandler((p,b)->callbackOverwirte(), GermGuiButton.EventType.LEFT_CLICK);
    public final GermGuiLabel value = UtilGerm2K.createLabel("value","",180,15,2.5F, GermGuiLabel.Align.CENTER);
    public final GermGuiInput subdivision = new GermGuiInput("input")
            .setSync(true).setBackground(false).setInput("1.0")
            .setLocationX("8/2560*w").setLocationY("40/1440*h")
            .setWidth("100/2560*w").setHeight("30/1440*h")
            .registerCallbackHandler((p,i)->callbackSubdivision(), GermGuiInput.EventType.ENTER)
            .registerCallbackHandler((p,i)->callbackSubdivision(), GermGuiInput.EventType.INPUT)
            ;

    public GSamplerBuilderVector(SampleBuilderVector<? extends GESActionVector> builder)
    {
        super(builder);
        this.builder=builder;
    }

    @Override
    public void build()
    {
        super.build();
        getOptions().setStartX("1101/2560*w").setStartY("1000/1440*h");

        addGuiPart(UtilGerm2K.createTexture("main","he/ges/builder/vector/main.png"));
        addGuiPart(overwrite);
        addGuiPart(value);
        addGuiPart(subdivision);
        update();
    }

    @Override
    public void update()
    {
        super.update();
        overwrite.setDefaultPath("he/ges/builder/vector/"+(builder.action.overwrite?"true":"false")+".png");
        Vector cal = builder.action.calTargetVector();
        value.setText("#FFC8C8C8"+String.format("%.1f",cal.getX()).replace(".0","")+","+String.format("%.1f",cal.getY()).replace(".0","")+","+String.format("%.1f",cal.getZ()).replace(".0",""));
        subdivision.setInput(String.valueOf(builder.getSubdivision()));
    }

    public void callbackSubdivision()
    {
        double v = subdivision.getInput().equals("")?1D:Double.parseDouble(subdivision.getInput());
        if(v==builder.getSubdivision())return;
        builder.setSubdivision(v);
    }

    public void callbackOverwirte()
    {
        builder.action.setOverwrite(!builder.action.overwrite);
        update();
    }
}
