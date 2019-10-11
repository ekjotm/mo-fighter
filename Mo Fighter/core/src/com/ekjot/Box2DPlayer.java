package com.ekjot;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Box2DPlayer {
	World world;
	Body body;
	boolean left = false, fLeft = false, right = false, fRight = false, jump = false, sJump = false, 
			shotFired = false;
	public boolean isTouchingGround,isTouchingBomb, isMo;

	public TextureAtlas atlas;
	public AtlasRegion standing;
	public Animation<TextureRegion> anim;
	public float elapsedTime, bombTime;

	public ArrayList<Bomb> bombs;
	Texture bText;

	BodyGenerator bodyGen;

	public Sprite player;

	int hp = 3;

	public Box2DPlayer(World world, Body body, Texture bullet, Boolean isMo, TextureAtlas tAtlas){
		this.isMo = isMo;
		
		if(isMo) body.setUserData("MO"); 
		else body.setUserData("OMS");
		
		this.world = world;
		this.body = body;
		body.setUserData(this);
		this.bText = bullet;
		this.atlas = tAtlas;
		standing = atlas.findRegion("Standing");
		Array<AtlasRegion> walking = new Array<AtlasRegion>();
		walking.add(atlas.findRegion("Run1"));
		walking.add(atlas.findRegion("Run2"));
		walking.add(atlas.findRegion("Run3"));
		walking.add(atlas.findRegion("Run4"));
		walking.add(atlas.findRegion("Run5"));
		walking.add(atlas.findRegion("Run6"));
		walking.add(atlas.findRegion("Run7"));
		walking.add(atlas.findRegion("Run8"));
		walking.add(atlas.findRegion("Run9"));

		anim = new Animation<TextureRegion>(1 / 10f, walking);
		player = new Sprite(standing);
		bodyGen = new BodyGenerator(world);
		elapsedTime = 0f;
		bombs = new ArrayList<Bomb>();
		//HELP OMSTEAD PLS
		System.out.println(body.getUserData());

	}

	public void update() {
		
		if(body.getLinearVelocity().x > 10 ){
			for(float i = body.getLinearVelocity().x; i > 10; i -= 1){
				body.setLinearVelocity(body.getLinearVelocity().x - 1, body.getLinearVelocity().y);
			}
		}
		
		if(body.getLinearVelocity().x < -10 ){
			for(float i = body.getLinearVelocity().x; i < -10; i += 1){
				body.setLinearVelocity(body.getLinearVelocity().x + 1, body.getLinearVelocity().y);
			}
		}
		
		if (left) {
			body.applyForce(-0.6f, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
			//body.setLinearVelocity(-2.5f, body.getLinearVelocity().y);
			player.setRegion(anim.getKeyFrame(elapsedTime));
			if(isMo)
			player.flip(true, false);
		}
		if (right) {
			body.applyForce(0.6f, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
			//body.setLinearVelocity(2.5f, body.getLinearVelocity().y);
			player.setRegion(anim.getKeyFrame(elapsedTime));
			if(!isMo){
				player.flip(true, false);
			}
				
		}
		if (jump && isTouchingGround) {
			body.applyLinearImpulse(0, 0.15f, body.getWorldCenter().x, body.getWorldCenter().y, true);
		}
		if (fLeft) {
			body.applyForce(-1f, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
			//body.setLinearVelocity(-5f, body.getLinearVelocity().y);
			player.setRegion(anim.getKeyFrame(elapsedTime));
			if(isMo)
				player.flip(true, false);
		}
		if (fRight) {
			//body.setLinearVelocity(5f, body.getLinearVelocity().y);
			
			body.applyForce(1f, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
			player.setRegion(anim.getKeyFrame(elapsedTime));
			if(!isMo){
				player.flip(true, false);
			}
		}
		if (sJump && isTouchingGround) {
			body.applyLinearImpulse(0, 0.3f, body.getWorldCenter().x, body.getWorldCenter().y, true);
		} else if (body.getLinearVelocity().x == 0) {
			player.setRegion(standing);
		}

		if (shotFired && bombTime > 1.25f) {
			Bomb r = new Bomb(world, body.getPosition().x, body.getPosition().y, isMo);
					
					
					
			bombs.add(r);

			bombTime = 0;
		}

		if (bombs.size() > 5) {
			world.destroyBody(bombs.get(0).b);
			bombs.remove(0);
		}
		
		
		if(isTouchingBomb){
			
		}
		
		
		elapsedTime += Gdx.graphics.getDeltaTime();
		
		bombTime += Gdx.graphics.getDeltaTime();

		if (elapsedTime >= anim.getAnimationDuration()) {
			elapsedTime -= anim.getAnimationDuration();
		}

		if (body.getPosition().x + 0.5f < 0) {
			body.setTransform(8.05f, body.getPosition().y, 0);
		} else if (body.getPosition().x > 8.5f) {
			body.setTransform(0f, body.getPosition().y, 0);
		} else {
			body.setTransform(body.getPosition(), body.getAngle());
		}
		
		
		//hearts
		
		

	}

	public void draw(SpriteBatch batch) {
		if (bombs.size() > 0) {
			for (int i = 0; i < bombs.size(); i++) {
				Sprite temp = new Sprite(bText);
				temp.setPosition(bombs.get(i).getX() * 100 - 12.5f, bombs.get(i).getY() * 100 - 6.75f);
				temp.draw(batch);
			}
		}
		player.setPosition(body.getPosition().x * 100 - player.getWidth() / 2,
				body.getPosition().y * 100 - player.getHeight() / 2 + 30);
		player.draw(batch);
	}

}
