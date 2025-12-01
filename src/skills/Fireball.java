package skills;

import entities.GameCharacter;

public class Fireball extends Skill {
    public Fireball() { 
        super("Bola de Fogo", 6); 
    }

    @Override 
    public int use(GameCharacter user, GameCharacter target) {
        // Mago causa muito dano baseado no ataque mágico
        return (user.getAttack() * 2) + (level * 3); 
    }
}