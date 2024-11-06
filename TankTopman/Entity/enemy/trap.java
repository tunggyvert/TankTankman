package Entity.enemy;

import Entity.*;
import TileSet.TileMap;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;

public class trap extends Enemy {
	
	private BufferedImage[] sprites;
	
	public trap(TileMap tm) {
		super(tm);
		// TODO Auto-generated constructor stub
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		health = maxHealth = 2;
		damage = 1;
		
		try {
			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/spriteenemy/trap_atlas.png"
				)
			);
			
			sprites = new BufferedImage[1];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(
					i * width,
					0,
					width,
					height
				);
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(200);
		
		right = true;
		facingRight = true;
		
	}
	
	
        @Override
	public void update() {}
	
        @Override
	public void draw(Graphics2D g) {
		
		//if(notOnScreen()) return;
		
		setMapPosition();
		
		super.draw(g);
		
	}
	
}
