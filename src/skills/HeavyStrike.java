package skills;

import entities.GameCharacter;

public class HeavyStrike extends Skill {
    public HeavyStrike() { 
        super("Golpe Pesado", 5); 
    }

    @Override 
    public int use(GameCharacter user, GameCharacter target) {
        // Dano base + (nível da skill * 2)
        // Convertendo para int pois o cálculo com double (1.5) retorna double
        return (int) (user.getAttack() * 1.5) + (level * 2);
    }
}