package me.kteq.hiddenarmor.listener;

import me.kteq.hiddenarmor.HiddenArmor;
import me.kteq.hiddenarmor.manager.PlayerManager;
import me.kteq.hiddenarmor.util.EventUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class GameModeListener implements Listener {
    HiddenArmor plugin;
    PlayerManager hiddenArmorManager;

    public GameModeListener(HiddenArmor plugin){
        EventUtil.register(this, plugin);

        this.plugin = plugin;
        this.hiddenArmorManager = plugin.getPlayerManager();
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event){
        Player player = event.getPlayer();
        if(!hiddenArmorManager.isEnabled(player)) return;

        if(event.getNewGameMode().equals(GameMode.CREATIVE)) {
            hiddenArmorManager.disablePlayer(player, false);
            plugin.getArmorUpdater().updatePlayer(player);
        }

        player.getScheduler().runDelayed(plugin, task -> {
            if (event.getNewGameMode().equals(GameMode.CREATIVE)) {
                hiddenArmorManager.enablePlayer(player, false);
            } else {
                plugin.getArmorUpdater().updatePlayer(player);
            }
        }, null, 1L);
    }


}
