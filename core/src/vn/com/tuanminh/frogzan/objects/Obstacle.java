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
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;

public class Obstacle {
	private final float PPM;
	private Vector2 position;
	private World world;
	private Body obstacleBd = null;
	private DistanceJoint disJoint = null;
	
	private TextureRegion obstacle;
	private Sprite obSprite;
	
	public Obstacle(World b2dWorld, Ground ground, float ppm) {
		this.world = b2dWorld;
		this.PPM = ppm;
		this.position = new Vector2(ground.getGroundBd().getWorldCenter().x, ground.getGroundBd().getPosition().y + 50 / PPM);

		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();

		CircleShape obstacleShape = new CircleShape();
		obstacleShape.setRadius(Constants.RAD / PPM);
		bdef.type = BodyType.DynamicBody;
		bdef.position.set(position);
		fdef.density = 1;
		fdef.friction = 0;
		fdef.restitution = 1;
		fdef.shape = obstacleShape;
		fdef.filter.categoryBits = Constants.BIT_OBSTACLE;
		fdef.filter.maskBits = Constants.BIT_FROG | Constants.BIT_GROUND;
		obstacleBd = world.createBody(bdef);
		obstacleBd.createFixture(fdef);
		createJoint(ground);
		obstacleBd.setAngularVelocity(Constants.ROTATION_SPEED);
		
		//Flunctuate
		float speed = MathUtils.random(Constants.MIN_SPEED, Constants.MAX_SPEED);
		if(obstacleBd.getAngle() * MathUtils.radiansToDegrees > 180)
			obstacleBd.setLinearVelocity(-speed, 0);
		else 
			obstacleBd.setLinearVelocity(speed, 0);
		
		//Draw
		obstacle = Assets.instance.ground.obstacle;
		obSprite = new Sprite(obstacle);
		obstacleBd.setUserData(new Box2DSprite(obSprite));
		obstacleShape.dispose();
	}
	
	public void createJoint(Ground ground) {
		float groundAnchorLeftX = ground.getGroundBd().getWorldCenter().x + ground.getWidth() / 2 / PPM;
		float groundAnchorRightX = ground.getGroundBd().getWorldCenter().x - ground.getWidth() / 2 / PPM;
		
		DistanceJointDef disDef = new DistanceJointDef();
		disDef.initialize(
				ground.getGroundBd(),
				obstacleBd,
				new Vector2((ground.getX() < 136 / 2 / PPM) ? groundAnchorLeftX : groundAnchorRightX, ground.getGroundBd().getWorldCenter().y), obstacleBd.getWorldCenter());
		disDef.length = MathUtils.random(Constants.MIN_LENGTH, Constants.MAX_LENGHT);
		disJoint = (DistanceJoint) world.createJoint(disDef);
	}
	
	public float getWidth() {
		return Constants.RAD;
	}

	public float getX() {
		return obstacleBd.getPosition().x;
	}
	
	public float getY() {
		return obstacleBd.getPosition().y;
	}
	
	public Body getObstacleBd() {
		return obstacleBd;
	}
	
	public DistanceJoint getDisJoint() {
		return disJoint;
	}
}
