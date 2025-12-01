package core;
import java.util.Scanner;

public class ConsoleOutput implements GameOutput {
    private Scanner scanner = new Scanner(System.in);

    @Override public void print(String text) { System.out.print(text); }
    @Override public void println(String text) { System.out.println(text); }
    
    // Simula limpeza pulando 50 linhas
    @Override public void clear() { 
        for(int i=0; i<50; i++) System.out.println(); 
    }
    
    @Override public String readString() { return scanner.nextLine(); }

    @Override
    public String readOption(String prompt, String... options) {
        System.out.println(prompt);
        for (String opt : options) {
            String[] parts = opt.split(":", 2);
            System.out.println("[" + parts[0] + "] " + parts[1]);
        }
        System.out.print("> ");
        return scanner.nextLine();
    }
}