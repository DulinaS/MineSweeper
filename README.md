# Minesweeper Game

A simple Java implementation of the classic Minesweeper game. This project uses the Swing framework to create an interactive GUI for players to uncover cells, flag mines, and avoid detonating them.

---

## Features

- **Game Modes**:
  - Easy (10x10 grid with 10 mines)
  - Medium (15x15 grid with 20 mines)

- **Graphical Interface**:
  - Clean and modern UI with gradient backgrounds.
  - Numbered cells with distinct colors for readability.
  - Black borders between cells for visual clarity.

- **Gameplay**:
  - Left-click to reveal a cell.
  - Right-click to flag or unflag a cell.
  - Automatic recursive reveal for empty cells.

- **Game Over Handling**:
  - Popup window when a mine is hit, offering options to start a new game or quit.
  - Difficulty selection for new games.

- **Timer and Flag Counter**:
  - Displays elapsed time.
  - Tracks remaining flags.

---

## Prerequisites

To run this project, you need:
- **Java Development Kit (JDK)**: Version 8 or higher.
- An IDE like IntelliJ IDEA, NetBeans, Eclipse, or VS Code, or a terminal to compile and run the project.

---

## How to Run

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/DulinaS/MineSweeper.git
   cd MineSweeper
2.**Compile the Code: Navigate to the project directory and compile the Java files:**
    javac -d bin src/com/mycompany/minisweeper/*.java
3.**Run the Game**
    java -cp bin com.mycompany.minisweeper.MiniSweeperGame


