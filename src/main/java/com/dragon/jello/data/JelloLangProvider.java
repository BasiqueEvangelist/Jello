package com.dragon.jello.data;

import com.dragon.forge.LanguageProvider;
import com.dragon.jello.Jello;
import com.dragon.jello.blocks.BlockRegistry;
import com.dragon.jello.items.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class JelloLangProvider extends LanguageProvider {

    public JelloLangProvider(DataGenerator gen) {
        super(gen, Jello.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {

        BlockRegistry.SlimeBlockRegistry.SLIME_BLOCKS.forEach((block) -> {
            addBlock(() -> block);
        });

        ItemRegistry.SlimeBlockItemRegistry.SLIME_BALLS.forEach((item) -> {
            addItem(() -> item);
        });
    }

    //-----------------------------------------------//

    private void addItem(Supplier<? extends Item> item) {
        addItem(item, getAutomaticNameItem(item));
    }

    private void addBlock(Supplier<? extends Block> block) {
        addBlock(block, getAutomaticNameBlock(block));
    }

    private void addEntityType(Supplier<? extends EntityType<?>> entity) {
        addEntityType(entity, getAutomaticNameEntityType(entity));
    }

    private String getAutomaticNameItem(Supplier<? extends Item> sup) {
        return toEnglishName(Registry.ITEM.getId(sup.get()).getPath());
    }

    private String getAutomaticNameBlock(Supplier<? extends Block> sup) {
        return toEnglishName(Registry.BLOCK.getId(sup.get()).getPath());
    }

    private String getAutomaticNameEntityType(Supplier<? extends EntityType<?>> sup) {
        return toEnglishName(Registry.ENTITY_TYPE.getId(sup.get()).getPath());
    }

    public static final String toEnglishName(String internalName) {
        return Arrays.stream(internalName.toLowerCase(Locale.ROOT).split("_"))
                .map(StringUtils::capitalize)
                .collect(Collectors.joining(" "));
    }
}