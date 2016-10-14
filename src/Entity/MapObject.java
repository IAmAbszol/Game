package Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import Entity.Player.Player;
import TileMap.Tile;
import TileMap.TileMap;

public abstract class MapObject {
	
	// tile stuff
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;  
	
	// position and vector
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;
	
	// dimensions
	protected int width;
	protected int height;
	
	// collision box
	protected int cwidth;
	protected int cheight;
	protected int coffsetx;
	protected int coffsety;
	
	// collision
	protected int currRow;
	protected int currCol;
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	
	// animation
	protected Animation animation;
	protected Animation rightAnimation;
	protected Animation leftAnimation;
	protected Animation upAnimation;
	protected Animation downAnimation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;
	protected boolean facingLeft;
	protected boolean facingUp;
	protected boolean facingDown;
	protected boolean object; // Objects don't move, simple animation
	protected boolean item;
	protected int itemX;
	protected int itemY;
	
	// movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	protected boolean attack;
	protected boolean cast;
	protected boolean save;
	protected boolean load;
	protected boolean dash;
	protected boolean friendly;
	
	// movement attributes
	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed;
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;
	
	//ai
	protected int distanceAwayFromOriginal;
	protected int anchoredX;
	protected int anchoredY;
	
	public static boolean debug = true;
	
	// constructor
	public MapObject() {}
	public MapObject(TileMap tm) {
		tileMap = tm;
		tileSize = tm.getTileSize();
	}
	
	public boolean withinArea(Player p, MapObject o, int distance) {
		/*
		 * I say that the width and height of the object AND the player are in account for measurements
		 * This could use the distance formula but instead of the center
		 * it takes the top, left, right, and bottom of the character into
		 * consideration, along with the object itself
		 */
		
		// determine center point - tweak player stuff
		int pxL = p.getx() + p.getMapX() - (p.getCWidth() / 2) - p.getCOffsetX(); // hopefully returns center point
		int pyL = p.gety() + p.getMapY() - (p.getCHeight() / 2) - p.getCOffsetY();
		int pxH = p.getx() + p.getMapX() + (p.getCWidth() / 2)  - p.getCOffsetX();
		int pyH = p.gety() + p.getMapY() + (p.getCHeight() / 2) - p.getCOffsetY();
		
		int oxL = o.getx() + o.getMapX() - (o.getCWidth() / 2) - o.getCOffsetX() - distance;
		int oyL = o.gety() + o.getMapY() - (o.getCHeight() / 2) - o.getCOffsetY() - distance;
		int oxH = o.getx() + o.getMapX() + (o.getCWidth() / 2) - o.getCOffsetX() + distance;
		int oyH = o.gety() + o.getMapY() + (o.getCHeight() / 2) - o.getCOffsetY() + distance;
		
		if(pxL >= oxL && pxH <= oxH) {
			if(pyL >= oyL && pyH <= oyH) {
				System.out.println("true");
				return true;
			}
		}
		System.out.println("false");
		return false;
	}
	
	public boolean withinArea(int lowerX, int lowerY, int largerX, int largerY) {
		if(x >= lowerX && x <= largerX) {
			if(y >= lowerY && y <= largerY) {
				return true;
			}
		}
		return false;
	}
	
	public boolean withinBounds() {
		if(x <= (anchoredX + distanceAwayFromOriginal) && x >= (anchoredX - distanceAwayFromOriginal)) {
			if(y <= (anchoredY + distanceAwayFromOriginal) && y >= (anchoredY - distanceAwayFromOriginal)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean mayInteract(MapObject o, int distance) {
		Rectangle r1 = getRectangleInteraction(distance);
		Rectangle r2 = o.getRectangleInteraction(distance);
		return r1.intersects(r2);
	}
	
	public Rectangle getRectangleInteraction(int distance) {
		return new Rectangle(
				(int)(x+dx) - ((cwidth/2) + coffsetx) - distance,
				(int)(y+dy) - ((cheight/2) + coffsety) - distance,
				cwidth + distance * 2,
				cheight + distance * 2
		);
	}
	
	public boolean inArea(MapObject n, MapObject o) {
		xdest = n.x + n.dx;
		ydest = n.y + n.dy;
		xtemp = n.x;
		ytemp = n.y;
		if(n.intersectsFuture(o)) {
			dy = 0;
			dx = 0;
			return true;
		} else {
			xtemp = xdest;
			ytemp = ydest;
		}
		return false;
	}
	
	public boolean intersectsFuture(MapObject o) {
		Rectangle r1 = getRectangleFuture();
		Rectangle r2 = o.getRectangleFuture();
		return r1.intersects(r2);
	}
	
	public Rectangle getRectangleFuture() {
		return new Rectangle(
				(int)(x+dx) - ((cwidth/2) + coffsetx),
				(int)(y+dy) - ((cheight/2) + coffsety),
				cwidth,
				cheight
		);
	}

	public boolean intersects(MapObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(
				(int)x - ((cwidth/2) + coffsetx),
				(int)y - ((cheight/2) + coffsety),
				cwidth,
				cheight
		);
	}
	
	
	// tiles
	public void calculateCorners(double x, double y) {
		
		int leftTile = (int)(x - cwidth / 2) / tileSize;
		int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
		int topTile = (int)(y - cheight / 2) / tileSize;
		int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;
		
		int tl = tileMap.getType(topTile, leftTile);
		int tr = tileMap.getType(topTile, rightTile);
		int bl = tileMap.getType(bottomTile, leftTile);
		int br = tileMap.getType(bottomTile, rightTile);
		// if it's blocked, then it's true
		topLeft = tl == Tile.BLOCKED;
		topRight = tr == Tile.BLOCKED;
		bottomLeft = bl == Tile.BLOCKED;
		bottomRight = br == Tile.BLOCKED;
		
	}
	
	public void checkTileMapCollision() {
		
		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
		
		calculateCorners(x, ydest);
		if(dy < 0) { // up
			if(topLeft || topRight) {
				dy = 0;
				ytemp = currRow * tileSize + cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		if(dy > 0) { // down
			if(bottomLeft || bottomRight) {
				dy = 0;
				falling = false;
				ytemp = (currRow + 1) * tileSize - cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		
		calculateCorners(xdest, y);
		if(dx < 0) {
			if(topLeft || bottomLeft) {
				dx = 0;
				xtemp = currCol * tileSize + cwidth / 2;
			}
			else {
				xtemp += dx;
			}
		}
		if(dx > 0) {
			if(topRight || bottomRight) {
				dx = 0;
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
			}
			else {
				xtemp += dx;
			}
		}
		
		if(!falling) {
			calculateCorners(x, ydest + 1);
			if(!bottomLeft && !bottomRight) {
				falling = true;
			}
		}
		
	}
	
	public boolean getFacingRight() { return facingRight; }
	public boolean getFacingLeft() { return facingLeft; }
	public boolean getFacingUp() { return facingUp; }
	public boolean getFacingDown() { return facingDown; }
	public int getx() { return (int)x; }
	public int gety() { return (int)y; }
	public int getMapX() { return (int)xmap; }
	public int getMapY() { return (int)ymap; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getCWidth() { return cwidth; }
	public int getCHeight() { return cheight; }
	public int getCOffsetX() { return coffsetx; }
	public int getCOffsetY() { return coffsety; }
	public void setx(double i) { x = i; }
	public void sety(double i) { y = i; }
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void setMapPosition() {
		xmap = tileMap.getx();
		ymap = tileMap.gety();
	}
	
	public void setTileMap(TileMap tm) {
		tileMap = tm;
		tileSize = tm.getTileSize();
	}
	
	public void setLeft(boolean b) { left = b; }
	public void setRight(boolean b) { right = b; }
	public void setUp(boolean b) { up = b; }
	public void setDown(boolean b) { down = b; }
	public void setAttack(boolean b) { attack = b; }
	public void setCast(boolean b) { cast = b; }
	public void setSave(boolean b) { save = b; }
	public void setLoad(boolean b) { load = b; }
	public void setDash(boolean b) { dash = b; }
	
	public void draw(java.awt.Graphics2D g) {
		if(!object) {
			if(facingRight) {
				g.drawImage(
					rightAnimation.getImage(),
					(int)(x + xmap - width / 2),
					(int)(y + ymap - height / 2),
					null
				);
				if(item) {
					g.drawImage(
							rightAnimation.getImage(),
							(int)(itemX + xmap - width / 2),
							(int)(itemY + ymap - height / 2),
							null
						);
				}
			}
			if(facingLeft) {
				if(item) {
					g.drawImage(
							leftAnimation.getImage(),
							(int)(itemX + xmap - width / 2),
							(int)(itemY + ymap - height / 2),
							null
						);
				}
				g.drawImage(
					leftAnimation.getImage(),
					(int)(x + xmap - width / 2),
					(int)(y + ymap - height / 2),
					null
				);
			}
			if(facingUp) {
				if(item) {
					g.drawImage(
							upAnimation.getImage(),
							(int)(itemX + xmap - width / 2),
							(int)(itemY + ymap - height / 2),
							null
						);
				}
				g.drawImage(
					upAnimation.getImage(),
					(int)(x + xmap - width / 2),
					(int)(y + ymap - height / 2),
					null
				);
			}
			if(facingDown) {
				if(item) {
					g.drawImage(
							downAnimation.getImage(),
							(int)(itemX + xmap - width / 2),
							(int)(itemY + ymap - height / 2),
							null
						);
				}
				g.drawImage(
					downAnimation.getImage(),
					(int)(x + xmap - width / 2),
					(int)(y + ymap - height / 2),
					null
				);
			}
		} else
			if(object) {
				g.drawImage(
					animation.getImage(),
					(int)(x + xmap - width / 2),
					(int)(y + ymap - height / 2),
					null
				);
			}
		if(debug) {
			g.setColor(Color.red);
			g.drawRect((int) (((int)x + xmap) - ((cwidth/2) + coffsetx)),
				(int) (((int)y + ymap) - ((cheight/2) + coffsety)),
				cwidth,
				cheight);
			
			Rectangle r1 = getRectangle(); // player
			//g.setColor(Color.blue);
			//g.drawString(".", (int)(r1.getX() + xmap), (int)(r1.getY() + ymap));
		}

	}

}