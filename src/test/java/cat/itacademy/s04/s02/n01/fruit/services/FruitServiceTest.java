package cat.itacademy.s04.s02.n01.fruit.services;

import cat.itacademy.s04.s02.n01.fruit.exception.FruitNotFoundException;
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
import org.springframework.test.util.ReflectionTestUtils;

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
        ReflectionTestUtils.setField(fruit, "id", 1L);

        when(fruitRepository.findById(1L)).thenReturn(Optional.of(fruit));

        Fruit result = fruitService.getFruitById(1L);

        assertEquals("Banana", result.getName());
        assertEquals(3, result.getWeight());

        verify(fruitRepository).findById(1L);
    }

    @Test
    void getFruitById_shouldThrowException_whenNotFound() {
        when(fruitRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(FruitNotFoundException.class, () -> fruitService.getFruitById(99L));
        verify(fruitRepository).findById(99L);
    }

    @Test
    void updateFruit_shouldUpdateExistingFruit() {
        Fruit existingFruit = new Fruit("Apple", 3);
        ReflectionTestUtils.setField(existingFruit, "id", 1L);
        when(fruitRepository.findById(1L)).thenReturn(Optional.of(existingFruit));

        Fruit updatedFruit = new Fruit("Apple Updated", 15);

        when(fruitRepository.save(any(Fruit.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Fruit result = fruitService.updateFruit(1L, updatedFruit);

        assertEquals("Apple Updated", result.getName());
        assertEquals(15, result.getWeight());

        verify(fruitRepository).findById(1L);
        verify(fruitRepository).save(existingFruit);
    }

    @Test
    void updateFruit_shouldThrowException_whenFruitNotFound() {
        when(fruitRepository.findById(99L)).thenReturn(Optional.empty());

        Fruit update = new Fruit("Nonexistent", 5);

        assertThrows(FruitNotFoundException.class, () -> fruitService.updateFruit(99L, update));
        verify(fruitRepository, never()).save(any());
    }

    @Test
    void updateFruit_shouldThrowException_whenInvalidName() {
        Fruit existing = new Fruit("Mango", 8);
        ReflectionTestUtils.setField(existing, "id", 1L);
        when(fruitRepository.findById(1L)).thenReturn(Optional.of(existing));

        Fruit invalid = new Fruit("", 3);

        doThrow(new InvalidFruitNameException("Weight must be positive"))
                .when(fruitValidator).validate(invalid);

        assertThrows(InvalidFruitNameException.class, () -> fruitService.updateFruit(1L, invalid));
        verify(fruitRepository, never()).save(any());
    }

    @Test
    void updateFruit_shouldThrowException_whenInvalidWeight() {
        Fruit existing = new Fruit("Mango", 8);
        ReflectionTestUtils.setField(existing, "id", 1L);
        when(fruitRepository.findById(1L)).thenReturn(Optional.of(existing));

        Fruit invalid = new Fruit("Mango", -5);

        doThrow(new InvalidFruitWeightException("Weight must be positive"))
                .when(fruitValidator).validate(invalid);

        assertThrows(InvalidFruitWeightException.class, () -> fruitService.updateFruit(1L, invalid));
        verify(fruitRepository, never()).save(any());
    }

    @Test
    void deleteFruit_shouldReturnNoContent_whenExists() {

    }

    @Test
    void deleteFruit_shouldThrowException_whenNotFound() {

    }
}
