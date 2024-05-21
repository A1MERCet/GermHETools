package net.mcbbs.a1mercet.germhetools.gui.germ;

import com.germ.germplugin.api.dynamic.gui.GermGuiSlot;
import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;

import java.util.UUID;

public class GPresetHEBlock extends GPreset
{

    public final GermGuiSlot slot = new GermGuiSlot(UUID.randomUUID().toString())
            .setWidth("70/2560*w").setHeight("70/1440*h")
            .setLocationX("20/2560*w").setLocationY("20/1440*h")
            .setSwallow(false).setIdentity(UUID.randomUUID().toString())
            .setFillPath("aestus/he/air.png").setEmptyPath("aestus/he/air.png")
            ;

    public final HEState state;
    public GPresetHEBlock(PlayerState ps, HEState state, int size)
    {
        super(ps, state, size);
        this.state=state;
    }

    @Override
    public void build()
    {
        super.build();
        addGuiPart(slot);
    }

    @Override
    public void update()
    {
        super.update();
        slot.setItemStack(state.createItemStack());
    }
}
