package cat.itacademy.s04.s02.n01.fruit.services;

import cat.itacademy.s04.s02.n01.fruit.model.Fruit;

import java.util.List;

public interface FruitService {
    Fruit createFruit(Fruit fruit);
    List<Fruit> getAllFruits();
    Fruit getFruitById(Long id);
}
