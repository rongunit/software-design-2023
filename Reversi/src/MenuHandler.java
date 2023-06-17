import java.util.Scanner;

import static java.lang.System.exit;
import static java.lang.System.setIn;

public class MenuHandler {
    static final String[] MAIN_MENU = {
            "1 - Start game",
            "2 - Change settings",
            "3 - View highest score",
            "4 - Exit"
    };
    static final String[] SETTINGS_MENU = {
            "1 - Select side",
            "2 - Select difficulty",
            "3 - Select game mode",
            "4 - Back"
    };
    static final String[] MODE_MENU = {
            "1 - Player vs Computer",
            "2 - Player vs Player",
            "3 - Back"
    };
    static final String[] SIDE_MENU = {
            "1 - Black",
            "2 - White",
            "3 - Back"
    };
    static final String[] DIFFICULTY_MENU = {
            "1 - Easy",
            "2 - Hard",
            "3 - Back"
    };
    Character side;
    Scanner scanner;

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    private int difficulty;
    int mode;

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    private int highScore;

    public MenuHandler() {
        scanner = new Scanner(System.in);
        side = 'b';
        setDifficulty(1);
        mode = 1;
    }

    public Game processMainMenu() {
        displayTab(MAIN_MENU);
        int option = getOption(4);
        switch (option) {
            case 2 -> processSettings();
            case 3 -> processResults();
            case 4 -> {
                //scanner.close();
                exit(0);
            }
        }
        return new Game(side, getDifficulty(), mode); //default side = 'b'
    }

    private int getOption(int numOfOptions) {
        int option = 0;
        if (scanner.hasNextInt()) {
            option = scanner.nextInt();
        } else {
            scanner.next();
        }
        while (option < 1 || option > numOfOptions) {
            System.out.print("\nIncorrect input! Try again: ");
            if (scanner.hasNextInt()) {
                option = scanner.nextInt();
            } else {
                scanner.next();
            }
        }
        return option;
    }

    private void processResults() {
        System.out.println(getHighScore());
        System.out.println("1 - Back");
        getOption(1);
        processMainMenu();
    }

    private void processSettings() {
        displayTab(SETTINGS_MENU);
        int option = getOption(4);
        switch (option) {
            case 1 -> processSideMenu();
            case 2 -> processDifficultyMenu();
            case 3 -> processModeMenu();
            case 4 -> processMainMenu();
        }
    }

    private void processModeMenu() {
        displayTab(MODE_MENU);
        int option = getOption(3);
        mode = option == 2 ? 2 : 1;
        processMainMenu();
    }

    private void processDifficultyMenu() {
        displayTab(DIFFICULTY_MENU);
        int option = getOption(3);
        setDifficulty(option == 2 ? 2 : 1);
        processMainMenu();
    }

    private void processSideMenu() {
        displayTab(SIDE_MENU);
        side = getOption(3) == 1 ? 'b' : 'w';
        processMainMenu();
    }

    private void displayTab(String[] options) {
        for (String option : options) {
            System.out.println(option);
        }
        System.out.print("Choose your option: ");
    }

    public void updateLeaderBoard(int maxScore) {
        setHighScore(maxScore);
    }
}
