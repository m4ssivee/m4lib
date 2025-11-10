package com.m4ssive.m4lib;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class M4Lib implements ClientModInitializer {
    public static final String MOD_ID = "m4lib";
    public static final String MOD_NAME = "m4lib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    
    private static M4Lib instance;
    private NametagRenderer nametagRenderer;
    private com.m4ssive.m4lib.util.PlayerTracker playerTracker;
    
    @Override
    public void onInitializeClient() {
        instance = this;
        LOGGER.info("m4lib initialized - Library mod for m4ssive's mods");
        
        nametagRenderer = new NametagRenderer();
        LOGGER.info("NametagRenderer created and ready");
        
        playerTracker = new com.m4ssive.m4lib.util.PlayerTracker(net.minecraft.client.MinecraftClient.getInstance());
        LOGGER.info("PlayerTracker created and ready");
        
        LOGGER.info("m4lib is ready to receive registrations from other mods");
        LOGGER.info("Available utilities: NametagRenderer, PlayerTracker, PotionHelper, ItemEntityTracker, ColorHelper");
    }
    
    public static M4Lib getInstance() {
        return instance;
    }
    
    public NametagRenderer getNametagRenderer() {
        return nametagRenderer;
    }
    
    public com.m4ssive.m4lib.util.PlayerTracker getPlayerTracker() {
        return playerTracker;
    }
}



