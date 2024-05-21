package net.mcbbs.a1mercet.germhetools.gui.germ;

import com.germ.germplugin.api.KeyType;
import com.germ.germplugin.api.dynamic.DynamicBase;
import com.germ.germplugin.api.dynamic.animation.GermAnimationFade;
import com.germ.germplugin.api.dynamic.animation.IAnimatable;
import com.germ.germplugin.api.dynamic.gui.*;
import net.mcbbs.a1mercet.germhetools.GermHETools;
import net.mcbbs.a1mercet.germhetools.api.BlockManager;
import net.mcbbs.a1mercet.germhetools.he.HEManager;
import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.gui.HEGuiManager;
import net.mcbbs.a1mercet.germhetools.util.UtilGerm2K;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GHEState extends GermGuiBase
{
    protected class GMaterialQuicklyPanel extends GermGuiCanvas
    {
        public final GMaterials parent;
        public final GInput inputName = new GInput("input_name").setLocation(6,16).setSize(178,38);
        public final GInput inputPath = new GInput("input_path").setLocation(189,16).setSize(366,38);

        public final GInput inputFolder = new GInput("input_folder").setLocation(6,76).setSize(178,38);
        public final GInput inputExclude = new GInput("input_exclude").setLocation(189,76).setSize(366,38);

        public final GInput inputMTL = new GInput("input_mtl").setLocation(6,151).setSize(178,38);
        public final GInput inputMTLPath = new GInput("input_mtl_path").setLocation(189,151).setSize(366,38);

        public GMaterialQuicklyPanel(GMaterials parent)
        {
            super("material_quickly_panel");
            this.parent=parent;

            addGuiPart(UtilGerm2K.createTexture("quickly_panel","aestus/he/edit/material_quicklypanel.png",-4,-9));
            addGuiPart(UtilGerm2K.createTexture("generate_panel","aestus/he/edit/material_generate_panel.png",-4,61));
            addGuiPart(UtilGerm2K.createTexture("mtl_panel","aestus/he/edit/material_mtl_panel.png",-4,131));
            addGuiPart(inputName
                    .setPreview("材质名/纹理名")
                    .registerCallbackHandler((p,i)->add(), GermGuiInput.EventType.ENTER));
            addGuiPart(inputPath
                    .setPreview("纹理路径"));

            addGuiPart(inputFolder
                    .setPreview("文件->材质"));
            addGuiPart(inputExclude
                    .setInput("_m.,_n.,_r."));

            addGuiPart(inputMTL
                    .setPreview("mtl文件"));
            addGuiPart(inputMTLPath
                    .setPreview("纹理路径"));

            addGuiPart(UtilGerm2K.createButton("add_material","aestus/he/edit/material_quicklypanel_add.png",560,6)
                    .registerCallbackHandler((p,b)->add(), GermGuiButton.EventType.LEFT_CLICK));
            addGuiPart(UtilGerm2K.createButton("generate_material","aestus/he/edit/material_quicklypanel_add.png",560,76)
                    .registerCallbackHandler((p,b)->generate(), GermGuiButton.EventType.LEFT_CLICK));
            addGuiPart(UtilGerm2K.createButton("generate_mtl","aestus/he/edit/material_quicklypanel_add.png",560,146)
                    .registerCallbackHandler((p,b)->generateMTL(), GermGuiButton.EventType.LEFT_CLICK));
        }
        public GMaterialBar add(){return add(inputName.getInput(),inputPath.getInput());}
        public GMaterialBar add(String name , String path)
        {
            String[] split = path.split("\\."); if(split.length==0)return null;
            String fileType =  "."+split[split.length-1];

            String customPath = inputName.getInput().contains("/")?inputName.getInput().split("/")[1]:null;

            return parent.addMaterialBar(name+":"+path.replace(fileType,"")+(customPath==null?name:customPath)+fileType);
        }

        public void generate()
        {
            List<String> list = HEManager.generateMaterial(inputFolder.getInput(),Arrays.asList(inputExclude.getInput().split(",")));

            for(int i = 0;i<list.size();i++)
            {
                int finalI = i;
                Bukkit.getScheduler().runTaskLater(GermHETools.getInstance(),()->parent.addMaterialBar(list.get(finalI)),i/10);
            }
        }

        public void generateMTL()
        {
            List<String> list = HEManager.generateMaterial(new File(inputMTL.getInput()),inputMTLPath.getInput());

            for(int i = 0;i<list.size();i++)
            {
                int finalI = i;
                Bukkit.getScheduler().runTaskLater(GermHETools.getInstance(),()->parent.addMaterialBar(list.get(finalI)),i/10);
            }
        }
    }
    protected class GMaterials extends GermGuiCanvas
    {
        public final List<GMaterialBar> list = new ArrayList<>();
        public final GermGuiScroll scroll = new GermGuiScroll("scroll")
                .setWidth("580/2560*w").setHeight("510/1440*h")
                .setLocationX("0/2560*w").setLocationY("55/1440*h")
                .setRelative(true).setScrollDraggable(true)
                .setInvalidV(false)
                .setScrollableV("0/1440*w")
                .setSliderV(new GermGuiColor("sliderV").setEndColor(0xAA000000).setColor(0xAA000000).setWidth("0").setHeight("0"))
                .setSliderH(new GermGuiColor("sliderH").setEndColor(0xAA000000).setColor(0xAA000000).setWidth("0").setHeight("0"));
        public final GermGuiLabel amount = UtilGerm2K.createLabel("amount","0",215,10,1.4F);
        public GMaterials()
        {
            super("materials");
            addGuiPart(UtilGerm2K.createTexture("tmaterials","aestus/he/edit/materials.png"));
            addGuiPart(UtilGerm2K.createButton("clear","aestus/he/edit/clear.png",165,10,40,40)
                    .registerCallbackHandler((p,b)->this.clear(), GermGuiButton.EventType.LEFT_CLICK));
            addGuiPart(UtilGerm2K.createButton("add","aestus/he/edit/material_add.png",530,10)
                    .registerCallbackHandler((p,b)->this.addMaterialBar(), GermGuiButton.EventType.LEFT_CLICK));
            addGuiPart(UtilGerm2K.createButton("add_quickly","aestus/he/edit/material_addquickly.png",385,10)
                    .registerCallbackHandler((p,b)->{
                        if(GHEState.this.getGuiPart("material_quickly_panel")!=null)
                            GHEState.this.removeGuiPart("material_quickly_panel");
                        else
                            GHEState.this.addGuiPart(UtilGerm2K.setLocation(new GMaterialQuicklyPanel(this),165,790));
                    }, GermGuiButton.EventType.LEFT_CLICK));
            addGuiPart(scroll);
            addGuiPart(amount);
        }

        public GMaterials update()
        {
            amount.setText(list.size()+"材质");
            return this;
        }

        public GMaterialBar addMaterialBar(String material)
        {
            GMaterialBar bar = addMaterialBar();
            bar.inputName.setInput(material.split(":")[0]);
            bar.inputPath.setInput(material.replace(bar.inputName.getInput()+":",""));
            return bar;
        }

        public GMaterialBar addMaterialBar()
        {
            GMaterialBar bar = new GMaterialBar(this);
            list.add(bar);
            scroll.addGuiPart(bar);
            updateLayout();
            return bar;
        }

        public GMaterials remove(GMaterialBar bar)
        {
            list.remove(bar);
            scroll.removeGuiPart(bar);
            updateLayout();
            return this;
        }
        public GMaterials updateLayout()
        {

            for(int i = 0;i<list.size();i++)
                UtilGerm2K.setLocation(list.get(i),10,i*65);

            scroll.setScrollableV(list.size()*65+"/1440*h");

            update();

            return this;
        }

        public GMaterials updateToState()
        {
            state.material.clear();
            for(GMaterialBar b : list)
            {
                String v = b.getMaterial();
                state.material.add(v);
            }
            return this;
        }

        public GMaterials clear()
        {
            list.forEach(scroll::removeGuiPart);
            list.clear();
            return this;
        }
    }
    protected class GMaterialBar extends GermGuiCanvas
    {
        public final GMaterials parent;
        public final GInput inputName = new GInput("input_name").setLocation(31,3).setSize(266,24);
        public final GInput inputPath = new GInput("input_path").setLocation(2,32).setSize(556,26);
        public final GermGuiButton remove = UtilGerm2K.createButton("remove","aestus/he/edit/material_remove.png",303,3)
                .registerCallbackHandler((p,b)->this.remove(), GermGuiButton.EventType.LEFT_CLICK);

        public GMaterialBar(GMaterials parent)
        {
            super("material_"+ UUID.randomUUID());
            this.parent=parent;
            addGuiPart(UtilGerm2K.createTexture("main","aestus/he/edit/material_bar.png"));
            addGuiPart(inputName.setInput("材质名"));
            addGuiPart(inputPath.setInput("纹理路径"));
            addGuiPart(remove);
        }

        public void remove()
        {
            parent.remove(this);
        }

        public String getMaterial()
        {
            return inputName.getInput()+":"+inputPath.getInput();
        }
    }

    protected class GInput extends GermGuiInput
    {
        public GInput(String indexName)
        {
            super(indexName);
            setBackground(false);
            setSync(true);
            setAutoClear(false);
            setLimit(200);
        }

        @Override
        public void onCallback(Player player, Enum<?> anEnum) {
            super.onCallback(player, anEnum);

            onInput(getInput());
            save.setEnable(true);

            if(anEnum==EventType.FOCUS){
                focus=this;
            }else  if(anEnum==EventType.LOSE_FOCUS&&equals(focus)){
                focus=null;
            }
        }

        public void updateToState()
        {
            onInput(getInput());
        }
        public void onInput(String v)
        {
            update();
        }
        protected GInput setLocation(int x, int y){UtilGerm2K.setLocation(this,x,y);return this;}
        protected GInput setSize(int w,int h){UtilGerm2K.setSize(this,w,h);return this;}
    }

    protected class GInputDouble extends GInput
    {
        public GInputDouble(String indexName) {
            super(indexName);
        }
    }

    protected final Player iplayer;
    protected final HEState state;
    protected final GermGuiSlot slot = new GermGuiSlot("slot").setEmptyPath("aestus/he/edit/air.png").setFillPath("aestus/he/edit/air.png");
    protected final GermGuiLabel name = UtilGerm2K.createLabel("name","",690,28,1.75F);
    protected final GermGuiLabel name_space = UtilGerm2K.createLabel("name_space","",690,10,1F);
    protected final GermGuiButton save = UtilGerm2K.createButton("save","aestus/he/edit/save.png",45,1330).setEnable(false).registerCallbackHandler((p,b)->{callbackSave();}, GermGuiButton.EventType.LEFT_CLICK);
    protected final GMaterials materials = new GMaterials();
    protected boolean hand;
    protected GInput focus;

    public GHEState(Player iplayer,HEState state,boolean hand)
    {
        super("hestate");
        this.hand=hand;
        this.iplayer=iplayer;
        this.state=state;
        init();
        registerKeyHandler();
    }
    protected void registerKeyHandler()
    {
        registerKeyDownHandler(KeyType.KEY_UP,(p,g)->{
            if(focus==null)return;

            if(focus instanceof GInputDouble){
                double v = Double.parseDouble(focus.getInput());
                focus.setInput((v+1D)+"");
                focus.onInput((v+1D)+"");
            }

        });
        registerKeyDownHandler(KeyType.KEY_DOWN,(p,g)->{
            if(focus==null)return;

            if(focus instanceof GInputDouble){
                double v = Double.parseDouble(focus.getInput());
                focus.setInput((v-1D)+"");
                focus.onInput((v-1D)+"");
            }

        });
        registerKeyDownHandler(KeyType.KEY_DELETE,(p,g)->{
            if(focus==null)return;

            if(focus instanceof GInputDouble){
                focus.setInput((0D)+"");
                focus.onInput((0D)+"");
            }else{
                focus.setInput("");
                focus.onInput("");
            }

        });
    }

    protected void init()
    {

        GermAnimationFade fade1 = new GermAnimationFade("fade").setFrom(0).setTo(1).setCycle(1).setDuration(300);

        addGuiPart(UtilGerm2K.createTexture("main","aestus/he/edit/main.png"));
        addGuiPart(UtilGerm2K.createTexture("title","aestus/he/edit/title.png",10,10));
        addGuiPart(UtilGerm2K.createTexture("toptions","aestus/he/edit/options.png",10,100));
        addGuiPart(UtilGerm2K.setLocation(materials,10,730));

        for(int i = 0;i<state.material.size();i++)
        {
            int finalI = i;
            Bukkit.getScheduler().runTaskLater(GermHETools.getInstance(),()->materials.addMaterialBar(state.material.get(finalI)),i/10);
        }

        addGuiPart(UtilGerm2K.createTexture("showcase","aestus/he/edit/showcase.png",610,10));
        addGuiPart(name);
        addGuiPart(name_space);

        UtilGerm2K.setLocation(slot,645,80);
        UtilGerm2K.setSize(slot,430,430);
        slot.setSize("430/2560*w");
        addGuiPart(slot);

        //base
        {

            addGuiPart(new GInput("model_path") {
                @Override public void onInput(String v) {
                    super.onInput(v);
                    state.model=v;
                }
            }.setLocation(22,187).setSize(556,26)
                    .setInput(state.model));
            addGuiPart(new GInput("texture_path") {
                @Override public void onInput(String v) {
                    super.onInput(v);
                    state.texture=v;
                }
            }.setLocation(22,252).setSize(556,26)
                    .setInput(state.texture));
            addGuiPart(new GInput("glow_path") {
                @Override public void onInput(String v) {
                    super.onInput(v);
                    state.glowTexture=v;
                }
            }.setLocation(22,317).setSize(556,26)
                    .setInput(state.glowTexture));

            addGuiPart(new GInputDouble("size") {
                @Override public void onInput(String v) {
                    super.onInput(v);
                    state.size=Double.parseDouble(v);
                }
            }.setLocation(172,352).setSize(124,26)
                    .setInput(""+state.size));

            addGuiPart(new GInput("follow_style") {
                @Override public void onInput(String v) {
                    super.onInput(v);
                    state.followStyle=v;
                }
            }.setLocation(454,352).setSize(556,26)
                    .setInput(state.followStyle));
            addGuiPart(UtilGerm2K.createButton("follow_style_child","aestus/he/edit/child.png",559,354).registerCallbackHandler((p,b)->{
                GermGuiCanvas c = new GermGuiCanvas("c");
                addGuiPart(c);
                UtilGerm2K.setLocation(c,454,378);
                c.addGuiPart(UtilGerm2K.createButton("close","aestus/he/edit/air.png",-50000,-50000,50000,50000).registerCallbackHandler((p2,b2)->{
                    GHEState.this.removeGuiPart(c);
                }, GermGuiButton.EventType.LEFT_CLICK));

                int index = 0;

                GermGuiTexture list = UtilGerm2K.createTexture("list","aestus/he/edit/child_list",0,0);

                c.addGuiPart(list);
                c.addGuiPart(UtilGerm2K.createButton("button_static","aestus/he/edit/child_button.png",4,2+index*28,120,26).registerCallbackHandler((p2,b2)->{
                    GHEState.this.removeGuiPart(c);
                    state.followStyle="static";
                    ((GInput)getGuiPart("follow_style")).setInput("static");
                }, GermGuiButton.EventType.LEFT_CLICK));
                c.addGuiPart(UtilGerm2K.createLabel("label_static","static",64,4+index*28,1F, GermGuiLabel.Align.CENTER));
                index++;

                c.addGuiPart(UtilGerm2K.createButton("button_4","aestus/he/edit/child_button.png",4,2+index*28,120,26).registerCallbackHandler((p2,b2)->{
                    GHEState.this.removeGuiPart(c);
                    state.followStyle="4f";
                    ((GInput)getGuiPart("follow_style")).setInput("4f");
                }, GermGuiButton.EventType.LEFT_CLICK));
                c.addGuiPart(UtilGerm2K.createLabel("label_4","4f",64,4+index*28,1F, GermGuiLabel.Align.CENTER));
                index++;

                c.addGuiPart(UtilGerm2K.createButton("button_8","aestus/he/edit/child_button.png",4,2+index*28,120,26).registerCallbackHandler((p2,b2)->{
                    GHEState.this.removeGuiPart(c);
                    state.followStyle="8f";
                    ((GInput)getGuiPart("follow_style")).setInput("8f");
                }, GermGuiButton.EventType.LEFT_CLICK));
                c.addGuiPart(UtilGerm2K.createLabel("label_8","8f",64,4+index*28,1F, GermGuiLabel.Align.CENTER));
                index++;

                c.addGuiPart(UtilGerm2K.createButton("button_16","aestus/he/edit/child_button.png",4,2+index*28,120,26).registerCallbackHandler((p2,b2)->{
                    GHEState.this.removeGuiPart(c);
                    state.followStyle="16f";
                    ((GInput)getGuiPart("follow_style")).setInput("16f");
                }, GermGuiButton.EventType.LEFT_CLICK));
                c.addGuiPart(UtilGerm2K.createLabel("label_16","16f",64,4+index*28,1F, GermGuiLabel.Align.CENTER));
                index++;

                UtilGerm2K.setSize(list,128,4+index*28);
            }, GermGuiButton.EventType.LEFT_CLICK));

            addGuiPart(UtilGerm2K.createButton("passable",state.passable?"aestus/he/edit/true.png":"aestus/he/edit/false.png",275,392,16,16).registerCallbackHandler((p,b)->{
                state.passable=!state.passable;
                b.setDefaultPath(state.passable?"aestus/he/edit/true.png":"aestus/he/edit/false.png");
            }, GermGuiButton.EventType.LEFT_CLICK));
        }

        //bb
        {
            addGuiPart(new GInputDouble("bb_minx") {
                @Override public void onInput(String v) {
                    super.onInput(v);
                    state.aabbMin.setX(Float.parseFloat(v));
                }
            }.setLocation(50,497).setSize(60,26)
                    .setInput(""+state.aabbMin.getX()));
            addGuiPart(new GInputDouble("bb_miny") {
                @Override public void onInput(String v) {
                    super.onInput(v);
                    state.aabbMin.setY(Float.parseFloat(v));
                }
            }.setLocation(237,497).setSize(60,26)
                    .setInput(""+state.aabbMin.getY()));
            addGuiPart(new GInputDouble("bb_minz") {
                @Override public void onInput(String v) {
                    super.onInput(v);
                    state.aabbMin.setZ(Float.parseFloat(v));
                }
            }.setLocation(424,497).setSize(60,26)
                    .setInput(""+state.aabbMin.getZ()));

            addGuiPart(new GInputDouble("bb_maxx") {
                @Override public void onInput(String v) {
                    super.onInput(v);
                    state.aabbMax.setX(Float.parseFloat(v));
                }
            }.setLocation(140,497).setSize(60,26)
                    .setInput(""+state.aabbMax.getX()));
            addGuiPart(new GInputDouble("bb_maxy") {
                @Override public void onInput(String v) {
                    super.onInput(v);
                    state.aabbMax.setY(Float.parseFloat(v));
                }
            }.setLocation(327,497).setSize(60,26)
                    .setInput(""+state.aabbMax.getY()));
            addGuiPart(new GInputDouble("bb_maxz") {
                @Override public void onInput(String v) {
                    super.onInput(v);
                    state.aabbMax.setZ(Float.parseFloat(v));
                }
            }.setLocation(514,497).setSize(60,26)
                    .setInput(""+state.aabbMax.getZ()));
        }

        //offset
        {
            addGuiPart(new GInputDouble("offset_x") {
                @Override public void onInput(String v) {
                    super.onInput(v);
                    state.transform.setX(Float.parseFloat(v));
                }
            }.setLocation(104,607).setSize(68,26)
                    .setInput(""+state.transform.getX()));
            addGuiPart(new GInputDouble("offset_y") {
                @Override public void onInput(String v) {
                    super.onInput(v);
                    state.transform.setY(Float.parseFloat(v));
                }
            }.setLocation(226,607).setSize(68,26)
                    .setInput(""+state.transform.getY()));
            addGuiPart(new GInputDouble("offset_z") {
                @Override public void onInput(String v) {
                    super.onInput(v);
                    state.transform.setZ(Float.parseFloat(v));
                }
            }.setLocation(348,607).setSize(68,26)
                    .setInput(""+state.transform.getZ()));

            addGuiPart(new GInputDouble("rotate_pitch") {
                @Override public void onInput(String v) {
                    super.onInput(v);
                    state.rotate.setX(Float.parseFloat(v));
                }
            }.setLocation(104,642).setSize(68,26)
                    .setInput(""+state.rotate.getX()));
            addGuiPart(new GInputDouble("rotate_yaw") {
                @Override public void onInput(String v) {
                    super.onInput(v);
                    state.rotate.setY(Float.parseFloat(v));
                }
            }.setLocation(226,642).setSize(68,26)
                    .setInput(""+state.rotate.getY()));
            addGuiPart(new GInputDouble("rotate_roll") {
                @Override public void onInput(String v) {
                    super.onInput(v);
                    state.rotate.setZ(Float.parseFloat(v));
                }
            }.setLocation(348,642).setSize(68,26)
                    .setInput(""+state.rotate.getZ()));

            addGuiPart(new GInputDouble("scale_length") {
                @Override public void onInput(String v) {
                    super.onInput(v);
                    state.scale.setX(Float.parseFloat(v));
                }
            }.setLocation(104,677).setSize(68,26)
                    .setInput(""+state.scale.getX()));
            addGuiPart(new GInputDouble("scale_width") {
                @Override public void onInput(String v) {
                    super.onInput(v);
                    state.scale.setY(Float.parseFloat(v));
                }
            }.setLocation(226,677).setSize(68,26)
                    .setInput(""+state.scale.getY()));
            addGuiPart(new GInputDouble("scale_height") {
                @Override public void onInput(String v) {
                    super.onInput(v);
                    state.scale.setZ(Float.parseFloat(v));
                }
            }.setLocation(348,677).setSize(68,26)
                    .setInput(""+state.scale.getZ()));
        }

        //clear
        {
            addGuiPart(UtilGerm2K.createButton("reset","aestus/he/edit/clear.png",520,20,60,60)
                    .registerCallbackHandler((p,b)->{

                        HEState s = new HEState().setLocation(state.location);
                        HEGuiManager.createHEStateGui(p,s,hand).openGuiTo(p);

                    }, GermGuiButton.EventType.LEFT_CLICK));
            addGuiPart(UtilGerm2K.createButton("remove_glow","aestus/he/edit/remove.png",552,317,26,26)
                    .registerCallbackHandler((p,b)->{

                        ((GInput)getGuiPart("glow_path")).setInput("");
                        state.glowTexture="";

                    }, GermGuiButton.EventType.LEFT_CLICK));
            addGuiPart(UtilGerm2K.createButton("clear_base","aestus/he/edit/clear.png",175,110,40,40)
                    .registerCallbackHandler((p,b)->{

                        state.model="";
                        state.texture="";
                        state.glowTexture="";
                        state.passable=false;
                        state.followStyle="static";

                        ((GInput)getGuiPart("model_path")).setInput("");
                        ((GInput)getGuiPart("texture_path")).setInput("");
                        ((GInput)getGuiPart("glow_path")).setInput("");
                        ((GInput)getGuiPart("follow_style")).setInput("static");
                        b.setDefaultPath(state.passable?"aestus/he/edit/true.png":"aestus/he/edit/false.png");

                    }, GermGuiButton.EventType.LEFT_CLICK));
            addGuiPart(UtilGerm2K.createButton("clear_bb","aestus/he/edit/clear.png",175,450,40,40)
                    .registerCallbackHandler((p,b)->{

                        state.aabbMax.setX(1).setY(1).setZ(1);
                        state.aabbMin.setX(0).setY(0).setZ(0);

                        ((GInput)getGuiPart("bb_minx")).setInput("0.0");
                        ((GInput)getGuiPart("bb_maxx")).setInput("1.0");
                        ((GInput)getGuiPart("bb_miny")).setInput("0.0");
                        ((GInput)getGuiPart("bb_maxy")).setInput("1.0");
                        ((GInput)getGuiPart("bb_minz")).setInput("0.0");
                        ((GInput)getGuiPart("bb_maxz")).setInput("1.0");

                    }, GermGuiButton.EventType.LEFT_CLICK));
            addGuiPart(UtilGerm2K.createButton("clear_offset","aestus/he/edit/clear.png",175,560,40,40)
                    .registerCallbackHandler((p,b)->{

                        state.transform.setX(0).setY(0).setZ(0);
                        state.rotate.setX(0).setY(0).setZ(0);
                        state.scale.setX(1).setY(1).setZ(1);

                        ((GInput)getGuiPart("offset_x")).setInput("0.0");
                        ((GInput)getGuiPart("offset_y")).setInput("0.0");
                        ((GInput)getGuiPart("offset_z")).setInput("0.0");

                        ((GInput)getGuiPart("rotate_pitch")).setInput("0.0");
                        ((GInput)getGuiPart("rotate_yaw")).setInput("0.0");
                        ((GInput)getGuiPart("rotate_roll")).setInput("0.0");

                        ((GInput)getGuiPart("scale_length")).setInput("1.0");
                        ((GInput)getGuiPart("scale_width")).setInput("1.0");
                        ((GInput)getGuiPart("scale_height")).setInput("1.0");

                    }, GermGuiButton.EventType.LEFT_CLICK));
        }

        //options
        {

            addGuiPart(UtilGerm2K.createButton("createitem","aestus/he/edit/createitem.png",850,535)
                    .registerCallbackHandler((p,b)->{
                        iplayer.getInventory().addItem(state.createItemStack());
                        iplayer.sendMessage("已添加至物品栏");
                    }, GermGuiButton.EventType.LEFT_CLICK));
            addGuiPart(UtilGerm2K.createButton("savepreset","aestus/he/edit/savepreset.png",685,535)
                    .registerCallbackHandler((p,b)->{
                    }, GermGuiButton.EventType.LEFT_CLICK));
        }


        for(GermGuiPart<? extends DynamicBase> p : getGuiParts())
            if(p instanceof IAnimatable)
                ((IAnimatable<? extends DynamicBase>)p).setAnimation(fade1);

        update();
        addGuiPart(save.setEnable(false));
    }
    protected void update()
    {
        this.name.setText(BlockManager.getOBJName(state));
        this.name_space.setText(BlockManager.getNameSpace(state));
        this.slot.setItemStack(state.createItemStack());
    }
    protected void callbackSave()
    {
        save.setEnable(false);

        for(GermGuiPart<? extends DynamicBase> part :getGuiParts())
            if(part instanceof GInput)
                ((GInput) part).updateToState();

        materials.updateToState();

        if(hand){
            int a = iplayer.getInventory().getItemInMainHand().getAmount();
            iplayer.getInventory().setItemInMainHand(state.createItemStack());
            iplayer.getInventory().getItemInMainHand().setAmount(a);
        }else {
            Location playerLocation = iplayer.getLocation();

            Chunk chunk = state.location.getChunk();
            boolean r = state.place();
            iplayer.teleport(new Location(iplayer.getWorld(),1000D,1000D,1000D));

            chunk.unload();
            chunk.load();
            Bukkit.getScheduler().runTaskLater(GermHETools.getInstance(),()->iplayer.teleport(playerLocation),4);
            iplayer.sendMessage(r?"已保存":"保存失败");
        }

    }
}
