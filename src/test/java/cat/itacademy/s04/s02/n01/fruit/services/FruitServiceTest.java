package cat.itacademy.s04.s02.n01.fruit.services;

import cat.itacademy.s04.s02.n01.fruit.exception.InvalidFruitNameException;
import cat.itacademy.s04.s02.n01.fruit.exception.InvalidFruitWeightException;
import cat.itacademy.s04.s02.n01.fruit.model.Fruit;
import cat.itacademy.s04.s02.n01.fruit.repository.FruitRepository;
import cat.itacademy.s04.s02.n01.fruit.validators.FruitValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FruitServiceTest {

    @Mock
    private FruitRepository fruitRepository;
    @Mock
    private FruitValidator fruitValidator;
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

        doThrow(new InvalidFruitNameException("Fruit name cannot be empty"))
                .when(fruitValidator).validate(fruit);

        assertThrows(InvalidFruitNameException.class, () -> fruitService.createFruit(fruit));
        verify(fruitRepository, never()).save(any());
    }

    @Test
    void createFruit_shouldThrowException_whenWeightIsInvalid() {
        Fruit fruit = new Fruit("Banana", -5);

        doThrow(new InvalidFruitWeightException("Weight must be positive"))
                .when(fruitValidator).validate(fruit);

        assertThrows(InvalidFruitWeightException.class, () -> fruitService.createFruit(fruit));
        verify(fruitRepository, never()).save(any());
    }

    @Test
    void getAllFruits_shouldReturnEmptyList_whenNoFruitsExist() {
        when(fruitRepository.findAll()).thenReturn(List.of());

        List<Fruit> fruits = fruitService.getAllFruits();

        assertTrue(fruits.isEmpty());
        verify(fruitRepository).findAll();
    }

    @Test
    void getAllFruits_shouldReturnAllFruits_whenTheyExist() {
        List<Fruit> mockFruits = List.of(
                new Fruit("Apple", 10),
                new Fruit("Banana", 5)
        );
        when(fruitRepository.findAll()).thenReturn(mockFruits);

        List<Fruit> fruits = fruitService.getAllFruits();

        assertEquals(2, fruits.size());
        assertEquals("Apple", fruits.get(0).getName());
        verify(fruitRepository).findAll();
    }

    @Test
    void getFruitById_shouldReturnFruit_whenExists() {
        Fruit fruit = new Fruit("Banana", 3);
        fruit.setId(1L);
        when(fruitRepository.findById(1L)).thenReturn(Optional.of(fruit));

        Fruit result = fruitService.getFruitById(1L);

        assertEquals("Banana", result.getName());
        assertEquals(3, result.getWeight());

        verify(fruitRepository).findById(1L);
    }

    @Test
    void getFruitById_shouldThrowException_whenNotFound() {
        when(fruitRepository.findById(anyLong())).thenReturn(Optional.empty());

        fruitService.getFruitById(1L);

        assertThrows(FruitNotFoundException.class, () -> fruitService.getFruitById(99L));
        verify(fruitRepository).findById(99L);
    }
}
