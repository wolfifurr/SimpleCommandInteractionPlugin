package com.wolfifurr.interactions;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.console.ConsoleSender;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class ExecuteCommandInteraction extends SimpleBlockInteraction {
    public static final BuilderCodec CODEC;

    protected String command;

    @Override
    protected void interactWithBlock(@Nonnull World world, @Nonnull CommandBuffer<EntityStore> commandBuffer, @Nonnull InteractionType interactionType, @Nonnull InteractionContext context, @Nullable ItemStack itemStack, @Nonnull Vector3i targetBlock, @Nonnull CooldownHandler cooldownHandler) {
        world.execute(() -> {
            if (command.contains(" @a ")) {
                world.getPlayerRefs().forEach(playerRef -> {
                    CommandManager.get().handleCommand(ConsoleSender.INSTANCE, (command == null || command.isEmpty()) ? "/say Hello" : command.replace("@a",playerRef.getUsername()));
                });
            } else if (command.contains(" @p ")) {
                AtomicReference<PlayerRef> closestPlayer= new AtomicReference<>();
                world.getPlayerRefs().forEach(playerRef -> {
                    if (closestPlayer.get() ==null) {
                        closestPlayer.set(playerRef);
                    } else if (closestPlayer.get().getTransform().getPosition().distanceTo(targetBlock)>playerRef.getTransform().getPosition().distanceTo(targetBlock)) {
                        closestPlayer.set(playerRef);
                    }
                });
                if (closestPlayer.get()!=null) {
                    CommandManager.get().handleCommand(ConsoleSender.INSTANCE, (command == null || command.isEmpty()) ? "/say Hello" : command.replace("@p",closestPlayer.get().getUsername()));
                }
            } else if (command.contains(" @r ")) {
                Random random = new Random();
                PlayerRef playerRef = world.getPlayerRefs().stream().toList().get(random.nextInt(world.getPlayerCount()));
                if (playerRef!=null) {
                    CommandManager.get().handleCommand(ConsoleSender.INSTANCE, (command == null || command.isEmpty()) ? "/say Hello" : command.replace("@r",playerRef.getUsername()));
                }
            } else if (command.contains(" @s ")) {
                PlayerRef player = world.getEntityStore().getStore().getComponent(context.getEntity(), PlayerRef.getComponentType());
                if (player!=null) {
                    CommandManager.get().handleCommand(ConsoleSender.INSTANCE, (command == null || command.isEmpty()) ? "/say Hello" : command.replace("@s",player.getUsername()));
                }
            } else {
                CommandManager.get().handleCommand(ConsoleSender.INSTANCE, (command == null || command.isEmpty()) ? "/say Hello" : command);
            }
        });
    }

    @Override
    protected void simulateInteractWithBlock(@Nonnull InteractionType interactionType, @Nonnull InteractionContext interactionContext, @Nullable ItemStack itemStack, @Nonnull World world, @Nonnull Vector3i vector3i) {

    }

    static {
        CODEC = BuilderCodec.builder(ExecuteCommandInteraction.class, ExecuteCommandInteraction::new, SimpleBlockInteraction.CODEC)
                .documentation("Executes command when interacted with block")
                .append(new KeyedCodec("Command", Codec.STRING),
                        (executeCommandInteraction, o) -> executeCommandInteraction.command=(String) o,
                        (executeCommandInteraction) -> executeCommandInteraction.command)
                .documentation("Command that will be executed when interacted").add().build();
    }
}
