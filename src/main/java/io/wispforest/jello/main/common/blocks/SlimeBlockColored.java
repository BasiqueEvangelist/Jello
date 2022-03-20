package io.wispforest.jello.main.common.blocks;

import io.wispforest.jello.api.dye.DyeColorant;
import io.wispforest.jello.api.mixin.ducks.DyeBlockStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlimeBlock;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

@EnvironmentInterface(value = EnvType.CLIENT, itf = BlockColorProvider.class)
public class SlimeBlockColored extends SlimeBlock implements BlockColorProvider {

    public SlimeBlockColored(DyeColorant dyeColor, Settings settings) {
        super(settings.mapColor(dyeColor.getMapColor()));

        ((DyeBlockStorage)this).setDyeColor(dyeColor);
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        return super.isSideInvisible(state, stateFrom, direction);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public int getColor(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos, int tintIndex) {
        return ((DyeBlockStorage)this).getDyeColor().getBaseColor();
    }
}
