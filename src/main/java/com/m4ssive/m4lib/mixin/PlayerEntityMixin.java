package com.m4ssive.m4lib.mixin;

import com.m4ssive.m4lib.M4Lib;
import com.m4ssive.m4lib.NametagRenderer;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * PlayerEntityMixin
 * 
 * Uku'nun yaklaşımını kullanarak PlayerEntity.getDisplayName() metodunu modify ediyor.
 * Bu, nametag'lerde, tab list'te ve diğer yerlerde otomatik olarak çalışır.
 */
@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    
    @ModifyReturnValue(method = "getDisplayName", at = @At("RETURN"))
    private Text modifyDisplayName(Text original) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        
        try {
            M4Lib m4Lib = M4Lib.getInstance();
            if (m4Lib == null) {
                return original;
            }
            
            NametagRenderer nametagRenderer = m4Lib.getNametagRenderer();
            if (nametagRenderer == null) {
                return original;
            }
            
            // Suffix'i al
            Text suffixText = nametagRenderer.getSuffixText(self);
            if (suffixText == null || suffixText.getString().isEmpty()) {
                return original;
            }
            
            // Text'i modify et ve suffix ekle
            MutableText modifiedText = original.copy().append(suffixText);
            
            M4Lib.LOGGER.debug("[PlayerEntityMixin] ✓ Modified displayName for {}: '{}' -> '{}'", 
                self.getNameForScoreboard(), original.getString(), modifiedText.getString());
            
            return modifiedText;
        } catch (Exception e) {
            M4Lib.LOGGER.error("[PlayerEntityMixin] ✗ Error modifying displayName for player {}", 
                self != null ? self.getNameForScoreboard() : "null", e);
            return original;
        }
    }
}

