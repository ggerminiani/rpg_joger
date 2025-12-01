package core;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.*;
import java.awt.*;

public class GuiOutput implements GameOutput {
    private JFrame frame;
    private JTextPane textPane;
    private JTextField inputField;
    private JPanel buttonPanel;
    
    private String lastInput = null;
    private final Object lock = new Object();

    // Cores
    public static final Color C_NORMAL = new Color(200, 200, 200);
    public static final Color C_LORE = new Color(100, 200, 255);
    public static final Color C_ENEMY = new Color(255, 80, 80);
    public static final Color C_PLAYER = new Color(80, 255, 80);
    public static final Color C_WARN = new Color(255, 200, 50);

    public GuiOutput() {
        frame = new JFrame("RPG JOGER: Lenda de Eldoria");
        frame.setSize(1100, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(20, 20, 25));

        // Texto
        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setBackground(new Color(15, 15, 20));
        textPane.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        textPane.setMargin(new Insets(20, 20, 20, 20));
        
        JScrollPane scroll = new JScrollPane(textPane);
        scroll.setBorder(null);
        // Auto-scroll forçado
        DefaultCaret caret = (DefaultCaret)textPane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        // Painel Inferior
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(30, 30, 35));
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        inputField = new JTextField();
        inputField.setBackground(new Color(45, 45, 50));
        inputField.setForeground(Color.CYAN);
        inputField.setCaretColor(Color.CYAN);
        inputField.setFont(new Font("Consolas", Font.BOLD, 16));
        inputField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(80, 80, 80), 1), 
            new EmptyBorder(8, 8, 8, 8)
        ));
        inputField.addActionListener(e -> submitInput(inputField.getText()));
        
        buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(30, 30, 35));
        buttonPanel.setBorder(new EmptyBorder(15, 0, 0, 0)); 

        bottomPanel.add(inputField, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);

        frame.add(scroll, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        inputField.requestFocus();
    }

    private void append(String msg, Color c) {
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        StyleConstants.setForeground(keyWord, c);
        StyleConstants.setFontFamily(keyWord, "Segoe UI Emoji");
        StyleConstants.setFontSize(keyWord, 18);
        try { doc.insertString(doc.getLength(), msg, keyWord); } catch (BadLocationException e) {}
    }

    @Override
    public void print(String text) { append(text, C_NORMAL); }
    
    @Override
    public void println(String text) {
        Color c = C_NORMAL;
        if (text.contains("[LORE]")) c = C_LORE;
        else if (text.contains("BOSS") || text.contains("Inimigo") || text.contains("☠️")) c = C_ENEMY;
        else if (text.contains("VC:") || text.contains("Você") || text.contains("❤️")) c = C_PLAYER;
        else if (text.contains("🎲") || text.contains("!")) c = C_WARN;
        append(text + "\n", c);
    }

    @Override public void clear() { textPane.setText(""); }

    private void submitInput(String text) {
        if (text == null || text.trim().isEmpty()) return;
        append("> " + text + "\n", Color.GRAY);
        inputField.setText("");
        synchronized (lock) { lastInput = text; lock.notify(); }
    }

    @Override
    public String readString() {
        SwingUtilities.invokeLater(() -> {
            buttonPanel.removeAll();
            buttonPanel.revalidate();
            buttonPanel.repaint();
        });
        return waitForInput();
    }

    @Override
    public String readOption(String prompt, String... options) {
        // 1. Imprime as opções no terminal também!
        append("\n" + prompt + "\n", Color.WHITE);
        for(String opt : options) {
            String[] parts = opt.split(":", 2);
            append(" [" + parts[0] + "] " + (parts.length>1 ? parts[1] : "") + "\n", Color.GRAY);
        }

        SwingUtilities.invokeLater(() -> {
            buttonPanel.removeAll();
            int cols = Math.min(options.length, 3);
            int rows = (int) Math.ceil((double)options.length / cols);
            buttonPanel.setLayout(new GridLayout(rows, cols, 15, 15)); 

            for (String opt : options) {
                String[] parts = opt.split(":", 2);
                JButton btn = new JButton(parts.length > 1 ? parts[1] : parts[0]);
                styleButton(btn);
                btn.addActionListener(e -> submitInput(parts[0]));
                buttonPanel.add(btn);
            }
            buttonPanel.revalidate();
            buttonPanel.repaint();
        });
        return waitForInput();
    }

    private String waitForInput() {
        synchronized (lock) {
            try { lock.wait(); } catch (InterruptedException e) {}
            return lastInput;
        }
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(60, 63, 70));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(100, 100, 110), 1),
            new EmptyBorder(15, 10, 15, 10)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
    }
}