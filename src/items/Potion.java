package items;

public class Potion extends Item {
    private int healAmount;
    
    public Potion(String name, int healAmount, int value) { // Value no final
        super(name, value);
        this.healAmount = healAmount;
    }
    
    public int getHealAmount() { return healAmount; }
    @Override public String getDescription() { return "Cura " + healAmount + " HP | Valor: " + getValue() + "G"; }
}