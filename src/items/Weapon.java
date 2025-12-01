package items;

public class Weapon extends Item {
    // Dano adicional da arma
    private int damageBonus;

    public Weapon(String name, int damageBonus, int value) { // Value no final
        super(name, value);
        this.damageBonus = damageBonus;
    }

    public int getDamageBonus() { return damageBonus; }
    @Override public String getDescription() { return "Arma (+" + damageBonus + " Atk) | Valor: " + getValue() + "G"; }
}