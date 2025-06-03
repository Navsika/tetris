package main.java.com.example.view;

import main.java.com.example.controller.Controller;
import main.java.com.example.model.ActiveFigure;
import main.java.com.example.model.GameState;
import main.java.com.example.model.Model;
import main.java.com.example.model.PlayerScore;

import java.util.List;
import java.util.Scanner;

public class ConsoleView {
    private final Model model;
    private final Controller controller;
    private final Scanner scanner;

    public ConsoleView(Model model, Controller controller) {
        this.model = model;
        this.controller = controller;
        this.scanner = new Scanner(System.in);
        model.addPropertyChangeListener(evt -> {
            switch (evt.getPropertyName()) {
                case "modelUpdated" -> {
                    if (model.isInGame() && evt.getNewValue() instanceof GameState state) {
                        renderBoard(state);
                    }
                }
                case "gameStarted" -> startGame();
                case "menuOpened" -> showMenu();
                case "aboutOpened" -> showAbout();
                case "leaderBoardUpdated" -> {
                    if (evt.getNewValue() instanceof List<?> list) {
                        @SuppressWarnings("unchecked")
                        List<PlayerScore> leaderBoard = (List<PlayerScore>) list;
                        showLeaderboard(leaderBoard);
                    }
                }
                case "gameOver" -> gameOver();
            }
        });
    }

    public void initialize() {
        showMenu();
        handleInput();
    }

    private void showMenu() {
        clearConsole();
        System.out.println("=== Tetris Menu ===");
        System.out.println("1. Start Game");
        System.out.println("2. Leaderboard");
        System.out.println("3. About");
        System.out.println("4. Exit");
        System.out.print("Choose an option: ");
    }

    private void showAbout() {
        clearConsole();
        System.out.println("=== About Tetris ===");
        System.out.println("Developed by [Your Name]");
        System.out.println("Version 1.0");
        System.out.println("\nPress Enter to return to menu...");
        scanner.nextLine();
        showMenu();
    }

    private void showLeaderboard(List<PlayerScore> leaderBoard) {
        clearConsole();
        System.out.println("=== Leaderboard ===");
        if (leaderBoard.isEmpty()) {
            System.out.println("No scores yet!");
        } else {
            for (int i = 0; i < leaderBoard.size(); i++) {
                PlayerScore ps = leaderBoard.get(i);
                System.out.printf("%d. %s: %d\n", i + 1, ps.getPlayerName(), ps.getScore());
            }
        }
        System.out.println("\nPress Enter to return to menu...");
        scanner.nextLine();
        showMenu();
    }

    private void startGame() {
        clearConsole();
    }

    private void gameOver() {
        clearConsole();
        System.out.println("Game Over!");
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine();
        controller.updateLeaderBoard(playerName);
        showMenu();
    }

    private void renderBoard(GameState state) {
        clearConsole();
        int[][] field = state.getField();
        ActiveFigure figure = state.getFigure();

        int[][] displayField = new int[Model.boardHeight][Model.boardWidth];
        for (int y = 0; y < Model.boardHeight; y++) {
            System.arraycopy(field[y], 0, displayField[y], 0, Model.boardWidth);
        }

        if (figure != null) {
            int[][] shape = figure.getShape();
            for (int x = 0; x < figure.getWidth(); x++) {
                for (int y = 0; y < figure.getHeight(); y++) {
                    if (shape[y][x] == 1) {
                        int boardX = figure.getPosition().x() + x;
                        int boardY = figure.getPosition().y() + y;
                        if (boardX >= 0 && boardX < Model.boardWidth && boardY >= 0 && boardY < Model.boardHeight) {
                            displayField[boardY][boardX] = figure.getColorCode();
                        }
                    }
                }
            }
        }

        System.out.println("+" + "-".repeat(Model.boardWidth * 2) + "+");
        for (int y = 0; y < Model.boardHeight; y++) {
            System.out.print("|");
            for (int x = 0; x < Model.boardWidth; x++) {
                System.out.print(getCharFromCode(displayField[y][x]) + " ");
            }
            System.out.println("|");
        }
        System.out.println("+" + "-".repeat(Model.boardWidth * 2) + "+");
        System.out.println("Score: " + state.getScore());
        System.out.println("\nControls: A (Left), D (Right), W (Rotate), Space (Drop), Q (Quit)");
    }

    private String getCharFromCode(int colorCode) {
        return switch (colorCode) {
            case 1 -> "I";
            case 2 -> "J";
            case 3 -> "L";
            case 4 -> "O";
            case 5 -> "S";
            case 6 -> "Z";
            case 7 -> "T";
            default -> ".";
        };
    }

    private void handleInput() {
        new Thread(() -> {
            while (true) {
                String input = scanner.nextLine().trim().toUpperCase();
                if (model.isInGame()) {
                    switch (input) {
                        case "A" -> controller.moveLeft();
                        case "D" -> controller.moveRight();
                        case "W" -> controller.rotate();
                        case " " -> controller.drop();
                        case "Q" -> controller.quitGame();
                        default -> System.out.println("Invalid input. Use A, D, W, Space, or Q.");
                    }
                } else {
                    switch (input) {
                        case "1" -> controller.startGame();
                        case "2" -> controller.showLeaderboard();
                        case "3" -> controller.showAbout();
                        case "4" -> System.exit(0);
                        default -> System.out.println("Invalid option. Choose 1-4.");
                    }
                    if (!model.isInGame()) {
                        showMenu();
                    }
                }
            }
        }).start();
    }

    private void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }
}