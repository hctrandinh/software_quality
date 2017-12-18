package exceptions;

public final class SoldOutException extends Exception {
    public SoldOutException(String message) {
        super(message);
    }
}
