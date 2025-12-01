package skills;

import entities.GameCharacter;
import java.io.Serializable;

public abstract class Skill implements Serializable {
    // Nome da Skill
    protected String name;
    // Nível da Skill
    protected int level = 1;
    // Custo de mana para usar
    protected int manaCost;

    public Skill(String name, int manaCost) { 
        this.name = name; 
        this.manaCost = manaCost;
    }
    
    public String getName() { return name; }
    public int getLevel() { return level; } 
    public int getManaCost() { return manaCost; }
    // Nível máximo = 10
    public void upgrade() {
        if (level < 10) level++;
    }

    public abstract int use(GameCharacter user, GameCharacter target);
}