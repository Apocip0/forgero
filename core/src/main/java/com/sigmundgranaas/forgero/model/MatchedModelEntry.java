package com.sigmundgranaas.forgero.model;

import com.sigmundgranaas.forgero.util.match.Context;
import com.sigmundgranaas.forgero.util.match.Matchable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class MatchedModelEntry implements ModelMatcher {
    List<ModelMatchPairing> models;
    String id;

    public MatchedModelEntry(List<ModelMatchPairing> models, String id) {
        this.models = models;
        this.id = id;
    }

    @Override
    public boolean match(Matchable state, Context context) {
        return models.stream().anyMatch(pair -> pair.match().test(state, context));

    }

    @Override
    public Optional<ModelTemplate> get(Matchable state, ModelProvider provider, Context context) {
        return models.stream()
                .filter(pairing -> pairing.match().test(state, context))
                .sorted()
                .map(ModelMatchPairing::model)
                .map(pairing -> pairing.get(state, provider, context))
                .filter(Optional::isPresent)
                .flatMap(Optional::stream)
                .findFirst();
    }

    @Override
    public int compareTo(@NotNull ModelMatcher o) {
        return 0;
    }
}
