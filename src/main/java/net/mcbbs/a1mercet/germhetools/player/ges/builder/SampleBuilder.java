package net.mcbbs.a1mercet.germhetools.player.ges.builder;

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
            dataHandle(key,value);
            return v;
        }

        @Override
        public IValue remove(Object key) {
            IValue v = super.remove(key);
            if(!(key instanceof String))return v;
            dataHandle((String) key,null);
            return v;
        }
    }
    public final BuilderData data = new BuilderData();
    public final GES ges;
    public T sample;

    public SampleBuilder(T sampler)
    {
        this.sample=sampler;
        this.ges=sampler.getGES();
    }

    public void onActive()
    {

    }

    public void onStateReSelect()
    {
        sample.onStateReSelect();
    }

    public boolean keyHandle(int key , int assist)
    {
        return ges.current != null;
    }
    public void dataHandle(String key , Options.IValue value)
    {

    }

    public GES getGES()
    {
        return sample.getGES();
    }

    public void clear()
    {

    }
    public void apply()
    {
        sample.onApply(ges);
        ges.history.addAction(sample);
    }
}
