package net.mcbbs.a1mercet.germhetools.gui.germ;

import com.germ.germplugin.api.dynamic.gui.GermGuiSlot;
import net.mcbbs.a1mercet.germhetools.gui.HEGuiManager;
import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;

import java.util.UUID;

public class GPresetHEBlock extends GPreset
{
    public interface ICallbackEdit
    {
        boolean handle(PlayerState ps , GPreset preset);
    }

    public ICallbackEdit callbackEdit;
    public GPreset registerCallbackEdit(ICallbackEdit i){callbackEdit =i;return this;}
    public GPreset clearCallbackEdit(){callbackEdit =null;return this;}


    public final GermGuiSlot slot = new GermGuiSlot(UUID.randomUUID().toString())
            .setWidth("70/2560*w").setHeight("70/1440*h")
            .setLocationX("20/2560*w").setLocationY("20/1440*h")
            .setSwallow(false).setIdentity(UUID.randomUUID().toString())
            .setFillPath("he/air.png").setEmptyPath("he/air.png")
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

    @Override
    public void onMidClick()
    {
        super.onMidClick();
        HEGuiManager.createHEStateGui(ps.player,state,false).openChildGuiTo(ps.player);
    }
}
