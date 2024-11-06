package GameState;

import Main.GamePanel;
import TileSet.*;
import Entity.*;
import Entity.enemy.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class FinalState extends GameState {
	
	private int score = 0;
	
	public boolean addedTime = false;
	public boolean addedHealth = false;
	public boolean addedHealth2 = false;
	private TileMap tileMap;
	private Background bg;
	
	private Player player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Enemy> traper;
	private ArrayList<Enemy> scorek;
	private ArrayList<Explosion> explosions;
	
	private HUD hud;
	private long startTime;
    private long timeLimit = 60000; 
    public long endTime = 0;
    
    private long lastTrapTime = 0;
    private long trapDelay = 1000;
    
    private long lastHitTime = 0; 
    private long hitCooldown = 1000;
    
    private boolean isPaused = false;
	private long currentTime = 0;
    public FinalState(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}
	
	public void init() {
		
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Map/s6");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/background/bglv2.png", 0.1);
		
		player = new Player(tileMap);
		player.setPosition(100, 100);
		
		populateEnemies();
		
		explosions = new ArrayList<Explosion>();
		
		hud = new HUD(player);
		
		
		startTime = System.currentTimeMillis();
	    endTime = startTime + timeLimit;
	        
	
	}
	
	private void populateEnemies() {
		scorek = new ArrayList<Enemy>();
		enemies = new ArrayList<Enemy>();
		traper = new ArrayList<Enemy>();
		Slugger s;
		Point[] points = new Point[] {
			new Point(300, 100),
			new Point(400, 200),
			new Point(450, 200),
			new Point(500, 200),
			new Point(550, 200),
			new Point(410, 195),
			new Point(490, 195),
			new Point(675, 195),
			new Point(795, 195),
			new Point(915, 195),
			new Point(1035,195),
			new Point(1250,195)
			
		};
		for(int i = 0; i < points.length; i++) {
			s = new Slugger(tileMap);
			s.setPosition(points[i].x, points[i].y);
			enemies.add(s);
		}
		
		trap t;
		Point[] points1 = new Point[] {
				new Point(410, 195),
				new Point(490, 195),
				new Point(675, 195),
				new Point(795, 195),
				new Point(915, 195),
				new Point(1035,195),
				new Point(1245,225),
				new Point(1370,225)
			
			};
		for(int i = 0; i < points1.length; i++) {
			t = new trap(tileMap);
			t.setPosition(points1[i].x, points1[i].y);
			traper.add(t);
		}
		scorekiller sc;
		Point[] points2 = new Point[] {
				new Point(780,50),
				new Point(380,110),
				new Point(450,50),
				new Point(500,110),
				new Point(1130,50),
				new Point(1080,110),
				new Point(1400,170),
				new Point(1300,255)
		};
		for(int i = 0; i < points2.length; i++) {
			sc = new scorekiller(tileMap);
			sc.setPosition(points2[i].x, points2[i].y);
			scorek.add(sc);
		}
	}
	
	public void update() {
		
		//update player
		
		if(!isPaused) {
		player.update();
		tileMap.setPosition(
			GamePanel.WIDTH / 2 - player.getx(),
			GamePanel.HEIGHT / 2 - player.gety()
		);
		
		
		// set background
		bg.setPosition(tileMap.getx(), tileMap.gety());
		
		// attack enemies
		player.checkAttack(enemies);
		player.checkAttack(traper);
		player.checkAttack(scorek);
		
		// update all enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead()) {
				enemies.remove(i);
				i--;
				score += 5;
				explosions.add(
					new Explosion(e.getx(), e.gety()));
				
			}
		}
		currentTime = System.currentTimeMillis();
		for(int i = 0;i < traper.size();i++) {
			Enemy tr = traper.get(i);
			tr.update();
			
			if (player.intersects(tr) && currentTime - lastTrapTime >= trapDelay) {
		        endTime -= 1000; 
		        lastTrapTime = currentTime; 
		    }
			if(tr.isDead()) {
				traper.remove(i);
				i--;
				score += 5;
				explosions.add(
					new Explosion(tr.getx(), tr.gety()));
			}
		}
		for(int i = 0;i < scorek.size();i++) {
			Enemy sck = scorek.get(i);
			sck.update();
			if (player.intersects(sck)) {       
		        
		        if (currentTime - lastHitTime > hitCooldown) {
		            score -= 10; 
		            if (score < 0) score = 0; 

		            lastHitTime = currentTime; 
		        }
		    }
			if(sck.isDead()) {
				scorek.remove(i);
				i--;
				score += 5;
				explosions.add(
					new Explosion(sck.getx(), sck.gety()));
			}
		}

		
		
		// update explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
		
		if(player.getHealth() <= 0) {
	        gsm.setState(GameStateManager.GAMEOVERSTATE);
	        return;
	    }
		if(enemies.isEmpty() && scorek.isEmpty()) {
	        gsm.setState(GameStateManager.WINSTATE); 
	        return;
	    }
		long currentTime = System.currentTimeMillis();
		
        if(currentTime >= endTime) {
            gsm.setState(GameStateManager.GAMEOVERSTATE);
            return;
        }
        if (score == 40 && !addedTime) {
	        endTime += 5000; // เพิ่มเวลา 5 วิ
	        addedTime = true; // ทำให้เพิ่มเวลาแค่ครั้งเดียว
	    }

	    if (score == 60 && !addedHealth) {
	        player.setHealth(player.getHealth() + 1); 
	        addedHealth = true; 
	    }
	    if (score == 90 && !addedHealth) {
	        player.setHealth(player.getHealth() + 1); 
	        addedHealth2 = true;
	    }
		}
		else {
	        // Record when the game was paused
	        if (isPaused)  return;
	    }
	     
  
	}
	
	
	
	public void draw(Graphics2D g) {
		
		// draw bg
		bg.draw(g);
		
		// draw tilemap
		tileMap.draw(g);
		
		// draw player
		player.draw(g);
		
		// draw enemies
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
		
		for(int i = 0;i < traper.size();i++) {
			traper.get(i).draw(g);
		}
		for(int i = 0;i < scorek.size();i++) {
			scorek.get(i).draw(g);
		}
		
		// draw explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition(
				(int)tileMap.getx(), (int)tileMap.gety());
			explosions.get(i).draw(g);
		}
		
		// draw hud
		hud.draw(g);
		long timeRemaining = (endTime - System.currentTimeMillis()) / 1000;
        g.setColor(Color.WHITE);
        g.drawString("Time: " + timeRemaining, 250, 30);
        g.drawString("Score: " + score, 250, 50);
        
        if (isPaused) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.drawString("Game Paused  "
            		+ "Press 'P' to Resume", 30, 120);
            g.drawString("Press 'S' to Restart", 30, 140);
            g.drawString("Press 'M' to Main Menu", 30, 160);
            return;
        }
	}
	

	
	public void keyPressed(int k) {
		if(!isPaused) {
		if(k == KeyEvent.VK_LEFT) player.setLeft(true);
		if(k == KeyEvent.VK_RIGHT) player.setRight(true);
		if(k == KeyEvent.VK_UP) player.setUp(true);
		if(k == KeyEvent.VK_DOWN) player.setDown(true);
		if(k == KeyEvent.VK_W) player.setJumping(true);
		if(k == KeyEvent.VK_E) player.setGliding(true);
		if(k == KeyEvent.VK_R) player.setScratching();
		if(k == KeyEvent.VK_F) player.setFiring();
		}
		if(k == KeyEvent.VK_P) {
		    isPaused = !isPaused;
		    return;
		}
		if (isPaused) {
	        if (k == KeyEvent.VK_S) {
	            init();  // Restart the level
	            isPaused = false;
	        }
	        if (k == KeyEvent.VK_M) {
	            gsm.setState(GameStateManager.MENUSTATE);  // Return to main menu
	        }
	        return;
	    }
	}
	
	public void keyReleased(int k) {
		if(k == KeyEvent.VK_LEFT) player.setLeft(false);
		if(k == KeyEvent.VK_RIGHT) player.setRight(false);
		if(k == KeyEvent.VK_UP) player.setUp(false);
		if(k == KeyEvent.VK_DOWN) player.setDown(false);
		if(k == KeyEvent.VK_W) player.setJumping(false);
		if(k == KeyEvent.VK_E) player.setGliding(false);
	}
	
}
