package com.dragon.jello.mixin.mixins;

import com.dragon.jello.events.DeColorizeCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MilkBucketItem.class)
public class MilkBucketItemMixin {

    @Inject(method = "finishUsing", at = @At(value = "HEAD"), cancellable = true)
    private void deColorizePlayer(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir){
        if(!world.isClient) {
            if (!DeColorizeCallback.EVENT.invoker().finishUsing(stack, world, user)) {
                cir.setReturnValue(stack.isEmpty() ? new ItemStack(Items.BUCKET) : stack);
            }
        }
    }
}
