package entities;

import skills.PoisonArrow;
import skills.PrecisionShot;

public class Archer extends Player {
    public Archer(String name) {
        // Nome, HP, MP, Atk
        super(name, 35, 30, 7);
        
        // Adiciona a habilidade específica
        this.skills.add(new PrecisionShot());
        skills.add(new PoisonArrow());
    }
}