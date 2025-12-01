package exceptions;

public class InventoryFullException extends GameException {
    public InventoryFullException() { super("Inventário cheio!"); }
}