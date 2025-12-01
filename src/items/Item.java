package items;
import java.io.Serializable;

public abstract class Item implements Serializable {
    // Nome do item
    private String name;
    // Valor do item (em ouros)
    private int value;

    public Item(String name, int value) { 
        this.name = name; 
        this.value = value;
    }
    
    public String getName() { return name; }
    public int getValue() { return value; }
    public abstract String getDescription();
}