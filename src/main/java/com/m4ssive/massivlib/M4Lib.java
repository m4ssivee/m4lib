package com.m4ssive.m4lib;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class M4Lib implements ClientModInitializer {
    public static final String MOD_ID = "m4lib";
    public static final String MOD_NAME = "M4Lib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    
    private static M4Lib instance;
    private NametagRenderer nametagRenderer;
    
    @Override
    public void onInitializeClient() {
        instance = this;
        LOGGER.info("M4Lib initialized - Library mod for m4ssive's mods");
        
        nametagRenderer = new NametagRenderer();
        LOGGER.info("NametagRenderer created and ready");
        LOGGER.info("M4Lib is ready to receive nametag registrations from other mods");
    }
    
    public static M4Lib getInstance() {
        return instance;
    }
    
    public NametagRenderer getNametagRenderer() {
        return nametagRenderer;
    }
}

