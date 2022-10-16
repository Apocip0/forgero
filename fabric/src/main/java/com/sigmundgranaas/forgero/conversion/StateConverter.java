package com.sigmundgranaas.forgero.conversion;

import com.sigmundgranaas.forgero.ForgeroStateRegistry;
import com.sigmundgranaas.forgero.item.StateItem;
import com.sigmundgranaas.forgero.state.State;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Optional;

import static com.sigmundgranaas.forgero.item.nbt.v2.CompoundParser.COMPOSITE_PARSER;
import static com.sigmundgranaas.forgero.item.nbt.v2.NbtConstants.FORGERO_IDENTIFIER;

public interface StateConverter {
    static Optional<? extends State> of(ItemStack stack) {
        if (stack.hasNbt() && stack.getOrCreateNbt().contains(FORGERO_IDENTIFIER)) {
            return COMPOSITE_PARSER.parse(stack.getOrCreateNbt().getCompound(FORGERO_IDENTIFIER));
        } else {
            return of(stack.getItem());
        }
    }

    static Optional<? extends State> of(Item item) {
        String id = Registry.ITEM.getId(item).toString();
        var stateFromId = ForgeroStateRegistry.STATES.get(id);
        if (stateFromId.isPresent()) {
            return stateFromId;
        } else if (ForgeroStateRegistry.CONTAINER_TO_STATE.containsKey(id)) {
            return ForgeroStateRegistry.STATES.get(ForgeroStateRegistry.CONTAINER_TO_STATE.get(id));
        } else {
            if (item instanceof StateItem stateItem) {
                return Optional.of(stateItem.defaultState());
            }
        }
        return Optional.empty();
    }

    static Optional<? extends State> of(Identifier id) {
        return Optional.empty();
    }
}
