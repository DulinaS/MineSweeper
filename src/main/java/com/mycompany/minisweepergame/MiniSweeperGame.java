package com.mycompany.minisweepergame;

import javax.swing.*;
import java.awt.*;

public class MiniSweeperGame extends JFrame {
    private GameBoard gameBoard;
    private JLabel timerLabel;
    private JLabel flagCounterLabel;

    public MiniSweeperGame() {
        setTitle("MiniSweeper");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel
        JPanel topPanel = StyleUtils.createGradientPanel(new Color(34, 139, 34), new Color(60, 179, 113));
        topPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("MiniSweeper");
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);

        timerLabel = new JLabel("Time: 0");
        timerLabel.setFont(new Font("Verdana", Font.BOLD, 16));
        timerLabel.setForeground(Color.YELLOW);
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        flagCounterLabel = new JLabel("Flags: 0");
        flagCounterLabel.setFont(new Font("Verdana", Font.BOLD, 16));
        flagCounterLabel.setForeground(Color.RED);
        flagCounterLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(timerLabel, BorderLayout.CENTER);
        topPanel.add(flagCounterLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Window close confirmation
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                int option = JOptionPane.showOptionDialog(
                        MiniSweeperGame.this,
                        "Do you want to quit the application or start a new game?",
                        "Quit Confirmation",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[]{"Quit", "New Game", "Cancel"},
                        "Cancel"
                );

                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                } else if (option == JOptionPane.NO_OPTION) {
                    resetGame();
                }
            }
        });

        setSize(750, 750);
        setLocationRelativeTo(null); // Center the window
        setResizable(false);
        promptGameMode();

        setVisible(true);
    }

    private void promptGameMode() {
        String[] options = {"Easy (10x10, 10 Mines)", "Medium (15x15, 20 Mines)"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose Game Difficulty:",
                "Select Difficulty",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            startGame(10, 10);
        } else if (choice == 1) {
            startGame(15, 20);
        } else {
            System.exit(0);
        }
    }

    private void startGame(int size, int mines) {
        if (gameBoard != null) {
            remove(gameBoard);
        }
        gameBoard = new GameBoard(this, size, mines);
        add(gameBoard, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void updateTimer(String time) {
        timerLabel.setText("Time: " + time);
    }

    public void updateFlagCounter(int flagsRemaining) {
        flagCounterLabel.setText("Flags: " + flagsRemaining);
    }

    public void resetGame() {
        promptGameMode();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MiniSweeperGame::new);
    }
}
