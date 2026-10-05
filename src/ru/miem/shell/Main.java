package ru.miem.shell;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;

public class Main {

    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 400;
    private static final String VFS_ARG = "--vfs";
    private static final String SCRIPT_ARG = "--script";

    public static void main(String[] args) {
        AppConfig config = parseArgs(args);

        JFrame window = createWindow();
        JTextArea output = createOutputArea();
        JTextField input = createInputField(output);

        window.add(new JScrollPane(output), BorderLayout.CENTER);
        window.add(input, BorderLayout.SOUTH);

        printDebugInfo(config, output);
        runStartupScript(config.scriptPath(), output);

        window.setVisible(true);
        input.requestFocusInWindow();
    }

    private static AppConfig parseArgs(String[] args) {
        String vfsPath = null;
        String scriptPath = null;

        for (int i = 0; i < args.length - 1; i++) {
            if (VFS_ARG.equals(args[i])) {
                vfsPath = args[i + 1];
            } else if (SCRIPT_ARG.equals(args[i])) {
                scriptPath = args[i + 1];
            }
        }
        return new AppConfig(vfsPath, scriptPath);
    }

    private static void printDebugInfo(AppConfig config, JTextArea output) {
        output.append("=== Отладочный вывод параметров ===\n");
        output.append("Путь к VFS: " + (config.vfsPath() != null ? config.vfsPath() : "не задан") + "\n");
        output.append("Путь к скрипту: " + (config.scriptPath() != null ? config.scriptPath() : "не задан") + "\n");
        output.append("===================================\n\n");
    }

    private static void runStartupScript(String scriptPath, JTextArea output) {
        if (scriptPath == null) {
            return;
        }

        output.append("--- Выполнение стартового скрипта ---\n");
        try (BufferedReader reader = new BufferedReader(new FileReader(scriptPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                output.append("VFS> " + line + "\n");
                String result = executeCommand(line);
                output.append(result + "\n");
            }
        } catch (IOException e) {
            output.append("Ошибка выполнения скрипта: " + e.getMessage() + "\n");
        }
        output.append("--- Скрипт завершен ---\n\n");
    }

    private static JFrame createWindow() {
        JFrame window = new JFrame();
        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
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
        output.append(executeCommand(line) + "\n");
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

    private static String executeCommand(String line) {
        String[] parts = line.split("\\s+");
        if (parts.length == 0 || parts[0].isEmpty()) {
            return "";
        }
        String cmd = parts[0];

        if (cmd.equals("ls") || cmd.equals("cd")) {
            return line;
        } else if (cmd.equals("exit")) {
            System.exit(0);
            return "";
        } else {
            return "Unknown command: " + cmd;
        }
    }

    private record AppConfig(String vfsPath, String scriptPath) {
    }
}