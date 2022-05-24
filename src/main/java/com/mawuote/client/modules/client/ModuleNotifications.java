package com.mawuote.client.modules.client;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.event.impl.render.EventRender2D;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.ValueBoolean;
import com.mawuote.api.manager.value.impl.ValueNumber;
import net.minecraft.client.gui.ScaledResolution;

public class ModuleNotifications extends Module<B> {
    public ModuleNotifications(){super("Notifications", "Notifications", "Renders notifications on your screen.", ModuleCategory.CLIENT);}

    public static ValueNumber lifetime = new ValueNumber("Lifetime", "Lifetime", "", 500, 500, 5000);
    public static ValueNumber inOutTime = new ValueNumber("InOutTime", "InOutTime", "", 200, 50, 500);
    ValueNumber height = new ValueNumber("Height", "Height", "", 50, 0, new ScaledResolution(mc).getScaledHeight());
    ValueNumber max = new ValueNumber("Max", "Max", "", 7, 1, 20);
    public static ValueBoolean addType = new ValueBoolean("AddDecrease", "AddDecrease", "", false);
    public static ValueBoolean pops = new ValueBoolean("Pops", "Pops", "", true);
    public static ValueBoolean chatNotify = new ValueBoolean("ChatNotify", "ChatNotify", "", true);

    public void onRender2D(EventRender2D event) {
        if(mc.player == null || mc.world == null)
            return;

        if(Kaotik.NOTIFICATION_PROCESSOR.notifications.size() > max.getValue().intValue()) {
            Kaotik.NOTIFICATION_PROCESSOR.notifications.remove(0);
        }

        Kaotik.NOTIFICATION_PROCESSOR.handleNotifications(height.getValue().intValue());
    }
}
