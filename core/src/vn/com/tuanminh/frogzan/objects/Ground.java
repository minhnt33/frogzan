package vn.com.tuanminh.frogzan.objects;

import net.dermetfan.utils.libgdx.graphics.Box2DSprite;
import vn.com.tuanminh.frogzan.game.Assets;
import vn.com.tuanminh.frogzan.utils.Constants;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Ground {
	private World myWorld;
	private Body groundBd;
	private Vector2 position;
	private float width;
	private float height;
	private float PPM;
	
	private TextureRegion groundText;
	private Sprite groundSprite;
	private boolean isMoving;
	private boolean isRotating;
	private int movingCount;
	private final int RIGHT_TO_LEFT = 0;
	private final int LEFT_TO_RIGHT = 1;
	private int rotatingDirection = MathUtils.random(0, 1);
	private boolean isFirstGround = true;
	
	public Ground(World world, float x, float y, float width, float height, float ppm) {
		myWorld = world;
		PPM = ppm;
		this.width = width;
		this.height = height;
		this.position = new Vector2(x / PPM , y / PPM);
		
		createGroundBd();
		createSpriteBd();
	}
	
	public Ground(World world, float x, float y, float width, float height, float ppm, boolean isMoving) {
		this(world, x, y, width, height, ppm);
		this.isMoving = isMoving;
	}
	
	public Ground(World world, float x, float y, float width, float height, float ppm, boolean isMoving, boolean isRotating) {
		this(world, x, y, width, height, ppm, isMoving);
		this.isRotating = isRotating;
	}

	public void createGroundBd() {
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		bdef.type = BodyType.KinematicBody;
		bdef.position.set(position);
		PolygonShape groundShape = new PolygonShape();
		groundShape.setAsBox(width / PPM, height / PPM);
		fdef.density = Constants.GROUND_DENSITY;
		fdef.friction = Constants.GROUND_FRICTION;
		fdef.restitution = Constants.GROUND_RESTITUTION;
		fdef.shape = groundShape;
		fdef.filter.categoryBits = Constants.BIT_GROUND;
		fdef.filter.maskBits = Constants.BIT_FROG | Constants.BIT_OBSTACLE;
		groundBd = myWorld.createBody(bdef);
		groundBd.createFixture(fdef);
		groundShape.dispose();
		
		if(getX() < 136 / 2 / PPM)
			movingCount = RIGHT_TO_LEFT;
		else
			movingCount = LEFT_TO_RIGHT;
	}

	public void movingGround(boolean isMoving, float speed) {
		if (isMoving) {
			calculateMovingCount();
			if(movingCount == 1)
				groundBd.setLinearVelocity(speed, 0);
			else
				groundBd.setLinearVelocity(-speed, 0);
		}
	}
	
	public void rotatingGround(boolean isRotating, float speed) {
		if(isRotating) {
			if (rotatingDirection == 0)
				groundBd.setAngularVelocity(speed);
			else
				groundBd.setAngularVelocity(-speed);
		}
	}
	
	public void createSpriteBd() {
		if (isFirstGround) {
			groundText = Assets.instance.ground.ground;
			groundSprite = new Sprite(groundText);
			groundBd.setUserData(new Box2DSprite(groundSprite));
		}
	}
	
	public void calculateMovingCount() {
		if(getX() <= width / PPM / 2)
			movingCount++;
		else if(getX() > 136 / PPM - width / PPM)
			movingCount--;
	}
	
	public void update(float delta) {
		movingGround(isMoving, 2f);
		rotatingGround(isRotating, 2f);
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public float getX() {
		return groundBd.getPosition().x;
	}
	
	public float getY() {
		return groundBd.getPosition().y;
	}
	
	public Body getGroundBd() {
		return groundBd;
	}
}
