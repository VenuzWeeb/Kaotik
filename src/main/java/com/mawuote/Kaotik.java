package com.mawuote;

import com.mawuote.api.manager.element.ElementManager;
import com.mawuote.api.manager.misc.ChatManager;
import com.mawuote.api.manager.misc.ConfigManager;
import com.mawuote.api.utilities.math.TPSUtils;
import com.mawuote.client.gui.special.EuropaMainMenu;
import com.mawuote.api.manager.command.CommandManager;
import com.mawuote.api.manager.event.EventManager;
import com.mawuote.api.manager.friend.FriendManager;
import com.mawuote.api.manager.module.ModuleManager;
import com.mawuote.client.modules.client.notifications.NotificationProcessor;
import com.mawuote.client.gui.click.ClickGuiScreen;
import com.mawuote.client.gui.font.FontManager;
import com.mawuote.client.gui.hud.HudEditorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

@Mod(modid = Kaotik.MODID, name = Kaotik.NAME, version = Kaotik.VERSION)
public class Kaotik {
    public static final String NAME = "Kaotik";
    public static final String VERSION = "1.3.3";
    public static final String MODID = "kaotik";

    public static final Logger LOGGER = LogManager.getLogger("Kaotik");

    @Mod.Instance(MODID)
    public static Kaotik INSTANCE;

    public static NotificationProcessor NOTIFICATION_PROCESSOR;
    public static ChatManager CHAT_MANAGER;
    public static ModuleManager MODULE_MANAGER;
    public static ElementManager ELEMENT_MANAGER;
    public static CommandManager COMMAND_MANAGER;
    public static FriendManager FRIEND_MANAGER;
    public static EventManager EVENT_MANAGER;

    public static FontManager FONT_MANAGER;
    public static ClickGuiScreen CLICK_GUI;
    public static HudEditorScreen HUD_EDITOR;
    public static EuropaMainMenu MAIN_MENU;

    public static ConfigManager CONFIG_MANAGER;

    @Mod.EventHandler
    public void initialize(FMLInitializationEvent event){
        LOGGER.info("Started Initialization process for Kaotik v" + VERSION + "!");
        setEuropaIcon();

        CHAT_MANAGER = new ChatManager();
        MODULE_MANAGER = new ModuleManager();
        ELEMENT_MANAGER = new ElementManager();
        COMMAND_MANAGER = new CommandManager();
        FRIEND_MANAGER = new FriendManager();
        EVENT_MANAGER = new EventManager();

        FONT_MANAGER = new FontManager();
        FONT_MANAGER.load();

        CLICK_GUI = new ClickGuiScreen();
        HUD_EDITOR = new HudEditorScreen();
        MAIN_MENU = new EuropaMainMenu();
        NOTIFICATION_PROCESSOR = new NotificationProcessor();

        CONFIG_MANAGER = new ConfigManager();
        CONFIG_MANAGER.load();
        CONFIG_MANAGER.attach();

        // This is so the TPS Utils start working
        new TPSUtils();

        LOGGER.info("Finished Initialization process for Kaotik v" + VERSION + "!");
    }

    @Mod.EventHandler
    public void postInitialize(FMLPostInitializationEvent event){
        LOGGER.info("Started Post-Initialization process for Kaotik v" + VERSION + "!");

        Display.setTitle(Kaotik.NAME + " " + Kaotik.VERSION + " | Kaotik Continued");

        LOGGER.info("Finished Post-Initialization process for Kaotik v" + VERSION + "!");
    }

    public static ModuleManager getModuleManager() {
        return MODULE_MANAGER;
    }

    public void setWindowIcon() {
        if (Util.getOSType() != Util.EnumOS.OSX) {
            try (InputStream inputStream16x = Minecraft.class.getResourceAsStream("/assets/mawuote/img16.png");
                 InputStream inputStream32x = Minecraft.class.getResourceAsStream("/assets/mawuote/img32.png")) {
                ByteBuffer[] icons = new ByteBuffer[]{readImageToBuffer(inputStream16x), readImageToBuffer(inputStream32x)};
                Display.setIcon(icons);
            } catch (Exception e) {
                LOGGER.error("couldnt display window icon", e);
            }
        }
    }

    public ByteBuffer readImageToBuffer(InputStream inputStream) throws IOException {
        BufferedImage bufferedimage = ImageIO.read(inputStream);
        int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), null, 0, bufferedimage.getWidth());
        ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);
        Arrays.stream(aint).map(i -> i << 8 | (i >> 24 & 255)).forEach(bytebuffer::putInt);
        bytebuffer.flip();
        return bytebuffer;
    }

    private void setEuropaIcon() {
        this.setWindowIcon();
    }
}
