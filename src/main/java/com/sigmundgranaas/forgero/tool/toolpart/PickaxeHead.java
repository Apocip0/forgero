package com.sigmundgranaas.forgero.tool.toolpart;

import com.sigmundgranaas.forgero.item.forgerotool.toolpart.ForgeroToolPartTypes;
import com.sigmundgranaas.forgero.material.material.PrimaryMaterial;
import com.sigmundgranaas.forgero.material.material.SecondaryMaterial;

import java.util.Locale;

public class PickaxeHead extends AbstractToolPartHead {
    public PickaxeHead(PrimaryMaterial primaryMaterial, SecondaryMaterial secondaryMaterial) {
        super(primaryMaterial, secondaryMaterial);
    }

    public PickaxeHead(PrimaryMaterial primaryMaterial) {
        super(primaryMaterial);
    }

    @Override
    public int getWeight() {
        return getPrimaryMaterial().getWeight();
    }

    @Override
    public float getWeightScale() {
        return 0;
    }

    @Override
    public int getDurability() {
        return getPrimaryMaterial().getDurability();
    }

    @Override
    public int getDurabilityScale() {
        return 0;
    }

    @Override
    public String getToolTypeName() {
        return "pickaxe";
    }

    @Override
    public String getToolPartName() {
        return ForgeroToolPartTypes.PICKAXEHEAD.toString().toLowerCase(Locale.ROOT);
    }

    @Override
    public ForgeroToolPartTypes getToolpartType() {
        return ForgeroToolPartTypes.PICKAXEHEAD;
    }

    @Override
    public int getSharpness() {
        return getPrimaryMaterial().getSharpness() / 2;
    }
}
