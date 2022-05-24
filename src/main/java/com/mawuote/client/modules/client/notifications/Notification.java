package com.mawuote.client.modules.client.notifications;

import com.mawuote.Kaotik;
import com.mawuote.api.utilities.math.AnimationUtils;
import com.mawuote.api.utilities.math.TimerUtils;
import com.mawuote.api.utilities.render.RenderUtils;
import com.mawuote.client.modules.client.ModuleColor;
import com.mojang.realmsclient.gui.ChatFormatting;

import java.awt.*;

public class Notification {
    private final String text;
    public final long disableTime;
    private final float width;
    public final TimerUtils timer = new TimerUtils();
    public AnimationUtils animationUtils;
    public AnimationUtils animationUtils2;

    public AnimationUtils reverse;
    public AnimationUtils reverse2;
    boolean didThing = false;
    boolean isReversing = false;
    boolean didFirstReverse = false;

    public Notification(String text, long disableTime, long inOutTime) {
        this.text = text;
        this.disableTime = disableTime;
        this.width = Kaotik.FONT_MANAGER.getStringWidth( text);
        this.animationUtils = new AnimationUtils(inOutTime, 0, width + 2);
        this.animationUtils2 = new AnimationUtils(inOutTime, 0, width + 4);
        this.reverse = new AnimationUtils(inOutTime, 0, width + 2);
        this.reverse2 = new AnimationUtils(inOutTime, 0, width + 4);
        this.timer.reset();
        this.animationUtils.reset();
        this.animationUtils2.reset();
        this.reverse.reset();
        this.reverse2.reset();
    }

    public void onDraw(int y) {
        if (this.timer.hasReached(this.disableTime)) {
            Kaotik.NOTIFICATION_PROCESSOR.getNotifications().remove(this);
        }
        RenderUtils.drawRecta(-(width + 4) + animationUtils2.getValue() - ((isReversing && didFirstReverse) ? reverse2.getValue() : 0), y, width + 4, 20, ModuleColor.globalColor(255).getRGB());
        if(animationUtils2.isDone()) {
            RenderUtils.drawRecta(-(width + 2) + animationUtils.getValue() - (isReversing ? reverse.getValue() : 0), y, width + 2, 20, new Color(28, 28, 28).getRGB());
            Kaotik.FONT_MANAGER.drawString(ChatFormatting.stripFormatting(this.text), -(width + 2) + animationUtils.getValue() - (isReversing ? reverse.getValue() : 0) + 2, y + 10 - (Kaotik.FONT_MANAGER.getHeight()/2f), Color.WHITE);
        }
    }
}
