package com.alazeprt.mcpixelart;

import com.alazeprt.mcpixelart.commands.ExportPaint;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MCPixelArt.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MCPixelArtEventHandler {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        ExportPaint.register(event.getDispatcher());
    }
}
