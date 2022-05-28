package com.sigmundgranaas.forgero.core.material;

import com.sigmundgranaas.forgero.Constants;
import com.sigmundgranaas.forgero.core.LegacyForgeroRegistry;
import com.sigmundgranaas.forgero.core.identifier.tool.ForgeroMaterialIdentifierImpl;
import com.sigmundgranaas.forgero.core.identifier.tool.ForgeroToolIdentifierImpl;
import com.sigmundgranaas.forgero.core.identifier.tool.ForgeroToolPartIdentifierImpl;
import net.minecraft.item.ToolMaterial;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MaterialCollectionImplTest {

    @Test
    void LoadMaterialsAsList() {
        assert (LegacyForgeroRegistry.getInstance().materialCollection().getMaterialsAsList().size() > 0);
    }

    @Test
    void LoadMaterialsAsMap() {

        assert (LegacyForgeroRegistry.getInstance().materialCollection().getMaterialsAsList().size() > 0);
    }

    @Test
    void getPrimaryMaterialsAsList() {
        LegacyForgeroRegistry.getInstance().materialCollection().getPrimaryMaterialsAsList().forEach(Assertions::assertNotNull);
    }

    @Test
    void getSecondaryMaterialsAsList() {
        LegacyForgeroRegistry.getInstance().materialCollection().getSecondaryMaterialsAsList().forEach(Assertions::assertNotNull);
    }

    @Test
    void getMaterialFromToolIdentifier() {
        LegacyForgeroRegistry.getInstance().materialCollection().getMaterial(new ForgeroToolIdentifierImpl(Constants.EXAMPLE_TOOL_IDENTIFIER));

    }

    @Test
    void getMaterialFromToolPartIdentifier() {
        LegacyForgeroRegistry.getInstance().materialCollection().getMaterial(new ForgeroToolPartIdentifierImpl(Constants.IRON_PICKAXEHEAD_IDENTIFIER));

    }

    @Test
    void getMaterialFromMaterialIdentifier() {
        LegacyForgeroRegistry.getInstance().materialCollection().getMaterial(new ForgeroMaterialIdentifierImpl(Constants.IRON_IDENTIFIER_STRING));
    }

    // Core modules, like material.json, should not depend on classes from Minecraft.
    @Test
    void assertMaterialsIsNotToolMaterial() {
        LegacyForgeroRegistry.getInstance().materialCollection().getMaterialsAsList().forEach(material -> Assertions.assertFalse(material instanceof ToolMaterial));
    }
}