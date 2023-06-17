public class App {

    private Game game;
    private MenuHandler menuHandler;
    public App() {
        menuHandler = new MenuHandler();
    }
    public void startApp() {
        int maxScore = 0;
        while (true) {
            game = menuHandler.processMainMenu();
            int score = game.processGame();
            if (score > maxScore) {
                maxScore = score;
                menuHandler.updateLeaderBoard(maxScore);
            }
        }
    }
}
