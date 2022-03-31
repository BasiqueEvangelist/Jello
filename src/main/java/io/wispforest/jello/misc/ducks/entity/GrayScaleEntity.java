package io.wispforest.jello.misc.ducks.entity;

import io.wispforest.jello.api.registry.GrayScaleRegistry;
import net.minecraft.entity.Entity;

public interface GrayScaleEntity {

    default boolean isGrayScaled(Entity entity) {
        return GrayScaleRegistry.isRegistered(entity);
    }

    default boolean isTrueColored() {
        return true;
    }
}