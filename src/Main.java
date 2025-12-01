// Engenharia de Computação BEC 4°Semestre
// Nome: João Henrique Teixeira Ferreira
// Nome: Gustavo Germiniani

import core.GuiOutput;
import core.Game;

public class Main {
    public static void main(String[] args) {
        // Inicia a Interface Gráfica diretamente em uma Thread segura
        javax.swing.SwingUtilities.invokeLater(() -> {
            GuiOutput gui = new GuiOutput();
            // Roda o jogo em outra thread para não travar a janela
            new Thread(() -> {
                Game game = new Game(gui);
                game.start();
            }).start();
        });
    }
}