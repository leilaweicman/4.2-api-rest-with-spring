package cat.itacademy.s04.s02.n01.fruit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class FruitDTO {

    @NotBlank(message = "Fruit name cannot be blank")
    private String name;

    @Positive(message = "Fruit weight must be positive")
    private int weight;

    public FruitDTO() {
    }

    public FruitDTO(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}