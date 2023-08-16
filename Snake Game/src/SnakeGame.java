import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.PrimitiveIterator;
import java.util.Random;


public class SnakeGame extends JPanel implements ActionListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int UNIT_SIZE = 20;
    private static final int GAME_UNITS = (WIDTH*HEIGHT)/(UNIT_SIZE*UNIT_SIZE);

    private LinkedList<Point> snake;
    private Point food;

    private int snakeLength = 2;
    private boolean isGameOver = false;
    private char direction = 'R';

    private Timer timer;

    public SnakeGame(){
        snake = new LinkedList<>();
        snake.add(new Point(0,0));
        generateFood();

        timer = new Timer(100,this);
        timer.start();

        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(new SnakeKeyListener());
    }
    public void actionPerformed(ActionEvent e){
        if(!isGameOver){
            move();
            checkCollisions();
            repaint();
        }
    }

    private void move(){
        Point newHead = snake.peekFirst();
        switch (direction){
            case 'U':
                newHead = new Point(newHead.x, newHead.y-UNIT_SIZE);
                break;
            case 'D':
                newHead = new Point(newHead.x, newHead.y+UNIT_SIZE);
                break;
            case 'L':
                newHead = new Point(newHead.x-UNIT_SIZE, newHead.y);
                break;
            case 'R':
                newHead = new Point(newHead.x+UNIT_SIZE, newHead.y);
                break;
        }
        snake.addFirst(newHead);
        if(snake.size()>snakeLength){
            snake.removeLast();
        }
    }
    private void checkCollisions(){
        Point head = snake.peekFirst();

        if(head.equals(food)){
            snakeLength++;
            generateFood();
        }
        if(head.x<0 || head.x >= WIDTH || head.y<0 || head.y>=HEIGHT){
            isGameOver = true;
            timer.stop();
        }

        for (Point segment : snake){
            if(segment.equals(head)&&!segment.equals(snake.peekFirst())){
                isGameOver = true;
                timer.stop();
                break;
            }
        }
    }
    private void generateFood(){
        Random rand = new Random();
        int x = rand.nextInt(WIDTH/UNIT_SIZE)*UNIT_SIZE;
        int y = rand.nextInt(HEIGHT/UNIT_SIZE)*UNIT_SIZE;
        food = new Point(x,y);
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        if(!isGameOver){
            for(Point segment : snake){
                g.setColor(Color.GREEN);
                g.fillRect(segment.x,segment.y,UNIT_SIZE,UNIT_SIZE);
            }
            g.setColor(Color.RED);
            g.fillRect(food.x, food.y, UNIT_SIZE,UNIT_SIZE);
        }else{
            g.setColor(Color.WHITE);
            g.setFont(new Font("Ink Free", Font.BOLD,75));
            g.drawString("Game Over",150,HEIGHT/2);
        }
    }
    private class SnakeKeyListener extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            int key = e.getKeyCode();
            if(key==KeyEvent.VK_UP && direction !='D'){
                direction ='U';
            }else if(key==KeyEvent.VK_DOWN && direction!='U'){
                direction = 'D';
            }else if(key==KeyEvent.VK_LEFT && direction!='R'){
                direction='L';
            }else if(key==KeyEvent.VK_RIGHT && direction!='L'){
                direction = 'R';
            }
        }
    }
    public static void  main(String[] args){
        JFrame frame = new JFrame("Snake Game");
        SnakeGame snakeGame = new SnakeGame();
        frame.add(snakeGame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
