package com.mawuote.client.modules.render;

import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.impl.*;
import com.mawuote.api.utilities.math.TimerUtils;

import java.awt.*;

public class ModulePlayerChams extends Module {
    public ModulePlayerChams(){super("PlayerChams", "Player Chams", "Draws chams on players to make them render better.", ModuleCategory.RENDER);}

    public enum outlineModes {
        Wire, Flat;
    }

    public static ValueBoolean syncColor = new ValueBoolean("SyncColor", "SyncColor", "", true);
    public static ValueBoolean outline = new ValueBoolean("Outline", "Outline", "", false);
    public static ValueEnum outlineMode = new ValueEnum("OutlineMode", "OutlineMode", "", outlineModes.Wire);
    public static ValueNumber width = new ValueNumber("Width", "Width", "", 3.0, 0.5, 5.0);
    public static ValueBoolean enchanted = new ValueBoolean("Glint", "Glint", "", false);
    public static ValueColor enchantedColor = new ValueColor("GlintColor", "GlintColor", "", new Color(0, 255, 120, 255));
    public static ValueBoolean visible = new ValueBoolean("Visible", "Visible", "", false);
    public static ValueBoolean hidden = new ValueBoolean("Hidden", "Hidden", "", false);

    public static ValueBoolean pulse = new ValueBoolean("Pulse", "Pulse", "", false);
    public static ValueBoolean friendPulse = new ValueBoolean("FriendPulse", "FriendPulse", "", false);
    ValueNumber pulseMax = new ValueNumber("PulseMax", "PulseMax", "", 255, 0, 255);
    ValueNumber pulseMin = new ValueNumber("PulseMin", "PulseMin", "", 100, 0, 255);
    ValueNumber pulseChangeAmt = new ValueNumber("PulseChangeAmt", "PulseChangeAmt", "", 5, 1, 50);

    public static ValueColor daColor = new ValueColor("VisibleColor", "VisibleColor", "", new Color(0, 255, 120, 255));
    public static ValueColor hiddenColor = new ValueColor("HiddenColor", "HiddenColor", "", new Color(0, 100, 255, 255));
    public static ValueBoolean hiddenSync = new ValueBoolean("HiddenSync", "HiddenSync", "", true);
    public static ValueColor outlineColor = new ValueColor("OutColor", "OutColor", "", new Color(255, 255, 255));

    public static ValueBoolean syncFriend = new ValueBoolean("SyncFriend", "SyncFriend", "", true);
    public static ValueBoolean fvisible = new ValueBoolean("FriendVisible", "FriendVisible", "", false);
    public static ValueBoolean fhidden = new ValueBoolean("FriendHidden", "FriendHidden", "", false);
    public static ValueColor fdaColor = new ValueColor("FriendVisibleColor", "FriendVisibleColor", "", new Color(255, 150, 255, 255));
    public static ValueColor fhiddenColor = new ValueColor("FriendHiddenColor", "FriendHiddenColor", "", new Color(255, 100, 100, 255));
    public static ValueBoolean fhiddenSync = new ValueBoolean("FriendHiddenSync", "FriendHiddenSync", "", true);
    public static ValueColor foutlineColor = new ValueColor("FriendOutColor", "FriendOutColor", "", new Color(255, 255, 255));

    public static Color color;
    public static Color outColor;
    public static Color hideColor;

    public static Color fcolor;
    public static Color foutColor;
    public static Color fhideColor;

    public static int pulseAlpha = 1;
    private boolean isReversing = false;
    private TimerUtils timer = new TimerUtils();
    int maxvalue;
    int minvalue;

    public void onUpdate() {
        maxvalue = pulseMax.getValue().intValue();
        minvalue = pulseMin.getValue().intValue();

        if(pulseAlpha < minvalue) {
            pulseAlpha = minvalue;
        }

        if (timer.hasReached(5)) {
            pulseAlpha += isReversing ? -pulseChangeAmt.getValue().intValue() : pulseChangeAmt.getValue().intValue();

            if (isReversing && pulseAlpha <= minvalue) {
                isReversing = false;
            } else if (!isReversing && pulseAlpha >= maxvalue) {
                isReversing = true;
            }
            timer.reset();
        }

        if(syncColor.getValue()) {
            color = this.globalColor(255);
            outColor = this.globalColor(255);
            hideColor = this.globalColor(255);

            fcolor = this.globalColor(255);
            foutColor = this.globalColor(255);
            fhideColor = this.globalColor(255);
        } else {
            color = daColor.getValue();
            outColor = outlineColor.getValue();
            hideColor = hiddenColor.getValue();

            if(syncFriend.getValue()) {
                fcolor = daColor.getValue();
                foutColor = outlineColor.getValue();
                fhideColor = hiddenColor.getValue();
            } else {
                fcolor = fdaColor.getValue();
                foutColor = foutlineColor.getValue();
                fhideColor = fhiddenColor.getValue();
            }
        }
    }
}
