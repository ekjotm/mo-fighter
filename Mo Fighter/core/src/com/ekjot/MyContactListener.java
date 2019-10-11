package com.ekjot;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

public class MyContactListener implements ContactListener {
	
	private Platformer platf;
	public ArrayList<Body> toBeDestroyed = new ArrayList<Body>();
	
	public MyContactListener(Platformer plat){
		this.platf = plat;
	}
	
	@Override
	public void beginContact(Contact contact) {
		Fixture a = contact.getFixtureA();
		Fixture b = contact.getFixtureB();
		if (a.isSensor() && a.getBody().getUserData() != null && a.getBody().getUserData() instanceof Box2DPlayer && (b.getBody().getUserData() == null || b.getBody().getUserData() != null && !(b.getBody().getUserData() instanceof Bomb))) {
			((Box2DPlayer) a.getBody().getUserData()).isTouchingGround = true;
			System.out.println("Touching the ground");
		}
		if (b.isSensor() && b.getBody().getUserData() != null && b.getBody().getUserData() instanceof Box2DPlayer &&(a.getBody().getUserData() == null || a.getBody().getUserData() != null && !(a.getBody().getUserData() instanceof Bomb))) {
			((Box2DPlayer) b.getBody().getUserData()).isTouchingGround = true;
			System.out.println("Touching the ground");
		}
		
		

		//bomb detection

		
		if (a.getBody().getUserData() != null && a.getBody().getUserData() instanceof Box2DPlayer && ((Box2DPlayer)a.getBody().getUserData()).isMo == false
				&& b.getBody().getUserData() != null && b.getBody().getUserData() instanceof Bomb  && ((Bomb)b.getBody().getUserData()).isMo == true) {
			platf.player2.hp--;
			platf.player1.bombs.remove(0);
			toBeDestroyed.add(b.getBody());
			((Box2DPlayer) a.getBody().getUserData()).isTouchingBomb = true;
			
			System.out.println("Touching a bomb om");
			
			
		}
		
		if (b.getBody().getUserData() != null && b.getBody().getUserData() instanceof Box2DPlayer && ((Box2DPlayer)b.getBody().getUserData()).isMo == false
				&& a.getBody().getUserData() != null && a.getBody().getUserData() instanceof Bomb  && ((Bomb)a.getBody().getUserData()).isMo == true) {
			platf.player2.hp--;
			platf.player1.bombs.remove(0);
			toBeDestroyed.add(a.getBody());
			((Box2DPlayer) b.getBody().getUserData()).isTouchingBomb = true;
			System.out.println("Touching a bomb om");
			
			
		}
		
		
		
		if (a.getBody().getUserData() != null && a.getBody().getUserData() instanceof Box2DPlayer && ((Box2DPlayer)a.getBody().getUserData()).isMo == true
				&& b.getBody().getUserData() != null && b.getBody().getUserData() instanceof Bomb  && ((Bomb)b.getBody().getUserData()).isMo == false) {
			platf.player1.hp--;
			platf.player2.bombs.remove(0);
			toBeDestroyed.add(b.getBody());
			
			//((Box2DPlayer) a.getBody().getUserData()).isTouchingBomb = true;
		
			System.out.println("Touching a bomb mo");
			
			
		}
		
		if (b.getBody().getUserData() != null && b.getBody().getUserData() instanceof Box2DPlayer && ((Box2DPlayer)b.getBody().getUserData()).isMo == true
				&& a.getBody().getUserData() != null && a.getBody().getUserData() instanceof Bomb  && ((Bomb)a.getBody().getUserData()).isMo == false) {
			platf.player1.hp--;
			platf.player2.bombs.remove(0);
			toBeDestroyed.add(a.getBody());
			((Box2DPlayer) b.getBody().getUserData()).isTouchingBomb = true;
			System.out.println("Touching a bomb mo");
			
			
		}

		
	}

	@Override
	public void endContact(Contact contact) {
		Fixture a = contact.getFixtureA();
		Fixture b = contact.getFixtureB();
		if (a.isSensor() && a.getBody().getUserData() != null && a.getBody().getUserData() instanceof Box2DPlayer) {
			((Box2DPlayer) a.getBody().getUserData()).isTouchingGround = false;
			System.out.println("Not Touching the ground");
		}
		if (b.isSensor() && b.getBody().getUserData() != null && b.getBody().getUserData() instanceof Box2DPlayer) {
			((Box2DPlayer) b.getBody().getUserData()).isTouchingGround = false;
			System.out.println("Not Touching the ground");
		}

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}
