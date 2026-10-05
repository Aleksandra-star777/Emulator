package ru.miem.shell;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;

public class Main {

    public static void main(String[] args) {
        JFrame window = createWindow();
        JTextArea output = createOutputArea();
        JTextField input = createInputField(output);

        window.add(new JScrollPane(output), BorderLayout.CENTER);
        window.add(input, BorderLayout.SOUTH);

        window.setVisible(true);
        input.requestFocusInWindow();
    }

    private static JFrame createWindow() {
        JFrame window = new JFrame();
        window.setSize(600, 400);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setTitle(buildTitle());
        return window;
    }

    private static String buildTitle() {
        String user = System.getProperty("user.name");
        String host = "localhost";

        try {
            host = InetAddress.getLocalHost().getHostName();
        } catch (Exception ignored) {
        }

        return "Эмулятор - " + user + "@" + host;
    }

    private static JTextArea createOutputArea() {
        JTextArea output = new JTextArea();
        output.setEditable(false);
        return output;
    }

    private static JTextField createInputField(JTextArea output) {
        JTextField input = new JTextField();
        input.addActionListener(e -> handleInput(input, output));
        return input;
    }

    private static void handleInput(JTextField input, JTextArea output) {
        String line = input.getText().trim();
        input.setText("");
        input.requestFocusInWindow();

        if (line.isEmpty()) {
            return;
        }

        line = substituteEnvVars(line);
        output.append("VFS> " + line + "\n");
        executeCommand(line, output);
    }

    private static String substituteEnvVars(String line) {
        String home = System.getenv("HOME");
        if (home == null) {
            home = System.getenv("USERPROFILE");
        }
        if (home != null) {
            line = line.replace("$HOME", home);
        }

        String login = System.getenv("USER");
        if (login == null) {
            login = System.getenv("USERNAME");
        }
        if (login != null) {
            line = line.replace("$USER", login);
        }

        return line;
    }

    private static void executeCommand(String line, JTextArea output) {
        String[] parts = line.split(" ");
        String cmd = parts[0];

        if (cmd.equals("ls") || cmd.equals("cd")) {
            output.append(line + "\n");
        } else if (cmd.equals("exit")) {
            System.exit(0);
        } else {
            output.append("Unknown command: " + cmd + "\n");
        }
    }
}