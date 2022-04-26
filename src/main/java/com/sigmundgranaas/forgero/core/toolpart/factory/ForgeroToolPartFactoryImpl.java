package com.sigmundgranaas.forgero.core.toolpart.factory;

import com.sigmundgranaas.forgero.core.ForgeroRegistry;
import com.sigmundgranaas.forgero.core.gem.EmptyGem;
import com.sigmundgranaas.forgero.core.identifier.tool.ForgeroToolPartIdentifier;
import com.sigmundgranaas.forgero.core.material.MaterialCollection;
import com.sigmundgranaas.forgero.core.material.material.EmptySecondaryMaterial;
import com.sigmundgranaas.forgero.core.material.material.PrimaryMaterial;
import com.sigmundgranaas.forgero.core.schematic.HeadSchematic;
import com.sigmundgranaas.forgero.core.schematic.Schematic;
import com.sigmundgranaas.forgero.core.schematic.SchematicCollection;
import com.sigmundgranaas.forgero.core.toolpart.ForgeroToolPart;
import com.sigmundgranaas.forgero.core.toolpart.binding.Binding;
import com.sigmundgranaas.forgero.core.toolpart.binding.BindingState;
import com.sigmundgranaas.forgero.core.toolpart.binding.ToolPartBinding;
import com.sigmundgranaas.forgero.core.toolpart.handle.Handle;
import com.sigmundgranaas.forgero.core.toolpart.handle.HandleState;
import com.sigmundgranaas.forgero.core.toolpart.handle.ToolPartHandle;
import com.sigmundgranaas.forgero.core.toolpart.head.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ForgeroToolPartFactoryImpl implements ForgeroToolPartFactory {
    private static ForgeroToolPartFactory INSTANCE;

    public static ForgeroToolPartFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ForgeroToolPartFactoryImpl();
        }
        return INSTANCE;
    }

    @Override
    public @NotNull
    ForgeroToolPart createToolPart(@NotNull ForgeroToolPartIdentifier identifier) {
        PrimaryMaterial material = (PrimaryMaterial) ForgeroRegistry.getInstance().materialCollection().getMaterial(identifier.getMaterial());
        Schematic pattern = ForgeroRegistry.getInstance().schematicCollection().getSchematics().stream().filter((Schematic element) -> element.getSchematicIdentifier().equals(identifier.getPattern().identifier())).findFirst().get();

        return switch (identifier.getToolPartType()) {
            case HEAD -> createToolPartHead(material, (HeadSchematic) pattern);
            case HANDLE -> new Handle(new HandleState(material, new EmptySecondaryMaterial(), EmptyGem.createEmptyGem(), pattern));
            case BINDING -> new Binding(new BindingState(material, new EmptySecondaryMaterial(), EmptyGem.createEmptyGem(), pattern));
        };
    }

    private ToolPartHead createToolPartHead(PrimaryMaterial material, @NotNull HeadSchematic pattern) {
        return switch (pattern.getToolType()) {
            case PICKAXE -> new PickaxeHead(new HeadState(material, new EmptySecondaryMaterial(), EmptyGem.createEmptyGem(), pattern));
            case SHOVEL -> new ShovelHead(new HeadState(material, new EmptySecondaryMaterial(), EmptyGem.createEmptyGem(), pattern));
            case AXE -> new AxeHead(new HeadState(material, new EmptySecondaryMaterial(), EmptyGem.createEmptyGem(), pattern));
            case SWORD -> null;
        };
    }

    @Override
    public @NotNull
    ToolPartHeadBuilder createToolPartHeadBuilder(@NotNull PrimaryMaterial material, HeadSchematic pattern) {
        return new ToolPartHeadBuilder(material, pattern);
    }

    @Override
    public @NotNull
    ToolPartHandleBuilder createToolPartHandleBuilder(@NotNull PrimaryMaterial material, @NotNull Schematic pattern) {
        return new ToolPartHandleBuilder(material, pattern);
    }

    @Override
    public @NotNull
    ToolPartBindingBuilder createToolPartBindingBuilder(@NotNull PrimaryMaterial material, @NotNull Schematic pattern) {
        return new ToolPartBindingBuilder(material, pattern);
    }

    @Override
    public @NotNull
    ToolPartBuilder createToolPartBuilderFromToolPart(@NotNull ForgeroToolPart toolPart) {
        return switch (toolPart.getToolPartType()) {
            case HEAD -> new ToolPartHeadBuilder((ToolPartHead) toolPart);
            case BINDING -> new ToolPartBindingBuilder((ToolPartBinding) toolPart);
            case HANDLE -> new ToolPartHandleBuilder((ToolPartHandle) toolPart);
        };
    }


    @Override
    public @NotNull
    List<ForgeroToolPart> createBaseToolParts(@NotNull MaterialCollection collection, SchematicCollection patternCollection) {
        return collection
                .getPrimaryMaterialsAsList()
                .stream()
                .map(material -> patternCollection
                        .getSchematics()
                        .stream()
                        .map(pattern -> createBaseToolPartsFromMaterial(material, pattern))
                        .flatMap(List::stream).toList())
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<ForgeroToolPart> createBaseToolPartsFromMaterial(PrimaryMaterial material, Schematic pattern) {
        List<ForgeroToolPart> toolParts = new ArrayList<>();
        switch (pattern.getType()) {
            case HEAD -> {
                switch (((HeadSchematic) pattern).getToolType()) {
                    case AXE -> toolParts.add(new AxeHead(new HeadState(material, new EmptySecondaryMaterial(), EmptyGem.createEmptyGem(), pattern)));
                    case SHOVEL -> toolParts.add(new ShovelHead(new HeadState(material, new EmptySecondaryMaterial(), EmptyGem.createEmptyGem(), pattern)));
                    case PICKAXE -> toolParts.add(new PickaxeHead(new HeadState(material, new EmptySecondaryMaterial(), EmptyGem.createEmptyGem(), pattern)));
                    default -> {
                    }
                }
            }
            case HANDLE -> toolParts.add(new Handle(new HandleState(material, new EmptySecondaryMaterial(), EmptyGem.createEmptyGem(), pattern)));
            case BINDING -> toolParts.add(new Binding(new BindingState(material, new EmptySecondaryMaterial(), EmptyGem.createEmptyGem(), pattern)));
        }
        return toolParts;
    }
}