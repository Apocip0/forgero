package com.sigmundgranaas.forgero.core.toolpart.head;

import com.sigmundgranaas.forgero.core.tool.ForgeroToolTypes;
import com.sigmundgranaas.forgero.core.toolpart.ForgeroToolPartTypes;
import com.sigmundgranaas.forgero.core.toolpart.ReloadableToolPart;
import com.sigmundgranaas.forgero.core.toolpart.ToolPartDescriptionWriter;

import java.util.Locale;

public abstract class AbstractToolPartHead extends ReloadableToolPart implements ToolPartHead {
    public AbstractToolPartHead(HeadState state) {
        super(state);

    }

    @Override
    public String getToolPartName() {
        return getToolType().getToolName() + getToolPartType().toString().toLowerCase(Locale.ROOT);
    }

    public ForgeroToolPartTypes getToolPartType() {
        return ForgeroToolPartTypes.HEAD;
    }

    public String getToolTypeName() {
        return this.getToolType().getToolName();
    }

    public String getToolPartIdentifier() {
        return getPrimaryMaterial().getResourceName() + "_" + getToolPartName() + "_" + getSchematic().getVariant();
    }

    @Override
    public abstract ForgeroToolTypes getToolType();

    @Override
    public void createToolPartDescription(ToolPartDescriptionWriter writer) {
        super.createToolPartDescription(writer);
    }
}
