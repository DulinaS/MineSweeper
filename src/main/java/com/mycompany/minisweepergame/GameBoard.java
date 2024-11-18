package com.mycompany.minisweepergame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GameBoard extends JPanel {
    private MiniSweeperGame miniSweeper;
    private JButton[][] buttons;
    private int[][] mineField;
    private boolean[][] revealed;
    private boolean[][] flagged;
    private boolean gameOver;
    private int gridSize;
    private int mineCount;
    private int flagCount;
    private Timer timer;
    private int timeElapsed;

    public GameBoard(MiniSweeperGame miniSweeper, int gridSize, int mineCount) {
        this.miniSweeper = miniSweeper;
        this.gridSize = gridSize;
        this.mineCount = mineCount;
        this.flagCount = mineCount;

        setLayout(new GridLayout(gridSize, gridSize));
        setBackground(new Color(173, 216, 230)); // Light blue background

        initializeGame();
    }

    private void initializeGame() {
        buttons = new JButton[gridSize][gridSize];
        mineField = new int[gridSize][gridSize];
        revealed = new boolean[gridSize][gridSize];
        flagged = new boolean[gridSize][gridSize];
        gameOver = false;

        miniSweeper.updateFlagCounter(flagCount);

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                JButton button = StyleUtils.createRoundedButton();
                int r = row, c = col;
                button.addActionListener(e -> handleLeftClick(r, c));
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            handleRightClick(r, c);
                        }
                    }
                });
                buttons[row][col] = button;
                add(button);
            }
        }

        placeMines();
        calculateAdjacentMines();
        startTimer();
    }

    private void placeMines() {
        Random rand = new Random();
        int placed = 0;
        while (placed < mineCount) {
            int row = rand.nextInt(gridSize);
            int col = rand.nextInt(gridSize);
            if (mineField[row][col] == 0) {
                mineField[row][col] = -1;
                placed++;
            }
        }
    }

    private void calculateAdjacentMines() {
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (mineField[row][col] == -1) continue;
                int count = 0;
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        int nr = row + dr, nc = col + dc;
                        if (nr >= 0 && nr < gridSize && nc >= 0 && nc < gridSize && mineField[nr][nc] == -1) {
                            count++;
                        }
                    }
                }
                mineField[row][col] = count;
            }
        }
    }

    private void startTimer() {
        timeElapsed = 0;
        timer = new Timer(1000, e -> {
            timeElapsed++;
            miniSweeper.updateTimer(String.valueOf(timeElapsed));
        });
        timer.start();
    }

    private void handleLeftClick(int row, int col) {
        if (gameOver || flagged[row][col] || revealed[row][col]) return;

        if (mineField[row][col] == -1) {
            buttons[row][col].setText("X");
            buttons[row][col].setBackground(new Color(255, 0, 0));
            gameOver = true;
            revealAllMines();
            endGame("Game Over! You hit a mine!");
        } else {
            reveal(row, col);
            checkWin();
        }
    }

    private void handleRightClick(int row, int col) {
        if (gameOver || revealed[row][col]) return;

        if (!flagged[row][col]) {
            if (flagCount > 0) {
                buttons[row][col].setText("F");
                buttons[row][col].setBackground(new Color(255, 215, 0));
                flagged[row][col] = true;
                flagCount--;
                miniSweeper.updateFlagCounter(flagCount);
            }
        } else {
            buttons[row][col].setText("");
            buttons[row][col].setBackground(new Color(240, 240, 240));
            flagged[row][col] = false;
            flagCount++;
            miniSweeper.updateFlagCounter(flagCount);
        }
    }

    private void reveal(int row, int col) {
        if (row < 0 || row >= gridSize || col < 0 || col >= gridSize || revealed[row][col] || flagged[row][col]) return;

        revealed[row][col] = true;
        JButton button = buttons[row][col];

        // Make the button look revealed
        button.setBackground(new Color(220, 220, 220)); // Light gray background for revealed cells
        button.setBorder(new LineBorder(Color.BLACK)); //Borders between cells

        if (mineField[row][col] > 0) {
            // Display the number of adjacent mines
            button.setText(String.valueOf(mineField[row][col]));
            button.setForeground(getNumberColor(mineField[row][col]));
            button.setFont(new Font("Verdana", Font.BOLD, 16));
        } else if (mineField[row][col] == 0) {
            // reveal surrounding cells
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    reveal(row + dr, col + dc);
                }
            }
        }

    
        for (ActionListener al : button.getActionListeners()) {
            button.removeActionListener(al);
        }
        button.setFocusPainted(false);
    }

    //Colours for revealing numbers
    private Color getNumberColor(int number) {
        switch (number) {
            case 1: return Color.BLUE;
            case 2: return new Color(34, 139, 34);
            case 3: return Color.RED;
            case 4: return new Color(72, 61, 139);
            case 5: return new Color(139, 0, 0);
            case 6: return new Color(0, 139, 139);
            case 7: return Color.BLACK;
            case 8: return Color.GRAY;
            default: return Color.BLACK;
        }
    }

    private void revealAllMines() {
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (mineField[row][col] == -1) {
                    buttons[row][col].setText("X");
                    buttons[row][col].setBackground(new Color(255, 0, 0));
                }
            }
        }
    }

    private void checkWin() {
        int revealedCount = 0;
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (revealed[row][col]) revealedCount++;
            }
        }
        if (revealedCount == gridSize * gridSize - mineCount) {
            gameOver = true;
            endGame("Congratulations! You won!");
        }
    }

    private void endGame(String message) {
        timer.stop();
        int option = JOptionPane.showOptionDialog(
                this,
                message + "\nWould you like to start a new game or quit?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"New Game", "Quit"},
                "New Game"
        );

        if (option == JOptionPane.YES_OPTION) {
            miniSweeper.resetGame();
        } else {
            System.exit(0);
        }
    }
}
