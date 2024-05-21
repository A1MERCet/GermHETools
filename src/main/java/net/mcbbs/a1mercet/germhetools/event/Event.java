package net.mcbbs.a1mercet.germhetools.event;

import net.mcbbs.a1mercet.germhetools.api.BlockManager;
import net.mcbbs.a1mercet.germhetools.gui.IGui;
import net.mcbbs.a1mercet.germhetools.he.HEState;
import net.mcbbs.a1mercet.germhetools.gui.HEGuiManager;
import net.mcbbs.a1mercet.germhetools.player.PlayerState;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

public class Event implements Listener
{
    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        Block block = e.getClickedBlock();

        if(p.isOp()&&p.getGameMode()==GameMode.CREATIVE&&e.getHand() == EquipmentSlot.HAND)
        {
            if(p.isSneaking() && p.getInventory().getItemInMainHand().getType().name().equals("HUEIHUEAENGINE_CUSTOMBLOCK") &&(e.getAction()==Action.RIGHT_CLICK_AIR||e.getAction()==Action.RIGHT_CLICK_BLOCK)){
                HEGuiManager.createHEStateGui(p,new HEState().parse(p.getInventory().getItemInMainHand()),true).openGuiTo(p);
                e.setCancelled(true);
            } else if(block!=null&&block.getType().name().equals("HUEIHUEAENGINE_CUSTOMBLOCK")&&e.getAction()==Action.RIGHT_CLICK_BLOCK) {
                IGui gui = HEGuiManager.createHEStateGui(p,new HEState().parse(block),false);
                if(gui!=null){
                    gui.openGuiTo(p);
                    e.setCancelled(true);
                }
            }
        }
    }

    public static void joinHandle(Player p)
    {
        PlayerState ps = PlayerState.create(p);
        try {
            ps.load();
        }catch (Exception e){e.printStackTrace();ps.player.kickPlayer("数据错误");}
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent evt)
    {
        joinHandle(evt.getPlayer());
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent evt)
    {
        Player p = evt.getPlayer();
        PlayerState ps = PlayerState.get(p);
        try {
            if(ps!=null){
                ps.save();
            }
        }catch (Exception e){e.printStackTrace();}finally {PlayerState.remove(p);}
    }
}
