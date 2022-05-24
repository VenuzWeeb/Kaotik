package com.mawuote.client.elements;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.element.Element;
import com.mawuote.api.manager.event.impl.render.EventRender2D;
import com.mawuote.api.manager.value.impl.ValueEnum;
import com.mawuote.api.manager.value.impl.ValueString;
import com.mawuote.client.modules.client.ModuleColor;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ElementFriends extends Element {
    public ElementFriends(){
        super("Friends", "Gives you a list of friends in your chunk distance.");
    }

    public static ValueString name = new ValueString("Name", "Name", "The name for the group of friends.", "The Goons");
    public static ValueEnum color = new ValueEnum("Color", "Color", "The color for the friend names.", Colors.White);

    @Override
    public void onRender2D(EventRender2D event){
        super.onRender2D(event);

        ArrayList<EntityPlayer> friends = mc.world.playerEntities.stream().filter(p -> Kaotik.FRIEND_MANAGER.isFriend(p.getName())).collect(Collectors.toCollection(ArrayList::new));
        friends.sort(Comparator.comparing(EntityPlayer::getName));

        frame.setWidth(friends.isEmpty() ? Kaotik.FONT_MANAGER.getStringWidth(name.getValue()) : Kaotik.FONT_MANAGER.getStringWidth(friends.get(0).getName()));
        frame.setHeight(Kaotik.FONT_MANAGER.getHeight() + (friends.isEmpty() ? 0 : 1 + ((Kaotik.FONT_MANAGER.getHeight() + 1) * (friends.size() + 1))));

        Kaotik.FONT_MANAGER.drawString(name.getValue(), frame.getX(), frame.getY(), ModuleColor.getActualColor());

        int index = 10;
        for (EntityPlayer player : friends){
            Kaotik.FONT_MANAGER.drawString(getColor() + player.getName(), frame.getX(), frame.getY() + index, ModuleColor.getActualColor());
            index += 10;
        }
    }

    private ChatFormatting getColor(){
        if (color.getValue().equals(Colors.White)){
            return ChatFormatting.WHITE;
        } else if (color.getValue().equals(Colors.Gray)){
            return ChatFormatting.GRAY;
        } else {
            return ChatFormatting.RESET;
        }
    }

    public enum Colors { Normal, White, Gray }
}
