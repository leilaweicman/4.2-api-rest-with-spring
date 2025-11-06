package cat.itacademy.s04.s02.n01.fruit.controllers;

import cat.itacademy.s04.s02.n01.fruit.dto.FruitDTO;
import cat.itacademy.s04.s02.n01.fruit.model.Fruit;
import cat.itacademy.s04.s02.n01.fruit.services.FruitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fruits")
public class FruitController {

    private final FruitService fruitService;

    @Autowired
    public FruitController(FruitService fruitService) {
        this.fruitService = fruitService;
    }

    @PostMapping
    public ResponseEntity<FruitDTO> createFruit(@Valid @RequestBody FruitDTO fruitDTO) {
        Fruit fruit = new Fruit(fruitDTO.getName(), fruitDTO.getWeight());
        Fruit created = fruitService.createFruit(fruit);
        FruitDTO response = new FruitDTO(created.getName(), created.getWeight());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<FruitDTO>> getAllFruits() {
        List<FruitDTO> fruits = fruitService.getAllFruits().stream()
                .map(f -> new FruitDTO(f.getName(), f.getWeight()))
                .toList();
        return ResponseEntity.ok(fruits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FruitDTO> getFruitById(@PathVariable Long id) {
        Fruit fruit = fruitService.getFruitById(id);
        FruitDTO response = new FruitDTO(fruit.getName(), fruit.getWeight());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FruitDTO> updateFruit(@PathVariable Long id, @RequestBody FruitDTO fruitDTO) {
        Fruit updatedFruit = fruitService.updateFruit(id, new Fruit(fruitDTO.getName(), fruitDTO.getWeight()));
        FruitDTO response = new FruitDTO(updatedFruit.getName(), updatedFruit.getWeight());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFruit(@PathVariable Long id) {
        fruitService.deleteFruit(id);
        return ResponseEntity.noContent().build();
    }
}
