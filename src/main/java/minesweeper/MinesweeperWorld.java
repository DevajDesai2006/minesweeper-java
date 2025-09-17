package minesweeper;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Random;

/**
 * The {@code MinesweeperWorld} class represents the logical and graphical state of a Minesweeper game.
 * <p>
 * It manages the grid of cells, mine placement, neighbor linking, mine counting, and drawing the game board
 * using JavaFX. It also provides game state management such as resetting the board and checking for win conditions.
 * </p>
 */
public class MinesweeperWorld {
  public int rows;
  public int cols;
  public int mines;
  public int cellSize = 30;
  public Cell[][] grid;
  public boolean gameOver = false;
  public boolean difficultySelect = true;
  public Pane root;
  public Random rand = new Random();
  public int totalMines;
  public int safeTilesLeft;

  /**
   * Constructs a new {@code MinesweeperWorld} with the given dimensions, mine count, and JavaFX root pane.
   * Initializes and resets the game board.
   *
   * @param rows  the number of rows in the grid
   * @param cols  the number of columns in the grid
   * @param mines the number of mines to place on the board
   * @param root  the JavaFX {@code Pane} to which the game board will be drawn
   */
  public MinesweeperWorld(int rows, int cols, int mines, Pane root) {
    this.rows = rows;
    this.cols = cols;
    this.mines = mines;
    this.root = root;
    reset();
  }

  /**
   * Resets the game board by clearing the grid, re-initializing all {@code Cell} objects,
   * placing mines, linking cell neighbors, counting adjacent mines, and drawing the grid if a root pane is present.
   * Also resets the game over state.
   */
  public void reset() {
    if (root != null) {
      root.getChildren().clear();
    }
    grid = new Cell[rows][cols];
    gameOver = false;

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        grid[r][c] = new Cell(r, c);
      }
    }

    placeMines();
    linkNeighbors();
    countMines();

    totalMines = mines;
    safeTilesLeft = rows * cols - mines;

    if (root != null) {
      drawGrid();
    }
  }

  /**
   * Randomly places mines on the grid until the specified number of mines has been placed.
   * Each mine is placed in a unique cell that does not already contain a mine.
   */
  private void placeMines() {
    int placed = 0;
    while (placed < mines) {
      int r = rand.nextInt(rows);
      int c = rand.nextInt(cols);
      if (!grid[r][c].hasMine) {
        grid[r][c].hasMine = true;
        placed++;
      }
    }
  }

  /**
   * Links each cell in the grid to its neighboring cells, allowing for adjacency checks.
   * Neighbors are cells that are horizontally, vertically, or diagonally adjacent.
   */
  private void linkNeighbors() {
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        Cell cell = grid[r][c];
        for (int dr = -1; dr <= 1; dr++) {
          for (int dc = -1; dc <= 1; dc++) {
            int nr = r + dr;
            int nc = c + dc;
            if ((dr != 0 || dc != 0) && nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
              cell.addNeighbor(grid[nr][nc]);
            }
          }
        }
      }
    }
  }

  /**
   * For each cell in the grid, counts the number of adjacent mines and stores the result in the cell.
   * This prepares the board for gameplay by setting the adjacent mine counts.
   */
  private void countMines() {
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        grid[r][c].countAdjacentMines();
      }
    }
  }

  /**
   * Counts the number of safe (non-mine) tiles that are still unrevealed.
   *
   * @return the number of safe tiles remaining
   */
  public int countRemainingSafeTiles() {
    int count = 0;
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        Cell cell = grid[r][c];
        if (!cell.hasMine && !cell.isRevealed) {
          count++;
        }
      }
    }
    return count;
  }


  /**
   * Draws the Minesweeper grid onto the JavaFX root pane.
   * Each cell is rendered as a rectangle with appropriate color and text indicating its state (hidden, revealed, flagged, or mined).
   * If the root pane is {@code null}, this method does nothing.
   */
  public void drawGrid() {
    if (root == null) {
      return;
    }
    root.getChildren().clear();

    if (difficultySelect) {
      Text msg = new Text("Select a difficulty: E = easy, M = medium, H = hard");
      // Center message more clearly with padding adjustment
      msg.setX(cols * cellSize / 4.0);
      msg.setY(rows * cellSize / 2.0);
      root.getChildren().add(msg);
      return;
    }

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        Cell cell = grid[r][c];
        Rectangle rect = new Rectangle(c * cellSize, r * cellSize, cellSize, cellSize);
        rect.setStroke(Color.BLACK);
        rect.setFill(cell.isRevealed ? Color.LIGHTGRAY : Color.CYAN);

        Text text = getText(c, r, cell);

        root.getChildren().addAll(rect, text);
      }
    }
    Text status = new Text("Mines: " + totalMines + "  Safe Tiles Left: " + safeTilesLeft);
    status.setX(10);
    status.setY(rows * cellSize + 20);
    root.getChildren().add(status);
  }

  /**
   * Creates a JavaFX {@code Text} object representing the content of a single cell on the Minesweeper grid.
   * The text is positioned within the cell based on its row and column.
   *
   * @param c    the column index of the cell
   * @param r    the row index of the cell
   * @param cell the {@code Cell} object containing the state of this grid cell
   * @return a {@code Text} object to be added to the JavaFX root pane
   */
  private Text getText(int c, int r, Cell cell) {
    Text text = new Text();
    text.setX(c * cellSize + cellSize / 4.0);
    text.setY(r * cellSize + cellSize * 0.75);
    if (cell.isRevealed) {
      if (cell.hasMine) {
        text.setText("*");
        text.setFill(Color.RED);
      } else if (cell.adjacentMines > 0) {
        text.setText(String.valueOf(cell.adjacentMines));
        text.setFill(Color.BLACK); // Set number color for better visibility
      }
    } else if (cell.isFlagged) {
      text.setText("F");
      text.setFill(Color.ORANGE);
    }
    return text;
  }

  /**
   * Checks if the player has won the game by revealing all non-mine cells.
   * If all non-mine cells are revealed, sets {@code gameOver} to {@code true}.
   */
  public void checkWin() {
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        if (!grid[r][c].hasMine && !grid[r][c].isRevealed) {
          return;
        }
      }
    }
    gameOver = true;
  }
}