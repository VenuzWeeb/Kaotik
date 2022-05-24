package com.mawuote.api.utilities.render;

import net.minecraft.util.ResourceLocation;

public class GifLocation {

    private final String folder;
    private final int frames;
    private final int fpt;

    private int currentTick = 0;
    private int currentFrame = 0;

    private ResourceLocation[] textures;

    public GifLocation(String folder, int frames, int fpt) {
        this.folder = folder;
        this.frames = frames;
        this.fpt = fpt;
        textures = new ResourceLocation[frames];

        for(int i = 0; i < frames; i++) {
            textures[i] = new ResourceLocation(folder + "/" + i + ".png");
        }

    }

    public ResourceLocation getTexture() {
        return textures[currentFrame];
    }

    public void update() {
        if(currentTick > fpt) {
            currentTick = 0;
            currentFrame++;
            if(currentFrame > textures.length - 1) {
                currentFrame = 0;
            }
        }
        currentTick++;
    }

}
