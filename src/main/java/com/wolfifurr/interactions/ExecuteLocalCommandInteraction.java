package com.wolfifurr.interactions;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.console.ConsoleSender;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class ExecuteLocalCommandInteraction extends SimpleInstantInteraction {
    public static final BuilderCodec CODEC;

    protected String command;

    static {
        CODEC = BuilderCodec.builder(ExecuteLocalCommandInteraction.class, ExecuteLocalCommandInteraction::new, SimpleInstantInteraction.CODEC)
                .documentation("Executes command FROM PLAYER when interacted")
                .append(new KeyedCodec("Command", Codec.STRING),
                        (executeCommandBlockInteraction, o) -> executeCommandBlockInteraction.command=(String) o,
                        (executeCommandBlockInteraction) -> executeCommandBlockInteraction.command)
                .documentation("Command that will be executed FROM PLAYER when interacted").add().build();
    }

    @Override
    protected void firstRun(@Nonnull InteractionType interactionType, @Nonnull InteractionContext interactionContext, @Nonnull CooldownHandler cooldownHandler) {
        World world = interactionContext.getEntity().getStore().getExternalData().getWorld();
        Ref<EntityStore> plrRef = interactionContext.getOwningEntity();
        Player plr = interactionContext.getOwningEntity().getStore().getComponent(plrRef,Player.getComponentType());
        world.execute(() -> {
            if (command.contains(" @a ")) {
                world.getPlayerRefs().forEach(playerRef -> {
                    CommandManager.get().handleCommand(plr, (command == null || command.isEmpty()) ? "/say Hello" : command.replace("@a",playerRef.getUsername()));
                });
            } else if (command.contains(" @p ")) {
                AtomicReference<PlayerRef> closestPlayer= new AtomicReference<>();
                if (interactionContext.getTargetBlock()!=null) {
                    Vector3i targetBlock = new Vector3i(interactionContext.getTargetBlock().x, interactionContext.getTargetBlock().y, interactionContext.getTargetBlock().z);
                    world.getPlayerRefs().forEach(playerRef -> {
                        if (closestPlayer.get() == null) {
                            closestPlayer.set(playerRef);
                        } else if (closestPlayer.get().getTransform().getPosition().distanceTo(targetBlock) > playerRef.getTransform().getPosition().distanceTo(targetBlock)) {
                            closestPlayer.set(playerRef);
                        }
                    });
                }
                if (closestPlayer.get()!=null) {
                    CommandManager.get().handleCommand(plr, (command == null || command.isEmpty()) ? "/say Hello" : command.replace("@p",closestPlayer.get().getUsername()));
                }
            } else if (command.contains(" @r ")) {
                Random random = new Random();
                PlayerRef playerRef = world.getPlayerRefs().stream().toList().get(random.nextInt(world.getPlayerCount()));
                if (playerRef!=null) {
                    CommandManager.get().handleCommand(plr, (command == null || command.isEmpty()) ? "/say Hello" : command.replace("@r",playerRef.getUsername()));
                }
            } else if (command.contains(" @s ")) {
                PlayerRef player = world.getEntityStore().getStore().getComponent(interactionContext.getOwningEntity(), PlayerRef.getComponentType());
                if (player!=null) {
                    CommandManager.get().handleCommand(plr, (command == null || command.isEmpty()) ? "/say Hello" : command.replace("@s",player.getUsername()));
                }
            } else {
                CommandManager.get().handleCommand(plr, (command == null || command.isEmpty()) ? "/say Hello" : command);
            }
        });
    }
}
