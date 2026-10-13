package com.wolfifurr.interactions;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.protocol.Interaction;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.wolfifurr.SimpleCommandInteractionPlugin;
import com.wolfifurr.helper.CommandExecutor;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

public class InstantCommandInteraction extends SimpleInstantInteraction {
    public static final BuilderCodec CODEC;

    protected String command;
    protected boolean fromPlayer = false;
    protected boolean excludePlayer = false;

    @Override
    protected void firstRun(@NonNullDecl InteractionType interactionType, @NonNullDecl InteractionContext context, @NonNullDecl CooldownHandler cooldownHandler) {
        CommandExecutor.executeCommand(
                command,
                fromPlayer,
                excludePlayer,
                context.getCommandBuffer().getExternalData().getWorld(),
                context,
                null
        );
    }

    @Nonnull
    protected Interaction generatePacket() {
        return new com.hypixel.hytale.protocol.RunRootInteraction();
    }

    protected void configurePacket(Interaction packet) {
        super.configurePacket(packet);
    }

    static {
        CODEC = BuilderCodec.builder(InstantCommandInteraction.class, InstantCommandInteraction::new, SimpleInstantInteraction.CODEC)
                .documentation("Executes command when interacted from item")
                .append(new KeyedCodec<>("Command", Codec.STRING),
                        (instantCommandInteraction, o) -> instantCommandInteraction.command = o,
                        (instantCommandInteraction) -> instantCommandInteraction.command).add()
                .append(new KeyedCodec<>("From player?", Codec.BOOLEAN),
                        (instantCommandInteraction, o) -> instantCommandInteraction.fromPlayer = o,
                        instantCommandInteraction -> instantCommandInteraction.fromPlayer)
                .add()
                .append(new KeyedCodec<>("Exclude player?", Codec.BOOLEAN),
                        (blockCommandInteraction, o) -> blockCommandInteraction.excludePlayer = o,
                        blockCommandInteraction -> blockCommandInteraction.excludePlayer)
                .documentation("Command that will be executed when interacted").add().build();
    }
}
