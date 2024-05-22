package net.mcbbs.a1mercet.germhetools.gui.germ.ges;

import com.germ.germplugin.api.dynamic.animation.AnimationGroup;
import com.germ.germplugin.api.dynamic.animation.GermAnimationFade;
import com.germ.germplugin.api.dynamic.animation.GermAnimationMove;
import com.germ.germplugin.api.dynamic.gui.*;
import net.mcbbs.a1mercet.germhetools.util.UtilGerm2K;

import java.util.UUID;


public class GMSGInfo extends GermGuiCanvas
{
    public enum Type
    {
        INFO,
        WARN,
        ERROR,
        ;
    }

    public String message;
    public Type type;
    public int layoutHeight             = 46;
    public int layoutWidth              = 340;
    public int duration                 = 80;
    public int left;
    public final GermGuiLabel label     = UtilGerm2K.createLabel("label","",182,13,2.25F, GermGuiLabel.Align.CENTER).setPopTime("20").setSplitWidth("550/2560*w");
    public final GermGuiTexture main    =UtilGerm2K.createTexture("main","he/ges/msg/info.png",0,0);

    public GMSGInfo(String message) {this(Type.INFO,message);}
    public GMSGInfo(Type type,String message)
    {
        super(UUID.randomUUID().toString());
        this.type=type;
        this.message=message;
    }


    public GMSGInfo setAnimation()
    {
        GermAnimationMove moveIn=new GermAnimationMove(getIndexName()+"_movein")
                .setCycle(1).setPermanent(false).setDuration(100)
                .setMoveX("w*0.035-w*0.035*2").setOffsetX("w*0.035");
        GermAnimationFade fade1 = new GermAnimationFade("menu_fade1").setCycle(1).setPermanent(true).setCycle(1)
            .setDelay(0).setDuration(1).setFrom(1).setTo(0);
        GermAnimationFade fade2 = new GermAnimationFade("menu_fade2").setCycle(1).setPermanent(true).setCycle(1)
                    .setDelay(0).setDuration(200).setFrom(0).setTo(1);
        setAnimationGroup(new AnimationGroup().addAnimation(fade1).addAnimation(fade2).addAnimation(moveIn));
        return this;
    }

    public GMSGInfo setEndAnimation()
    {
        GermAnimationMove move=new GermAnimationMove(getIndexName()+"_moveout")
                .setCycle(1).setPermanent(true).setDuration(500)
                .setMoveX("w*0.07");
        GermAnimationFade fade = new GermAnimationFade(getIndexName()+"_fadeout")
                .setPermanent(true).setCycle(1).setDuration(500)
                .setFrom(1).setTo(0);
        setAnimationGroup(new AnimationGroup().addAnimation(fade).addAnimation(move));
        return this;
    }


    protected void build()
    {
        this.left=duration;

        setAnimation();
        addGuiPart(main.setPath("he/ges/msg/"+type.name().toLowerCase()+".png"));
        addGuiPart(label.setText("#FF4B4B4B"+message));
    }
}
