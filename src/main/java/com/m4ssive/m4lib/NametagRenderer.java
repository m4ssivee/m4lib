package com.m4ssive.m4lib;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class NametagRenderer {
    private final Map<UUID, Function<PlayerEntity, Text>> nametagSuffixProviders;
    private final Map<String, Function<PlayerEntity, Text>> nametagSuffixProvidersByMod;
    private final Map<UUID, Function<PlayerEntity, ItemStack>> nametagItemProviders;
    private final Map<String, Function<PlayerEntity, ItemStack>> nametagItemProvidersByMod;
    
    public NametagRenderer() {
        this.nametagSuffixProviders = new HashMap<>();
        this.nametagSuffixProvidersByMod = new HashMap<>();
        this.nametagItemProviders = new HashMap<>();
        this.nametagItemProvidersByMod = new HashMap<>();
    }
    
    public void registerPlayerSuffix(UUID playerUuid, Function<PlayerEntity, Text> suffixProvider) {
        nametagSuffixProviders.put(playerUuid, suffixProvider);
    }
    
    public void registerModSuffix(String modId, Function<PlayerEntity, Text> suffixProvider) {
        nametagSuffixProvidersByMod.put(modId, suffixProvider);
        M4Lib.LOGGER.info("[NametagRenderer] Registered mod suffix provider for mod: {}", modId);
    }
    
    public void registerPlayerItem(UUID playerUuid, Function<PlayerEntity, ItemStack> itemProvider) {
        nametagItemProviders.put(playerUuid, itemProvider);
    }
    
    public void registerModItem(String modId, Function<PlayerEntity, ItemStack> itemProvider) {
        nametagItemProvidersByMod.put(modId, itemProvider);
    }
    
    public void unregisterPlayerSuffix(UUID playerUuid) {
        nametagSuffixProviders.remove(playerUuid);
    }
    
    public void unregisterModSuffix(String modId) {
        nametagSuffixProvidersByMod.remove(modId);
    }
    
    public void unregisterPlayerItem(UUID playerUuid) {
        nametagItemProviders.remove(playerUuid);
    }
    
    public void unregisterModItem(String modId) {
        nametagItemProvidersByMod.remove(modId);
    }
    
    public Text getSuffixText(PlayerEntity player) {
        if (player == null) {
            return null;
        }
        
        // Uku'nun mantığı: İlk bulunan suffix'i döndür (sadece bir mod için)
        for (Map.Entry<String, Function<PlayerEntity, Text>> entry : nametagSuffixProvidersByMod.entrySet()) {
            try {
                Text suffix = entry.getValue().apply(player);
                if (suffix != null && !suffix.getString().isEmpty()) {
                    M4Lib.LOGGER.debug("[NametagRenderer] Found suffix for player {} from mod {}: '{}'", 
                        player.getNameForScoreboard(), entry.getKey(), suffix.getString());
                    return suffix;
                }
            } catch (Exception e) {
                M4Lib.LOGGER.error("[NametagRenderer] Error getting mod-specific nametag suffix for mod: {}", entry.getKey(), e);
            }
        }
        
        // Player-specific suffix (öncelikli)
        UUID playerUuid = player.getUuid();
        Function<PlayerEntity, Text> playerSuffixProvider = nametagSuffixProviders.get(playerUuid);
        if (playerSuffixProvider != null) {
            try {
                Text suffix = playerSuffixProvider.apply(player);
                if (suffix != null && !suffix.getString().isEmpty()) {
                    M4Lib.LOGGER.debug("[NametagRenderer] Found player-specific suffix for {}: '{}'", 
                        player.getNameForScoreboard(), suffix.getString());
                    return suffix;
                }
            } catch (Exception e) {
                M4Lib.LOGGER.error("[NametagRenderer] Error getting player-specific nametag suffix", e);
            }
        }
        
        return null;
    }
    
    public ItemStack getItemStack(PlayerEntity player) {
        if (player == null) {
            return null;
        }
        
        UUID playerUuid = player.getUuid();
        
        // Önce player-specific item'ı kontrol et
        Function<PlayerEntity, ItemStack> playerItemProvider = nametagItemProviders.get(playerUuid);
        if (playerItemProvider != null) {
            try {
                ItemStack item = playerItemProvider.apply(player);
                if (item != null && !item.isEmpty()) {
                    return item;
                }
            } catch (Exception e) {
                M4Lib.LOGGER.error("Error getting player-specific nametag item", e);
            }
        }
        
        // Sonra mod-specific item'ları kontrol et (ilk bulunanı döndür)
        for (Map.Entry<String, Function<PlayerEntity, ItemStack>> entry : nametagItemProvidersByMod.entrySet()) {
            try {
                ItemStack item = entry.getValue().apply(player);
                if (item != null && !item.isEmpty()) {
                    return item;
                }
            } catch (Exception e) {
                M4Lib.LOGGER.error("Error getting mod-specific nametag item for mod: {}", entry.getKey(), e);
            }
        }
        
        return null;
    }
    
    public void clearAll() {
        nametagSuffixProviders.clear();
        nametagSuffixProvidersByMod.clear();
        nametagItemProviders.clear();
        nametagItemProvidersByMod.clear();
    }
}

