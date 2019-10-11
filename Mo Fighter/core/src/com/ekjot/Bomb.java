package com.ekjot;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Bomb {
	World world;
	boolean isMo;
	Body b;
	
	public Bomb(World world, float initX, float initY, boolean isMo){
		this.world = world;
		this.isMo = isMo;
		this.b = generateBomb(initX,initY);
		b.setUserData(this);
	}
	
	public Body generateBomb(float x, float y) {
		
		// create a body with a position and type
		BodyDef bodydef = new BodyDef();
		bodydef.position.set(x,y);
		bodydef.type = BodyDef.BodyType.DynamicBody;
		
		Body body = world.createBody(bodydef);
		
		// create the shape of the object
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.14f, 0.06f);
		
		// create the fixture
		FixtureDef fixturedef = new FixtureDef();
		fixturedef.density = 5f;
		fixturedef.restitution = 0.3f;
		fixturedef.friction = 1f;
		fixturedef.shape = shape;
		
		body.createFixture(fixturedef);
	
		return body;
	}


	public Array<Fixture> getFixtureList() {
		
		return b.getFixtureList();
	}


	public float getX() {
		
		return b.getPosition().x;
	}

	public float getY() {
		
		return b.getPosition().y ;
	}

	public void destroyFixture(Fixture fixture) {
		this.b.destroyFixture(fixture);
		
	}
}
