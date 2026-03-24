package com.gordonfromblumberg.games.core.common.utils;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import java.io.File;

public class TextureAtlasPacker {
    public static void main(String[] args) {
        File inputDir = new File("../../core/resources/image");
        File[] subdirs = inputDir.listFiles();
        if (subdirs == null) {
            throw new IllegalStateException("Could not find directories in ../../core/resources/image");
        }
        for (File subdir : subdirs) {
            if (subdir.isDirectory()) {
                TexturePacker.process(subdir.getPath(), "image", subdir.getName());
            }
        }

        // ui
        TexturePacker.process("../../core/resources/ui_image", "ui", "uiskin");
    }
}
