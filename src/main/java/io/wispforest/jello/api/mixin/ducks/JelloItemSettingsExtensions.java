package io.wispforest.jello.api.mixin.ducks;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.Map;

public interface JelloItemSettingsExtensions {

    Map<Identifier, Item> getRecipeSpecificRemainder();

    Item.Settings addRecipeSpecificRemainder(Identifier identifier, Item item);
}
