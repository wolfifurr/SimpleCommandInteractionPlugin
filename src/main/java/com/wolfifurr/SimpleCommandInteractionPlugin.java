package com.wolfifurr;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.wolfifurr.interactions.BlockCommandInteraction;
import com.wolfifurr.interactions.InstantCommandInteraction;

import javax.annotation.Nonnull;

public class SimpleCommandInteractionPlugin extends JavaPlugin {
//    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public SimpleCommandInteractionPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        Interaction.CODEC.register("BlockCommand", BlockCommandInteraction.class, BlockCommandInteraction.CODEC);
        Interaction.CODEC.register("InstantCommand", InstantCommandInteraction.class, InstantCommandInteraction.CODEC);
    }
}
