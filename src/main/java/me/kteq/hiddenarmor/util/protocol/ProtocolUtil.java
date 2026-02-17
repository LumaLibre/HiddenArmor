package me.kteq.hiddenarmor.util.protocol;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import me.kteq.hiddenarmor.HiddenArmor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ProtocolUtil {

    public static void broadcastPlayerPacket(ProtocolManager manager, PacketContainer packet, Player player) {
        World world = player.getWorld();
        Location loc = player.getLocation();
        int viewRadius = Bukkit.getViewDistance() * 16;
        for(Player p : Bukkit.getOnlinePlayers()){
            p.getScheduler().run(HiddenArmor.getInstance(), task -> {
                if(!(p.getWorld().equals(world) && p.getLocation().distance(loc) < viewRadius && !p.equals(player))) {
                    return;
                }
                manager.sendServerPacket(p, packet);
            }, null);
        }
    }

    public static boolean isArmorSlot(Pair<EnumWrappers.ItemSlot, ItemStack> pair) {
        return pair.getFirst().equals(EnumWrappers.ItemSlot.FEET) ||
                pair.getFirst().equals(EnumWrappers.ItemSlot.LEGS) ||
                pair.getFirst().equals(EnumWrappers.ItemSlot.CHEST) ||
                pair.getFirst().equals(EnumWrappers.ItemSlot.HEAD);
    }

    public enum ArmorType {
        HELMET(5), CHEST(6), LEGGS(7), BOOTS(8);

        private final int value;

        public static ArmorType getType(int value){
            for(int i = 0; i < values().length; i++){
                if(values()[i].getValue() == value) return values()[i];
            }
            return null;
        }

        public int getValue(){
            return value;
        }

        ArmorType(int i){
            this.value = i;
        }
    }

    public static ItemStack getArmor(ArmorType type, PlayerInventory inv) {
        switch (type) {
            case HELMET: if(inv.getHelmet()!=null) return inv.getHelmet().clone();
                break;
            case CHEST: if(inv.getChestplate()!=null) return inv.getChestplate().clone();
                break;
            case LEGGS: if(inv.getLeggings()!=null) return inv.getLeggings().clone();
                break;
            case BOOTS: if(inv.getBoots()!=null) return inv.getBoots().clone();
                break;
        }
        return new ItemStack(Material.AIR);
    }
}
