package net.mcbbs.a1mercet.germhetools.gui.germ;

import com.germ.germplugin.api.GermBlockAPI;
import com.germ.germplugin.api.dynamic.gui.GermGuiSlot;
import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import net.mcbbs.a1mercet.germhetools.player.ges.preset.GermBlock;

import java.util.UUID;

public class GPresetGermBlock extends GPreset
{

    public final GermGuiSlot slot = new GermGuiSlot(UUID.randomUUID().toString())
            .setWidth("70/2560*w").setHeight("70/1440*h")
            .setLocationX("20/2560*w").setLocationY("20/1440*h")
            .setSwallow(false).setIdentity(UUID.randomUUID().toString())
            .setFillPath("he/air.png").setEmptyPath("he/air.png")
            ;

    public final GermBlock state;
    public GPresetGermBlock(PlayerState ps, GermBlock state, int size)
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
//        slot.setItemStack(GermBlockAPI.);
    }
}
