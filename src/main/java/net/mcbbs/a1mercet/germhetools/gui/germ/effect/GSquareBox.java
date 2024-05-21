package net.mcbbs.a1mercet.germhetools.gui.germ.effect;

import com.germ.germplugin.api.dynamic.effect.GermEffectEntity;

import java.util.UUID;

public class GSquareBox extends GermEffectEntity
{
    public GSquareBox()
    {
        super(UUID.randomUUID().toString());
        setModel("pig").setName("audio_box").setDuration("5000");
        setYaw("0").setPitch("0").setRoll("0");
        setFollowPitch(false).setFollowYaw(false);
        setFollowBindX(false).setFollowBindY(false).setFollowBindZ(false);
        setRenderRange(1000F)
        .setOffsetX("0.5").setOffsetZ("0.5");
    }
}
