import minesweeper.Cell;
import minesweeper.MinesweeperWorld;
import org.junit.Test;
import static org.junit.Assert.*;

import javafx.scene.layout.Pane;

public class MinesweeperTests {

  @Test
  public void testAddNeighbor() {
    Cell c1 = new Cell(0,0);
    Cell c2 = new Cell(0,1);
    c1.addNeighbor(c2);
    assertEquals(1, c1.neighbors.size());
    assertEquals(c2, c1.neighbors.get(0));
    assertNotEquals(new Cell(1,1), c1.neighbors.get(0));
  }

  @Test
  public void testCountAdjacentMines() {
    Cell c1 = new Cell(0,0);
    Cell c2 = new Cell(0,1);
    c2.hasMine = true;
    c1.addNeighbor(c2);
    c1.countAdjacentMines();
    assertEquals(1, c1.adjacentMines);

    c2.hasMine = false;
    c1.countAdjacentMines();
    assertEquals(0, c1.adjacentMines);
  }

  @Test
  public void testReveal() {
    Cell c1 = new Cell(0,0);
    Cell c2 = new Cell(0,1);
    c1.addNeighbor(c2);
    c1.reveal();
    assertTrue(c1.isRevealed);

    c1.isRevealed = false;
    c1.isFlagged = true;
    c1.reveal();
    assertFalse(c1.isRevealed); // flagged cells should not reveal
  }

  @Test
  public void testRecursiveReveal() {
    Cell center = new Cell(1,1);
    Cell neighbor1 = new Cell(1,0);
    Cell neighbor2 = new Cell(0,1);
    center.addNeighbor(neighbor1);
    center.addNeighbor(neighbor2);

    center.countAdjacentMines(); // 0 mines
    neighbor1.countAdjacentMines();
    neighbor2.countAdjacentMines();

    center.reveal();
    assertTrue(center.isRevealed);
    assertTrue(neighbor1.isRevealed);
    assertTrue(neighbor2.isRevealed);
  }

  @Test
  public void testFlagging() {
    Cell c1 = new Cell(0,0);
    assertFalse(c1.isFlagged);
    c1.isFlagged = true;
    assertTrue(c1.isFlagged);
    c1.isFlagged = false;
    assertFalse(c1.isFlagged);
  }

  @Test
  public void testMinePlacement() {
    Pane root = new Pane();
    MinesweeperWorld world = new MinesweeperWorld(3,3,2, root);

    int mineCount = 0;
    for (int r=0; r<world.rows; r++) {
      for (int c=0; c<world.cols; c++) {
        if (world.grid[r][c].hasMine) mineCount++;
      }
    }
    assertEquals(2, mineCount);
  }

  @Test
  public void testWinConditionAndReset() {
    Pane root = new Pane();
    MinesweeperWorld world = new MinesweeperWorld(2,2,1, root);

    // Reveal all non-mine cells to trigger win
    for (int r=0; r<world.rows; r++) {
      for (int c=0; c<world.cols; c++) {
        if (!world.grid[r][c].hasMine) world.grid[r][c].isRevealed = true;
      }
    }
    world.checkWin();
    assertTrue(world.gameOver);

    // Reset the board
    world.reset();
    for (int r=0; r<world.rows; r++) {
      for (int c=0; c<world.cols; c++) {
        assertFalse(world.grid[r][c].isRevealed);
      }
    }
    assertFalse(world.gameOver);
  }
}