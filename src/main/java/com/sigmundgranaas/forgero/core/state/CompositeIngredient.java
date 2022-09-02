package com.sigmundgranaas.forgero.core.state;

import com.sigmundgranaas.forgero.core.util.MatchContext;
import com.sigmundgranaas.forgero.core.util.Matchable;
import com.sigmundgranaas.forgero.core.util.Type;

public class CompositeIngredient extends Composite implements Ingredient {
    public CompositeIngredient(Composite composite) {
        super(composite.ingredients(), composite.upgrades(), composite.name(), composite.type());
    }

    @Override
    public boolean test(Matchable match) {
        if (match instanceof Type typeMatch) {
            if (this.type().test(typeMatch)) {
                return true;
            } else {
                return ingredients().stream().anyMatch(ingredient -> ingredient.test(match, MatchContext.COMPOSITE));
            }
        }
        return match.test(this);
    }
}
