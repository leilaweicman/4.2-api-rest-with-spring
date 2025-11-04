package cat.itacademy.s04.s02.n01.fruit.model;

public class Fruit {

    private final String name;
    private final int weight;

    public Fruit(String name, int weight) {

        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }
}
