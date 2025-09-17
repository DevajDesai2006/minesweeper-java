package minesweeper;

import java.util.ArrayList;

public class Cell {
  public boolean hasMine;
  public boolean isRevealed;
  public boolean isFlagged;
  public int adjacentMines;
  public ArrayList<Cell> neighbors;
  public int row, col;

  public Cell(int row, int col) {
    this.row = row;
    this.col = col;
    this.hasMine = false;
    this.isRevealed = false;
    this.isFlagged = false;
    this.adjacentMines = 0;
    this.neighbors = new ArrayList<>();
  }

  public void addNeighbor(Cell other) {
    neighbors.add(other);
  }

  public void countAdjacentMines() {
    int count = 0;
    for (Cell c : neighbors) {
      if (c.hasMine) count++;
    }
    this.adjacentMines = count;
  }


  public void reveal() {
    if (isRevealed || isFlagged) return;
    isRevealed = true;

    if (adjacentMines == 0 && !hasMine) {
      for (Cell c : neighbors) c.reveal();
    }
  }
}