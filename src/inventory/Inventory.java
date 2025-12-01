package inventory;

import items.Item;
import exceptions.InventoryFullException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Inventory<T extends Item> implements Serializable {
    private List<T> items;
    private int capacity = 10;

    public Inventory(int capacity) {
        this.capacity = capacity;
        this.items = new ArrayList<>();
    }

    public void add(T item) throws InventoryFullException {
        if (items.size() >= capacity) {
            throw new InventoryFullException();
        }
        items.add(item);
    }

    public void remove(T item) { items.remove(item); }
    public List<T> getItems() { return items; }
    
    public T findByName(String name) {
        return items.stream()
            .filter(i -> i.getName().equalsIgnoreCase(name))
            .findFirst().orElse(null);
    }
}