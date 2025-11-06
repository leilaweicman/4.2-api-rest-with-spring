package cat.itacademy.s04.s02.n01.fruit.services;

import cat.itacademy.s04.s02.n01.fruit.exception.FruitNotFoundException;
import cat.itacademy.s04.s02.n01.fruit.model.Fruit;
import cat.itacademy.s04.s02.n01.fruit.repository.FruitRepository;
import cat.itacademy.s04.s02.n01.fruit.validators.FruitValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FruitServiceImpl implements FruitService {

    private final FruitRepository fruitRepository;
    private final FruitValidator fruitValidator;

    @Autowired
    public FruitServiceImpl(FruitRepository fruitRepository, FruitValidator fruitValidator) {
        this.fruitRepository = fruitRepository;
        this.fruitValidator = fruitValidator;
    }

    @Override
    public Fruit createFruit(Fruit fruit) {
        fruitValidator.validate(fruit);
        return fruitRepository.save(fruit);
    }

    @Override
    public List<Fruit> getAllFruits() {
        return fruitRepository.findAll();
    }

    @Override
    public Fruit getFruitById(Long id) {
        return fruitRepository.findById(id)
                .orElseThrow(() -> new FruitNotFoundException(id));
    }

    @Override
    public Fruit updateFruit(Long id, Fruit fruit) {
        Fruit existing = getFruitById(id);
        fruitValidator.validate(fruit);
        existing.setName(fruit.getName());
        existing.setWeight(fruit.getWeight());

        return fruitRepository.save(existing);

    }

}
