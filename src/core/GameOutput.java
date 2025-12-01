package core;

public interface GameOutput {
    void print(String text);
    void println(String text);
    void clear();
    String readString(); 
    String readOption(String prompt, String... options);
}