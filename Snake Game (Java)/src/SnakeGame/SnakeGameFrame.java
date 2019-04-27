package SnakeGame;

import javax.swing.JFrame;

public class SnakeGameFrame {
	static JFrame frame = new JFrame("Snake Game");
	static int width = 706;
	static int height = 629;
	
	public static void main(String[] args) {
		initFrame();
	}
	
	public static void initFrame() {
		frame.setDefaultCloseOperation(3);
		frame.setSize(width, height);
		frame.setResizable(false);
		
		SnakeGamePanel panel = new SnakeGamePanel();
		frame.getContentPane().add(panel);
		frame.setVisible(true);
	}
}
