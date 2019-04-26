package SnakeGame;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SnakeGamePanel extends JPanel {
	
	ArrayList<Coordinates> snake = new ArrayList<Coordinates>();
	int xVel = 0, yVel = 0;
	
	boolean moved = false;
	
	Coordinates food = new Coordinates(25, 25);
	
	int recDim = 20;
	Coordinates[][] array = new Coordinates[40][40];
	
	public SnakeGamePanel() {
		setPreferredSize(new Dimension(Frame.WIDTH, Frame.HEIGHT));
		setFocusable(true);
		requestFocusInWindow();
		
		initGrid();
		
		Coordinates head = new Coordinates(10, 10);
		snake.add(head);
		
		start();
		
		addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {}
            public void keyReleased(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {
            	if (!moved) {
            		if (e.getKeyChar() == 'w' && yVel != 1) {
	        			yVel = -1;
	        			xVel = 0;
	            	}
	            	else if (e.getKeyChar() == 'a' && xVel != 1) {
	        			xVel = -1;
	        			yVel = 0;
	        		}
	            	else if (e.getKeyChar() == 's' && yVel != -1) {
	        			yVel = 1;
	        			xVel = 0;
	        		}
	            	else if (e.getKeyChar() == 'd' && xVel != -1) {
	        			xVel = 1;
	        			yVel = 0;
	        		}
            		moved = true;
            	}
            }
        });
	}
	
	Timer timer = new Timer();
	TimerTask task = new TimerTask() {
		public void run() {
			updateSnake();
			
			if (snake.get(0).x < 0 || snake.get(0).x > 39 || 
					snake.get(0).y < 0 || snake.get(0).y > 39) {
				stop();
			}
			
			for (int i = 1; i < snake.size(); i++) {
				if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y)
					stop();
			}
			
			if (snake.get(0).x == food.x && snake.get(0).y == food.y) {
				increaseSize();
				Coordinates foodPos = new Coordinates(0, 0);
				for (int i = 0; i < 2; i++) {
					Random rand = new Random();
					int pos = rand.nextInt(39)+0;
					if (i == 0)
						foodPos.x = pos;
					else
						foodPos.y = pos;
				}
				food = foodPos;
			}
			
			repaint();	
		}
	};
	
	public void updateSnake() {
		if (snake.size() > 1) {
			for (int i = snake.size()-1; i > 0; i--) {
				snake.get(i).x = snake.get(i-1).x;
				snake.get(i).y = snake.get(i-1).y;
				//System.out.println("Moving section "+i+" to "+snake.get(i-1).x+" "+snake.get(i-1).y);
			}
		}
		snake.get(0).x += xVel;
		snake.get(0).y += yVel;
		moved = false;
	}
	
	public void increaseSize() {
		Coordinates end = snake.get(snake.size()-1);
		String direction = getDirection(end);
		//System.out.println(direction);
		if (direction.equals("right")) {
			snake.add(new Coordinates(end.x+1, end.y));
			snake.add(new Coordinates(end.x+2, end.y));
			snake.add(new Coordinates(end.x+3, end.y));
		}
		else if (direction.equals("left")) {
			snake.add(new Coordinates(end.x-1, end.y));
			snake.add(new Coordinates(end.x-2, end.y));
			snake.add(new Coordinates(end.x-3, end.y));
		}
		else if (direction.equals("up")) {
			snake.add(new Coordinates(end.x, end.y+1));
			snake.add(new Coordinates(end.x, end.y+2));
			snake.add(new Coordinates(end.x, end.y+3));
		}
		else if (direction.equals("down")) {
			snake.add(new Coordinates(end.x, end.y-1));
			snake.add(new Coordinates(end.x, end.y-2));
			snake.add(new Coordinates(end.x, end.y-3));
		}
	}
	
	public String getDirection(Coordinates e) {
		if (snake.size() > 1) {
			Coordinates end = e;
			Coordinates second = snake.get(snake.size()-2);
			if (end.x-1 == second.x && end.y == second.y)
				return "right";
			else if (end.x+1 == second.x && end.y == second.y)
				return "left";
			else if (end.x == second.x && end.y-1 == second.y)
				return "up";
			else if (end.x == second.x && end.y+1 == second.y)
				return "down";
		}
		
		else {
			if (xVel == -1)
				return "right";
			else if (xVel == 1)
				return "left";
			else if (yVel == -1)
				return "up";
			else if (yVel == 1)
				return "down";
		}
		
		return null;
	}
	
	public void start() {
		timer.scheduleAtFixedRate(task, 0, 90);
	}
	
	public void stop() {
		timer.cancel();
		
		
		System.out.println("You are dead");
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				boolean empty = true;
				//Check for snake
				for (int index = 0; index < snake.size(); index++) {
					if (snake.get(index).x == i && snake.get(index).y == j) {
						g.setColor(Color.WHITE);
						g.fillRect(array[i][j].x, array[i][j].y, recDim, recDim);
						empty = false;
						break;
					}
				}
				if (food.x == i && food.y == j) {
					g.setColor(Color.RED);
					g.fillRect(array[i][j].x, array[i][j].y, recDim, recDim);
					empty = false;
				}
				if (empty) {
					g.setColor(Color.BLACK);
					g.fillRect(array[i][j].x, array[i][j].y, recDim, recDim);
				}
			}
		}
	}
	
	public void initGrid() {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				Coordinates box = new Coordinates(recDim*i, recDim*j);
				array[i][j] = box;	
			}
		}
	}
}
