package net.mcbbs.a1mercet.germhetools.gui.germ;

import com.germ.germplugin.api.dynamic.gui.*;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import net.mcbbs.a1mercet.germhetools.player.ges.preset.IPreset;
import net.mcbbs.a1mercet.germhetools.player.ges.preset.PresetLibrary;
import net.mcbbs.a1mercet.germhetools.player.ges.preset.PresetList;
import net.mcbbs.a1mercet.germhetools.util.UtilGerm2K;

import java.util.ArrayList;
import java.util.List;

public class GPresetLibrary extends GermGuiBase
{
    public interface ICallbackSelect
    {
        void onSelect(PlayerState ps , GPreset preset);
    }

    public static int PSIZE = 110;
    public static int PINTERVAL = 5;
    public static int PMAX_ROW = 3;
    public static int PMAX_RANK = 5;

    public class GList extends GermGuiCanvas
    {
        public String color = "blue";
        public final PresetList<IPreset<?>> list;
        public boolean isOpen = false;

        public final GermGuiTexture background  = UtilGerm2K.createTexture("background","",0,20,590,5);
        public final GermGuiButton title        = UtilGerm2K.createButton("title","",0,0,590,35)
                .registerCallbackHandler((p,b)->{
                    if(isOpen)  clear();
                    else        reset();
                }, GermGuiButton.EventType.LEFT_CLICK);
        public final GermGuiButton delete       = UtilGerm2K.createButton("delete","he/library/delete.png",560,5).registerCallbackHandler((p,b)->delete(), GermGuiButton.EventType.LEFT_CLICK);
        public final GermGuiLabel name          = UtilGerm2K.createLabel("name","",550,8,2.5F, GermGuiLabel.Align.RIGHT);
        public final GermGuiTexture searchTex   = UtilGerm2K.createTexture("search_tex","he/library/search.png",48,3);
        public final GermGuiInput search        = new GermGuiInput("search")
                .setPermanentFocus(false).setPreview("...").setSync(true).setSwallow(true).setAutoClear(false)
                .setBackground(false).setWidth("173/2560*w").setHeight("25/1440*h").setLocationX("60/2560*w").setLocationY("3/1440*h");

        public final GermGuiScroll scroll       = new GermGuiScroll("scroll")
                .setWidth("570/2560*w").setHeight("1000/1440*h")
                .setLocationX("15/2560*w").setLocationY("40/1440*h")
                .setRelative(true).setScrollDraggable(true)
                .setInvalidV(false)
                .setScrollableV("0/1440*w")
                .setSliderV(new GermGuiColor("sliderV").setEndColor(0xFF232323).setColor(0xFF232323).setWidth("7/2560*w").setHeight("110/1440*h"))
                .setSliderH(new GermGuiColor("sliderH").setEndColor(0xAA000000).setColor(0xAA000000).setWidth("0").setHeight("0"))
                .setLocationVX("-15/2560*w").setWidthV("10/2560*w");
        public final List<GPreset> presets = new ArrayList<>();

        public GList(PresetList<IPreset<?>> list , String color)
        {
            super(list.category);
            this.list   = list;
            this.color  = color;
            init();
            update();
        }
        public void delete()
        {
            if(library.deleteList(list.category)!=null)
                GPresetLibrary.this.removeList(this);
        }
        protected void init()
        {
            addGuiPart(background);
            addGuiPart(title);
            addGuiPart(name);
            addGuiPart(delete);
            addGuiPart(searchTex);
            addGuiPart(search.registerCallbackHandler((p,b)->search(search.getInput()), GermGuiInput.EventType.ENTER));

            addGuiPart(scroll);
        }
        public void search(String v)
        {
            if(v==null||"".equals(v)){reset();return;}

            clear();

            List<IPreset<?>> presets = new ArrayList<>();

            for(IPreset<?> s : list)
                if(s.getID().contains(v) || s.getName().contains(v))
                    presets.add(s);

            isOpen=true;
            presets.forEach(e-> this.presets.add(createGPreset(e)));
            updateLayout();
        }
        public void reset()
        {
            clear();

            isOpen=true;
            for(IPreset<?> preset : list)
                presets.add(createGPreset(preset));
            updateLayout();
        }
        public void clear()
        {
            isOpen=false;
            scroll.clearGuiPart();
            presets.clear();
            updateLayout();
        }

        public void updateLayout()
        {
            int rank = 0,row = 0;
            for(int i = 0;i<presets.size();i++,rank++)
            {
                if(rank>=PMAX_RANK){rank=0;row++;}
                GPreset g = presets.get(i);
                UtilGerm2K.setLocation(g,rank*(PSIZE+PINTERVAL),row*(PSIZE+PINTERVAL));

                if(scroll.getGuiPart(g.getIndexName())==null)scroll.addGuiPart(g);
            }

            int layoutHeight = getLayoutHeight();

            background.setHeight(layoutHeight+"/1440*h");
            scroll.setScrollableV(layoutHeight+"/1440*h");
            scroll.setHeightV("("+layoutHeight+"-110)/1440*h");

            update();
            GPresetLibrary.this.updateLayout();
        }
        public void update()
        {
            background.setPath("he/library/list_"+color+"_color.png");
            title.setDefaultPath("he/library/list_"+color+(isOpen?"_open":"")+".png");
            name.setText("#FFAFAFAF"+list.category+"("+list.size()+")");
        }
        public void remove(IPreset<?> preset)
        {
            for(GPreset g : presets)
                if(g.preset.equals(preset))
                {
                    scroll.removeGuiPart(g);
                    presets.remove(g);
                    updateLayout();
                    name.setText("#FFAFAFAF"+list.category+"("+(list.size()-1)+")");
                    break;
                }
        }
        public void update(IPreset<?> preset)
        {
            for(GPreset g : presets)
                if(g.preset.equals(preset))
                {
                    g.update();
                    break;
                }
        }
        public void add(IPreset<?> preset)
        {
            remove(preset);
            GPreset g = createGPreset(preset);
            presets.add(g);
            updateLayout();
            name.setText("#FFAFAFAF"+list.category+"("+(list.size()+1)+")");
        }
        public GPreset createGPreset(IPreset<?> p)
        {
            GPreset gpreset = p.createGPreset(ps,PSIZE);
            gpreset.registerCallbackSelect((pr,g)->callbackSelect(gpreset));
            return gpreset;
        }

        public int getLayoutHeight()    {return presets.size()==0?0:Math.min(Math.max(1,presets.size()/PMAX_RANK)*(PSIZE+PINTERVAL),PMAX_ROW*(PSIZE+PINTERVAL))+20;}
        public int getListHeight()      {return 35+getLayoutHeight();}
    }
    public final PlayerState ps;
    public final PresetLibrary library;
    public final List<GList> lists = new ArrayList<>();

    public final GermGuiTexture main        = UtilGerm2K.createTexture("main","he/library/main.png");
    public final GermGuiButton close        = UtilGerm2K.createButton("close","he/library/close.png",562,2)
            .registerCallbackHandler((p,b)->close(), GermGuiButton.EventType.LEFT_CLICK);

    public final GermGuiScroll scroll = new GermGuiScroll("scroll")
            .setWidth("590/2560*w").setHeight("545/1440*h")
            .setLocationX("5/2560*w").setLocationY("45/1440*h")
            .setRelative(true).setScrollDraggable(true)
            .setInvalidV(false)
            .setScrollableV("0/1440*w")
            .setSliderV(new GermGuiColor("sliderV").setEndColor(0xFF232323).setColor(0xFF232323).setWidth("0/2560*w").setHeight("0/1440*h"))
            .setSliderH(new GermGuiColor("sliderH").setEndColor(0xAA000000).setColor(0xAA000000).setWidth("0").setHeight("0"));

    public GPresetLibrary(PlayerState ps)
    {
        super("library");
        this.ps=ps;
        this.library=ps.ges.library;
        init();
        update();
    }
    protected void init()
    {
        addGuiPart(main);
        addGuiPart(close);
        addGuiPart(scroll);

        getOptions().setStartX("980/2560*w").setStartY("420/1440*h");
        getOptions().setDrag(new GermGuiOptions.Drag().setWidth("660/2560*w").setHeight("40/1440*h").setLocationX("0").setLocationY("0"));
        getOptions().setDragSwallow(false);
        reset();
    }
    protected ICallbackSelect callbackSelect;
    public GPresetLibrary registerCallbackSelect(ICallbackSelect i){callbackSelect=i;return this;}
    public GPresetLibrary clearCallbackSelect(){callbackSelect=null;return this;}
    public void callbackSelect(GPreset preset)
    {
        if(callbackSelect!=null)
            callbackSelect.onSelect(ps,preset);
    }
    public void update()
    {

    }
    public void reset()
    {
        scroll.clearGuiPart();
        lists.clear();

        library.getList().forEach(e->lists.add(new GList(e,e.color)));
        updateLayout();
    }

    public void add(GList list)
    {
        lists.add(list);
        updateLayout();
    }

    public void updateLayout()
    {
        int h = 0;
        for(GList list : lists)
        {
            UtilGerm2K.setLocation(list,0,h);
            h+=list.getListHeight()+5;
            if(scroll.getGuiPart(list.getIndexName())==null)scroll.addGuiPart(list);
        }
        scroll.setScrollableV((h+50)+"/1440*h");
    }
    public void removeList(GList list)
    {
        scroll.removeGuiPart(list);
        lists.remove(list);
        updateLayout();
    }
    public void update(String category , IPreset<?> preset)
    {
        for(GList l : lists)
            if(l.list.category.equals(category))
            {
                l.update(preset);
                break;
            }
    }
    public void add(String category , IPreset<?> preset)
    {
        for(GList l : lists)
            if(l.list.category.equals(category))
            {
                l.add(preset);
                break;
            }
    }
    public void remove(String category , IPreset<?> preset)
    {
        for(GList l : lists)
            if(l.list.category.equals(category))
            {
                l.remove(preset);
                break;
            }
    }

}
