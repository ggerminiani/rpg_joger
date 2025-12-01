package skills;
import entities.GameCharacter;

public class IronSkin extends Skill {
    public IronSkin() { super("Pele de Ferro", 15); } // Custa 15 MP

    @Override
    public int use(GameCharacter user, GameCharacter target) {
        // Aumenta defesa permanentemente (ou poderia ser temporário com lógica de buff)
        // Para simplificar: cura um pouco e aumenta defesa base em 1
        user.heal(10);
        user.setDefense(user.getDefense() + 1);
        System.out.println(">> Buff: Defesa aumentada em +1!");
        return 0; // Não causa dano
    }
}