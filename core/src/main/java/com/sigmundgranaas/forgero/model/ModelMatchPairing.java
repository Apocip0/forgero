package com.sigmundgranaas.forgero.model;

import com.sigmundgranaas.forgero.state.State;

import java.util.Optional;

public record ModelMatchPairing(ModelMatch match, ModelMatcher model) implements ModelMatcher {
    @Override
    public Optional<ModelTemplate> find(State state, ModelProvider provider) {
        if (match.test(state)) {
            return model.find(state, provider);
        }
        return Optional.empty();
    }
}
