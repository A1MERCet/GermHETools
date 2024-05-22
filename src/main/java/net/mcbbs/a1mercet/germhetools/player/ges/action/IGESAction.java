package net.mcbbs.a1mercet.germhetools.player.ges.action;

import com.germ.germplugin.api.dynamic.effect.GermEffectPart;
import net.mcbbs.a1mercet.germhetools.gui.germ.effect.GSquareBox;
import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import net.mcbbs.a1mercet.germhetools.player.ges.builder.SampleBuilder;
import net.mcbbs.a1mercet.germhetools.player.ges.target.IGESLocation;
import net.mcbbs.a1mercet.germhetools.player.ges.target.IGESTarget;
import net.mcbbs.a1mercet.germhetools.util.IConfig;
import net.mcbbs.a1mercet.germhetools.util.Options;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public interface IGESAction extends IConfig
{
    @Override
    default String getDefaultPath(){return getID();}
    @Override
    default void save(ConfigurationSection section)
    {
        section.set("Type",getType());
        getData().save(section.createSection("Data"));
    }

    @Override
    default void load(ConfigurationSection section)
    {
        if(section.getConfigurationSection("Data")!=null)
            getData().load(section.getConfigurationSection("Data"));
    }

    String getID();
    String getName();
    String getType();
    Options<?> getData();
    default IGESTarget getTarget(){return getGES().target;}
    GES getGES();

    IGESAction createInstance(GES ges);

    boolean onTargetReSelect();

    default boolean onApply(GES ges)
    {
        ges.msg("已应用操作["+getName()+"]");
        return true;
    }
    default boolean onRevoke(GES ges)
    {
        ges.msg("已撤回操作["+getName()+"]");
        return true;
    }
    SampleBuilder<? extends IGESAction> createBuilder();

    default GermEffectPart<?> createEffect(){return new GSquareBox();}
    default Location getEffectLocation(){return getTarget() instanceof IGESLocation?((IGESLocation) getTarget()).getLocation():getGES().ps.player.getLocation();}
    default boolean onShowSample()
    {
        onUnshowSample();

        IGESTarget target = getTarget();
        if(target==null)return false;

        GermEffectPart<?> e = createEffect();

        e.spawnToLocation(getGES().ps.player,getEffectLocation());

        target.getTargetData().putDefault("effect",e);
        return true;
    }
    default boolean onUnshowSample()
    {
        IGESTarget target = getTarget();
        if(target==null)return false;

        Options.IValue ivalue = target.getTargetData().get("effect");
        if(ivalue!=null&&ivalue.getValue()!=null)
            ((GermEffectPart<?>)ivalue.getValue()).despawn(getGES().ps.player);
        return true;
    }
    default boolean showInHUD(){return true;}
    default int[] getKey(){return new int[]{-1,-1};}
}
