package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import TileSet.Background;
public class GameOverState extends GameState {
	
	private GameStateManager gsm;
	private Font font;
	private Background bg;
	
	private int currentChoice = 0;
	private String[] options1 = {
		"restart",
		"Quit"
		
	};
	private Color titleColor;
	private Font titleFont;
	public GameOverState(GameStateManager gsm) {
		this.gsm = gsm;
		init();
		try {
			
			bg = new Background("/background/Bspace.png",1);
			bg.setVector(-0.1, 0);
			
			titleColor = new Color(128, 0, 0);
			titleFont = new Font(
					"Century Gothic",
					Font.PLAIN,
					28);
			
			
			font = new Font("Arial", Font.PLAIN, 12);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		bg.update();
		System.out.println("Updating GameOverState");
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		bg.draw(g);
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("GAME OVER", 80, 70);
		
		// draw menu options
		g.setFont(font);
		for(int i = 0; i < options1.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.YELLOW);
			}
			else {
				g.setColor(Color.RED);
			}
			g.drawString(options1[i], 150, 140 + i * 15);
		}
	}

	@Override
	public void keyPressed(int k) {
		// TODO Auto-generated method stub
		if(k == KeyEvent.VK_ENTER){
			select();
		}
		if(k == KeyEvent.VK_UP) {
			currentChoice--;
			if(currentChoice == -1) {
				currentChoice = options1.length - 1;
			}
		}
		if(k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if(currentChoice == options1.length) {
				currentChoice = 0;
			}
		}
	}

	private void select() {
		// TODO Auto-generated method stub
		if(currentChoice == 0) {
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
		if(currentChoice == 1) {
			gsm.setState(GameStateManager.MENUSTATE);;
		}
	}
	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}

}
