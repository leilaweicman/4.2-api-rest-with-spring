package cat.itacademy.s04.s02.n01.fruit.services;

import cat.itacademy.s04.s02.n01.fruit.exception.InvalidFruitNameException;
import cat.itacademy.s04.s02.n01.fruit.exception.InvalidFruitWeightException;
import cat.itacademy.s04.s02.n01.fruit.model.Fruit;
import cat.itacademy.s04.s02.n01.fruit.repository.FruitRepository;
import org.springframework.stereotype.Service;

@Service
public class FruitServiceImpl implements FruitService {

    private final FruitRepository fruitRepository;

    public FruitServiceImpl(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository;
    }

    @Override
    public Fruit createFruit(Fruit fruit) {
        if (fruit.getName() == null || fruit.getName().isBlank()) {
            throw new InvalidFruitNameException("Fruit name cannot be empty");
        }
        if (fruit.getWeight() <= 0) {
            throw new InvalidFruitWeightException("Fruit weight must be positive");
        }

        return fruitRepository.save(fruit);
    }

}
