package checkmate;

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

public class checkers extends JFrame {
    private static final long serialVersionUID = 1L;
    
    public static final int ROWS = 10;
    public static final int COLS = 10;
    public static final int CELL_SIZE = 90;
    public static final int CANVAS_WIDTH = CELL_SIZE * COLS;
    public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;
    public static final int GRID_WIDTH = 8;
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;
    public static final int CELL_PADDING = CELL_SIZE / 6;
    public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;
    public static final int SYMBOL_STROKE_WIDTH = 8;

    public enum GameState {
        PLAYING, DRAW, White_WON, Black_WON;
    }

    private GameState currentState;
    
    public enum Seed {
        EMPTY, White, Black;
    }

    private Seed currentPlayer;
    
    private Seed[][] board;
    private DrawCanvas canvas;

    private JLabel statusBar;

    public checkers() {
        canvas = new DrawCanvas();
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent select) {
                int mouseX = select.getX();
                int mouseY = select.getY();
                
                int rowSelected = mouseY / CELL_SIZE;
                int colSelected = mouseX / CELL_SIZE;
                
                if (currentState == GameState.PLAYING) {
                    if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0 && colSelected < COLS && board[rowSelected][colSelected] == Seed.EMPTY) {
                        board [rowSelected][colSelected] = currentPlayer;
                        updateGame(currentPlayer, rowSelected, colSelected);
                        
                        currentPlayer = (currentPlayer == Seed.White) ? Seed.Black : Seed.White;
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
                    board[row][col] = Seed.EMPTY;
                }
            }
            currentState = GameState.PLAYING;
            currentPlayer = Seed.White;
            
        }
        public void updateGame(Seed theSeed, int rowSelected, int colSelected) {
            if (hasWon(theSeed, rowSelected, colSelected)) {
                currentState = (theSeed == Seed.White) ? GameState.White_WON : GameState.Black_WON;
            } else if (isDraw()) {
                currentState = GameState.DRAW;
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
        public boolean hasWon (Seed theSeed, int rowSelected, int colSelected) {
            return(board[rowSelected][0] == theSeed 
                    && board[rowSelected][1] == theSeed 
                    && board[rowSelected][2] == theSeed
                || board[0][colSelected] == theSeed && 
                    board[1][colSelected] == theSeed && 
                    board[2][colSelected] == theSeed 
                || rowSelected == colSelected && 
                    board[0][0] == theSeed &&
                    board[1][1] == theSeed && 
                    board[2][2] == theSeed 
                || rowSelected + colSelected == 2 && 
                    board[0][2] == theSeed &&
                    board[1][1] == theSeed &&
                    board[2][0] == theSeed);
        }
        
        class DrawCanvas extends JPanel {
            private static final long serialVersionUID = 6845978118490338769L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(Color.WHITE);
                
                /* if (row % 2 == 0 && col % 2) {
                    setBackground(Color.BLACK);
                } */
                
                g.setColor(Color.LIGHT_GRAY);
                for (int row = 1; row < ROWS; ++row){
                    g.fillRoundRect(0, CELL_SIZE * row - GRID_WIDTH_HALF, CANVAS_WIDTH - 1, GRID_WIDTH, GRID_WIDTH, GRID_WIDTH);
                }
                for (int col = 1; col < COLS; ++col){
                    g.fillRoundRect(CELL_SIZE * col - GRID_WIDTH_HALF, 0, GRID_WIDTH, CANVAS_HEIGHT - 1, GRID_WIDTH, GRID_WIDTH);
                }
                Graphics2D g2d = (Graphics2D)g;
                g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                for (int row = 0; row < ROWS; ++row) {
                    for (int col = 0; col < COLS; ++col) {
                        int x1 = col * CELL_SIZE + CELL_PADDING;
                        int y1 = row * CELL_SIZE + CELL_PADDING;
                        if (board[row][col] == Seed.White) {
                            g2d.setColor(Color.RED);
                            int x2 = (col + 1) * CELL_SIZE - CELL_PADDING;
                            int y2 = (row + 1) * CELL_SIZE - CELL_PADDING;
                            g2d.drawLine(x1, y1, x2, y2);
                            g2d.drawLine(x2, y1, x1, y2);
                        }
                        else if (board[row][col] == Seed.Black) {
                            g2d.setColor(Color.BLUE);
                            g2d.drawOval(x1, y1, SYMBOL_SIZE, SYMBOL_SIZE);
                        }
                    }
                }
                if (currentState == GameState.PLAYING) {
                    statusBar.setForeground(Color.BLACK);
                    if (currentPlayer == Seed.White) {
                        statusBar.setText("White's Turn!");
                    }
                    else {
                        statusBar.setText("Blacks's Turn!");
                    }
                }
                else if (currentState == GameState.DRAW) {
                    statusBar.setForeground(Color.RED);
                    statusBar.setText("Draw! Click to Play Again!");
                }
                else if (currentState == GameState.White_WON) {
                    statusBar.setForeground(Color.RED);
                    statusBar.setText("White Won! Click to Play Again");
                }
                else if (currentState == GameState.Black_WON) {
                    statusBar.setForeground(Color.RED);
                    statusBar.setText("Black Won! Click to PLay Again!");
                }
            }
        }
        
        public static void main(String[] args) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new checkers();
                }
            });
        }
    }
