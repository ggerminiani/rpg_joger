package skills;

import entities.GameCharacter;
import utils.Dice;

public class PrecisionShot extends Skill {
    public PrecisionShot() { 
        super("Tiro Preciso", 5); 
    }

    @Override 
    public int use(GameCharacter user, GameCharacter target) {
         // Chance de crítico: 10% base + 5% por nível da skill
         boolean crit = Dice.roll(0, 100) < (10 + level * 5);
         
         int dmg = user.getAttack();
         
         if (crit) {
             System.out.println(">> CRÍTICO! (3x Dano)");
             return dmg * 3;
         } else {
             return dmg;
         }
    }
}