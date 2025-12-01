package skills;

import entities.GameCharacter;

public class ArcaneExplosion extends Skill {
    public ArcaneExplosion() { super("Explosão Arcana", 25); }

    @Override
    public int use(GameCharacter user, GameCharacter target) {
        // Lógica do dano da Skill
        int dmg = (user.getAttack() * 3) + (level * 5);
        return dmg;
    }
}