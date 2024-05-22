package net.mcbbs.a1mercet.germhetools.player.ges.builder;

import net.mcbbs.a1mercet.germhetools.gui.germ.ges.samplerbuilder.GSamplerBuilder;
import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import net.mcbbs.a1mercet.germhetools.player.ges.action.IGESAction;
import net.mcbbs.a1mercet.germhetools.util.Options;

public class SampleBuilder<T extends IGESAction>
{
    public class BuilderData extends Options<String>
    {
        @Override
        public IValue put(String key, IValue value)
        {
            IValue v = super.put(key, value);
            onDataHandle(key,value);
            return v;
        }

        @Override
        public IValue remove(Object key) {
            IValue v = super.remove(key);
            if(!(key instanceof String))return v;
            onDataHandle((String) key,null);
            return v;
        }
    }

    protected GSamplerBuilder hud;

    public final BuilderData data = new BuilderData();
    public final GES ges;
    public T action;

    public SampleBuilder(T action)
    {
        this.action =action;
        this.ges=action.getGES();
    }

    public void onActive()
    {

    }

    public void exit() {getGES().removeBuilder();}

    public void onTargetReSelect()
    {
        action.onTargetReSelect();
        if(hud!=null)hud.update();
    }

    public boolean keyHandle(int key , int assist)
    {
        if(ges.target != null) {
            if(hud!=null)hud.onKeyHandle(key,assist);
            if(hud!=null)hud.update();
            return true;
        }else {
            return false;
        }
    }
    public void onDataHandle(String key , Options.IValue value)
    {
        if(hud!=null)hud.onDataHandle(key,value);
    }

    public GES getGES() {return action.getGES();}

    public void clear()
    {
        if(hud!=null)hud.onClear();
    }
    public void apply()
    {
        action.onApply(ges);
        ges.history.addAction(action);

        if(hud!=null)hud.onApply();
    }

    public GSamplerBuilder createHUD() {
        GSamplerBuilder g =  new GSamplerBuilder(this);
        g.build();
        return g;
    }


    public GSamplerBuilder getHud() {if(hud==null)hud=createHUD();return hud;}
}
