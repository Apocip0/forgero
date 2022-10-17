package com.sigmundgranaas.forgero.state;

import com.sigmundgranaas.forgero.util.match.Context;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.sigmundgranaas.forgero.identifier.Common.ELEMENT_SEPARATOR;
import static com.sigmundgranaas.forgero.type.Type.*;

public class NameCompositor {
    public String compositeName(List<State> ingredients) {
        return ingredients.stream()
                .sorted(Comparator.comparingInt(this::sorter))
                .map(this::mapper)
                .flatMap(Optional::stream)
                .collect(Collectors.joining(ELEMENT_SEPARATOR));
    }

    private int sorter(State ingredient) {
        if (ingredient.test(MATERIAL, Context.of())) {
            return 1;
        } else if (ingredient.test(SCHEMATIC, Context.of())) {
            return 2;
        }
        return 0;
    }

    private Optional<String> mapper(State ingredient) {
        if (ingredient.test(TOOL_PART_HEAD, Context.of())) {
            return Optional.of(ingredient.name().replace("_head", ""));

        } else if (ingredient.test(HANDLE, Context.of())) {
            return Optional.empty();

        } else if (ingredient.test(SCHEMATIC, Context.of())) {
            var elements = ingredient.name().split(ELEMENT_SEPARATOR);
            if (elements.length == 2) {
                return Optional.of(elements[0]);
            }
        }
        return Optional.of(ingredient.name());
    }
}
