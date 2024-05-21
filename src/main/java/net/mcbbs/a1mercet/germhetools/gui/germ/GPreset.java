package net.mcbbs.a1mercet.germhetools.gui.germ;

import com.germ.germplugin.api.dynamic.gui.*;
import net.mcbbs.a1mercet.germhetools.gui.HEGuiManager;
import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import net.mcbbs.a1mercet.germhetools.player.ges.PresetLibrary;
import net.mcbbs.a1mercet.germhetools.util.UtilGerm2K;

import java.util.UUID;

public class GPreset extends GermGuiCanvas
{
    public final PlayerState ps;
    public final HEState state;
    public final GermGuiTexture img     = UtilGerm2K.createTexture("img","",5,5,100,100).setEnable(false);
    public final GermGuiLabel name      = UtilGerm2K.createLabel("name","",55,87,2F, GermGuiLabel.Align.CENTER).setOmitWidth("180/2560*w");
    public final GermGuiButton button   = UtilGerm2K.createButton("button","aestus/he/library/preset.png",0,0)
            .registerCallbackHandler((p,b)->onLeftClick(),  GermGuiButton.EventType.LEFT_CLICK)
            .registerCallbackHandler((p,b)->onRightClick(), GermGuiButton.EventType.RIGHT_CLICK)
            ;
    public final GermGuiSlot slot = new GermGuiSlot(UUID.randomUUID().toString())
            .setWidth("70/2560*w").setHeight("70/1440*h")
            .setLocationX("20/2560*w").setLocationY("20/1440*h")
            .setSwallow(false).setIdentity(UUID.randomUUID().toString())
            .setFillPath("aestus/he/air.png").setEmptyPath("aestus/he/air.png")
            ;

    public GPreset(PlayerState ps , HEState state)
    {
        super(UUID.randomUUID().toString());

        this.ps=ps;
        this.state=state;
        init();
        update();
    }

    protected void init()
    {
        addGuiPart(button);
        addGuiPart(slot);
        addGuiPart(img);
        addGuiPart(name);
    }
    public void update()
    {
        slot.setItemStack(state.createItemStack());
        name.setText("#FF808080"+state.name);
        img.setEnable(state.data.containsKey("enable_img"));
        img.setPath("aestus/he/img/"+state.id+".png");
    }

    public void onLeftClick()
    {

    }
    public void onRightClick()
    {
        createActionPanel().openChildGui(ps.player);
    }

    public GActionPanel createActionPanel()
    {
        GActionPanel g = new GActionPanel(ps.player);

        PresetLibrary.PresetList list = ps.ges.library.getFrom(state.id);


        g.addAction(new GActionPanel.Action("id",state.id));
        g.addAction(new GActionPanel.Action("edit","编辑"){
            @Override public boolean callback(GActionPanel parent) {
                if(!super.callback(parent))return false;

                HEGuiManager.createHEStateGui(ps.player,state,false).openGuiTo(ps.player);
                parent.close();

                return true;
            }});
        g.addAction(new GActionPanel.Action("fave","收藏"){
            @Override public boolean callback(GActionPanel parent) {
                if(!super.callback(parent))return false;


                PresetLibrary.PresetList fav = ps.ges.library.getList("收藏夹");
                if(fav!=null){
                    HEState copy = new HEState(state);
                    copy.id=copy.id+"_fav";
                    fav.add(copy);
                }

                parent.close();

                return true;
            }});
        g.addAction(new GActionPanel.Action("create","创建物品"){
            @Override public boolean callback(GActionPanel parent) {
                if(!super.callback(parent))return false;

                parent.ps.player.getInventory().addItem(state.createItemStack());
                parent.close();

                return true;
            }});
        g.addAction(new GActionPanel.Action("enable_img",(state.data.containsKey("enable_img")?"关闭":"打开")+"自定义图片"){
            @Override public boolean callback(GActionPanel parent) {
                if(!super.callback(parent))return false;

                if(state.data.containsKey("enable_img"))state.data.remove("enable_img");
                else state.data.putDefault("enable_img",true);

                update();
                parent.close();

                return true;
            }});
        if(list!=null)
            g.addAction(new GActionPanel.Action("delete","#FFE96363从收藏夹中删除"){
                @Override public boolean callback(GActionPanel parent) {
                    if(!super.callback(parent))return false;

                    list.remove(state);

                    parent.close();

                    return true;
                }});
        g.selectUp();
        return g;
    }

}
