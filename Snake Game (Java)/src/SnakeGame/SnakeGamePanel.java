package SnakeGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SnakeGamePanel extends JPanel {
	Timer timer;
	TimerTask task;
	JLabel label1 = new JLabel();
	JLabel label2 = new JLabel();
	ArrayList<Coordinates> snake = new ArrayList();
	int xVel;
	int yVel;
	boolean moved = false;
	Coordinates food;
	int recDim = 15;
	Coordinates[][] array = new Coordinates[40][40];
	  
	public SnakeGamePanel() {
	    setFocusable(true);
	    requestFocusInWindow();
	    
	    setLayout(null);
	    this.label1.setHorizontalAlignment(0);
	    
	    this.label1.setFont(new Font("Yu Gothic UI Semibold", 0, 28));
	    this.label1.setForeground(Color.BLACK);
	    this.label1.setText("Score");
	    this.label1.setBounds(0, 11, 95, 38);
	    add(this.label1);
	    this.label2.setHorizontalAlignment(0);
	    
	    this.label2.setFont(new Font("Yu Gothic UI Semibold", 0, 28));
	    this.label2.setForeground(Color.BLACK);
	    this.label2.setText(Integer.toString(0));
	    this.label2.setBounds(0, 49, 95, 38);
	    add(this.label2);
	    
	    start();
	    
	    addKeyListener(new KeyListener() {
	    	public void keyTyped(KeyEvent e) {}
	      
	    	public void keyReleased(KeyEvent e) {}
	      
	    	public void keyPressed(KeyEvent e) {
	    		if (!SnakeGamePanel.this.moved) {
	    			if (((e.getKeyChar() == 'w') || (e.getKeyCode() == 38)) && (SnakeGamePanel.this.yVel != 1)) {
	    				SnakeGamePanel.this.yVel = -1;
	    				SnakeGamePanel.this.xVel = 0;
	    				}
	    			else if (((e.getKeyChar() == 'a') || (e.getKeyCode() == 37)) && (SnakeGamePanel.this.xVel != 1)) {
	    				SnakeGamePanel.this.xVel = -1;
	    				SnakeGamePanel.this.yVel = 0;
					}
	    			else if (((e.getKeyChar() == 's') || (e.getKeyCode() == 40)) && (SnakeGamePanel.this.yVel != -1)) {
	    				SnakeGamePanel.this.yVel = 1;
	    				SnakeGamePanel.this.xVel = 0;
					}
	    			else if (((e.getKeyChar() == 'd') || (e.getKeyCode() == 39)) && (SnakeGamePanel.this.xVel != -1)) {
	    				SnakeGamePanel.this.xVel = 1;
	    				SnakeGamePanel.this.yVel = 0;
					}
	    			
	    			SnakeGamePanel.this.moved = true;
				}
			}
		});
	}
	
	public void start() {
		  initGrid();
		  Coordinates head = new Coordinates(10, 10);
		  this.snake.clear();
		  this.snake.add(head);
		  this.food = new Coordinates(25, 25);
	    
		  this.xVel = 0;
		  this.yVel = 0;
	    
		  this.timer = new Timer();
		  this.task = new TimerTask() {
			  public void run() {
				  SnakeGamePanel.this.updateSnake();
				  SnakeGamePanel.this.checkForCollision();
				  SnakeGamePanel.this.spawnFood();
				  SnakeGamePanel.this.repaint();
				  SnakeGamePanel.this.updateScore();
			  }
		  };
		  this.timer.scheduleAtFixedRate(this.task, 0L, 90L);
	}
	  
	public void stop() {
		this.timer.cancel();
		try {
			Thread.sleep(1000L);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		start();
	}
	
	public void updateSnake() {
		if (this.snake.size() > 1) {
			for (int i = this.snake.size() - 1; i > 0; i--) {
				((Coordinates)this.snake.get(i)).x = ((Coordinates)this.snake.get(i - 1)).x;
				((Coordinates)this.snake.get(i)).y = ((Coordinates)this.snake.get(i - 1)).y;
			}
		}
		((Coordinates)this.snake.get(0)).x += this.xVel;
		((Coordinates)this.snake.get(0)).y += this.yVel;
		this.moved = false;
	}
	
	public void checkForCollision() {
		if ((((Coordinates)this.snake.get(0)).x < 0) || (((Coordinates)this.snake.get(0)).x > 39) || (((Coordinates)this.snake.get(0)).y < 0) || (((Coordinates)this.snake.get(0)).y > 39))
			stop();
			
		for (int i = 1; i < this.snake.size(); i++) {
			if ((((Coordinates)this.snake.get(0)).x == ((Coordinates)this.snake.get(i)).x) && (((Coordinates)this.snake.get(0)).y == ((Coordinates)this.snake.get(i)).y))
				stop();
		}
	}
	
	public void spawnFood() {
		if ((((Coordinates)this.snake.get(0)).x == this.food.x) && (((Coordinates)this.snake.get(0)).y == this.food.y)) {
			increaseSize();
			Coordinates foodCord = new Coordinates(0, 0);
			boolean newCords = true;
			do {
				Random rand = new Random();
				int foodX = rand.nextInt(39) + 0;
				int foodY = rand.nextInt(39) + 0;
				foodCord = new Coordinates(foodX, foodY);
				for (int index = 0; index < this.snake.size(); index++) {
					if ((((Coordinates)this.snake.get(index)).x == foodCord.x) && (((Coordinates)this.snake.get(index)).y == foodCord.y))
						break;
					
					if (index == this.snake.size() - 1)
						newCords = false;
				}
			}
			while (newCords);
			
			this.food = foodCord;
		}
	}
	
	public void increaseSize() {
		Coordinates end = (Coordinates)this.snake.get(this.snake.size() - 1);
		String direction = getDirection(end);
		if (direction.equals("right")) {
			this.snake.add(new Coordinates(end.x + 1, end.y));
			this.snake.add(new Coordinates(end.x + 2, end.y));
			this.snake.add(new Coordinates(end.x + 3, end.y));
		}
		else if (direction.equals("left")) {
			this.snake.add(new Coordinates(end.x - 1, end.y));
			this.snake.add(new Coordinates(end.x - 2, end.y));
			this.snake.add(new Coordinates(end.x - 3, end.y));
		}
		else if (direction.equals("up")) {
			this.snake.add(new Coordinates(end.x, end.y + 1));
			this.snake.add(new Coordinates(end.x, end.y + 2));
			this.snake.add(new Coordinates(end.x, end.y + 3));
		}
		else if (direction.equals("down")) {
			this.snake.add(new Coordinates(end.x, end.y - 1));
			this.snake.add(new Coordinates(end.x, end.y - 2));
			this.snake.add(new Coordinates(end.x, end.y - 3));
		}
	}
	
	public String getDirection(Coordinates e) {
		if (this.snake.size() > 1) {
			Coordinates end = e;
			Coordinates second = (Coordinates)this.snake.get(this.snake.size() - 2);
			if ((end.x - 1 == second.x) && (end.y == second.y))
				return "right";
			if ((end.x + 1 == second.x) && (end.y == second.y))
				return "left";
			if ((end.x == second.x) && (end.y - 1 == second.y))
				return "up";
			if ((end.x == second.x) && (end.y + 1 == second.y))
				return "down";
		}
		else {
			if (this.xVel == -1)
				return "right";
			if (this.xVel == 1)
				return "left";	
			if (this.yVel == -1)
				return "up";
			if (this.yVel == 1)
				return "down";
		}
		return null;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	    
		g.setColor(Color.ORANGE);
		g.fillRect(0, 0, 100, 800);
		for (int i = 0; i < this.array.length; i++) {
			for (int j = 0; j < this.array[i].length; j++) {
				boolean empty = true;
				for (int index = 0; index < this.snake.size(); index++) {
					if ((((Coordinates)this.snake.get(index)).x == i) && (((Coordinates)this.snake.get(index)).y == j)) {
						g.setColor(Color.WHITE);
						g.fillRect(this.array[i][j].x, this.array[i][j].y, this.recDim, this.recDim);
						empty = false;
						break;
					}
				}
				if ((this.food.x == i) && (this.food.y == j)) {
					g.setColor(Color.RED);
					g.fillRect(this.array[i][j].x, this.array[i][j].y, this.recDim, this.recDim);
					empty = false;
				}
				if (empty) {
					g.setColor(Color.BLACK);
					g.fillRect(this.array[i][j].x, this.array[i][j].y, this.recDim, this.recDim);
				}
			}
		}
	}
	
	public void updateScore() {
		this.label2.setText(Integer.toString(this.snake.size() - 1));
	}
	
	public void initGrid() {
		for (int i = 0; i < this.array.length; i++) {
			for (int j = 0; j < this.array[i].length; j++) {
				Coordinates box = new Coordinates(this.recDim * i + 100, this.recDim * j);
				this.array[i][j] = box;
			}
		}
	}
}
