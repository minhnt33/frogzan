package vn.com.tuanminh.frogzan.objects;

import vn.com.tuanminh.frogzan.game.Assets;
import vn.com.tuanminh.frogzan.utils.AudioManager;
import vn.com.tuanminh.frogzan.utils.Constants;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;


public class Frog {
	private World b2dWorld;
	private Body frogBd = null;
	private BodyDef bdef;
	private float PPM;
	
	private float width;
	private float height;
	private Vector2 position;

	private DistanceJoint disJoint = null;
	private boolean isHooked;
	private boolean isFalling;
	private boolean isBooming;
	
	public Frog(World world, float x, float y,  float width, float height, float ppm) {
		this.b2dWorld = world;
		this.width = width;
		this.height = height;
		this.PPM = ppm;
		this.position = new Vector2(x/PPM, y/PPM);
		isHooked = false;
		isFalling = false;
		isBooming = false;
		
		bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		bdef.type = BodyType.DynamicBody;
		bdef.position.set(position);
		PolygonShape frogShape  = new PolygonShape();
		frogShape.setAsBox(width/PPM, height/PPM);
		fdef.density = Constants.FROG_DENSITY;
		fdef.friction = Constants.FROG_FRICTION;
		fdef.restitution = Constants.FROG_RESTITUTION;
		fdef.shape = frogShape;
		fdef.filter.categoryBits = Constants.BIT_FROG;
		this.frogBd = world.createBody(bdef);
		this.frogBd.createFixture(fdef);
		frogShape.dispose();
	}
	
	public void update(float delta) {
		if (isHooked) {
			frogBd.setAwake(true);
			disJoint.setLength(disJoint.getLength() * Constants.REWIND_SPEED);
		}
	}
	
	public void dieByBooming() {
		isBooming = true;
		isHooked = false;
		AudioManager.instance.play(Assets.instance.sound.boom);
	}
	
	public void dieByFalling() {
		isFalling = true;
	}
	
	public float getWidth() {
		return width / PPM;
	}
	
	public float getHeight() {
		return height / PPM;
	}
	
	public float getX() {
		return frogBd.getPosition().x;
	}
	
	public float getY() {
		return frogBd.getPosition().y;
	}
	
	public boolean isHooked() {
		return isHooked;
	}
	
	public boolean isFalling() { 
		return isFalling;
	}
	
	public boolean isBooming() {
		return isBooming;
	}
	
	public Body getFrogBd() {
		return frogBd;
	}
	
	public DistanceJoint getDistanceJoint() {
		return disJoint;
	}
	
	public void fireHook(Body touchedBd, Vector3 testPoint) {
		if (!isFalling && !isBooming) {
			AudioManager.instance.play(Assets.instance.sound.shoot);
			isHooked = true;
			DistanceJointDef disJointDef = new DistanceJointDef();
			disJointDef.initialize(frogBd, touchedBd, frogBd.getWorldCenter(),
					new Vector2(testPoint.x, testPoint.y));
			disJointDef.collideConnected = true;
			disJoint = (DistanceJoint) b2dWorld.createJoint(disJointDef);
		}
	}
	
	public void rewindHook() {
		if (isHooked) {
			b2dWorld.destroyJoint(disJoint);
			isHooked = false;
		}
	}
}
