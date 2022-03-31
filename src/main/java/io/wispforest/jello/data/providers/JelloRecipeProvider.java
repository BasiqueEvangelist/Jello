package io.wispforest.jello.data.providers;

import io.wispforest.jello.api.dye.registry.DyeColorantRegistry;
import io.wispforest.jello.block.JelloBlocks;
import io.wispforest.jello.misc.ducks.DyeBlockStorage;
import io.wispforest.jello.Jello;
import io.wispforest.jello.data.JelloComplexRecipeJsonBuilder;
import io.wispforest.jello.data.tags.JelloTags;
import io.wispforest.jello.item.JelloItems;
import io.wispforest.jello.data.recipe.JelloRecipeSerializers;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipesProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Consumer;

public class JelloRecipeProvider extends FabricRecipesProvider {
    public JelloRecipeProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
        for (int i = 0; i < DyeColorantRegistry.Constants.VANILLA_DYES.size(); i++) {
            String slabPath = DyeColorantRegistry.Constants.VANILLA_DYES.get(i).getName() + "_slime_slab";
            Block slab = Registry.BLOCK.get(Jello.id(slabPath));
            //Block slab = BlockRegistry.SlimeSlabRegistry.COLORED_SLIME_SLABS.get(i);

            String blockPath = DyeColorantRegistry.Constants.VANILLA_DYES.get(i).getName() + "_slime_block";
            //SlimeBlockColored block = (SlimeBlockColored) BlockRegistry.SlimeBlockRegistry.COLORED_SLIME_BLOCKS.get(i);
            Block block = Registry.BLOCK.get(Jello.id(blockPath));

            Item item = JelloItems.Slimeballs.SLIME_BALLS.get(i);
            String itemPath = Registry.ITEM.getId(item).getPath();

            createSlabRecipe(slab, Ingredient.ofItems(block))
                    .criterion("has_" + Registry.BLOCK.getId(block).getPath(), conditionsFromItem(block))
                    .offerTo(exporter);

            offerReversibleCompactingRecipes(exporter, item, block);

            ShapedRecipeJsonBuilder.create(Blocks.STICKY_PISTON)
                    .input('P', Blocks.PISTON)
                    .input('S', item)
                    .pattern("S")
                    .pattern("P")
                    .criterion("has_slime_ball", conditionsFromItem(item))
                    .offerTo(exporter, Jello.id("sticky_piston_" + itemPath));

            ShapedRecipeJsonBuilder.create(Items.LEAD, 2)
                    .input('~', Items.STRING)
                    .input('O', item)
                    .pattern("~~ ")
                    .pattern("~O ")
                    .pattern("  ~")
                    .criterion("has_slime_ball", conditionsFromItem(item))
                    .offerTo(exporter, Jello.id("lead_" + itemPath));

            ShapelessRecipeJsonBuilder.create(Items.MAGMA_CREAM)
                    .input(Items.BLAZE_POWDER)
                    .input(item)
                    .criterion("has_blaze_powder", conditionsFromItem(Items.BLAZE_POWDER))
                    .offerTo(exporter, Jello.id("magma_cream" + itemPath));

            Item dyeItem = Registry.ITEM.get(new Identifier(((DyeBlockStorage) block).getDyeColor().getName() + "_dye"));
            String dyePath = ((DyeBlockStorage) block).getDyeColor().getName() + "_dye";

            offerSlimeBlockDyeingRecipe(exporter, block, dyeItem, blockPath, ((DyeBlockStorage) block).getDyeColor().getName() + "_dye");
            offerSlimeBlockDyeingFullRecipe(exporter, block, dyeItem, blockPath, ((DyeBlockStorage) block).getDyeColor().getName() + "_dye");

            offerSlimeSlabDyeingRecipe(exporter, slab, dyeItem, slabPath, ((DyeBlockStorage) block).getDyeColor().getName() + "_dye");
            offerSlimeSlabDyeingFullRecipe(exporter, slab, dyeItem, slabPath, ((DyeBlockStorage) block).getDyeColor().getName() + "_dye");

            offerSlimeBallDyeingRecipe(exporter, item, dyeItem, itemPath, dyePath);
        }

        ShapelessRecipeJsonBuilder.create(JelloItems.SPONGE)
                .input(Items.WET_SPONGE)
                .input(Items.SHEARS)
                .group("")
                .criterion("has_sponge_item", conditionsFromItem(Items.WET_SPONGE))
                .criterion("has_shears_item", conditionsFromItem(Items.SHEARS))
                .offerTo(exporter, Jello.id("sponge_item_from_wet_sponge"));

        ShapelessRecipeJsonBuilder.create(JelloItems.SPONGE)
                .input(Items.SPONGE)
                .input(Items.SHEARS)
                .group("")
                .criterion("has_sponge_item", conditionsFromItem(Items.SPONGE))
                .criterion("has_shears_item", conditionsFromItem(Items.SHEARS))
                .offerTo(exporter, Jello.id("sponge_item_from_dry_sponge"));

        JelloComplexRecipeJsonBuilder.create(JelloRecipeSerializers.ARTIST_PALETTE).offerTo(exporter, Jello.id("fill_artist_palette"));

        ShapelessRecipeJsonBuilder.create(JelloItems.EMPTY_ARTIST_PALETTE)
                .input(Items.SHEARS)
                .input(ItemTags.WOODEN_PRESSURE_PLATES)
                .criterion("has_shears_item", conditionsFromItem(Items.SHEARS))
                .offerTo(exporter, Jello.id("artist_palette"));

        ShapedRecipeJsonBuilder.create(JelloBlocks.PAINT_MIXER)
                .input('l', Items.LAPIS_LAZULI)
                .input('c', Blocks.CAULDRON)
                .input('p', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE)
                .pattern("lpl")
                .pattern("lcl")
                .pattern("lll")
                .criterion("has_cauldron", conditionsFromItem(Blocks.CAULDRON))
                .offerTo(exporter, Jello.id("paint_mixer"));
    }

    public static void offerSlimeBlockDyeingRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible output, ItemConvertible input, String blockPath, String dyePath) {
        ShapelessRecipeJsonBuilder.create(output)
                .input(input)
                .input(JelloTags.Items.SLIME_BLOCKS)
                .group("slime_block")
                .criterion("has_slime_block_var", conditionsFromTag(JelloTags.Items.SLIME_BLOCKS))
                .offerTo(exporter, Jello.id(blockPath + "_" + dyePath));
    }

    public static void offerSlimeBlockDyeingFullRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible output, ItemConvertible input, String blockPath, String dyePath) {
        ShapedRecipeJsonBuilder.create(output, 8)
                .input('#', JelloTags.Items.SLIME_BLOCKS)
                .input('X', input)
                .pattern("###")
                .pattern("#X#")
                .pattern("###")
                .group("slime_block")
                .criterion("has_slime_block_var", conditionsFromTag(JelloTags.Items.SLIME_BLOCKS))
                .offerTo(exporter, Jello.id(blockPath + "_" + dyePath + "_full"));
    }

    public static void offerSlimeSlabDyeingRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible output, ItemConvertible input, String blockPath, String dyePath) {
        ShapelessRecipeJsonBuilder.create(output)
                .input(input)
                .input(JelloTags.Items.SLIME_SLABS)
                .group("slime_block")
                .criterion("has_slime_block_var", conditionsFromTag(JelloTags.Items.SLIME_SLABS))
                .offerTo(exporter, Jello.id(blockPath + "_" + dyePath));
    }

    public static void offerSlimeSlabDyeingFullRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible output, ItemConvertible input, String blockPath, String dyePath) {
        ShapedRecipeJsonBuilder.create(output, 8)
                .input('#', JelloTags.Items.SLIME_SLABS)
                .input('X', input)
                .pattern("###")
                .pattern("#X#")
                .pattern("###")
                .group("slime_block")
                .criterion("has_slime_block_var", conditionsFromTag(JelloTags.Items.SLIME_SLABS))
                .offerTo(exporter, Jello.id(blockPath + "_" + dyePath + "_full"));
    }

    public static void offerSlimeBallDyeingRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible output, ItemConvertible input, String itemPath, String dyePath) {
        ShapelessRecipeJsonBuilder.create(output)
                .input(input)
                .input(JelloTags.Items.SLIME_BALLS)
                .group("slime_ball")
                .criterion("has_slime_block_var", conditionsFromTag(JelloTags.Items.SLIME_BLOCKS))
                .offerTo(exporter, Jello.id(itemPath + "_" + dyePath));
    }
}