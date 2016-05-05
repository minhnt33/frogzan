package vn.com.tuanminh.frogzan.game;

import vn.com.tuanminh.frogzan.utils.Constants;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class Box2DContactListener implements ContactListener{
	private GameWorld gameWorld;
	
	public Box2DContactListener(GameWorld world) {
		this.gameWorld = world;
	}
	
	@Override
	public void beginContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		if(fa == null || fb == null)
			return;

		if (fa.getFilterData().categoryBits == Constants.BIT_FROG && fb.getFilterData().categoryBits == Constants.BIT_OBSTACLE) {
			gameWorld.getFrog().dieByBooming();
			fb.getBody().setUserData(null);
		}
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}
}
