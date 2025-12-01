package skills;
import entities.GameCharacter;

public class PoisonArrow extends Skill {
    public PoisonArrow() { super("Flecha Envenenada", 10); }

    @Override
    public int use(GameCharacter user, GameCharacter target) {
        // Dano baseado na Agilidade/Sorte (simulado aqui como fixo alto)
        return 15 + (level * 3);
    }
}