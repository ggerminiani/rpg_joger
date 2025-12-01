package items;

public class ManaPotion extends Item {
    private int restoreAmount;

    public ManaPotion(String name, int restoreAmount, int value) {
        super(name, value);
        this.restoreAmount = restoreAmount;
    }

    public int getRestoreAmount() { return restoreAmount; }
    
    @Override 
    public String getDescription() { 
        return "Recupera " + restoreAmount + " MP | Valor: " + getValue() + "G"; 
    }
}