package items;

public class StatElixir extends Item {
    // Tipo de status: 1=HP, 2=MP, 3=Atk, 4=Def
    private int statType; 
    private int amount;

    public StatElixir(String name, int statType, int amount, int value) {
        super(name, value);
        this.statType = statType;
        this.amount = amount;
    }

    public int getStatType() { return statType; }
    public int getAmount() { return amount; }

    @Override
    public String getDescription() {
        String type = "";
        switch(statType) {
            case 1: type = "HP Max"; break;
            case 2: type = "MP Max"; break;
            case 3: type = "Ataque"; break;
            case 4: type = "Defesa"; break;
        }
        return "Aumenta PERMANENTEMENTE " + amount + " de " + type;
    }
}