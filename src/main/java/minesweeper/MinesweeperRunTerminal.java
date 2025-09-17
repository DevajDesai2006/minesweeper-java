package minesweeper;

import java.util.Scanner;

/**
 * A console-based Minesweeper game runner.
 * <p>
 * This class allows the user to play Minesweeper in the terminal by specifying
 * the number of rows, columns, and mines. Players can reveal cells or flag them.
 * The board is updated and displayed after each action, with color coding for numbers
 * representing the count of adjacent mines.
 * </p>
 */
public class MinesweeperRunTerminal {

  /**
   * The main entry point for the Minesweeper console game.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    System.out.println("Welcome to Minesweeper!");
    System.out.print("Enter rows: ");
    int rows = sc.nextInt();
    System.out.print("Enter cols: ");
    int cols = sc.nextInt();
    System.out.print("Enter number of mines: ");
    int mines = sc.nextInt();

    // Initialize game world
    MinesweeperWorld world = new MinesweeperWorld(rows, cols, mines, null);

    // Game loop
    while (!world.gameOver) {
      printBoard(world);
      System.out.print("Enter row, col, action (r=Reveal, f=Flag): ");
      int row = sc.nextInt();
      int col = sc.nextInt();
      String action = sc.next();

      if (row < 0 || row >= rows || col < 0 || col >= cols) {
        System.out.println("Invalid coordinates, try again.");
        continue;
      }

      Cell cell = world.grid[row][col];

      if (action.equalsIgnoreCase("r")) {
        if (cell.hasMine) {
          cell.isRevealed = true;
          world.gameOver = true;
          System.out.println("Boom! You hit a mine. Game over.");
        } else {
          cell.reveal();
          world.checkWin();
          if (world.gameOver) {
            System.out.println("Congratulations! You cleared the board.");
          }
        }
      } else if (action.equalsIgnoreCase("f")) {
        cell.isFlagged = !cell.isFlagged;
      } else {
        System.out.println("Unknown action, use 'r' to reveal or 'f' to flag.");
      }
    }

    printBoard(world);
    sc.close();
  }

  /**
   * Prints the current state of the Minesweeper board to the console.
   * Revealed cells display the number of adjacent mines in color.
   * Flagged cells are shown with 'F', unrevealed cells with '?'.
   *
   * @param world the MinesweeperWorld containing the game grid
   */
  private static void printBoard(MinesweeperWorld world) {
    System.out.println();
    for (int r = 0; r < world.rows; r++) {
      for (int c = 0; c < world.cols; c++) {
        Cell cell = world.grid[r][c];
        if (cell.isFlagged) {
          System.out.print("F ");
        } else if (!cell.isRevealed) {
          System.out.print("? ");
        } else if (cell.hasMine) {
          System.out.print("* ");
        } else {
          String color = colorForNumber(cell.adjacentMines);
          System.out.print(color + cell.adjacentMines + "\u001B[0m ");
        }
      }
      System.out.println();
    }
    System.out.println();
  }

  /**
   * Returns an ANSI color code string based on the number of adjacent mines.
   * Used to color the numbers displayed on the console board.
   *
   * @param n the number of adjacent mines
   * @return the ANSI escape code string for the corresponding color
   */
  private static String colorForNumber(int n) {
    return switch (n) {
      case 0 -> "\u001B[30m"; // Black
      case 1 -> "\u001B[32m"; // Green
      case 2 -> "\u001B[34m"; // Blue
      case 3 -> "\u001B[36m"; // Cyan
      case 4 -> "\u001B[33m"; // Yellow
      case 5 -> "\u001B[31m"; // Red
      case 6 -> "\u001B[35m"; // Purple
      case 7 -> "\u001B[37m"; // White
      case 8 -> "\u001B[37m"; // White
      default -> "\u001B[0m"; // Reset / default
    };
  }

}