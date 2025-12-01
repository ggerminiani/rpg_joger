package entities;

import inventory.Inventory;
import items.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Player extends GameCharacter {
    private int level = 1;
    private int xp = 0;
    private int skillPoints = 0;
    private Inventory<Item> inventory;
    
    // Equipamentos
    private Weapon equippedWeapon;
    private Item equippedArmor;
    private Item equippedAccessory;
    
    // Conquistas
    private List<String> achievements = new ArrayList<>();

    public Player(String name, int hp, int mp, int atk) {
        super(name, hp, mp, atk);
        this.inventory = new Inventory<>(10);
        this.gold = 50;
    }

    public void addXp(int amount) {
        this.xp += amount;
        if (this.xp >= 5) { // XP para subir de nível
            levelUp();
            this.xp -= 5;
        }
    }

    private void levelUp() {
        level++;
        skillPoints += 3;
        // Bônus base de nível
        this.maxHp += 10;
        this.maxMp += 5;
        this.attack += 1;
        
        // Recupera tudo ao subir de nível
        this.hp = getMaxHp();
        this.mp = getMaxMp();
        
        System.out.println(">> LEVEL UP! Nível " + level);
    }

    // --- LÓGICA DE EQUIPAMENTOS ---

    public void equip(Item item) {
        if (item instanceof Weapon) {
            this.equippedWeapon = (Weapon) item;
        } 
        // Lógica para Armaduras e Acessórios (baseada no nome ou tipo se criarmos classe Armor)
        else if (item.getName().contains("Armadura")) {
            this.equippedArmor = item;
        }
        else if (item.getName().contains("Colar") || item.getName().contains("Anel")) {
            this.equippedAccessory = item;
        }
    }
    
    public void unequip() {
        // Remove arma por padrão (pode melhorar para especificar qual slot depois)
        this.equippedWeapon = null;
    }
    
    // Sobrescrevemos os Getters para somar os bônus dos itens dinamicamente

    @Override
    public int getMaxHp() {
        int bonus = 0;
        // Exemplo: Colar de Gelo pode dar vida no futuro
        return super.maxHp + bonus;
    }

    @Override
    public int getAttack() {
        int bonus = 0;
        if (equippedWeapon != null) bonus += equippedWeapon.getDamageBonus();
        // Acessórios podem dar dano também
        return super.attack + bonus;
    }

    @Override
    public int getDefense() {
        int bonus = 0;
        // Armadura de Ossos dá defesa
        if (equippedArmor != null && equippedArmor.getName().equals("Armadura de Ossos")) {
            bonus += 5; 
        }
        // Buffs passivos de skills (como Pele de Ferro) já estão no super.defense
        return super.defense + bonus;
    }
    
    // Acessório Especial (Colar de Gelo dá +1 Skill Point no level up - lógica no levelUp seria complexa, 
    @Override
    public int getMaxMp() {
        int bonus = 0;
        if (equippedAccessory != null && equippedAccessory.getName().equals("Colar de Gelo")) {
            bonus += 20;
        }
        return super.maxMp + bonus;
    }

    // --- CONQUISTAS ---
    public void unlockAchievement(String title) {
        if (!achievements.contains(title)) {
            achievements.add(title);
        }
    }
    
    public List<String> getAchievements() { return achievements; }

    public Weapon getEquippedWeapon() { return equippedWeapon; }
    public Item getEquippedArmor() { return equippedArmor; }
    public Item getEquippedAccessory() { return equippedAccessory; }
    
    public Inventory<Item> getInventory() { return inventory; }
    public int getSkillPoints() { return skillPoints; }
    public void spendSkillPoint() { if(skillPoints > 0) skillPoints--; }
    public int getLevel() { return level; }
}