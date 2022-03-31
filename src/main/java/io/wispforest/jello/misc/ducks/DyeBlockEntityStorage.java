package io.wispforest.jello.misc.ducks;

import io.wispforest.jello.api.dye.DyeColorant;

public interface DyeBlockEntityStorage {

    void setDyeColor(DyeColorant dyeColorant);

    DyeColorant getDyeColor();
}