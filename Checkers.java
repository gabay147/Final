package game;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Checkers extends JFrame {
    private static final long serialVersionUID = 1L;

    public static final int ROWS = 8;
    public static final int COLS = 8;
    public static final int CELL = 8;
    public static final int CELL_SIZE = 80;
    public static final int CANVAS_WIDTH = CELL_SIZE * COLS;
    public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;
    public static final int GRID_WIDTH = 8;
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;
    public static final int CELL_PADDING = CELL_SIZE / 6;
    public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;
    public static final int SYMBOL_STROKE_WIDTH = 8;

    public enum GameState {
        PLAYING, DRAW, RED_WON, BLUE_WON;
    }

    private GameState currentState;

    public enum Seed {
        EMPTY, RED, BLUE, RED_KING, BLUE_KING;
    }

    private Seed currentPlayer;
    private Seed idlePlayer;

    private Seed[][] board;
    private DrawCanvas canvas;

    private JLabel statusBar;

    private int row;
    private int col;

    public int getRow() {
        return this.row;
    }
    public int getCol() {
        return this.col;
    }

    public void setRow(int value) {
        this.row = value;
    }
    
    public void setCol(int value) {
        this.col = value;
    }

    public Checkers() {
        canvas = new DrawCanvas();
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));


        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent remove) {
                int mouseX = remove.getX();
                int mouseY = remove.getY();

                int rowSelected = mouseY / CELL_SIZE;
                int colSelected = mouseX / CELL_SIZE;

                if (currentState == GameState.PLAYING) {
                    if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0 && colSelected < COLS && board[rowSelected][colSelected] == currentPlayer) {
                        board[rowSelected][colSelected] = Seed.EMPTY;
                        setRow(rowSelected);
                        setCol(colSelected);
                        repaint();
                    }
                    else if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0 && colSelected < COLS && board[rowSelected][colSelected] == idlePlayer) {
                        board[rowSelected][colSelected] = idlePlayer;
                    }
                    else if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0 && colSelected < COLS && board[rowSelected][colSelected] == Seed.EMPTY) {
                        board[rowSelected][colSelected] = Seed.EMPTY;
                    }

                }
                
            }
        });
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent place) {
                int mouseX1 = place.getX();
                int mouseY1 = place.getY();
                int mouseY2 = getRow();
                int mouseX2 = getCol();
                int rowSelected1 = mouseY1 / CELL_SIZE;
                int colSelected1 = mouseX1 / CELL_SIZE;
                int rowJump;
                int colJump;

                if (currentState == GameState.PLAYING) {
                    if (rowSelected1 >= 0 && rowSelected1 < ROWS && colSelected1 >= 0 && colSelected1 < COLS && 
                            (rowSelected1 == mouseY2 + 1 && colSelected1 == mouseX2 + 1) 
                            || (rowSelected1 == mouseY2 - 1 && colSelected1 == mouseX2 - 1) 
                            || (rowSelected1 == mouseY2 - 1 && colSelected1 == mouseX2 + 1) 
                            || (rowSelected1 == mouseY2 + 1 && colSelected1 == mouseX2 - 1) 
                            && board[rowSelected1][colSelected1] == Seed.EMPTY) {
                        if((rowSelected1 == mouseY2 + 1 && colSelected1 == mouseX2 + 1) && board[rowSelected1][colSelected1] == idlePlayer) {
                            rowJump = mouseY2 + 2;
                            colJump = mouseX2 + 2;
                            if(board[rowJump][colJump] == idlePlayer) {
                                board[mouseY2][mouseX2] = currentPlayer;
                            }
                            else {
                                board[rowSelected1][colSelected1] = Seed.EMPTY;
                                board[rowJump][colJump] = currentPlayer;
                                updateGame(currentPlayer, rowJump, colJump);
                                currentPlayer = (currentPlayer == Seed.RED) ? Seed.BLUE : Seed.RED;
                                idlePlayer = (currentPlayer == Seed.BLUE) ? Seed.RED : Seed.BLUE;
                            }
                        }
                        else if ((rowSelected1 == mouseY2 - 1 && colSelected1 == mouseX2 - 1) && board[rowSelected1][colSelected1] == idlePlayer) {
                            rowJump = mouseY2 - 2;
                            colJump = mouseX2 - 2;
                            if(board[rowJump][colJump] == idlePlayer) {
                                board[mouseY2][mouseX2] = currentPlayer;
                            }
                            else {
                                board[rowSelected1][colSelected1] = Seed.EMPTY;
                                board[rowJump][colJump] = currentPlayer;
                                updateGame(currentPlayer, rowJump, colJump);
                                currentPlayer = (currentPlayer == Seed.RED) ? Seed.BLUE : Seed.RED;
                                idlePlayer = (currentPlayer == Seed.BLUE) ? Seed.RED : Seed.BLUE;
                            }
                        }
                        else if ((rowSelected1 == mouseY2 - 1 && colSelected1 == mouseX2 + 1) && board[rowSelected1][colSelected1] == idlePlayer) {
                            rowJump = mouseY2 - 2;
                            colJump = mouseX2 + 2;
                            if(board[rowJump][colJump] == idlePlayer) {
                                board[mouseY2][mouseX2] = currentPlayer;
                            }
                            else {
                                board[rowSelected1][colSelected1] = Seed.EMPTY;
                                board[rowJump][colJump] = currentPlayer;
                                updateGame(currentPlayer, rowJump, colJump);
                                currentPlayer = (currentPlayer == Seed.RED) ? Seed.BLUE : Seed.RED;
                                idlePlayer = (currentPlayer == Seed.BLUE) ? Seed.RED : Seed.BLUE;
                            }
                        }
                        else if ((rowSelected1 == mouseY2 + 1 && colSelected1 == mouseX2 - 1) && board[rowSelected1][colSelected1] == idlePlayer) {
                            rowJump = mouseY2 + 2;
                            colJump = mouseX2 - 2;
                            if(board[rowJump][colJump] == idlePlayer) {
                                board[mouseY2][mouseX2] = currentPlayer;
                            }
                            else {
                                board[rowSelected1][colSelected1] = Seed.EMPTY;
                                board[rowJump][colJump] = currentPlayer;
                                updateGame(currentPlayer, rowJump, colJump);
                                currentPlayer = (currentPlayer == Seed.RED) ? Seed.BLUE : Seed.RED;
                                idlePlayer = (currentPlayer == Seed.BLUE) ? Seed.RED : Seed.BLUE;
                            }
                        }
                        else{
                        board[rowSelected1][colSelected1] = currentPlayer;
                        updateGame(currentPlayer, rowSelected1, colSelected1);
                        

                        currentPlayer = (currentPlayer == Seed.RED) ? Seed.BLUE : Seed.RED;
                        idlePlayer = (currentPlayer == Seed.BLUE) ? Seed.RED : Seed.BLUE;
                        }
                        
                    }
                    else {
                        board[mouseY2][mouseX2] = currentPlayer;
                    }
                } else {
                    initGame();
                }

                repaint();
            }
        });

        statusBar = new JLabel("  ");
        statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
        statusBar.setBorder(BorderFactory.createEmptyBorder( 2, 5, 4, 5));

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(canvas, BorderLayout.CENTER);
        cp.add(statusBar, BorderLayout.PAGE_END);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        setTitle("Checkers");
        setVisible(true);
        board = new Seed[ROWS][COLS];

        initGame();
    } 
    public void initGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if(row % 2 == 0) {
                    if(col % 2 == 0) {
                        board[row][col] = Seed.EMPTY;
                    }
                }
                if(row % 2 == 1){
                    if (col % 2 == 1) {
                        board[row][col] = Seed.EMPTY;
                    }
                }
                board[0][0] = Seed.RED;
                board[0][2] = Seed.RED;
                board[0][4] = Seed.RED;
                board[0][6] = Seed.RED;

                board[1][1] = Seed.RED;
                board[1][3] = Seed.RED;
                board[1][5] = Seed.RED;
                board[1][7] = Seed.RED;

                board[2][0] = Seed.RED;
                board[2][2] = Seed.RED;
                board[2][4] = Seed.RED;
                board[2][6] = Seed.RED;

                board[5][1] = Seed.BLUE;
                board[5][3] = Seed.BLUE;
                board[5][5] = Seed.BLUE;
                board[5][7] = Seed.BLUE;

                board[6][0] = Seed.BLUE;
                board[6][2] = Seed.BLUE;
                board[6][4] = Seed.BLUE;
                board[6][6] = Seed.BLUE;

                board[7][1] = Seed.BLUE;
                board[7][3] = Seed.BLUE;
                board[7][5] = Seed.BLUE;
                board[7][7] = Seed.BLUE;
            }
        }
        currentState = GameState.PLAYING;
        currentPlayer = Seed.RED;
        idlePlayer = Seed.BLUE;

    }
    public void updateGame(Seed theSeed, int rowSelected, int colSelected) {
        if (hasWon(theSeed, rowSelected, colSelected) == Seed.RED) {
            currentState = GameState.RED_WON;
        } else if (hasWon(theSeed, rowSelected, colSelected) == Seed.BLUE) {
            currentState = GameState.BLUE_WON;
        }
    }
    public boolean isDraw() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (board[row][col] == Seed.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
    public Seed hasWon (Seed theSeed, int rowSelected, int colSelected) {
        //TODO: Modify hasWon to be a victory for the game "Checkers," not Tic-Tac-Toe
        int countRed = 0;
        int countBlue = 0;
        for(row = 0; row <= 7; row++) {
            for(col = 0; col <= 7; col++) {
                if(board[row][col] == Seed.RED) {
                    countRed++;
                }
                else if(board[row][col] == Seed.BLUE) {
                    countBlue++;
                }
                else {
                    continue;
                }
            }
        }
        if(countRed == 0) {
            return Seed.BLUE;
        }
        else if(countBlue == 0) {
            return Seed.RED;
        }
        else {
            return Seed.EMPTY;
        }
    }

    class DrawCanvas extends JPanel {
        private static final long serialVersionUID = 6845978118490338769L;

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.BLACK);

            /*g.setColor(Color.WHITE);
                for (int cell = 0; cell < CELL; ++cell) {
                    g.fillRect(CELL_SIZE * cell, CELL_SIZE * cell, CELL_SIZE, CELL_SIZE); //PAINTS CELLS BLUE, DIAGONAL FROM TOP LEFT CORNER TO LOWER RIGHT CORNER.
                    g.fillRect((80*2) + CELL_SIZE * cell, CELL_SIZE * cell, CELL_SIZE, CELL_SIZE); //PAINTS CELL BLUE, DIAGONAL FROM TOP LEFT (TWO TO THE RIGHT) TO BOTTOM
                    g.fillRect((80*4) + CELL_SIZE * cell, CELL_SIZE * cell, CELL_SIZE, CELL_SIZE);
                    g.fillRect((80*6) + CELL_SIZE * cell, CELL_SIZE * cell, CELL_SIZE, CELL_SIZE);
                    g.fillRect((80*8) + CELL_SIZE * cell, CELL_SIZE * cell, CELL_SIZE, CELL_SIZE);
                    g.fillRect(CELL_SIZE * cell, (80*2) + CELL_SIZE * cell, CELL_SIZE, CELL_SIZE); //PAINTS CELL BLUE, DIAGONAL FROM TOP LEFT (TWO TO THE LEFT/DOWN) TO BOTTOM
                    g.fillRect(CELL_SIZE * cell, (80*4) + CELL_SIZE * cell, CELL_SIZE, CELL_SIZE);
                    g.fillRect(CELL_SIZE * cell, (80*6) + CELL_SIZE * cell, CELL_SIZE, CELL_SIZE);
                    g.fillRect(CELL_SIZE * cell, (80*8) + CELL_SIZE * cell, CELL_SIZE, CELL_SIZE); //LINES 163-178 COMPLETED ON TUESDAY, MARCH 31st 2015
                }*/
            Graphics2D g2d = (Graphics2D)g;
            g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int row = 0; row < ROWS; ++row) {
                for (int col = 0; col < COLS; ++col) {
                    int x1 = col * CELL_SIZE + CELL_PADDING;
                    int y1 = row * CELL_SIZE + CELL_PADDING;

                    int x2 = col * CELL_SIZE;
                    int y2 = row * CELL_SIZE;
                    if (board[row][col] == Seed.EMPTY) {
                        g2d.setColor(Color.LIGHT_GRAY);
                        for (int cell = 0; cell < CELL; ++cell) {
                            g2d.fillRect(x2, y2, CELL_SIZE, CELL_SIZE);
                        }
                    }

                    else if (board[row][col] == Seed.RED) {
                        g2d.setColor(Color.LIGHT_GRAY);
                        g2d.fillRect(x2, y2, CELL_SIZE, CELL_SIZE);
                        g2d.setColor(Color.RED);
                        g2d.fillOval(x1, y1, SYMBOL_SIZE, SYMBOL_SIZE);
                    }
                    else if (board[row][col] == Seed.BLUE) {
                        g2d.setColor(Color.LIGHT_GRAY);
                        g2d.fillRect(x2, y2, CELL_SIZE, CELL_SIZE);
                        g2d.setColor(Color.BLUE);
                        g2d.fillOval(x1, y1, SYMBOL_SIZE, SYMBOL_SIZE);
                    }
                }
            }
            if (currentState == GameState.PLAYING) {
                statusBar.setForeground(Color.BLUE);
                if (currentPlayer == Seed.RED) {
                    statusBar.setText("RED's Turn!");
                }
                else {
                    statusBar.setText("BLUEs's Turn!");
                }
            }
            else if (currentState == GameState.DRAW) {
                statusBar.setForeground(Color.RED);
                statusBar.setText("Draw! Click to Play Again!");
            }
            else if (currentState == GameState.RED_WON) {
                statusBar.setForeground(Color.RED);
                statusBar.setText("RED Won! Click to Play Again");
            }
            else if (currentState == GameState.BLUE_WON) {
                statusBar.setForeground(Color.RED);
                statusBar.setText("BLUE Won! Click to PLay Again!");
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Checkers();
            }
        });
    }
}