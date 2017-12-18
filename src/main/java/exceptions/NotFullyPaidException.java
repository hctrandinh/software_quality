package exceptions;

public final class NotFullyPaidException extends Exception {
    private final int remaining;

    public NotFullyPaidException(String msg, int remaining) {
        super(msg);
        this.remaining = remaining;
    }

    public double getRemaining() {
        return remaining;
    }
}
