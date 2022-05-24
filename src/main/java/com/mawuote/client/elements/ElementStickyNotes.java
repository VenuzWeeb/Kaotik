package com.mawuote.client.elements;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.element.Element;
import com.mawuote.api.manager.event.impl.render.EventRender2D;
import com.mawuote.api.manager.value.impl.ValueEnum;
import com.mawuote.api.manager.value.impl.ValueString;
import com.mawuote.client.modules.client.ModuleColor;

public class ElementStickyNotes extends Element {
    public ElementStickyNotes() {
        super("StickyNotes", "Sticky Notes", "Let's you write custom stuff on the screen.");
    }

    public static ValueEnum lines = new ValueEnum("Lines", "Lines", "The amount of lines that should be rendered.", LinesAmount.One);
    public static ValueString lineOne = new ValueString("LineOne", "LineOne", "The first line.", "Placeholder");
    public static ValueString lineTwo = new ValueString("LineTwo", "LineTwo", "The second line.", "Placeholder");
    public static ValueString lineThree = new ValueString("LineThree", "LineThree", "The third line.", "Placeholder");
    public static ValueString lineFour = new ValueString("LineFour", "LineFour", "The fourth line.", "Placeholder");

    @Override
    public void onRender2D(EventRender2D event){
        super.onRender2D(event);

        frame.setWidth(Kaotik.FONT_MANAGER.getStringWidth(lineOne.getValue()));
        frame.setHeight((Kaotik.FONT_MANAGER.getHeight() * getMultiplier()) + getMultiplier());

        for (int i = 0; i <= getMultiplier() - 1; i++){
            Kaotik.FONT_MANAGER.drawString(i == 1 ? lineTwo.getValue() : i == 2 ? lineThree.getValue() : i == 3 ? lineFour.getValue() : lineOne.getValue(), frame.getX(), frame.getY() + ((Kaotik.FONT_MANAGER.getHeight() + 1) * i), ModuleColor.getActualColor());
        }
    }

    public int getMultiplier(){
        switch ((LinesAmount)lines.getValue()){
            case Two:
                return 2;
            case Three:
                return 3;
            case Four:
                return 4;
            default:
                return 1;
        }
    }

    public enum LinesAmount { One, Two, Three, Four }
}
