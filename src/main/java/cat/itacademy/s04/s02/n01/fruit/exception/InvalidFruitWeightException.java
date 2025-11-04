package cat.itacademy.s04.s02.n01.fruit.exception;

public class InvalidFruitWeightException extends RuntimeException {
    public InvalidFruitWeightException(String message) {
        super(message);
    }
}