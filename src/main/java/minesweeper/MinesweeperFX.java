package minesweeper;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * A JavaFX-based Minesweeper game.
 * <p>
 * This class launches a graphical Minesweeper game using JavaFX.
 * Players can select difficulty using the keyboard (E, M, H),
 * reveal cells with left-click, and flag cells with right-click.
 * The game board updates visually after each interaction.
 * </p>
 */
public class MinesweeperFX extends Application {
  private int rows = 9;
  private int cols = 9;
  private int mines = 10;
  private Pane root = new Pane();
  private MinesweeperWorld world;

  /**
   * The main entry point for the JavaFX application.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Starts the JavaFX application, initializes the game world,
   * sets up the scene, and registers event handlers for keyboard
   * and mouse interactions.
   *
   * @param stage the primary stage for this application
   */
  @Override
  public void start(Stage stage) {
    // Initialize the game world without generating the board yet
    world = new MinesweeperWorld(rows, cols, mines, root);

    // Enable difficulty selection mode
    world.difficultySelect = true;

    // Set up scene size based on board
    root.setPrefSize(cols * 30, rows * 30 + 50);
    Scene scene = new Scene(root);
    stage.setTitle("Minesweeper");
    stage.setScene(scene);
    stage.show();

    // Initially draw the prompt to select difficulty
    world.drawGrid();

    // Keyboard event handler for selecting difficulty
    scene.setOnKeyPressed(e -> {
      if (world.difficultySelect) {
        switch (e.getText().toLowerCase()) {
          case "e" -> {
            rows = 9;
            cols = 9;
            mines = 10;

            world.rows = rows;
            world.cols = cols;
            world.mines = mines;

            world.difficultySelect = false;
            world.reset();
          }
          case "m" -> {
            rows = 16;
            cols = 16;
            mines = 40;

            world.rows = rows;
            world.cols = cols;
            world.mines = mines;

            world.difficultySelect = false;
            world.reset();
          }
          case "h" -> {
            rows = 16;
            cols = 30;
            mines = 99;

            world.rows = rows;
            world.cols = cols;
            world.mines = mines;

            world.difficultySelect = false;
            world.reset();
          }
        }
      }
    });

    // Mouse click event handler for revealing or flagging cells
    root.setOnMouseClicked(e -> {
      if (world.gameOver) {
        world.reset();
      } else {
        int col = (int) (e.getX() / world.cellSize);
        int row = (int) (e.getY() / world.cellSize);

        if (row < rows && col < cols) {
          Cell clicked = world.grid[row][col];

          if (e.getButton() == MouseButton.PRIMARY) {
            if (clicked.hasMine) {
              clicked.isRevealed = true;
              world.gameOver = true;
            } else {
              clicked.reveal();
              world.safeTilesLeft = world.countRemainingSafeTiles();
              world.checkWin();
            }
            world.drawGrid();
          } else if (e.getButton() == MouseButton.SECONDARY) {
            clicked.isFlagged = !clicked.isFlagged;
            world.drawGrid();
          }
        }
      }
    });
  }
}