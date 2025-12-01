package entities;

import items.Item;

public class NPC extends GameCharacter {
    // Itens dropados pelos NPCs
    private Item drop;
    // Total de XP ganho por derrotar um chefe
    private int xpReward;
    // Total de ouro ganho por derrotar um chefe
    private int goldReward;
    // Diálogo do NPC
    private String dialogue;

    // Construção da classe NPC
    public NPC(String name, int hp, int mp, int atk, Item drop, int xpReward, int goldReward, String dialogue) {
        super(name, hp, mp, atk);
        this.drop = drop;
        this.xpReward = xpReward;
        this.goldReward = goldReward;
        this.dialogue = dialogue;
    }
    
    public Item getDrop() { return drop; }
    public int getXpReward() { return xpReward; }
    public int getGoldReward() { return goldReward; }
    public String getDialogue() { return dialogue; }
}