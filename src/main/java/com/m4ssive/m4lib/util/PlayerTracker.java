package com.m4ssive.m4lib.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

import java.util.*;
import java.util.function.Predicate;

/**
 * Utility class for tracking and managing player data
 */
public class PlayerTracker {
    private final Map<UUID, PlayerData> playerData = new HashMap<>();
    private final MinecraftClient client;
    
    public static class PlayerData {
        private final UUID uuid;
        private String cachedName;
        private long lastSeen;
        private final Map<String, Object> customData = new HashMap<>();
        
        public PlayerData(UUID uuid) {
            this.uuid = uuid;
            this.lastSeen = System.currentTimeMillis();
        }
        
        public UUID getUuid() {
            return uuid;
        }
        
        public String getCachedName() {
            return cachedName;
        }
        
        public void setCachedName(String name) {
            this.cachedName = name;
        }
        
        public long getLastSeen() {
            return lastSeen;
        }
        
        public void updateLastSeen() {
            this.lastSeen = System.currentTimeMillis();
        }
        
        public void setCustomData(String key, Object value) {
            customData.put(key, value);
        }
        
        @SuppressWarnings("unchecked")
        public <T> T getCustomData(String key, Class<T> type) {
            Object value = customData.get(key);
            if (value != null && type.isInstance(value)) {
                return (T) value;
            }
            return null;
        }
        
        public void removeCustomData(String key) {
            customData.remove(key);
        }
    }
    
    public PlayerTracker(MinecraftClient client) {
        this.client = client;
    }
    
    /**
     * Gets or creates player data for a UUID
     */
    public PlayerData getOrCreatePlayerData(UUID uuid) {
        return playerData.computeIfAbsent(uuid, PlayerData::new);
    }
    
    /**
     * Gets player data for a UUID
     */
    public PlayerData getPlayerData(UUID uuid) {
        return playerData.get(uuid);
    }
    
    /**
     * Gets the player entity from the world
     */
    public PlayerEntity getPlayerEntity(UUID uuid) {
        if (client.world == null) {
            return null;
        }
        return client.world.getPlayerByUuid(uuid);
    }
    
    /**
     * Gets all tracked player UUIDs
     */
    public Set<UUID> getTrackedPlayers() {
        return new HashSet<>(playerData.keySet());
    }
    
    /**
     * Gets all player data entries
     */
    public Map<UUID, PlayerData> getAllPlayerData() {
        return new HashMap<>(playerData);
    }
    
    /**
     * Gets players matching a filter
     */
    public List<PlayerEntity> getPlayersMatching(Predicate<PlayerEntity> filter) {
        List<PlayerEntity> players = new ArrayList<>();
        if (client.world == null) {
            return players;
        }
        
        for (UUID uuid : playerData.keySet()) {
            PlayerEntity player = client.world.getPlayerByUuid(uuid);
            if (player != null && filter.test(player)) {
                players.add(player);
            }
        }
        
        return players;
    }
    
    /**
     * Updates cached player names from the world
     */
    public void updatePlayerNames() {
        if (client.world == null) {
            return;
        }
        
        for (Map.Entry<UUID, PlayerData> entry : playerData.entrySet()) {
            PlayerEntity player = client.world.getPlayerByUuid(entry.getKey());
            if (player != null) {
                entry.getValue().setCachedName(player.getName().getString());
                entry.getValue().updateLastSeen();
            }
        }
    }
    
    /**
     * Removes player data for a UUID
     */
    public void removePlayer(UUID uuid) {
        playerData.remove(uuid);
    }
    
    /**
     * Clears all player data
     */
    public void clearAll() {
        playerData.clear();
    }
    
    /**
     * Cleans up players that haven't been seen in a while
     */
    public void cleanupOldPlayers(long maxAgeMs) {
        long currentTime = System.currentTimeMillis();
        playerData.entrySet().removeIf(entry -> {
            long age = currentTime - entry.getValue().getLastSeen();
            return age > maxAgeMs;
        });
    }
}

