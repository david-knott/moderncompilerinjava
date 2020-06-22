package Core;

public interface Listener<T> {
    public void handle(T message);
}