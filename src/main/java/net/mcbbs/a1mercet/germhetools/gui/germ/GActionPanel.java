package net.mcbbs.a1mercet.germhetools.gui.germ;

import com.germ.germplugin.api.KeyType;
import com.germ.germplugin.api.dynamic.animation.GermAnimationFade;
import com.germ.germplugin.api.dynamic.gui.*;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import net.mcbbs.a1mercet.germhetools.util.UtilGerm2K;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GActionPanel extends GermGuiBase
{

    public interface CallbackAction { boolean handle(Player p , GActionBar bar);}

    public static class GActionBar extends GermGuiCanvas
    {
        public final GActionPanel parent;
        public final Action action;
        public GermGuiButton button;

        public GActionBar(GActionPanel parent , Action a)
        {
            super(a.id);
            this.parent=parent;this.action=a;
            init();
        }
        protected void onSelect()
        {
            button.setDefaultPath("aestus/att/action/bar2.png");
        }
        protected void onUnSelect()
        {
            button.setDefaultPath("aestus/att/action/bar.png");
        }
        protected void onAction(Player p ,Enum<?> e)
        {
            if(e == GermGuiButton.EventType.BEGIN_HOVER)
                parent.select(this);
        }
        protected void init()
        {
            button = UtilGerm2K.createButton("button","aestus/att/action/bar.png",5,0)
                    .registerCallbackHandler((p,b)->{

                        for(CallbackAction c : parent.getCallbacks())
                            if(!c.handle(p,this))
                                return;

                        action.callback((GActionPanel)getGermGuiScreen());
                    }, GermGuiButton.EventType.LEFT_CLICK)
                    .registerCallbackHandler((p,b)->onAction(p,GermGuiButton.EventType.BEGIN_HOVER), GermGuiButton.EventType.BEGIN_HOVER)
                    .registerCallbackHandler((p,b)->onAction(p,GermGuiButton.EventType.LEAVE_HOVER), GermGuiButton.EventType.LEAVE_HOVER)
            ;
            addGuiPart(button);
            addGuiPart(UtilGerm2K.createLabel("name", action.name, 42,10,2.5F).setOmitWidth("280/2560*w"));
            addGuiPart(UtilGerm2K.createTexture("icon", action.icon.contains("/")? action.icon :("icon/" + action.icon), 14,9,22,22)
                    .addAnimation(new GermAnimationFade("fade").setFrom(1).setTo(0.3).setPermanent(true).setDuration(1).setCycle(1)));
        }
    }

    public static class Action
    {
        final String id;
        final String name;
        String icon = "setting.png";
        boolean invalid = false;

        public Action(String id, String name)
        {
            this.id = id;
            this.name = name;
        }

        public boolean callback(GActionPanel parent)
        {
            return !invalid;
        }

        protected GActionBar createPart(GActionPanel parent)
        {
            return new GActionBar(parent,this);
        }
        protected int getLayoutHeight()
        {
            return 42;
        }

        public String getId() {return id;}
        public String getName() {return name;}
        public boolean isInvalid() {return invalid;}
        public Action setInvalid(boolean invalid) {this.invalid = invalid;return this;}
        public String getIcon() {return icon;}
        public Action setIcon(String icon) {this.icon = icon;return this;}
    }

    public final Player iplayer;
    public final PlayerState ps;
    public final List<Action> actions;
    public final List<CallbackAction> callbacks = new ArrayList<>();
    public int height = 0;


    public GActionPanel(Player iplayer)
    {
        this(iplayer,new ArrayList<>());
    }
    public GActionPanel(Player iplayer, List<Action> actions)
    {
        super("action");
        this.iplayer=iplayer;
        this.ps= PlayerState.get(iplayer);
        this.actions=actions;
        registerKeyDownHandler(KeyType.KEY_UP,(p,g)->selectDown());
        registerKeyDownHandler(KeyType.KEY_DOWN,(p,g)->selectUp());
        registerKeyDownHandler(KeyType.KEY_F,(p,g)->confirm());
        registerKeyDownHandler(KeyType.KEY_SPACE,(p,g)->confirm());
        registerKeyDownHandler(KeyType.KEY_EQUALS,(p,g)->confirm());
    }

    public GActionPanel registerCallback(CallbackAction c)  {callbacks.add(c);return this;}
    public List<CallbackAction> getCallbacks()              {return callbacks;}

    public GActionPanel addAction(Action a){actions.add(a);return this;}
    public GActionPanel addAction(Action a,int index){actions.add(index,a);return this;}

    protected GActionBar current = null;
    public void confirm()
    {
        if(current==null)return;
        current.action.callback(this);
    }
    public void select(GActionBar bar)
    {
        if(bar.equals(current))return;
        bars.forEach(GActionBar::onUnSelect);
        bar.onSelect();
        current=bar;
    }
    public void selectUp()
    {
        if(bars.size()==0)return;
        if(current==null)current=bars.get(0);

        int index = bars.indexOf(current);
        int v = index>=bars.size()-1?0:index+1;
        select(bars.get(v));
    }
    public void selectDown()
    {
        if(bars.size()==0)return;
        if(current==null)current=bars.get(0);

        int index = bars.indexOf(current);
        int v = index<=0?bars.size()-1:index-1;
        select(bars.get(v));
    }

    public final List<GActionBar> bars = new ArrayList<>();
    protected boolean build()
    {
        addGuiPart(new GermGuiButton("closebg").setDefaultPath("item/air.png").setHoverPath("item/air.png").setWidth("30000").setHeight("30000").setLocationX(-15000).setLocationY(-15000)
                .registerCallbackHandler((p,b)->close(), GermGuiButton.EventType.LEFT_CLICK)
                .registerCallbackHandler((p,b)->close(), GermGuiButton.EventType.RIGHT_CLICK));


        GermGuiTexture top = UtilGerm2K.createTexture("top","aestus/att/action/top.png");
        GermGuiTexture mid = UtilGerm2K.createTexture("mid","aestus/att/action/mid.png",0,50);
        GermGuiTexture under = UtilGerm2K.createTexture("under","aestus/att/action/under.png");

        addGuiPart(top);
        addGuiPart(mid);
        addGuiPart(under);

        int h = 52;
        for (Action a : actions) {
            GActionBar part = a.createPart(this);
            UtilGerm2K.setLocation(part, 0, h);
            h = h + 3 + a.getLayoutHeight();
            addGuiPart(part);
            bars.add(part);
        }

        UtilGerm2K.setSize(mid,300,h-50+20);
        UtilGerm2K.setLocation(under,0,h-1+20);

        addGuiPart(UtilGerm2K.createButton("closeee","aestus/att/action/close.png",255,10)
                .registerCallbackHandler((p,b)->close(), GermGuiButton.EventType.LEFT_CLICK));

        this.height=h;
        getOptions().setStartX("min(w-(320/2560*w),x)").setStartY("min(h-("+height+"+70)/1440*h,y)");

        if(bars.size()>0){select(bars.get(0));}

        return true;
    }

    public Player getIplayer() {return iplayer;}
    public PlayerState getPs() {return ps;}
    public List<Action> getActions() {return actions;}
    public Action getAction(String id) {
        for(Action a : actions)
            if(a.id.equals(id))
                return a;
        return null;
    }

    protected boolean callEvent()
    {
        return true;
    }

    @Override
    public void openChildGui(Player player) {
        if(!build())return;
        super.openChildGui(player);
    }
    @Override
    public void openGui(Player player) {
        if(!build())return;
        super.openGui(player);
    }
    @Override
    public void openHud(Player player) {
        if(!build())return;
        getOptions().setStartX("(1280+120)/2560*w").setStartY("(720+120)/1440*h");
        super.openHud(player);
    }

    public static void close(Player p)
    {
        for(GermGuiScreen g : GuiManager.getOpenedAllGui(p))
            if(g instanceof GActionPanel)
                g.close();
    }
}
