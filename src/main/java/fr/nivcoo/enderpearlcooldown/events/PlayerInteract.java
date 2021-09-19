package fr.nivcoo.enderpearlcooldown.events;

import fr.nivcoo.enderpearlcooldown.EnderPearlCooldown;
import fr.nivcoo.enderpearlcooldown.utils.Config;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class PlayerInteract implements Listener {

    private HashMap<String, Long> wait = new HashMap<>();
    private EnderPearlCooldown gc = EnderPearlCooldown.get();
    private Config config = gc.getConfiguration();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractEvent(PlayerInteractEvent e) {

        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        Action action = e.getAction();
        if (p.hasPermission("enderpearlcooldown.bypass")
                || (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK)
                || item.getType() != Material.ENDER_PEARL)
            return;
        String timeBeforeUseString = config.getString("time_before_use");
        int multipleAmount = 1;
        if (timeBeforeUseString.contains("m")) {
            multipleAmount = 60;
            timeBeforeUseString = timeBeforeUseString.substring(0, timeBeforeUseString.length() - 1);
        }
        int timeBeforeUse = Integer.parseInt(timeBeforeUseString);

        Long currentMillis = System.currentTimeMillis();
        String UUID = p.getUniqueId().toString();
        Long time = wait.get(UUID);

        if (time == null)
            wait.put(UUID, currentMillis);
        else {
            long timeInSecondBeforeUse = timeBeforeUse * multipleAmount;
            if ((currentMillis - time) / 1000 > timeInSecondBeforeUse) {
                wait.put(UUID, currentMillis);
                return;
            }
            sendCancelMessage(p, time, timeInSecondBeforeUse);
            e.setCancelled(true);
        }
    }

    private void sendCancelMessage(Player p, long timeBeforeUse, long timeInSecondBeforeUse) {
        Long currentMillis = System.currentTimeMillis();
        timeBeforeUse = ((timeBeforeUse + (timeInSecondBeforeUse * 1000)) - currentMillis) / 1000;

        int minutes = (int) (timeBeforeUse / 60);

        timeBeforeUse -= (minutes * 60);
        int seconds = (int) timeBeforeUse;

        String globalMessage = "";
        String type = config.getString("messages.minute");
        if (minutes > 0) {
            if (minutes > 1) {
                type = config.getString("messages.minutes");
            }
            globalMessage = minutes + " " + type + ", ";
        }
        type = config.getString("messages.second");
        if (seconds > 1) {
            type = config.getString("messages.seconds");
        }
        globalMessage += seconds + " " + type;

        p.sendMessage(config.getString("messages.prefix")
                + config.getString("messages.cancel_message").replace("{0}", globalMessage));
    }

}
