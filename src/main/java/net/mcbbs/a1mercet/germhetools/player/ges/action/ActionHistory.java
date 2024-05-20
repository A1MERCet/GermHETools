package net.mcbbs.a1mercet.germhetools.player.ges.action;

import net.mcbbs.a1mercet.germhetools.Config;
import net.mcbbs.a1mercet.germhetools.player.ges.GES;

import java.util.ArrayList;
import java.util.List;

public class ActionHistory
{
    public final GES parent;
    public final List<IGESAction> history  = new ArrayList<>();
    public final List<IGESAction> recovery = new ArrayList<>();


    public ActionHistory(GES parent)
    {
        this.parent = parent;
    }

    public ActionHistory addAction(IGESAction action)
    {
        if(history.size()>=getHistorySize())history.remove(0);
        history.add(action);
        return this;
    }

    public ActionHistory removeAction(IGESAction action)
    {
        history.remove(action);
        return this;
    }

    public ActionHistory revoke()
    {
        if(history.size()==0)return this;

        IGESAction action = history.get(history.size()-1);
        action.onRevoke(parent);
        recovery.add(action);

        return this;
    }

    public ActionHistory recovery()
    {
        if(recovery.size()==0)return this;

        IGESAction action = recovery.get(recovery.size()-1);
        action.onApply(parent);
        history.add(action);

        return this;
    }

    public int getHistorySize(){return Config.historySize;}
}
