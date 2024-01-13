package com.alazeprt.mcpixelart.commands;

import com.alazeprt.mcpixelart.utils.PixelException;
import com.alazeprt.mcpixelart.utils.PixelManipulation;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.SessionManager;
import net.minecraft.block.*;
import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.system.CallbackI;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ExportPaint {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(literal("/export")
                .then(argument("path", greedyString())
                        .executes(ctx -> handle(ctx.getSource(), getString(ctx, "path"))))); // 你可以在这里处理参数，并处理成命令。
    }

    public static int handle(ServerCommandSource source, String path) {
        source.sendFeedback(new LiteralText("Generating..."), false);
        SessionManager manager = WorldEdit.getInstance().getSessionManager();
        Region region;
        try {
            LocalSession session = manager.get(FabricAdapter.adaptPlayer(source.getPlayer()));
            if(session.getSelectionWorld() == null) {
                source.sendFeedback(new LiteralText("An error has occurred: You didn't select an area!"), false);
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
            Color[][] colors = new Color[maxX-minX+1][maxZ-minZ+1];
            for(BlockVector2 blockVector2 : iterable) {
                BlockPos finalBlockPos = null;
                Color color = null;
                for(int i = maxY; i >= minY; i--) {
                    BlockPos blockPos = new BlockPos(blockVector2.getBlockX(), i, blockVector2.getBlockZ());
                    BlockState blockState = source.getWorld().getBlockState(blockPos);
                    if(blockState.getMaterial().equals(Material.AIR)) {
                        continue;
                    }
                    finalBlockPos = blockPos;
                    MapColor color1 = blockState.getMapColor(source.getWorld(), blockPos);
                    color = PixelManipulation.generateColor(color1);
                    break;
                }
                if(finalBlockPos == null) {
                    color = PixelManipulation.generateColor(MapColor.WHITE);
                    finalBlockPos = new BlockPos(blockVector2.getBlockX(), minY, blockVector2.getBlockZ());
                }
                colors[finalBlockPos.getX()-minX][finalBlockPos.getZ()-minZ] = color;
            }
            PixelException exception = PixelManipulation.generateImage(maxX-minX+1, maxZ-minZ+1, path, colors);
            source.sendFeedback(new LiteralText(exception.getMessage()), false);
        } catch (Exception e) {
            source.sendFeedback(new LiteralText("An error has occurred: \n" + e), false);
            e.printStackTrace();
        }
        return Command.SINGLE_SUCCESS; // 成功
    }
}
