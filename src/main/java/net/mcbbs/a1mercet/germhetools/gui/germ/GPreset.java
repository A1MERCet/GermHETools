package net.mcbbs.a1mercet.germhetools.gui.germ;

import com.germ.germplugin.api.dynamic.gui.GermGuiButton;
import com.germ.germplugin.api.dynamic.gui.GermGuiCanvas;
import com.germ.germplugin.api.dynamic.gui.GermGuiLabel;
import com.germ.germplugin.api.dynamic.gui.GermGuiTexture;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import net.mcbbs.a1mercet.germhetools.player.ges.preset.IPreset;
import net.mcbbs.a1mercet.germhetools.util.UtilGerm2K;

import java.util.UUID;

public class GPreset extends GermGuiCanvas
{
    public interface ICallbackSelect
    {
        void onSelect(PlayerState ps,GPreset preset);
    }

    protected ICallbackSelect callbackSelect;
    public GPreset registerCallbackSelect(ICallbackSelect i){callbackSelect=i;return this;}

    public GPreset clearCallbackSelect(){callbackSelect=null;return this;}
    public void callbackSelect()
    {
        if(callbackSelect!=null)
            callbackSelect.onSelect(ps,this);
    }

    public int size;
    public final PlayerState ps;
    public final IPreset<?> preset;
    public GermGuiTexture img;
    public GermGuiLabel name;
    public GermGuiButton button;

    public GPreset(PlayerState ps , IPreset<?> preset , int size)
    {
        super(UUID.randomUUID().toString());

        this.ps=ps;
        this.preset =preset;
        this.size=size;
    }

    public void build()
    {
        img     = UtilGerm2K.createTexture("img","",5,5,size-10,size-10).setEnable(false);
        name    = UtilGerm2K.createLabel("name","",size/2,size-25,2F, GermGuiLabel.Align.CENTER).setOmitWidth("180/2560*w");
        button  = UtilGerm2K.createButton("button","he/library/preset.png",0,0)
            .registerCallbackHandler((p,b)->onLeftClick(),  GermGuiButton.EventType.LEFT_CLICK)
            .registerCallbackHandler((p,b)->onRightClick(), GermGuiButton.EventType.RIGHT_CLICK)
            .registerCallbackHandler((p,b)->onMidClick(), GermGuiButton.EventType.MIDDLE_CLICK)
        ;

        addGuiPart(button);
        addGuiPart(img);
        addGuiPart(name);
        update();
    }
    public void update()
    {
        name.setText("#FF808080"+ preset.getName());
        img.setEnable(preset.getData().containsKey("enable_img"));
        img.setPath("he/img/"+ preset.getID()+".png");
    }

    public void onLeftClick()
    {
        callbackSelect();
    }
    public void onMidClick()
    {

    }
    public void onRightClick()
    {
        preset.createGActionPanel(this).openChildGui(ps.player);
    }

}
