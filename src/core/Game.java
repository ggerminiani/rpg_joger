package core;

import entities.*;
import items.*;
import skills.Skill;
import exceptions.*;
import utils.Dice; 
import java.io.*;
import java.util.*;

public class Game implements Serializable {
    private Player player;
    private List<NPC> bosses;
    private List<String> defeatedBosses;
    private boolean gameOver;
    private transient GameOutput out; 

    public Game(GameOutput out) {
        this.out = out;
        this.defeatedBosses = new ArrayList<>();
        initBosses();
    }
    
    public void setOutput(GameOutput out) { this.out = out; }

    private void initBosses() {
        bosses = new ArrayList<>();
        
        bosses.add(new NPC("Lobo de Gelo", 60, 0, 7, 
            new Item("Colar de Gelo", 100) { @Override public String getDescription() { return "Acessório: +20 MP Máximo"; } }, 
            5, 50, 
            "Vejo o vapor sair de sua boca, humano. O frio eterno das montanhas preservará seu cadáver perfeitamente."));
            
        bosses.add(new NPC("Esqueleto Ancião", 80, 20, 9, 
            new Item("Armadura de Ossos", 150) { @Override public String getDescription() { return "Armadura: +5 Defesa"; } }, 
            5, 80, 
            "Creek... A carne é uma fraqueza. Joger nos deu o presente da eternidade. Por que você resiste à inevitável sepultura?"));
            
        bosses.add(new NPC("Mago Sombrio", 70, 100, 14, 
            new Weapon("Cajado Sombrio", 6, 300), 
            5, 120, 
            "Tolo. Você manipula aço e madeira enquanto eu dobro a realidade. A Torre caiu por uma razão, e você cairá com ela."));
            
        bosses.add(new NPC("Dragão de Obsidiana", 120, 50, 18, 
            new Item("Anel de Joger", 600) { @Override public String getDescription() { return "Anel: Prova de Força Lendária"; } }, 
            10, 250, 
            "ROAAAR! MINHAS ESCAMAS SÃO FEITAS DO SANGUE DA TERRA! VOCÊ OUSA TRAZER ESSA LÂMINA PARA O MEU NINHO? QUEIME!"));
        
        bosses.add(new NPC("Joger", 300, 999, 25, null, 0, 0, 
            "Então é você... O grão de areia que travou as engrenagens do destino. Eu quebrei o mundo para salvá-lo de si mesmo. Agora, terei que quebrar você."));
    }

    public void start() {
        out.clear();
        out.println("=== RPG JOGER: A LENDA DE ELDORIA ===");
        String opt = out.readOption("Menu Principal:", "1:🆕 Novo Jogo", "2:📂 Carregar Jogo");
        
        if (opt.equals("2")) loadGame();
        else {
            playIntroSequence();
            createCharacter();
        }
        gameLoop();
    }

    private void playIntroSequence() {
        out.clear();
        out.println("[LORE] Há milênios, a Era Dourada de Eldoria era sustentada pela Torre do Meio.");
        out.println("A magia fluía como rios de luz, conectando todos os seres vivos em harmonia.");
        out.readOption("", "ok:Continuar");
        
        out.clear();
        out.println("[LORE] Mas a ambição de um homem, o Arquimago Joger, corrompeu o fluxo.");
        out.println("Buscando a imortalidade, ele tentou absorver o Núcleo da Torre.");
        out.println("A estrutura colapsou, fragmentando o mundo em reinos isolados de dor e sombras.");
        out.readOption("", "ok:Continuar");

        out.clear();
        out.println("[LORE] Para impedir a destruição total, Joger selou seu próprio espírito no Templo do Fim.");
        out.println("Quatro Guardiões Elementais foram criados para proteger as chaves do selo.");
        out.println("Dizem as lendas que apenas um herói capaz de derrotá-los poderá desafiar Joger.");
        out.readOption("", "ok:ACORDAR");
        
        out.clear();
        out.println("[LORE] Você desperta na Vila de Eldoria, o último bastião da humanidade.");
        out.println("O céu tem uma cor doentia de violeta. Sua jornada começa agora.");
        out.readOption("", "ok:Levantar-se");
    }

    private void createCharacter() {
        out.clear();
        out.println("Como você deseja ser chamado nas lendas, viajante?");
        String name = out.readString();
        
        out.clear();
        String type = out.readOption("Qual foi seu treinamento antes do colapso?", 
            "1:🛡️ Guerreiro (Alta Defesa, Dano Consistente)", 
            "2:🔥 Mago (Dano Explosivo, Frágil)", 
            "3:🏹 Arqueiro (Crítico Alto, Veloz)"
        );
        
        switch(type) {
            case "2": player = new Mage(name); break;
            case "3": player = new Archer(name); break;
            default: player = new Warrior(name); break;
        }
        
        try {
            player.getInventory().add(new Weapon("Espada Enferrujada", 2, 5));
            player.getInventory().add(new Potion("Poção Pequena", 20, 15));
            player.addGold(50);
        } catch (Exception e) {}
    }

    private void gameLoop() {
        while (!gameOver) {
            out.clear();
            out.println("--- 🏰 VILA DE ELDORIA (Refúgio Seguro) ---");
            out.println("O vento uiva lá fora. Aqui, mercadores e curandeiros tentam sobreviver.");
            out.println(String.format("👤 %s (Lvl %d) | ❤️ %d/%d | 🌀 %d/%d | 🛡️ Def: %d | 💰 %d G", 
                player.getName(), player.getLevel(), player.getHp(), player.getMaxHp(), player.getMp(), player.getMaxMp(), player.getDefense(), player.getGold()));
            
            String input = out.readOption("Para onde você vai?",
                "1:🗺️ Mapa Mundi (Caçar Chefes)", 
                "2:🎒 Mochila & Equipamentos", 
                "3:📊 Ver Status & Skills",
                "4:⚖️ Mercado Geral", 
                "5:🏥 Tenda da Curandeira", 
                "6:🔮 Caverna da Feiticeira",
                "7:☠️ TEMPLO DE JOGER (Final)", 
                "8:💾 Salvar & Sair"
            );

            switch (input) {
                case "1": battleMenu(); break;
                case "2": manageInventoryMenu(); break;
                case "3": showStatusMenu(); break;
                case "4": visitMarket(); break;
                case "5": visitHealer(); break;
                case "6": visitWitch(); break;
                case "7": fightJoger(); break;
                case "8": saveGame(); gameOver = true; break;
            }
            
            if (!player.isAlive()) handleDeath();
        }
    }

    private void startBattle(NPC enemy) {
        out.clear();
        out.println("\n--- " + enemy.getName().toUpperCase() + " ---");
        out.println("[LORE] \"" + enemy.getDialogue() + "\"");
        out.readOption("", "ok:Preparar para Combate");
        
        while(player.isAlive() && enemy.isAlive()) {
            out.clear();
            out.println("========================================");
            out.println(String.format("💚 %s: %d/%d HP | %d/%d MP", player.getName().toUpperCase(), player.getHp(), player.getMaxHp(), player.getMp(), player.getMaxMp()));
            out.println(String.format("❤️ %s: %d HP", enemy.getName().toUpperCase(), enemy.getHp()));
            out.println("========================================");
            
            String op = out.readOption("Sua Vez:", 
                "1:⚔️ Atacar (Dano: " + player.getAttack() + " + D20)", 
                "2:✨ Skill", 
                "3:🧪 Item", 
                "4:🏃 Fugir"
            );
            boolean playerActed = false;

            if(op.equals("1")) {
                out.readOption("Teste sua sorte!", "roll:🎲 ROLAR DADO DE ATAQUE");
                int d20 = Dice.roll(1, 20);
                out.println("🎲 Resultado do D20: " + d20 + (d20==20 ? " (CRÍTICO!)" : ""));
                
                int hitTotal = player.getAttack() + d20;
                int defenseTotal = enemy.getDefense() + Dice.roll(1, 20);
                int dmg = Math.max(0, hitTotal - defenseTotal);
                if (d20 == 20) dmg *= 2;
                if (d20 == 1) { out.println("😓 FALHA CRÍTICA! Você escorregou."); dmg = 0; }

                enemy.takeDamage(dmg);
                out.println(">> Você causou " + dmg + " de dano!");
                
                if (!enemy.isAlive()) {
                    out.println("\n💀 O inimigo tombou!");
                    out.readOption("", "win:🏆 RECLAMAR VITÓRIA");
                }
                playerActed = true;

            } else if (op.equals("2")) {
                playerActed = useSkillMenu(enemy);
            } else if (op.equals("3")) {
                if(useItemInBattle()) out.println("Turno continua...");
            } else if (op.equals("4")) {
                if(enemy.getName().equals("Joger")) out.println("As portas do templo estão seladas. Lute ou morra."); 
                else return;
            }

            if(playerActed && enemy.isAlive()) {
                out.readOption("", "next:Turno do Inimigo");
                out.println("\n⚠️ " + enemy.getName() + " prepara um ataque feroz!");
                out.readOption("Tente bloquear!", "def:🛡️ LEVANTAR ESCUDO (D20)");
                
                int d20Def = Dice.roll(1, 20);
                int d20Atk = Dice.roll(1, 20);
                out.println("🎲 Sua Defesa: " + d20Def + " vs Ataque Boss: " + d20Atk);
                
                int dmg = Math.max(0, (enemy.getAttack() + d20Atk) - (player.getDefense() + d20Def));
                player.takeDamage(dmg);
                out.println("☠️ Você sofreu " + dmg + " de dano!");
                
                if(player.isAlive()) out.readOption("", "next:Seu Turno");
            }
        }
        
        if(player.isAlive()) processVictory(enemy);
    }
    
    private boolean useSkillMenu(NPC enemy) {
        List<String> sOpts = new ArrayList<>();
        for(int i=0; i<player.getSkills().size(); i++) {
            Skill s = player.getSkills().get(i);
            // Mostra o nome e custo no botão
            sOpts.add((i+1) + ":" + s.getName() + " (MP:" + s.getManaCost() + ")");
        }
        sOpts.add("0:Voltar");
        
        String sOp = out.readOption("Grimório de Habilidades:", sOpts.toArray(new String[0]));
        try {
            int idx = Integer.parseInt(sOp) - 1;
            if(idx >= 0) {
                Skill s = player.getSkills().get(idx);
                if(player.spendMp(s.getManaCost())) {
                    out.readOption("Canalizando energia...", "roll:🎲 ROLAR DANO MÁGICO");
                    int d20 = Dice.roll(1, 20);
                    out.println("🎲 D20 Mágico: " + d20);
                    
                    int baseDmg = s.use(player, enemy); 
                    int totalDmg = baseDmg + (d20 / 2);
                    if (d20 == 20) { out.println("🔥 MAGIA CRÍTICA! O poder transbordou!"); totalDmg *= 1.5; }
                    
                    enemy.takeDamage(totalDmg);
                    out.println("✨ " + s.getName() + " explodiu causando " + totalDmg + " de dano!");
                    
                    if (!enemy.isAlive()) {
                        out.println("\n💀 O inimigo foi desintegrado!");
                        out.readOption("", "win:🏆 VITÓRIA");
                    }
                    return true;
                } else {
                    out.println("❌ Você tenta conjurar, mas sua mente está exausta (Sem Mana).");
                    out.readOption("", "ok:Voltar");
                }
            }
        } catch(Exception e) {}
        return false;
    }

    private void visitMarket() {
        boolean shopping = true;
        while(shopping) {
            out.clear();
            out.println("--- ⚖️ MERCADO GERAL ---");
            out.println("Um mercador com cicatrizes de batalha exibe seus itens sobre caixas de madeira.");
            out.println("Seu Ouro: " + player.getGold());
            String op = out.readOption("Mercador: 'O que vai levar hoje?'", 
                "1:🍎 Fruta da Força (+1 Atk Perma) - 150G", 
                "2:💖 Fruta da Vida (+10 HP Perma) - 100G", 
                "3:💰 Vender Itens", 
                "4:🔙 Sair"
            );
            if(op.equals("1")) buy(new StatElixir("Fruta da Força", 3, 1, 150), 150);
            else if(op.equals("2")) buy(new StatElixir("Fruta da Vida", 1, 10, 100), 100);
            else if(op.equals("3")) sellMenu();
            else shopping = false;
        }
    }
    
    private void visitWitch() {
        boolean shopping = true;
        while(shopping) {
            out.clear();
            out.println("--- 🔮 CAVERNA DA FEITICEIRA ---");
            out.println("O ar aqui cheira a enxofre e ozônio. Frascos borbulham sozinhos.");
            out.println("Seu Ouro: " + player.getGold());
            String op = out.readOption("Feiticeira: 'O conhecimento custa caro...'", 
                "1:💧 Mana Pequena (+20 MP) - 40G", 
                "2:🌊 Mana Grande (+50 MP) - 80G", 
                "3:🔙 Sair"
            );
            if(op.equals("1")) buy(new ManaPotion("Mana Peq.", 20, 20), 40);
            else if(op.equals("2")) buy(new ManaPotion("Mana Grd.", 50, 40), 80);
            else shopping = false;
        }
    }

    private void visitHealer() {
        out.clear();
        out.println("--- 🏥 TENDA DA CURANDEIRA ---");
        out.println("Uma senhora gentil tritura ervas medicinais em um pilão.");
        out.println("Seu Ouro: " + player.getGold());
        
        String op = out.readOption("Curandeira: 'Como posso aliviar sua dor?'", 
            "1:💖 Curar Feridas (Recuperar HP) - 15G",
            "2:🌀 Restaurar Espírito (Recuperar Mana) - 15G",
            "3:✨ Tratamento Completo (HP + Mana) - 30G",
            "4:🔙 Sair"
        );
        
        if(op.equals("1")) {
            if(player.spendGold(15)) { player.heal(999); out.println("Suas feridas se fecham."); } else out.println("Ouro insuficiente.");
            out.readOption("", "ok:Continuar");
        }
        else if(op.equals("2")) {
            if(player.spendGold(15)) { player.restoreMp(999); out.println("Sua mente clareia."); } else out.println("Ouro insuficiente.");
            out.readOption("", "ok:Continuar");
        }
        else if(op.equals("3")) {
            if(player.spendGold(30)) { 
                player.heal(999); player.restoreMp(999); 
                out.println("Você se sente invencível novamente!"); 
            } else out.println("Ouro insuficiente.");
            out.readOption("", "ok:Continuar");
        }
    }

    private void buy(Item i, int price) {
        if (player.getGold() < price) {
            out.println("❌ Você revira seus bolsos, mas não tem ouro suficiente.");
            out.readOption("", "ok:Voltar");
            return;
        }
        try {
            player.getInventory().add(i); 
            player.spendGold(price); 
            out.println("✅ Negócio fechado! Você obteve: " + i.getName());
        } catch (InventoryFullException e) {
            out.println("❌ 'Sua mochila está explodindo, viajante. Libere espaço primeiro.'");
        }
        out.readOption("", "ok:Continuar");
    }
    
    private void battleMenu() {
        out.clear();
        out.println("--- 🗺️ MAPA MUNDI ---");
        out.println("O mundo é vasto e perigoso. Escolha seu destino:");
        
        List<String> opts = new ArrayList<>();
        for(int k=0; k<4; k++) {
            NPC b = bosses.get(k);
            String st = defeatedBosses.contains(b.getName()) ? "(DERROTADO)" : "(PERIGO)";
            opts.add((k+1) + ":" + b.getName() + " " + st);
        }
        opts.add("0:🔙 Voltar para a Vila");
        
        String c = out.readOption("Local de Caça:", opts.toArray(new String[0]));
        try {
            int idx = Integer.parseInt(c)-1;
            if(idx>=0 && idx<4) {
                if(!defeatedBosses.contains(bosses.get(idx).getName())) startBattle(bosses.get(idx));
                else { out.println("Apenas o vento sopra aqui. O guardião já caiu."); out.readOption("", "ok"); }
            }
        }catch(Exception e){}
    }
    
    private void fightJoger() {
        NPC j = bosses.get(4);
        out.clear();
        out.println("!!! O TEMPLO PROIBIDO !!!");
        out.println("Você está diante dos portões negros do Templo do Fim.");
        out.println("Uma aura opressora emana lá de dentro.");
        
        String confirm = out.readOption("Tem certeza que está pronto?", 
            "s:💀 ENTRAR E ENFRENTAR O DEUS", 
            "n:🏃 Voltar para a segurança"
        );
        
        if(!confirm.equals("s")) return;

        if (defeatedBosses.size() < 4) {
            out.println("⚠️ AVISO: Você sente os Selos Elementais pulsando!");
            out.println("Joger está protegido por magia antiga. (Defesa Extrema / Dano Dobrado)");
            j.setDefense(15); 
        } else {
            out.println("Os selos estão quebrados. Joger está exposto e furioso.");
            j.setDefense(3);
        }
        startBattle(j);
    }
    
    private void handleDeath() {
        out.clear();
        out.println("\n☠️ A ESCURIDÃO TE CONSOME ☠️");
        out.println("[LORE] Seu corpo cai sem vida no chão frio de Eldoria.");
        out.println("Sem um herói, as sombras de Joger engolem o mundo.");
        out.println("A humanidade perece, esperando por uma salvação que nunca veio.");
        
        String op = out.readOption("O destino te dá outra chance?", 
            "1:🔄 Voltar no Tempo (Carregar Save)", 
            "2:❌ Aceitar o Fim (Sair)"
        );
        
        if(op.equals("1")) { 
            loadGame(); 
            if(player==null) {
                out.println("Nenhuma memória encontrada no fluxo do tempo.");
                createCharacter();
            }
        }
        else { gameOver=true; System.exit(0); }
    }
    
    private void manageInventoryMenu() {
        boolean back = false;
        while (!back) {
            out.clear();
            out.println("--- 🎒 MOCHILA (" + player.getInventory().getItems().size() + "/10) ---");
            
            String w = (player.getEquippedWeapon()!=null) ? player.getEquippedWeapon().getName() : "Mãos Nuas";
            String a = (player.getEquippedArmor()!=null) ? player.getEquippedArmor().getName() : "Roupa Comum";
            String ac = (player.getEquippedAccessory()!=null) ? player.getEquippedAccessory().getName() : "Nenhum";
            out.println(String.format("Equipado: 🗡️ %s | 🛡️ %s | 💍 %s", w, a, ac));
            out.println("--------------------------------");
            
            List<String> opts = new ArrayList<>();
            List<Item> items = player.getInventory().getItems();
            for(int i=0; i<items.size(); i++) {
                Item it = items.get(i);
                String mk = (it==player.getEquippedWeapon() || it==player.getEquippedArmor() || it==player.getEquippedAccessory()) ? "[E] " : "";
                opts.add((i+1) + ":" + mk + it.getName());
            }
            opts.add("0:🔙 Voltar");
            
            String sel = out.readOption("Selecione um item:", opts.toArray(new String[0]));
            if(sel.equals("0")) back=true;
            else {
                try {
                    Item it = items.get(Integer.parseInt(sel)-1);
                    String act = out.readOption(it.getName() + " (" + it.getDescription() + ")", 
                        "eq:⚔️ Equipar", "use:🧪 Usar", "del:🗑️ Jogar Fora", "c:🔙 Cancelar");
                    
                    if(act.equals("eq")) {
                        player.equip(it);
                        out.println("✅ Você equipou " + it.getName());
                        out.readOption("", "ok");
                    }
                    else if(act.equals("use")) consumeItem(it);
                    else if(act.equals("del")) { player.getInventory().remove(it); out.println("Item descartado."); out.readOption("", "ok"); }
                } catch(Exception e){}
            }
        }
    }
    
    private void sellMenu() {
        List<String> opts = new ArrayList<>();
        List<Item> items = player.getInventory().getItems();
        for(int i=0; i<items.size(); i++) opts.add((i+1)+":"+items.get(i).getName()+" ("+items.get(i).getValue()/2+"G)");
        opts.add("0:Voltar");
        String s = out.readOption("Selecione para vender:", opts.toArray(new String[0]));
        try{ 
            int idx = Integer.parseInt(s)-1; 
            if(idx>=0) {
                Item it = items.get(idx);
                if(it!=player.getEquippedWeapon() && it!=player.getEquippedArmor() && it!=player.getEquippedAccessory()) { 
                    player.getInventory().remove(it); 
                    player.addGold(it.getValue()/2); 
                    out.println("💰 Vendido por " + (it.getValue()/2) + "G"); 
                }
                else out.println("❌ Você não pode vender o que está usando!");
                out.readOption("", "ok");
            }
        }catch(Exception e){}
    }
    
    private boolean consumeItem(Item i) {
        boolean u = false;
        if(i instanceof Potion) { 
            player.heal(((Potion)i).getHealAmount()); 
            out.println("❤️ Vida recuperada!"); 
            u=true;
        }
        else if(i instanceof ManaPotion) { 
            player.restoreMp(((ManaPotion)i).getRestoreAmount()); 
            out.println("🌀 Mana fluindo!"); 
            u=true;
        }
        else if(i instanceof StatElixir) { 
            StatElixir e=(StatElixir)i; 
            if(e.getStatType()==1) player.upgradeMaxHp(e.getAmount());
            if(e.getStatType()==3) player.upgradeAttack(e.getAmount());
            out.println("🌟 Você sente um poder permanente crescendo em suas veias!"); 
            u=true;
        }
        else {
            out.println("Este item não pode ser consumido.");
        }
        
        if(u) { 
            player.getInventory().remove(i); 
            out.readOption("", "ok:Continuar"); 
        }
        return u;
    }
    
    private boolean useItemInBattle() {
        List<String> ops = new ArrayList<>();
        for(int i=0; i<player.getInventory().getItems().size(); i++) {
            Item it = player.getInventory().getItems().get(i);
            // Mostra o valor de cura no botão
            if(it instanceof Potion) ops.add((i+1)+":"+it.getName() + " (+" + ((Potion)it).getHealAmount() + "HP)");
            else if(it instanceof ManaPotion) ops.add((i+1)+":"+it.getName() + " (+" + ((ManaPotion)it).getRestoreAmount() + "MP)");
        }
        ops.add("0:Voltar");
        
        String sel = out.readOption("Inventário Rápido:", ops.toArray(new String[0]));
        try { 
            int idx=Integer.parseInt(sel)-1; 
            if(idx>=0) return consumeItem(player.getInventory().getItems().get(idx)); 
        } catch(Exception e){}
        return false;
    }

    private void showStatusMenu() {
        out.clear();
        out.println("--- 📊 STATUS E PROGRESSÃO ---");
        out.println("ATK: " + player.getAttack() + " | DEF: " + player.getDefense());
        out.println("Pontos de Skill Disponíveis: " + player.getSkillPoints());
        
        List<String> opts = new ArrayList<>();
        for(int i=0; i<player.getSkills().size(); i++) {
            Skill s = player.getSkills().get(i);
            if(player.getSkillPoints()>0 && s.getLevel()<10) opts.add("u"+i+":Evoluir "+s.getName());
        }
        opts.add("0:Voltar");
        
        String op = out.readOption("Evoluir Habilidades:", opts.toArray(new String[0]));
        if(op.startsWith("u")) {
            player.getSkills().get(Integer.parseInt(op.substring(1))).upgrade();
            player.spendSkillPoint();
            out.println("✨ Habilidade aprimorada!"); 
            out.readOption("", "ok");
        }
    }

    private void processVictory(NPC enemy) {
        out.clear();
        out.println("\n🌟 INIMIGO DERROTADO! 🌟");
        out.println("O " + enemy.getName() + " cai no chão, derrotado.");
        
        defeatedBosses.add(enemy.getName());
        player.addXp(enemy.getXpReward());
        player.addGold(enemy.getGoldReward());
        
        // Verifica conquistas
        String achievement = "";
        if(enemy.getName().equals("Lobo de Gelo")) achievement = "DOMADOR DO INVERNO";
        if(enemy.getName().equals("Esqueleto Ancião")) achievement = "EXORCISTA";
        if(enemy.getName().equals("Mago Sombrio")) achievement = "ANTI-MAGIA";
        if(enemy.getName().equals("Dragão de Obsidiana")) achievement = "MATADOR DE DRAGÕES";
        
        if(!achievement.isEmpty()) {
            player.unlockAchievement(achievement);
            out.println("🏆 CONQUISTA DESBLOQUEADA: " + achievement);
        }

        if(enemy.getDrop() != null) {
            try {
                player.getInventory().add(enemy.getDrop());
                out.println("🎁 Você saqueou: " + enemy.getDrop().getName());
            } catch (InventoryFullException e) {
                forceInventorySpace(enemy.getDrop());
            }
        }
        
        out.println("Ganhos: " + enemy.getXpReward() + " XP | " + enemy.getGoldReward() + " Ouro");
        out.readOption("", "ok:Continuar Jornada");
        
        if(enemy.getName().equals("Joger")) {
            finishGame(defeatedBosses.size() >= 5);
        }
    }
    
    private void forceInventorySpace(Item drop) {
        while(true) {
            out.println("\n⚠️ MOCHILA CHEIA! Você precisa de espaço para pegar o Drop Lendário.");
            out.println("Item no chão: " + drop.getName());
            
            String op = out.readOption("O que fazer?", 
                "1:🗑️ Descartar Drop (Perder para sempre)", 
                "2:🎒 Abrir Mochila (Jogar lixo fora)"
            );
            
            if (op.equals("1")) {
                out.println("Você deixou o item para trás com pesar.");
                break;
            } else if (op.equals("2")) {
                manageInventoryMenu();
                try {
                    player.getInventory().add(drop);
                    out.println("Você pegou " + drop.getName() + "!");
                    break;
                } catch (InventoryFullException e) {
                    out.println("Ainda não tem espaço...");
                }
            }
        }
    }

    private void finishGame(boolean platinum) {
        out.clear();
        out.println("\n#######################################");
        out.println("       🏆 A LENDA SE CONCRETIZA 🏆");
        out.println("#######################################");
        
        if(platinum) {
            out.println("[LORE] Com o último suspiro de Joger, a magia negra se dissipa.");
            out.println("Os reinos, antes separados, começam a se curar.");
            out.println("Você não apenas salvou o mundo; você o unificou.");
            out.println("Estátuas serão erguidas em sua honra.");
            out.println("🏆 CONQUISTA SUPREMA: O LIBERTADOR DE ELDORIA (PLATINA)");
        } else {
            out.println("[LORE] Joger caiu perante sua lâmina, mas os Guardiões ainda vivem.");
            out.println("Sem seu mestre, eles enlouquecem e causam o caos.");
            out.println("O mundo está salvo da tirania, mas condenado à anarquia.");
            out.println("Sua vitória é incompleta.");
            out.println("⚡ CONQUISTA: O CARRASCO (SPEEDRUN)");
        }
        
        out.println("\nObrigado por jogar!");
        out.readOption("", "ok:ENCERRAR JOGO");
        gameOver = true;
        new File("save.dat").delete();
        System.exit(0);
    }
    
    private void saveGame() { try(ObjectOutputStream o=new ObjectOutputStream(new FileOutputStream("save.dat"))){o.writeObject(this); out.println("✅ Jogo salvo."); out.readOption("","ok");}catch(Exception e){out.println("Erro ao salvar.");} }
    private void loadGame() { try(ObjectInputStream o=new ObjectInputStream(new FileInputStream("save.dat"))){Game g=(Game)o.readObject(); player=g.player; bosses=g.bosses; defeatedBosses=g.defeatedBosses;}catch(Exception e){createCharacter();} }
}