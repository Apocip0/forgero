package com.sigmundgranaas.forgero.item.implementation;

import com.sigmundgranaas.forgero.core.property.attribute.Target;
import com.sigmundgranaas.forgero.core.schematic.Schematic;
import com.sigmundgranaas.forgero.core.tool.ForgeroTool;
import com.sigmundgranaas.forgero.core.toolpart.ForgeroToolPart;
import com.sigmundgranaas.forgero.item.ItemFactory;
import com.sigmundgranaas.forgero.item.ItemGroups;
import com.sigmundgranaas.forgero.item.adapter.DescriptionWriter;
import com.sigmundgranaas.forgero.item.adapter.SimpleToolMaterialAdapter;
import com.sigmundgranaas.forgero.item.items.SchematicItem;
import com.sigmundgranaas.forgero.item.items.ToolPartItemImpl;
import com.sigmundgranaas.forgero.item.items.tool.ForgeroAxeItem;
import com.sigmundgranaas.forgero.item.items.tool.ForgeroPickaxeItem;
import com.sigmundgranaas.forgero.item.items.tool.ShovelItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ItemFactoryImpl implements ItemFactory {
    public static ItemFactory INSTANCE;

    public static ItemFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ItemFactoryImpl();
        }
        return INSTANCE;
    }

    @Override
    public Item createTool(ForgeroTool tool) {
        Target target = Target.createEmptyTarget();
        return switch (tool.getToolType()) {
            case PICKAXE -> new ForgeroPickaxeItem(new SimpleToolMaterialAdapter(tool.getMaterial()), new Item.Settings().group(ItemGroup.TOOLS), tool);
            case SHOVEL -> new ShovelItem(new SimpleToolMaterialAdapter(tool.getMaterial()), new Item.Settings().group(ItemGroup.TOOLS), tool);
            case AXE -> new ForgeroAxeItem(new SimpleToolMaterialAdapter(tool.getMaterial()), new Item.Settings().group(ItemGroup.TOOLS), tool);
            case SWORD -> null;
        };
    }

    @Override
    public Item createToolPart(ForgeroToolPart toolPart) {
        return new ToolPartItemImpl(new Item.Settings().group(ItemGroups.FORGERO_TOOL_PARTS), toolPart.getPrimaryMaterial(), toolPart.getToolPartType(), toolPart);
    }

    @Override
    public SchematicItem createPattern(Schematic pattern) {
        return new SchematicItem(new Item.Settings().group(ItemGroups.FORGERO_TOOL_PARTS).rarity(DescriptionWriter.getRarityFromInt(pattern.getRarity())), pattern);
    }
}
