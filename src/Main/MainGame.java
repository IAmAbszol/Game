package Main;

import javax.swing.JFrame;

public class MainGame {
	
	public static final String GAME_NAME = "Realm of the Gods";
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame(GAME_NAME + " by Kyle");
		frame.setContentPane(new GamePanel());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
		
	}

}
