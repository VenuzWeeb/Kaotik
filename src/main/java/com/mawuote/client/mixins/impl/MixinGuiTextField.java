package com.mawuote.client.mixins.impl;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiTextField.class)
public class MixinGuiTextField extends Gui {

    @Shadow
    public boolean isEnabled;
    @Shadow
    public int enabledColor;
    @Shadow
    public int disabledColor;
    @Shadow
    public int cursorPosition;
    @Shadow
    private int lineScrollOffset;
    @Shadow
    private String text;
    @Shadow
    private int selectionEnd;
    @Shadow
    private FontRenderer fontRenderer;
    @Shadow
    private boolean enableBackgroundDrawing;
    @Shadow
    private boolean isFocused;
    @Shadow
    private int cursorCounter;
    @Shadow
    private int x;
    @Shadow
    private int y;
    @Shadow
    private int height;
    @Shadow
    private int width;

    @Shadow
    public boolean getVisible() {
        return false;
    }
    @Shadow
    public boolean getEnableBackgroundDrawing() {
        return false;
    }
    @Shadow
    private int getWidth() {return 0;}

    ///@Inject(method = "drawTextBox", at = @At("HEAD"), cancellable = false)
    ///public void drawTextBox(CallbackInfo info) {
    ///    RenderUtils.drawStringWithShadow(false, ChatFormatting.GRAY + "Help", 4, new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() - FontUtils.getFontHeight(false) - 3, -1);
    ///}

    @Overwrite
    public void drawTextBox() {
        if (this.getVisible()) {
            if (this.getEnableBackgroundDrawing()) {
                drawRect(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, -6250336);
                drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -16777216);
            }

            int i = this.isEnabled ? this.enabledColor : this.disabledColor;
            int j = this.cursorPosition - this.lineScrollOffset;
            int k = this.selectionEnd - this.lineScrollOffset;
            String s = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            boolean flag = j >= 0 && j <= s.length();
            boolean flag1 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag;
            int l = this.enableBackgroundDrawing ? this.x + 4 : this.x;
            int i1 = this.enableBackgroundDrawing ? this.y + (this.height - 8) / 2 : this.y;
            int j1 = l;
            if (k > s.length()) {
                k = s.length();
            }

            if (!s.isEmpty()) {
                String s1 = flag ? s.substring(0, j) : s;
                String string = "";
                for(Command c : Kaotik.COMMAND_MANAGER.getCommands()) {
                    String cN = Kaotik.COMMAND_MANAGER.getPrefix() + c.getName();
                    if(cN.startsWith(s)) {
                        string = cN;
                    }
                }
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(ChatFormatting.GRAY + string, 4, new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT - 3, -1);
                j1 = this.fontRenderer.drawStringWithShadow(s1, (float)l, (float)i1, i);
            }

            boolean flag2 = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
            int k1 = j1;
            if (!flag) {
                k1 = j > 0 ? l + this.width : l;
            } else if (flag2) {
                k1 = j1 - 1;
                --j1;
            }

            if (!s.isEmpty() && flag && j < s.length()) {
                j1 = this.fontRenderer.drawStringWithShadow(s.substring(j), (float)j1, (float)i1, i);
            }

            if (flag1) {
                if (flag2) {
                    Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + this.fontRenderer.FONT_HEIGHT, -3092272);
                } else {
                    this.fontRenderer.drawStringWithShadow("_", (float)k1, (float)i1, i);
                }
            }

            if (k != j) {
                int l1 = l + this.fontRenderer.getStringWidth(s.substring(0, k));
                this.drawSelectionBox(k1, i1 - 1, l1 - 1, i1 + 1 + this.fontRenderer.FONT_HEIGHT);
            }
        }

    }
    @Shadow
    private int getMaxStringLength() {
        return 0;
    }
    @Shadow
    private void drawSelectionBox(int startX, int startY, int endX, int endY) {
    }
}
