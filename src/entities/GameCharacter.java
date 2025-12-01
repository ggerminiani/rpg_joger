package entities;

import java.io.Serializable;
import skills.Skill;
import java.util.ArrayList;
import java.util.List;

public abstract class GameCharacter implements Serializable {
    // Nome do usuário
    protected String name;
    // Vida e vida máxima
    protected int maxHp, hp;
    // Estamina e estamina máxima
    protected int maxMp, mp;
    // Ataque
    protected int attack;
    // Defesa
    protected int defense = 0;
    // Ouro
    protected int gold = 0;
    // Lista de skills disponíveis
    protected List<Skill> skills = new ArrayList<>();

    // Construtor
    public GameCharacter(String name, int maxHp, int maxMp, int attack) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.maxMp = maxMp;
        this.mp = maxMp;
        this.attack = attack;
    }

    // Define se o jogador está vivo ou morto
    public boolean isAlive() { return hp > 0; }
    
    // lógica de dano recebido pelo inimigo
    public void takeDamage(int dmg) { 
        int effectiveDmg = Math.max(0, dmg - defense);
        this.hp -= effectiveDmg; 
        if(this.hp < 0) this.hp = 0;
    }
    
    // Lógica de cura
    public void heal(int amount) {
        this.hp += amount;
        if(this.hp > maxHp) this.hp = maxHp;
    }

    // Lógica de estamina
    public void restoreMp(int amount) {
        this.mp += amount;
        if(this.mp > maxMp) this.mp = maxMp;
    }

    public boolean spendMp(int cost) {
        if (this.mp >= cost) {
            this.mp -= cost;
            return true;
        }
        return false;
    }

    // Lógica de Ouro
    public void addGold(int amount) { this.gold += amount; }
    
    public boolean spendGold(int amount) {
        if (this.gold >= amount) {
            this.gold -= amount;
            return true;
        }
        return false;
    }

    // Getters e Setters
    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getMp() { return mp; }
    public int getMaxMp() { return maxMp; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public void setDefense(int def) { this.defense = def; }
    public int getGold() { return gold; }
    public List<Skill> getSkills() { return skills; }
    public void upgradeMaxHp(int v) { this.maxHp += v; this.hp += v; }
    public void upgradeMaxMp(int v) { this.maxMp += v; this.mp += v; }
    public void upgradeAttack(int v) { this.attack += v; }
    public void upgradeDefense(int v) { this.defense += v; }
}