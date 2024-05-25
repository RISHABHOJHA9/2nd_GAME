import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PacManGame extends JPanel implements ActionListener {
    private final int TILE_SIZE = 24;
    private final int N_TILES = 15;
    private final int SCREEN_SIZE = N_TILES * TILE_SIZE;
    private final int PACMAN_SPEED = 6;
    private final int[][] maze = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1},
            {1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1},
            {1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1},
            {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    private final Point startingPoint = new Point(1, 1);
    private final Point endingPoint = new Point(13, 13);

    private boolean inGame = true;
    private int pacman_x, pacman_y, pacman_dx, pacman_dy;
    
    private String playerName;

    public PacManGame() {
        initGame();
        playerName = "";
    }
    
     private void startGame() {
        playerName = JOptionPane.showInputDialog(this, "Enter your name:");
        if (playerName == null || playerName.isEmpty()) {
            playerName = "Anonymous";
        }
        JOptionPane.showMessageDialog(this, "Welcome, " + playerName + "!\nPress OK to start the game!");
    }

    private void initGame() {
    setFocusable(true);
    setBackground(Color.black);
    setPreferredSize(new Dimension(SCREEN_SIZE, SCREEN_SIZE));
    addKeyListener(new TAdapter());
    
    // Adjusting initial position to be in the middle of the path
    pacman_x = (startingPoint.x * TILE_SIZE) + (TILE_SIZE / 2);
    pacman_y = (startingPoint.y * TILE_SIZE) + (TILE_SIZE / 2);

    Timer timer = new Timer(40, this);
    timer.start();
}


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMaze(g);
        drawPacman(g);
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawMaze(Graphics g) {
    for (int y = 0; y < N_TILES; y++) {
        for (int x = 0; x < N_TILES; x++) {
            if (maze[y][x] == 1) {
                g.setColor(Color.blue);
                g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            } else if (x == startingPoint.x && y == startingPoint.y) {
                g.setColor(Color.green);
                g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            } else if (x == endingPoint.x && y == endingPoint.y) {
                g.setColor(Color.red);
                g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }
}


    private void drawPacman(Graphics g) {
        g.setColor(Color.yellow);
        g.fillOval(pacman_x, pacman_y, TILE_SIZE, TILE_SIZE);
    }

    private void movePacman() {
        int new_x = pacman_x + pacman_dx;
        int new_y = pacman_y + pacman_dy;

        if (canMove(new_x, new_y)) {
            pacman_x = new_x;
            pacman_y = new_y;
            checkEndingPoint();
        }
    }

    private boolean canMove(int x, int y) {
        int tile_x = x / TILE_SIZE;
        int tile_y = y / TILE_SIZE;
        return maze[tile_y][tile_x] == 0;
    }

    private void checkEndingPoint() {
        int pacmanTileX = pacman_x / TILE_SIZE;
        int pacmanTileY = pacman_y / TILE_SIZE;
        if (pacmanTileX == endingPoint.x && pacmanTileY == endingPoint.y) {
            inGame = false;
            JOptionPane.showMessageDialog(this, "Congratulations! "+ playerName +" You won the game!");
            System.exit(0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            movePacman();
            repaint();
        }
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT) {
                pacman_dx = -PACMAN_SPEED;
                pacman_dy = 0;
            } else if (key == KeyEvent.VK_RIGHT) {
                pacman_dx = PACMAN_SPEED;
                pacman_dy = 0;
            } else if (key == KeyEvent.VK_UP) {
                pacman_dx = 0;
                pacman_dy = -PACMAN_SPEED;
            } else if (key == KeyEvent.VK_DOWN) {
                pacman_dx = 0;
                
                pacman_dy = PACMAN_SPEED;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
                pacman_dx = 0;
            }

            if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
                pacman_dy = 0;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PacManGame game = new PacManGame();
            JFrame frame = new JFrame("Pac-Man");
            frame.add(game);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            System.out.println("Game window set to visible.");

            game.startGame();
        });
    }
}
