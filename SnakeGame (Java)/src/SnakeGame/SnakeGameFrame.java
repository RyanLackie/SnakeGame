package SnakeGame;

import javax.swing.*;

public class SnakeGameFrame {

	static JFrame frame = new JFrame("Snake Game");
	static SnakeGamePanel panel = new SnakeGamePanel();

	public static void main(String[] args) {
		initFrame();
	}

	public static void initFrame() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(806, 829);
		frame.setResizable(false);
		frame.add(panel);
		
		frame.setVisible(true);
		
	}
}
