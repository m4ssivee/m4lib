package com.m4ssive.m4lib.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.m4ssive.m4lib.M4Lib;
import com.m4ssive.m4lib.NametagRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.DisplayEntityRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DisplayEntityRenderer.TextDisplayEntityRenderer.class)
public class TextDisplayEntityRendererMixin {
    private static boolean mixinInitialized = false;
    
    @WrapOperation(
        method = "render(Lnet/minecraft/client/render/entity/state/TextDisplayEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;IF)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/decoration/DisplayEntity$TextDisplayEntity$TextLine;contents()Lnet/minecraft/text/OrderedText;")
    )
    public OrderedText label(DisplayEntity.TextDisplayEntity.TextLine instance, Operation<OrderedText> original) {
        if (!mixinInitialized) {
            M4Lib.LOGGER.info("[TextDisplayEntityRendererMixin] Mixin successfully injected and active!");
            mixinInitialized = true;
        }
        
        final Text text = getStyledText(instance.contents());
        final String stringText = text.getString();
        final ClientWorld world = MinecraftClient.getInstance().world;

        if (!stringText.isBlank() && world != null) {
            // Uku'nun mantığı: Text'te oyuncu isimlerini bul ve suffix ekle
            // TODO: maybe implement some sort of client-side cache for faster lookups? currently this being computed every frame lol
            for (PlayerEntity player : world.getPlayers()) {
                String playerName = player.getNameForScoreboard();
                int index = stringText.indexOf(playerName);
                if (!isSurrounded(stringText, index, playerName.length())) {
                    try {
                        M4Lib m4Lib = M4Lib.getInstance();
                        if (m4Lib != null) {
                            NametagRenderer nametagRenderer = m4Lib.getNametagRenderer();
                            if (nametagRenderer != null) {
                                Text suffixText = nametagRenderer.getSuffixText(player);
                                if (suffixText != null && !suffixText.getString().isEmpty()) {
                                    M4Lib.LOGGER.debug("[TextDisplayEntityRendererMixin] Found player '{}' in text '{}', adding suffix: '{}'", 
                                        playerName, stringText, suffixText.getString());
                                    MutableText result = text.copy().append(suffixText);
                                    return result.asOrderedText();
                                }
                            }
                        }
                    } catch (Exception e) {
                        M4Lib.LOGGER.error("[TextDisplayEntityRendererMixin] Error adding suffix for player {}", playerName, e);
                    }
                }
            }
        }

        return instance.contents();
    }

    // 2024 edit: i have no fucking clue what this does but sure uku3lig from the past, slay queen
    @Unique
    private boolean isSurrounded(String stringText, int index, int length) {
        return index == -1 || // not found
                (index > 0 && Character.isLetterOrDigit(stringText.charAt(index - 1))) || // first char is alphanumeric
                (index + length < stringText.length() && Character.isLetterOrDigit(stringText.charAt(index + length)));
    }

    @Unique
    private MutableText getStyledText(OrderedText text) {
        MutableText builder = Text.empty();
        text.accept((index, style, codePoint) -> {
            builder.append(Text.literal(Character.toString(codePoint)).setStyle(style));
            return true;
        });
        return builder;
    }
}

