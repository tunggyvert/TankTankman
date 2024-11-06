package Main;

 import javax.swing.JFrame;
 
public class GameLaunch {
	
	JFrame window;
	
	public GameLaunch(){
		window = new JFrame("TankTopman");
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    window.setResizable(false);
	    window.pack();
	    window.setVisible(true);	
	}
	
	public static void main(String[] args) {
		new GameLaunch();
	}
}
