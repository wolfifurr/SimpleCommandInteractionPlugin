package com.wolfifurr.helper;

import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.console.ConsoleSender;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.joml.Vector3i;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class CommandExecutor {
    public static void executeCommand(String command, boolean fromPlayer, boolean excludePlayer, World world, InteractionContext context, Vector3i comparePos) {
        if (command == null) {command="";}
        if (command.startsWith("/")) { command = command.replaceFirst("/",""); }

        PlayerRef interactionPlayer = world.getEntityStore().getStore().getComponent(context.getEntity(), PlayerRef.getComponentType());
        if (interactionPlayer!=null) {
            Vector3d position = world.getEntityStore().getStore().getComponent(context.getEntity(), TransformComponent.getComponentType()).getPosition();
            if (position!=null) comparePos = new Vector3i((int) position.x, (int) position.y, (int) position.z);
        }
        CommandSender sender = (interactionPlayer != null && fromPlayer) ? interactionPlayer : ConsoleSender.INSTANCE;

        String finalCommand = command;
        Vector3i finalComparePos = comparePos;
        world.execute(() -> {
            String[] commands = finalCommand.split(" ");
            StringBuilder final_command = new StringBuilder();

            for (int i = 0; i < commands.length; i++) {
                String s = commands[i];

                if (s.equals("@p") && finalComparePos != null) {
                    AtomicReference<PlayerRef> closestPlayer= new AtomicReference<>();
                    world.getPlayerRefs().forEach(playerRef -> {
                        if (excludePlayer && interactionPlayer != null && playerRef==interactionPlayer){return;}
                        if (closestPlayer.get() ==null) {
                            closestPlayer.set(playerRef);
                        } else if (closestPlayer.get().getTransform().getPosition().distance((Vector3dc) finalComparePos)>playerRef.getTransform().getPosition().distance((Vector3dc) finalComparePos)) {
                            closestPlayer.set(playerRef);
                        }
                    });
                    if (closestPlayer.get()!=null) {
                        final_command.append(closestPlayer.get().getUsername());
                    }
                } else if (s.equals("@r")) {
                    Random random = new Random();
                    PlayerRef playerRef = world.getPlayerRefs().stream().toList().get(random.nextInt(world.getPlayerCount()));
                    if (playerRef!=null) {
                        final_command.append(playerRef.getUsername());
                    }
                } else if (s.equals("@s")) {
                    if (interactionPlayer!=null) {
                        final_command.append(interactionPlayer.getUsername());
                    }
                } else if (s.equals("@w")) {
                    String worldName = world.getName();
                    if (worldName!=null) {
                        final_command.append(worldName);
                    }
                } else {
                    final_command.append(s);
                }
                if (i!=commands.length-1) {
                    final_command.append(" ");
                }
            }

            if (Arrays.stream(commands).toList().contains("@a")) {
                world.getPlayerRefs().forEach(playerRef -> {
                    if (excludePlayer && interactionPlayer != null && playerRef==interactionPlayer){return;}
                    CommandManager.get().handleCommand(sender, final_command.isEmpty() ? "say Hello" : final_command.toString().replace("@a",playerRef.getUsername()));
                });
            } else {
                CommandManager.get().handleCommand(sender, final_command.isEmpty() ? "say Hello" : final_command.toString());
            }

        });
    }
}
