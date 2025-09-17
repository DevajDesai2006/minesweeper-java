

# Minesweeper – Java Implementation  

This project is a custom implementation of the classic **Minesweeper** game written in Java.  
It provides two different ways to play:  

1. **Terminal-based version** – Play directly in the console with text-based output.  
2. **JavaFX-based version** – Play with a graphical user interface (GUI) powered by JavaFX.  

---

## Features
- Classic Minesweeper rules:
  - Reveal cells to uncover empty tiles or numbers indicating adjacent mines.
  - Flag suspected mines.
  - Win by revealing all safe tiles without triggering a mine.
- **Two modes of play**:
  - **Console Mode** – Color-coded numbers in the terminal using ANSI escape codes.  
  - **JavaFX Mode** – Interactive GUI with mouse clicks and difficulty selection.  
- Multiple difficulty levels (Easy, Medium, Hard).  
- Tracks and displays:
  - Total mines  
  - Flags placed  
  - Safe tiles left  

---

## Terminal Version  

### Run
```bash
java minesweeper.MinesweeperRunTerminal
```

### Gameplay
1. Enter board dimensions and number of mines when prompted.  
2. On each turn, enter:
   - Row number
   - Column number
   - Action (`r` = reveal, `f` = flag)  

Example:
```
Enter row, col, action (r=Reveal, f=Flag): 2 3 r
```

### Display
- `?` → unrevealed tile  
- `F` → flagged tile  
- `*` → mine  
- Numbers (colored) → adjacent mine counts  

---

## JavaFX Version  

### Run
```bash
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml minesweeper.MinesweeperFX
```

*(Replace `/path/to/javafx-sdk/lib` with the actual path to your JavaFX SDK.)*

### Gameplay
- When the game starts, select a difficulty level using your keyboard:
  - `E` = Easy (9×9, 10 mines)  
  - `M` = Medium (16×16, 40 mines)  
  - `H` = Hard (16×30, 99 mines)  
- Use your mouse:
  - **Left-click** = reveal a cell  
  - **Right-click** = flag/unflag a cell  
- The bottom of the window displays:
  - Total mines  
  - Flags placed  
  - Safe tiles left  

---

## Requirements
- **Java 21** (or compatible version)  
- **JavaFX SDK 21** (for GUI mode)  

---

## Project Structure
- `Cell.java` → Represents individual cells (mine, flag, reveal logic).  
- `MinesweeperWorld.java` → Core game logic and grid management.  
- `MinesweeperRunTerminal.java` → Console-based runner.  
- `MinesweeperFX.java` → JavaFX GUI runner.  
- `MinesweeperTests.java` → Unit tests.  

---

## Future Improvements
- Add timer and score tracking.  
- Implement a restart button in JavaFX mode.  
- Allow custom board sizes via GUI.  