package com.alazeprt.mcpixelart.commands;

import com.alazeprt.utils.PixelException;
import com.alazeprt.utils.PixelManipulation;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.forge.ForgeAdapter;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.SessionManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.awt.*;
import java.util.UUID;

import static com.alazeprt.utils.PixelUtils.decimalToHex;
import static com.alazeprt.utils.PixelUtils.hexToRGB;

public class ExportPaint {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("export")
                .then(Commands.argument("path", StringArgumentType.greedyString())
                        .executes(context -> handle(context.getSource(), context.getArgument("path", String.class)))));
    }

    private static int handle(CommandSourceStack source, String path) {
        try {
            UUID playerUUID = source.getPlayerOrException().getUUID();
            source.getPlayerOrException().sendMessage(new TextComponent("Generating..."), playerUUID);
            SessionManager manager = WorldEdit.getInstance().getSessionManager();
            Region region;
            LocalSession session = manager.get(ForgeAdapter.adaptPlayer(source.getPlayerOrException()));
            if(session.getSelectionWorld() == null) {
                source.getPlayerOrException().sendMessage(new TextComponent("An error has occurred: You didn't select an area!"), playerUUID);
                return Command.SINGLE_SUCCESS;
            }
            region = session.getSelection();
            int maxY = Math.max(region.getMaximumPoint().getBlockY(), region.getMinimumPoint().getBlockY());
            int minY = Math.min(region.getMaximumPoint().getBlockY(), region.getMinimumPoint().getBlockY());
            int maxX = Math.max(region.getMaximumPoint().getBlockX(), region.getMinimumPoint().getBlockX());
            int minX = Math.min(region.getMaximumPoint().getBlockX(), region.getMinimumPoint().getBlockX());
            int maxZ = Math.max(region.getMaximumPoint().getBlockZ(), region.getMinimumPoint().getBlockZ());
            int minZ = Math.min(region.getMaximumPoint().getBlockZ(), region.getMinimumPoint().getBlockZ());
            Iterable<BlockVector2> iterable = region.getBoundingBox().asFlatRegion();
            PixelManipulation pixelManipulation = new PixelManipulation(maxX-minX+1, maxZ-minZ+1);
            for(BlockVector2 blockVector2 : iterable) {
                BlockPos finalBlockPos = null;
                Color color = null;
                for(int i = maxY; i >= minY; i--) {
                    BlockPos blockPos = new BlockPos(blockVector2.getBlockX(), i, blockVector2.getBlockZ());
                    BlockState blockState = source.getPlayerOrException().getLevel().getBlockState(blockPos);
                    if(blockState.getMaterial().equals(Material.AIR)) {
                        continue;
                    }
                    finalBlockPos = blockPos;
                    color = generateColor(blockState.getMapColor(source.getPlayerOrException().getLevel(), blockPos));
                    break;
                }
                if(finalBlockPos == null) {
                    color = new Color(0, 0, 0, 1);
                    finalBlockPos = new BlockPos(blockVector2.getBlockX(), minY, blockVector2.getBlockZ());
                }
                pixelManipulation.setRGB(finalBlockPos.getX()-minX, finalBlockPos.getZ()-minZ, color);
            }
            PixelException exception = pixelManipulation.export(path);
            source.getPlayerOrException().sendMessage(new TextComponent(exception.getMessage()), playerUUID);
        } catch (Exception e) {
            try {
                e.printStackTrace();
                source.getPlayerOrException().sendMessage(new TextComponent("An error has occurred: \n" + e), source.getPlayerOrException().getUUID());
                return Command.SINGLE_SUCCESS;
            } catch (Exception e2) {
                source.sendFailure(new TextComponent("An error has orrurred: \n" + e2));
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    private static Color generateColor(MaterialColor mapColor) {
        long colorCode = mapColor.col;
        System.out.println(colorCode);
        String hexColor = decimalToHex(colorCode);
        return hexToRGB(hexColor);
    }
}
