package entities;

import skills.ArcaneExplosion;
import skills.Fireball;

public class Mage extends Player {
    public Mage(String name) {
        // Nome, Vida, Estamina, Ataque base
        super(name, 25, 60, 9);
        
        // Adiciona as habilidades específicas
        this.skills.add(new Fireball());
        skills.add(new ArcaneExplosion());
    }
}