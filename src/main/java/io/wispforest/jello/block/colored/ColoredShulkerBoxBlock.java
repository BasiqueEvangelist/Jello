package io.wispforest.jello.block.colored;

import io.wispforest.jello.api.ducks.DyeBlockStorage;
import io.wispforest.jello.api.dye.DyeColorant;
import io.wispforest.jello.api.dye.registry.DyeColorantRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

@EnvironmentInterface(value = EnvType.CLIENT, itf = BlockColorProvider.class)
public class ColoredShulkerBoxBlock extends ShulkerBoxBlock implements BlockColorProvider {

    public ColoredShulkerBoxBlock(DyeColorant dyeColorant, Settings settings) {
        super(DyeColorantRegistry.Constants.NULL_VALUE_OLD, settings);

        ((DyeBlockStorage)this).setDyeColor(dyeColorant);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public int getColor(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos, int tintIndex) {
        DyeColorant dyeColorant = ((DyeBlockStorage) this).getDyeColorant();

        return dyeColorant.getBaseColor();
    }
}
