package net.mcbbs.a1mercet.germhetools.gui.germ.ges;

import com.germ.germplugin.api.dynamic.gui.GermGuiCanvas;
import com.germ.germplugin.api.dynamic.gui.GermGuiScreen;
import net.mcbbs.a1mercet.germhetools.GermHETools;
import net.mcbbs.a1mercet.germhetools.util.UtilGerm2K;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class GMSGContainer extends GermGuiCanvas
{
    public final List<GMSGInfo> messages = new ArrayList<>();
    public final GermGuiScreen parent;
    public final ReentrantLock lock = new ReentrantLock();

    public GMSGContainer(GermGuiScreen parent)
    {
        super("msg_container");
        this.parent=parent;
        this.parent.removeGuiPart(getIndexName());
        this.parent.addGuiPart(this);
        UtilGerm2K.setLocation(this,1280,1080);
    }

    public GMSGContainer removeMessage(String id)
    {
        lock.lock();
        try {
            for(Iterator<GMSGInfo> it = messages.iterator() ;it.hasNext();)
            {
                GMSGInfo m = it.next();
                if(m.getIndexName().equals(id))
                {
                    removeGuiPart(m);
                    it.remove();
                }
            }

            updateLayout();
        }finally {lock.unlock();}
        return this;
    }

    public void updateLayout()
    {
        int h = 0;
        for (GMSGInfo m : messages)
        {
            UtilGerm2K.setLocation(m,m.layoutWidth/2*-1,h);
            h+=m.layoutHeight;
        }
    }
    public GMSGContainer sendMessage(GMSGInfo m)
    {
        lock.lock();
        try {
            m.build();
            addGuiPart(m);
            messages.add(m);

            updateLayout();

            if(m.duration>0){
                Bukkit.getScheduler().runTaskLater(GermHETools.getInstance(),()->removeMessage(m.getIndexName()),m.duration);
                Bukkit.getScheduler().runTaskLater(GermHETools.getInstance(), m::setEndAnimation,m.duration-10);
            }
        }finally {lock.unlock();}
        return this;
    }
}
