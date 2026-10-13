package com.wolfifurr.interactions;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.protocol.Interaction;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.protocol.WaitForDataFrom;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.wolfifurr.helper.CommandExecutor;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.joml.Vector3i;

import javax.annotation.Nonnull;

public class BlockCommandInteraction extends SimpleBlockInteraction {
    public static final BuilderCodec CODEC;

    protected String command;
    protected boolean fromPlayer = false;
    protected boolean excludePlayer = false;

    @Nonnull
    public WaitForDataFrom getWaitForDataFrom() {
        return WaitForDataFrom.Server;
    }

    @Override
    protected void interactWithBlock(@NonNullDecl World world, @NonNullDecl CommandBuffer<EntityStore> commandBuffer, @NonNullDecl InteractionType interactionType, @NonNullDecl InteractionContext context, @NullableDecl ItemStack itemStack, @NonNullDecl Vector3i targetBlock, @NonNullDecl CooldownHandler cooldownHandler) {
        CommandExecutor.executeCommand(command,fromPlayer,excludePlayer,world,context,targetBlock);
    }

    @Override
    protected void simulateInteractWithBlock(@NonNullDecl InteractionType interactionType, @NonNullDecl InteractionContext interactionContext, @NullableDecl ItemStack itemStack, @NonNullDecl World world, @NonNullDecl org.joml.Vector3i vector3i) {

    }

    static {
        CODEC = BuilderCodec.builder(BlockCommandInteraction.class, BlockCommandInteraction::new, SimpleBlockInteraction.CODEC)
                .documentation("Executes command when interacted with block")
                .append(new KeyedCodec<>("Command", Codec.STRING),
                        (blockCommandInteraction, o) -> blockCommandInteraction.command = o,
                        (blockCommandInteraction) -> blockCommandInteraction.command).add()
                .append(new KeyedCodec<>("From player?", Codec.BOOLEAN),
                        (blockCommandInteraction, o) -> blockCommandInteraction.fromPlayer = o,
                        blockCommandInteraction -> blockCommandInteraction.fromPlayer).add()
                .append(new KeyedCodec<>("Exclude player?", Codec.BOOLEAN),
                        (blockCommandInteraction, o) -> blockCommandInteraction.excludePlayer = o,
                        blockCommandInteraction -> blockCommandInteraction.excludePlayer)
                .documentation("Command that will be executed when interacted").add().build();
    }
}
