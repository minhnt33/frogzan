package vn.com.tuanminh.frogzan.objects;

import vn.com.tuanminh.frogzan.game.Assets;
import vn.com.tuanminh.frogzan.game.GameWorld;
import vn.com.tuanminh.frogzan.utils.AudioManager;
import vn.com.tuanminh.frogzan.utils.Constants;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

public class SpawnGround {
	private GameWorld gWorld;
	private Frog frog;
	private Array<Ground> grounds;
	private Array<Obstacle> obstacles;
	private Array<Body> bodies;
	private float gameHeight;
	private float PPM;
	private float groundY = 0;
	private int numRemovedGround = 0;
	private int numRemovedObstacle = 0;
	
	public SpawnGround(GameWorld gWorld, float gHeight, float ppm) {
		this.gWorld = gWorld;
		this.gameHeight = gHeight;
		this.PPM = ppm;
		frog = gWorld.getFrog();
		grounds = new Array<Ground>();
		obstacles = new Array<Obstacle>();
		bodies = new Array<Body>();
		this.generateFisrtGrounds();
	}
	
	public void update(float delta) {
		for(Ground ground : grounds) {
			ground.update(delta);
		}
		
		randomGroundSituations();
		
		// Score method must be called before removeGround()
		if (gWorld.getScore() == 0) {
			score(Constants.FIRST_GAP);
			removeGround(Constants.FIRST_GAP);
		} else {
			score(Constants.GAP_TO_DELETE);
			removeGround(Constants.GAP_TO_DELETE);
		}
		
	}
	
	public void randomGroundSituations() {
		if (frog.getY() - grounds.get(grounds.size - 1).getY() < Constants.GAP_TO_CREATE / PPM) {
			groundY -= Constants.GAP_GROUNDS;
			int i = MathUtils.random(0, 12);
			if(i == 0) {
				grounds.add(generateShortGround(0, 1, groundY));
				grounds.add(generateLongGround(1, 0, groundY, false, false));
			}
			else if(i == 1) {
				grounds.add(generateLongGround(0, 1, groundY, false, false));
				grounds.add(generateShortGround(1, 0, groundY));
			}
			else if(i == 2) {
				grounds.add(generateNormalGround(0, 1, groundY, false, false));
				grounds.add(generateNormalGround(1, 0, groundY, false, false));
			}
			else if(i == 3)
				grounds.add(generateLongGround(0, 1, groundY, true, false));
			else if(i == 4)
				grounds.add(generateLongGround(1, 0, groundY, true, false));
			else if(i == 5)
				grounds.add(generateLongGround(1, 0, groundY, true, true));
			else if(i == 6)
				grounds.add(generateLongGround(0, 1, groundY, true, true));
			else if(i == 7)
				grounds.add(generateLongGround(1, 0, groundY, false, true));
			else if(i == 8)
				grounds.add(generateLongGround(0, 1, groundY, false, true));
			else if(i == 9) {
				grounds.add(generateLongGround(1, 0, groundY, false, true));
				grounds.add(generateLongGround(0, 1, groundY, false, true));
			}
			else if(i == 10) {
				grounds.add(generateNormalGround(0, 1, groundY, false, true));
				grounds.add(generateNormalGround(1, 0, groundY, false, true));
			}
			else if(i == 11) {
				grounds.add(generateLongGround(1, 0, groundY, false, true));
				grounds.add(generateShortGround(0, 1, groundY));
			}
			else if(i == 12) {
				grounds.add(generateShortGround(0, 1, groundY));
				grounds.add(generateLongGround(1, 0, groundY, false, true));
			}
		}
	}
	
	public void generateFisrtGrounds() {
		Ground terrainGround = new Ground(gWorld.getWorld(), 136 / 2f,
				gameHeight - 10f, 80, 10, PPM);
		grounds.add(terrainGround);
		grounds.add(generateNormalGround(0, 1, terrainGround.getY() * PPM
				- Constants.FIRST_GAP, false, false));
		grounds.add(generateNormalGround(1, 0, terrainGround.getY() * PPM
				- Constants.FIRST_GAP, false, false));
		groundY = terrainGround.getY() * PPM - Constants.FIRST_GAP;
	}
	
	public Ground generateShortGround(int x, int r, float y) {
		Ground shortGround = new Ground(gWorld.getWorld(), x
				* (136 - Constants.SHORT_GROUND_WIDTH) + r * Constants.SHORT_GROUND_WIDTH,
				y, Constants.SHORT_GROUND_WIDTH, Constants.GROUND_HEIGHT, PPM);
		groundY = shortGround.getY() * PPM;
		int i = MathUtils.random(0, 1);
		if (i == 0) {
			Obstacle obstacle = new Obstacle(gWorld.getWorld(), shortGround, PPM);
			obstacles.add(obstacle);
		}
		return shortGround;
	}
	
	public Ground generateLongGround(int x, int r, float y, boolean isMoving, boolean isRotating) {
		Ground longGround = new Ground(gWorld.getWorld(), x
				* (136f - Constants.LONG_GROUND_WIDTH) + r * Constants.LONG_GROUND_WIDTH, y,
				Constants.LONG_GROUND_WIDTH, Constants.GROUND_HEIGHT, PPM, isMoving, isRotating);
		groundY = longGround.getY() * PPM;
		int i = MathUtils.random(0, 1);
		if (i == 0) {
			Obstacle obstacle = new Obstacle(gWorld.getWorld(), longGround, PPM);
			obstacles.add(obstacle);
		}
		return longGround;
	}
	
	public Ground generateNormalGround(int x, int r, float y, boolean isMoving, boolean isRotating) {
		Ground normalGround = new Ground(gWorld.getWorld(),
				x * (136f - Constants.NORMAL_GROUND_WIDTH) + r
						* Constants.NORMAL_GROUND_WIDTH, y, Constants.NORMAL_GROUND_WIDTH,
						Constants.GROUND_HEIGHT, PPM, isMoving, isRotating);
		groundY = normalGround.getY() * PPM;
		int i = MathUtils.random(0, 2);
		if (i == 0) {
			Obstacle obstacle = new Obstacle(gWorld.getWorld(), normalGround, PPM);
			obstacles.add(obstacle);
		}
		return normalGround;
	}
	
	public void removeGround(float deletegap) {
		gWorld.getWorld().getBodies(bodies);
		for (Body bd : bodies) {
			if (bd.getType() == BodyType.KinematicBody && bd.getPosition().y > frog.getY() + deletegap / PPM) {
				gWorld.getWorld().destroyBody(bd);
				numRemovedGround++;
				grounds.removeIndex(numRemovedGround - 1);
			}

			else if(bd.getType() == BodyType.DynamicBody && bd.getPosition().y > frog.getY() + (deletegap + 20) / PPM){
				gWorld.getWorld().destroyBody(bd);
				numRemovedObstacle++;
				obstacles.removeIndex(numRemovedObstacle - 1);
			}
			
			numRemovedObstacle = 0;
			numRemovedGround = 0;
		}
	}
	
	public void score(float scoregap) {
		if (grounds.get(0).getGroundBd().getPosition().y > frog.getY() + scoregap / PPM) {
			AudioManager.instance.play(Assets.instance.sound.score);
			gWorld.addScore();
		}
	}
		
	public Array<Ground> getGroundArray() {
		return grounds;
	}
	
	public Array<Obstacle> getObstacleArray(){
		return obstacles;
	}
	
}
