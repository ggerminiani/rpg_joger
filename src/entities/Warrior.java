package entities;

import skills.HeavyStrike;
import skills.IronSkin; 

public class Warrior extends Player {
    public Warrior(String name) {
        // Nome, HP, MP, Atk
        super(name, 50, 15, 6);
        
        // Adiciona a habilidade específica
        skills.add(new HeavyStrike());
        skills.add(new IronSkin());
    }
}