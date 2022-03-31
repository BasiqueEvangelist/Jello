package io.wispforest.jello.mixin.entitycolor;

import io.wispforest.jello.api.dye.events.ColorEntityEvent;
import io.wispforest.jello.misc.ducks.entity.ConstantColorEntity;
import io.wispforest.jello.api.registry.ColorizeRegistry;
import io.wispforest.jello.Jello;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BucketItem.class)
public abstract class WaterBucketMixin extends Item {

    @Shadow
    protected abstract void playEmptyingSound(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos);

    public WaterBucketMixin(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (!Jello.getConfig().enableDyeingEntities || (entity instanceof PlayerEntity && !Jello.getConfig().enableDyeingPlayers)) {
            return ActionResult.PASS;
        }

        if (stack.getItem() == Items.WATER_BUCKET) {
            if (ColorizeRegistry.isRegistered(entity)) {
                if (entity instanceof ConstantColorEntity constantColorEntity && constantColorEntity.isColored()) {
                    return ActionResult.PASS;
                }

                if (ColorEntityEvent.washEntityEvent(user, entity, user.getMainHandStack())) {
                    if (!user.world.isClient) {
                        user.setStackInHand(hand, ItemUsage.exchangeStack(stack, user, Items.BUCKET.getDefaultStack()));
                    }

                    this.playEmptyingSound(user, entity.getWorld(), entity.getBlockPos());

                    return ActionResult.SUCCESS;
                } else {
                    return ActionResult.FAIL;
                }
            }
        }


        return super.useOnEntity(stack, user, entity, hand);
    }
}