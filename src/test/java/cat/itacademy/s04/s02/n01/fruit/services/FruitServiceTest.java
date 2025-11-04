package cat.itacademy.s04.s02.n01.fruit.services;

import cat.itacademy.s04.s02.n01.fruit.exception.InvalidFruitNameException;
import cat.itacademy.s04.s02.n01.fruit.exception.InvalidFruitWeightException;
import cat.itacademy.s04.s02.n01.fruit.model.Fruit;
import cat.itacademy.s04.s02.n01.fruit.repository.FruitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FruitServiceTest {

    @Mock
    private FruitRepository fruitRepository;

    @InjectMocks
    private FruitServiceImpl fruitService;

    @Test
    void createFruit_shouldAddFruit_whenValidData() {
        Fruit fruit = new Fruit("Banana", 3);

        when(fruitRepository.save(any(Fruit.class))).thenReturn(fruit);

        Fruit result = fruitService.createFruit(fruit);

        assertEquals("Banana", result.getName());
        assertEquals(3, result.getWeight());

        verify(fruitRepository, times(1)).save(any(Fruit.class));

    }

    @Test
    void createFruit_shouldThrowException_whenNameIsEmpty() {
        Fruit fruit = new Fruit("", 10);

        assertThrows(InvalidFruitNameException.class, () -> fruitService.createFruit(fruit));
        verify(fruitRepository, never()).save(any());
    }

    @Test
    void createFruit_shouldThrowException_whenWeightIsInvalid() {
        Fruit fruit = new Fruit("Banana", -5);

        assertThrows(InvalidFruitWeightException.class, () -> fruitService.createFruit(fruit));
        verify(fruitRepository, never()).save(any());
    }
}
