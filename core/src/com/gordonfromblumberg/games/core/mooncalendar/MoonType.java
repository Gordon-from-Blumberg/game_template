package com.gordonfromblumberg.games.core.mooncalendar;

public enum MoonType {
    NEW("moon-0", false),
    WAXING1("moon-1", false),
    QUARTER1("moon-2", false),
    WAXING2("moon-3", false),
    FULL("moon-4", false),
    WANING1("moon-3", true),
    QUARTER2("moon-2", true),
    WANING2("moon-1", true),
    ;

    public final String image;
    public final boolean flipped;

    MoonType(String image, boolean flipped) {
        this.image = image;
        this.flipped = flipped;
    }
}
