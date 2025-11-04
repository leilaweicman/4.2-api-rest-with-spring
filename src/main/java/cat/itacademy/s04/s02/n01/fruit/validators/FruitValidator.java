package cat.itacademy.s04.s02.n01.fruit.validators;

import cat.itacademy.s04.s02.n01.fruit.exception.InvalidFruitNameException;
import cat.itacademy.s04.s02.n01.fruit.exception.InvalidFruitWeightException;
import cat.itacademy.s04.s02.n01.fruit.model.Fruit;
import org.springframework.stereotype.Component;

@Component
public class FruitValidator {

    public void validate(Fruit fruit) {
        if (fruit == null) {
            throw new IllegalArgumentException("Fruit cannot be null");
        }

        if (fruit.getName() == null || fruit.getName().isBlank()) {
            throw new InvalidFruitNameException("Fruit name cannot be empty");
        }

        if (fruit.getWeight() <= 0) {
            throw new InvalidFruitWeightException("Fruit weight must be positive");
        }
    }
}
