package com.example.view;

import com.example.contorller.Controller;
import com.example.model.ActiveFigure;
import com.example.model.GameState;
import com.example.model.Model;
import com.example.model.PlayerScore;

import java.util.List;
import java.util.Scanner;

public class ConsoleView {
    private final Model model;
    private final Controller controller;
    private final Scanner scanner;
    private boolean inputLocked;

    public ConsoleView(Model model, Controller controller){
        this.model = model;
        this.controller = controller;
        this.scanner = new Scanner(System.in);
        this.inputLocked = false;
        model.addPropertyChangeListener(evt-> {
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
                        List<PlayerScore> leaderBoard = (List<PlayerScore>) list;
                        showLeaderBoard(leaderBoard);
                    }
                }
                case "gameOver" -> gameOver();
            }
        });
    }
    private void clearConsole(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private String getCharFromCode(int colorCode) {
        return switch (colorCode) {
            case 1 -> "\033[36mI\033[0m"; // Циан для I
            case 2 -> "\033[34mJ\033[0m"; // Синий для J
            case 3 -> "\033[33mL\033[0m"; // Оранжевый для L
            case 4 -> "\033[31mO\033[0m"; // Красный для O
            case 5 -> "\033[32mS\033[0m"; // Зеленый для S
            case 6 -> "\033[35mZ\033[0m"; // Пурпурный для Z
            case 7 -> "\033[37mT\033[0m"; // Белый для T
            default -> ".";
        };
    }

    private void showMenu(){
        clearConsole();
        System.out.println("=================");
        System.out.println("|   TETRIS      |");
        System.out.println("=================");
        System.out.println("1. Start Game");
        System.out.println("2. Leaderboard");
        System.out.println("3. About");
        System.out.println("4. Exit");
        System.out.print("Choose: ");
    }

    private void showLeaderBoard(List <PlayerScore> leaderBoard){
        clearConsole();
        System.out.println("======================");
        System.out.println("|     Leaderboard    |");
        System.out.println("======================");
        if (leaderBoard.isEmpty()) {
            System.out.println("No scores yet!");
        } else {
            for (int i = 0; i < leaderBoard.size(); i++) {
                PlayerScore ps = leaderBoard.get(i);
                System.out.printf("%d. %s: %d\n", i + 1, ps.getPlayerName(), ps.getScore());
            }
        }
        System.out.println("\nPress Enter to return to menu...");
        inputLocked = true;
        scanner.nextLine();
        inputLocked = false;
        showMenu();
    }

    private void showAbout(){
        clearConsole();
        System.out.println("=== About Tetris ===");
        System.out.println();

        System.out.println("Controls:");
        String[] controls = {
                "W - Rotate the piece",
                "Space - Drop the piece faster",
                "D - Move piece right",
                "A - Move piece left",
                "Q - Quit game"
        };
        for (String control : controls) {
            System.out.println("  " + control);
        }

        System.out.println();
        System.out.println("Scoring Rules:");
        String[] scoringRules = {
                "1 line cleared - 1 point",
                "2 lines cleared simultaneously - 5 points",
                "3 lines cleared simultaneously - 20 points",
                "4 lines cleared simultaneously - 100 points"
        };
        for (String rule : scoringRules) {
            System.out.println("  " + rule);
        }
        System.out.println("\nPress Enter to return to menu...");
        inputLocked = true;
        scanner.nextLine();
        inputLocked = false;
        controller.showMenu();
    }

    private void startGame(){
        clearConsole();
    }
    public void initialize(){
        clearConsole();
        showMenu();
        handleInput();
    }

    public void shutdown() {
        scanner.close();
    }

    private void gameOver() {
        clearConsole();
        System.out.println("Game Over!");
        System.out.println("Enter your name: ");
        inputLocked = true;
        String playerName = scanner.nextLine();
        inputLocked = false;
        controller.updateLeaderBoard(playerName);
        showMenu();
    }

    private void renderBoard(GameState gameState) {
        clearConsole();
        int[][] field = gameState.getField();
        ActiveFigure activeFigure = gameState.getFigure();
        int[][] displayField = new int[Model.boardHeight][Model.boardWidth];
        for (int y = 0; y < Model.boardHeight; y++) {
            System.arraycopy(field[y], 0, displayField[y], 0, Model.boardWidth);
        }

        if (activeFigure != null) {
            int[][] shape = activeFigure.getShape();
            for (int y = 0; y < activeFigure.getHeight(); y++)
                for (int x = 0; x < activeFigure.getWidth(); x++) {
                    if (shape[y][x] == 1) {
                        int boardX = activeFigure.getPosition().x() + x;
                        int boardY = activeFigure.getPosition().y() + y;
                        if (boardX >= 0 && boardX < Model.boardWidth && boardY >= 0 && boardY < Model.boardHeight) {
                            displayField[boardY][boardX] = activeFigure.getColorCode();
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
        System.out.println("Score: " + gameState.getScore());
        System.out.println("\nControls: A (Left), D (Right), W (Rotate), Space (Drop), Q (Quit)");
    }

    private void handleInput() {
        while (true) {
            if (inputLocked) continue;

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
                    case "2" -> controller.showLeaderBoard();
                    case "3" -> controller.showAbout();
                    case "4" -> {
                        shutdown();
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid option. Choose 1-4.");
                }
                if (!model.isInGame()) {
                    showMenu();
                }
            }
        }
    }
}
