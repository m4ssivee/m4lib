package com.m4ssive.m4lib.util;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Generic ItemEntity tracking utility for mods
 */
public class ItemEntityTracker {
    private final Map<UUID, Long> lastItemTime = new HashMap<>();
    private final long duplicateCooldown;
    private final ItemFilter filter;
    private final Consumer<ItemEntity> onItemDetected;
    
    @FunctionalInterface
    public interface ItemFilter {
        boolean shouldTrack(ItemEntity itemEntity);
    }
    
    /**
     * Creates a new ItemEntityTracker
     * @param duplicateCooldown Cooldown in milliseconds to prevent duplicate detections
     * @param filter Filter to determine which items to track
     * @param onItemDetected Callback when an item is detected
     */
    public ItemEntityTracker(long duplicateCooldown, ItemFilter filter, Consumer<ItemEntity> onItemDetected) {
        this.duplicateCooldown = duplicateCooldown;
        this.filter = filter;
        this.onItemDetected = onItemDetected;
    }
    
    /**
     * Processes an ItemEntity and triggers callback if it matches the filter
     */
    public void processItemEntity(ItemEntity itemEntity) {
        if (itemEntity == null || !filter.shouldTrack(itemEntity)) {
            return;
        }
        
        Entity owner = itemEntity.getOwner();
        if (owner == null) {
            return;
        }
        
        UUID throwerId = owner.getUuid();
        
        long currentTime = System.currentTimeMillis();
        Long lastTime = lastItemTime.get(throwerId);
        
        // Check cooldown to prevent duplicates
        if (lastTime != null && (currentTime - lastTime) < duplicateCooldown) {
            return;
        }
        
        lastItemTime.put(throwerId, currentTime);
        onItemDetected.accept(itemEntity);
    }
    
    /**
     * Clears tracking data for a specific player
     */
    public void clearPlayer(UUID playerId) {
        lastItemTime.remove(playerId);
    }
    
    /**
     * Clears all tracking data
     */
    public void clearAll() {
        lastItemTime.clear();
    }
    
    /**
     * Gets the last detection time for a player
     */
    public Long getLastDetectionTime(UUID playerId) {
        return lastItemTime.get(playerId);
    }
}

