package com.ekjot;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class BodyGenerator {
	World world;
	
	public BodyGenerator(World world) {
		this.world = world;
	}
	
	public Body generatePlayer(float x, float y) {
		// create a body with a position and type
		BodyDef bodydef = new BodyDef();
		bodydef.position.set(x,y);
		bodydef.type = BodyDef.BodyType.DynamicBody;
		bodydef.fixedRotation = true;
		Body body = world.createBody(bodydef);
		
		// create the Torso
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(.16f, .16f);
		
		FixtureDef fixturedef = new FixtureDef();
		fixturedef.density = 1f;
		fixturedef.restitution = 0.2f;
		fixturedef.friction = 0.3f;
		fixturedef.shape = shape;
		body.createFixture(fixturedef);
		
		//create the ground sensor circle
		CircleShape circle = new CircleShape();
		circle.setRadius(0.08f);
		circle.setPosition(new Vector2(0,-0.16f));
		
		fixturedef = new FixtureDef();
		fixturedef.density = 0.0f;
		fixturedef.friction = 0.3f;
		fixturedef.restitution = 0.2f;
		fixturedef.shape = circle;
		fixturedef.isSensor = true;
		body.createFixture(fixturedef);
		
		return body;
		
	}
	
	public Body generateRectangle(float x, float y,
			float width, float height,
			float density, float restitution, float friction,
			BodyDef.BodyType type) {
		
		// create a body with a position and type
		BodyDef bodydef = new BodyDef();
		bodydef.position.set(x,y);
		bodydef.type = type;
		Body body = world.createBody(bodydef);
		
		// create the shape of the object
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width, height);
		
		// create the fixture
		FixtureDef fixturedef = new FixtureDef();
		fixturedef.density = density;
		fixturedef.restitution = restitution;
		fixturedef.friction = friction;
		fixturedef.shape = shape;
		body.createFixture(fixturedef);
		
		return body;
	}
	
	

}
