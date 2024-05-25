package net.mcbbs.a1mercet.germhetools.player.ges.actiontile;

import net.mcbbs.a1mercet.germhetools.player.ges.GES;
import net.mcbbs.a1mercet.germhetools.player.ges.action.GESActionCreate;
import net.mcbbs.a1mercet.germhetools.player.ges.preset.IPreset;
import net.mcbbs.a1mercet.germhetools.player.ges.target.IGESBlock;
import net.mcbbs.a1mercet.germhetools.player.ges.target.IGESTarget;

public class GESTitleActionCopyBlock extends GESTitleActionBase
{
    public GESTitleActionCopyBlock()
    {
        super("copy_block", "复制目标");
    }

    @Override
    public boolean execute(GES ges)
    {
        if(!super.execute(ges))return false;

        if(ges.target==null){ges.warn("无目标选择");return false;}
        if(!(ges.target instanceof IPreset<?>)){ges.warn("目标类型不适用["+ges.target.getType()+"]");return false;}
        if(!(ges.target instanceof IGESBlock)){ges.warn(" 目标类型不适用["+ges.target.getType()+"]");return false;}
        IPreset<?> target = ((IPreset<?>) ges.target);
        IPreset<?> copy = target.createInstance();
        copy.copy(target);


        ges.removeBuilder();

        ges.setTarget((IGESTarget) copy);

        GESActionCreate action = new GESActionCreate(ges).setBlock((IGESBlock) copy);
        ges.setBuilder(action);

        return true;
    }

    @Override public int[] getKey() {return new int[]{21,-1};}
}
