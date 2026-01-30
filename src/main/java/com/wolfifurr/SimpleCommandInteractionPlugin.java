package com.wolfifurr;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.wolfifurr.interactions.ExecuteCommandInteraction;
import com.wolfifurr.interactions.SimpleCommandInteraction;

import javax.annotation.Nonnull;

public class SimpleCommandInteractionPlugin extends JavaPlugin {

    public SimpleCommandInteractionPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        Interaction.CODEC.register("ExecuteCommand", ExecuteCommandInteraction.class,ExecuteCommandInteraction.CODEC);
        Interaction.CODEC.register("SimpleCommand", SimpleCommandInteraction.class,SimpleCommandInteraction.CODEC);
    }
}
