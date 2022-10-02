package com.sigmundgranaas.forgero.state;

import com.google.common.collect.ImmutableList;
import com.sigmundgranaas.forgero.Forgero;
import com.sigmundgranaas.forgero.property.Property;
import com.sigmundgranaas.forgero.property.PropertyContainer;
import com.sigmundgranaas.forgero.type.Type;
import com.sigmundgranaas.forgero.util.match.MatchContext;
import com.sigmundgranaas.forgero.util.match.Matchable;
import com.sigmundgranaas.forgero.util.match.NameMatch;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("ClassCanBeRecord")
public class Composite implements Upgradeable<Composite> {
    private final List<State> ingredientList;
    private final ImmutableList<State> upgrades;
    private final String name;
    private final String nameSpace;
    private final Type type;

    protected Composite(List<State> ingredientList, ImmutableList<State> upgrades, String name, Type type) {
        this.name = name;
        this.ingredientList = ingredientList;
        this.upgrades = upgrades;
        this.type = type;
        this.nameSpace = Forgero.NAMESPACE;
    }

    protected Composite(List<State> ingredientList, ImmutableList<State> upgrades, String name, String nameSpace, Type type) {
        this.name = name;
        this.ingredientList = ingredientList;
        this.upgrades = upgrades;
        this.type = type;
        this.nameSpace = nameSpace;
    }

    public static CompositeBuilder builder() {
        return new CompositeBuilder();
    }

    @Override
    @NotNull
    public String name() {
        return name;
    }

    @Override
    public String nameSpace() {
        return nameSpace;
    }

    @Override
    @NotNull
    public Type type() {
        return type;
    }

    @Override
    public @NotNull List<Property> getProperties() {
        return Stream.of(ingredientList, upgrades)
                .flatMap(List::stream)
                .map(PropertyContainer::getProperties)
                .flatMap(List::stream)
                .toList();
    }

    @Override
    public boolean test(Matchable match) {
        return test(match, MatchContext.COMPOSITE);
    }

    @Override
    public boolean test(Matchable match, MatchContext context) {
        if (match instanceof Type typeMatch) {
            if (this.type().test(typeMatch)) {
                return true;
            } else {
                return ingredientList.stream().anyMatch(ingredient -> ingredient.test(match, MatchContext.COMPOSITE));
            }

        }
        if (match instanceof NameMatch name) {
            if (name.test(this)) {
                return true;
            } else {
                return ingredientList.stream().anyMatch(name::test);
            }
        }
        return match.test(this, context);
    }

    public List<State> ingredients() {
        return ingredientList;
    }

    @Override
    public Composite upgrade(State upgrade) {
        var upgrades = ImmutableList.<State>builder().addAll(upgrades()).add(upgrade).build();
        return builder()
                .addIngredients(ingredients())
                .addUpgrades(upgrades)
                .type(type())
                .name(name())
                .build();
    }

    @Override
    public ImmutableList<State> upgrades() {
        return upgrades;
    }

    public static class CompositeBuilder {
        private final List<State> ingredientList;
        private final List<State> upgrades;
        private final NameCompositor compositor = new NameCompositor();
        private Type type = Type.UNDEFINED;
        private String name;
        private String nameSpace = Forgero.NAMESPACE;

        public CompositeBuilder() {
            this.ingredientList = new ArrayList<>();
            this.upgrades = new ArrayList<>();
        }

        public CompositeBuilder addIngredient(State ingredient) {
            ingredientList.add(ingredient);
            return this;
        }

        public CompositeBuilder addIngredients(List<State> ingredients) {
            ingredientList.addAll(ingredients);
            return this;
        }

        public CompositeBuilder addUpgrade(State upgrade) {
            upgrades.add(upgrade);
            return this;
        }

        public CompositeBuilder addUpgrades(ImmutableList<State> upgrades) {
            this.upgrades.addAll(upgrades);
            return this;
        }

        public CompositeBuilder type(Type type) {
            this.type = type;
            return this;
        }

        public CompositeBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CompositeBuilder nameSpace(String nameSpace) {
            this.nameSpace = nameSpace;
            return this;
        }

        public Composite build() {
            if (name == null) {
                this.name = compositor.compositeName(ingredientList);
            }
            return new Composite(ingredientList, ImmutableList.<State>builder().addAll(upgrades).build(), name, nameSpace, type);
        }
    }
}
