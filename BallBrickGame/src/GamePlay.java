import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.Timer;


import javax.swing.JPanel;

public class GamePlay extends JPanel implements KeyListener, ActionListener {
	Random r =new Random();
	private boolean play = false;
	private int score = 0;
	int row = r.nextInt(3)+5;
	int col = r.nextInt(3)+5;
	private int totalBricks=row*col;
	private Timer timer;
	private int delay=8;
	private int playerX  = r.nextInt(200)+100;
	
	//ball Property

	
	private int ballposX = r.nextInt(100)+100;
	private int ballposY = 300;
	private int balldirX=-1;
	private int balldirY=-2;
	
	private MapGenerator map;
	
	public GamePlay() {
		map = new MapGenerator(row, col);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay,this);
		timer.start();
	}
	
	public void paint(Graphics g) {
		//background
		g.setColor(Color.BLACK);
		g.fillRect(1,1,692,592);

		//borders
		g.setColor(Color.YELLOW);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		
		//scores
		g.setColor(Color.WHITE);
		g.setFont(new Font("serif",Font.BOLD,25));
		g.drawString(""+score, 590, 30);
		
		//graw map
		map.draw((Graphics2D)g);
		
		//paddle
		g.setColor(Color.GREEN);
		g.fillRect(playerX, 550, 100, 8);
		
		//the ball
		g.setColor(Color.YELLOW);
		g.fillOval(ballposX, ballposY, 20, 20); 
		
		if(totalBricks==0) {
			play=false;
			balldirX=0;
			balldirY=0;
			g.setColor(Color.GREEN);
			g.setFont(new Font("serif",Font.BOLD,30));
			g.drawString("You Win!! Congratulations",190 , 300);
			g.setColor(Color.YELLOW);
			g.drawString("Your Score:"+score, 190, 350);
			g.setColor(Color.CYAN);
			g.setFont(new Font("serif",Font.BOLD,20));
			g.drawString("Press Enter to Restart",190 , 400);
		}
		
		if(ballposY>570) {
			play=false;
			balldirX=0;
			balldirY=0;
			g.setColor(Color.RED);
			g.setFont(new Font("serif",Font.BOLD,30));
			g.drawString("Game Over",190 , 300);
			g.setColor(Color.GREEN);
			g.drawString("Your Score:"+score, 190, 350);
			g.setColor(Color.YELLOW);
			g.setFont(new Font("serif",Font.BOLD,20));
			g.drawString("Press Enter to Restart",190 , 400);
		}
		
		g.dispose();
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		timer.start();
		if(play) {
			if(new Rectangle(ballposX,ballposY,20,20).intersects(new Rectangle(playerX,550,100,8))) {
				balldirY=-balldirY;
			}
			
			A:for(int i=0;i<map.map.length;i++) {
				for(int j=0;j<map.map[0].length;j++) {
					if(map.map[i][j]>0) {
						int brickX = j*map.brickWidth + 80;
						int brickY = i*map.brickHeight +50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						
						Rectangle rect=new Rectangle(brickX,brickY,brickWidth,brickHeight);
						Rectangle ballrect = new Rectangle(ballposX,ballposY,20,20);
						Rectangle brickRect = rect;
						if(ballrect.intersects(brickRect)) {
							map.setBrickValue(0,i,j);
							totalBricks--;
							score+=5;
							if(ballposX+19<brickRect.x||ballposX+1>=brickRect.x+brickRect.width) {
								balldirX=-balldirX;
							}else {
								balldirY=-balldirY;
							}
							break A;
						}
					}
				}
			}
			
			ballposX+=balldirX;
			ballposY+=balldirY;
			if(ballposX<0) {
				balldirX=-balldirX;
			}
			if(ballposY<0) {
				balldirY=-balldirY;
			}
			if(ballposX>670) {
				balldirX=-balldirX;
			} 	
		}
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
			if(playerX>=600) {
				playerX = 600;
			}
			else {
				moveRight();
			}
		}
		if(e.getKeyCode()==KeyEvent.VK_LEFT) {
			if(playerX< 10) {
				playerX = 10;
			}
			else {
				moveLeft();
			}
		}
		
		if(e.getKeyCode()==KeyEvent.VK_ENTER) {
			if(!play) {
				play=true;
				ballposX=r.nextInt(100)+100;;
				ballposY=400;
				balldirX=-1;
				balldirY=-2;
				playerX=320;
				score=0;
				int row = r.nextInt(3)+5;
				int col = r.nextInt(3)+5;
				totalBricks=row*col;
				map = new MapGenerator(row, col);
				repaint();
				
			}
		}
		
	}
	
	public void moveRight() {
		play=true;
		playerX +=20;
	}
	
	public void moveLeft() {
		play = true;
		playerX -=20;
	}

	

}
