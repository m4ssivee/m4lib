package com.m4ssive.m4lib.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.registry.entry.RegistryEntry;

/**
 * Utility class for potion-related operations
 */
public class PotionHelper {
    
    public enum PotionType {
        HEALING,
        SPEED,
        STRENGTH,
        WEAKNESS,
        POISON,
        REGENERATION,
        FIRE_RESISTANCE,
        WATER_BREATHING,
        INVISIBILITY,
        NIGHT_VISION,
        SLOW_FALLING,
        OTHER
    }
    
    /**
     * Checks if an ItemStack is a potion item (potion, splash potion, or lingering potion)
     */
    public static boolean isPotionItem(ItemStack stack) {
        return stack.isOf(Items.POTION) || 
               stack.isOf(Items.SPLASH_POTION) || 
               stack.isOf(Items.LINGERING_POTION);
    }
    
    /**
     * Gets the potion from an ItemStack (1.21+ compatible)
     */
    private static Potion getPotion(ItemStack stack) {
        if (!isPotionItem(stack)) {
            return null;
        }
        
        PotionContentsComponent potionContents = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (potionContents != null && potionContents.potion().isPresent()) {
            return potionContents.potion().get().value();
        }
        
        return null;
    }
    
    /**
     * Checks if an ItemStack has a valid potion effect (not a water bottle)
     */
    public static boolean hasPotionEffect(ItemStack stack) {
        if (!isPotionItem(stack)) {
            return false;
        }
        Potion potion = getPotion(stack);
        return potion != null && !potion.getEffects().isEmpty();
    }
    
    /**
     * Gets the potion type from an ItemStack
     */
    public static PotionType getPotionType(ItemStack stack) {
        if (!hasPotionEffect(stack)) {
            return PotionType.OTHER;
        }
        
        Potion potion = getPotion(stack);
        if (potion == null) {
            return PotionType.OTHER;
        }
        
        Identifier potionId = Registries.POTION.getId(potion);
        if (potionId == null) {
            return PotionType.OTHER;
        }
        
        return getPotionTypeFromId(potionId);
    }
    
    /**
     * Gets the potion type from a potion identifier
     */
    public static PotionType getPotionTypeFromId(Identifier potionId) {
        String potionName = potionId.getPath().toLowerCase();
        
        if (potionName.contains("healing") || potionName.contains("instant_health")) {
            return PotionType.HEALING;
        } else if (potionName.contains("swiftness") || potionName.contains("speed")) {
            return PotionType.SPEED;
        } else if (potionName.contains("strength")) {
            return PotionType.STRENGTH;
        } else if (potionName.contains("weakness")) {
            return PotionType.WEAKNESS;
        } else if (potionName.contains("poison")) {
            return PotionType.POISON;
        } else if (potionName.contains("regeneration") || potionName.contains("regen")) {
            return PotionType.REGENERATION;
        } else if (potionName.contains("fire_resistance") || potionName.contains("fire_resist")) {
            return PotionType.FIRE_RESISTANCE;
        } else if (potionName.contains("water_breathing") || potionName.contains("water_breath")) {
            return PotionType.WATER_BREATHING;
        } else if (potionName.contains("invisibility") || potionName.contains("invis")) {
            return PotionType.INVISIBILITY;
        } else if (potionName.contains("night_vision") || potionName.contains("night_vis")) {
            return PotionType.NIGHT_VISION;
        } else if (potionName.contains("slow_falling") || potionName.contains("slow_fall")) {
            return PotionType.SLOW_FALLING;
        } else {
            return PotionType.OTHER;
        }
    }
    
    /**
     * Gets a display name for a potion type
     */
    public static String getPotionTypeDisplayName(PotionType type) {
        switch (type) {
            case HEALING: return "Healing";
            case SPEED: return "Speed";
            case STRENGTH: return "Strength";
            case WEAKNESS: return "Weakness";
            case POISON: return "Poison";
            case REGENERATION: return "Regeneration";
            case FIRE_RESISTANCE: return "Fire Resistance";
            case WATER_BREATHING: return "Water Breathing";
            case INVISIBILITY: return "Invisibility";
            case NIGHT_VISION: return "Night Vision";
            case SLOW_FALLING: return "Slow Falling";
            default: return "Other";
        }
    }
    
    /**
     * Gets a short display name for a potion type (for HUD)
     */
    public static String getPotionTypeShortName(PotionType type) {
        switch (type) {
            case HEALING: return "Heal";
            case SPEED: return "Speed";
            case STRENGTH: return "Str";
            case WEAKNESS: return "Weak";
            case POISON: return "Pois";
            case REGENERATION: return "Regen";
            case FIRE_RESISTANCE: return "Fire";
            case WATER_BREATHING: return "Water";
            case INVISIBILITY: return "Invis";
            case NIGHT_VISION: return "NV";
            case SLOW_FALLING: return "Slow";
            default: return "Other";
        }
    }
}

