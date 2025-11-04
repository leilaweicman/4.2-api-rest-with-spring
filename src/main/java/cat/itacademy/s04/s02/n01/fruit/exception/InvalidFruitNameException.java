package cat.itacademy.s04.s02.n01.fruit.exception;

public class InvalidFruitNameException extends RuntimeException {
    public InvalidFruitNameException(String message) {
        super(message);
    }
}