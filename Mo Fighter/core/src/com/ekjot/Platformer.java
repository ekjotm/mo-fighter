package com.ekjot;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class Platformer implements Screen, InputProcessor {

	boolean started = false;

	LessonBox2D game;
	SpriteBatch batch;
	World world;
	Box2DDebugRenderer debug;
	OrthographicCamera debugCam;
	OrthographicCamera camera;

	BodyGenerator bodyGen;
	Box2DPlayer player1, player2;

	TextureAtlas player1Atlas, player2Atlas;

	Texture bg, endscreen, intro, blueHeart3, blueHeart2, blueHeart1, redHeart1, redHeart2, redHeart3;

	Music bgMusic, endMusic;

	MyContactListener contactListener;

	public Platformer(LessonBox2D game) {
		this.game = game;
		this.batch = game.batch;

		bgMusic = Gdx.audio.newMusic(Gdx.files.internal("bgMusic.mp3"));
		bgMusic.setLooping(true);
		endMusic = Gdx.audio.newMusic(Gdx.files.internal("OutroMusic.wav"));
		endMusic.setVolume(0.15f);
		endMusic.setLooping(true);

		bg = new Texture("MoFighterBG.png");
		intro = new Texture("Start Screen.png");
		endscreen = new Texture("EndScreen.jpg");

		blueHeart3 = new Texture("blueH3.png");
		blueHeart2 = new Texture("blueH2.png");
		blueHeart1 = new Texture("blueH1.png");

		redHeart3 = new Texture("redH3.png");
		redHeart2 = new Texture("redH2.png");
		redHeart1 = new Texture("redH1.png");

		world = new World(new Vector2(0, -9.8f), true);
		contactListener = new MyContactListener(this);
		world.setContactListener(contactListener);

		debug = new Box2DDebugRenderer();
		debugCam = new OrthographicCamera();
		camera = new OrthographicCamera();
		debugCam.setToOrtho(false, 8f, 4.8f);
		camera.setToOrtho(false, 800, 480);

		bodyGen = new BodyGenerator(world);

		// crate
		// bodyGen.generateRectangle(4, 2.4f, .25f, .25f, 1f, 0.7f, 0.5f,
		// BodyDef.BodyType.DynamicBody);
		// bodyGen.generateRectangle(3.8f, 3.4f, .10f, .10f, 1f, 0.7f, 0.5f,
		// BodyDef.BodyType.DynamicBody);

		// boundaries
		bodyGen.generateRectangle(4, 0.05f, 8, 0.05f, 1f, 0f, 0.3f, BodyDef.BodyType.StaticBody);
		// bodyGen.generateRectangle(0.05f, 2.4f, 0.05f, 2.4f, 1f, 0f, 1f,
		// BodyDef.BodyType.StaticBody);
		// bodyGen.generateRectangle(4, 4.75f, 4, 0.05f, 1f, 0f, 1f,
		// BodyDef.BodyType.StaticBody);
		// bodyGen.generateRectangle(7.95f, 2.4f, 0.05f, 2.4f, 1f, 0f, 1f,
		// BodyDef.BodyType.StaticBody);

		// mid plat
		bodyGen.generateRectangle(4f, 1f, 1f, 0.05f, 1f, 0f, 0.4f, BodyDef.BodyType.StaticBody);
		// top right plat
		bodyGen.generateRectangle(6f, 3.5f, 1f, 0.05f, 1f, 0f, 0.4f, BodyDef.BodyType.StaticBody);
		// top left plat
		bodyGen.generateRectangle(2f, 3.5f, 1f, 0.05f, 1f, 0f, 0.4f, BodyDef.BodyType.StaticBody);

		player1Atlas = new TextureAtlas(Gdx.files.internal("MoSheet.txt"));
		player1 = new Box2DPlayer(world, bodyGen.generatePlayer(2f, 0.1f), new Texture("MoBullet.png"), true,
				player1Atlas);

		player2Atlas = new TextureAtlas(Gdx.files.internal("OmSheet.txt"));
		player2 = new Box2DPlayer(world, bodyGen.generatePlayer(6f, 0.1f), new Texture("OMBullet.png"), false,
				player2Atlas);

		Gdx.input.setInputProcessor(this); // this class handles input
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		bgMusic.play();

		player1.update();
		player2.update();

		debugCam.update();
		camera.update();

		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		batch.draw(intro, 0, 0);

		if (started) {
			batch.draw(bg, 0, 0);

			// lives
			if (player1.hp == 3) {
				batch.draw(redHeart3, 0, 0);
			} else if (player1.hp == 2) {
				batch.draw(redHeart2, 0, 0);
			} else if (player1.hp == 1) {
				batch.draw(redHeart1, 0, 0);
			}

			if (player2.hp == 3) {
				batch.draw(blueHeart3, 704, 0);
			} else if (player2.hp == 2) {
				batch.draw(blueHeart2, 704, 0);
			} else if (player2.hp == 1) {
				batch.draw(blueHeart1, 704, 0);
			}

			player1.draw(batch);
			player2.draw(batch);
		}

		if (player1.hp <= 0 || player2.hp <= 0) {
			batch.draw(endscreen, 0, 0);
			bgMusic.dispose();
			endMusic.play();
		}
		batch.end();
		// debug.render(world, debugCam.combined);

		
		world.step(1 / 60f, 6, 2);
		
		wipeDestroyed();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.A) {
			if (Gdx.input.isKeyPressed(Keys.S)) {
				player1.fLeft = true;
			} else {
				player1.left = true;
			}
		}

		if (keycode == Keys.D) {
			if (Gdx.input.isKeyPressed(Keys.S)) {
				player1.fRight = true;
			} else {
				player1.right = true;
			}
		}

		if (keycode == Keys.W) {
			if (Gdx.input.isKeyPressed(Keys.S)) {
				player1.sJump = true;
			} else {
				player1.jump = true;
			}

		}

		if (keycode == Keys.Q) {
			player1.shotFired = true;
		}

		// p2

		if (keycode == Keys.LEFT) {
			if (Gdx.input.isKeyPressed(Keys.DOWN)) {
				player2.fLeft = true;
			} else {
				player2.left = true;
			}
		}

		if (keycode == Keys.RIGHT) {
			if (Gdx.input.isKeyPressed(Keys.DOWN)) {
				player2.fRight = true;
			} else {
				player2.right = true;
			}
		}

		if (keycode == Keys.UP) {
			if (Gdx.input.isKeyPressed(Keys.DOWN)) {
				player2.sJump = true;
			} else {
				player2.jump = true;
			}

		}

		if (keycode == Keys.SHIFT_RIGHT) {
			player2.shotFired = true;
		}

		if (keycode == Keys.ENTER) {
			started = true;
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.A) {
			player1.left = false;
			player1.fLeft = false;
			// if(player1.isTouchingGround)
			// player1.body.setLinearVelocity(-1f,
			// player1.body.getLinearVelocity().y);
		}

		if (keycode == Keys.D) {
			player1.right = false;
			player1.fRight = false;
			// if(player1.isTouchingGround)
			// player1.body.setLinearVelocity(1f,
			// player1.body.getLinearVelocity().y);
		}

		if (keycode == Keys.W) {
			player1.jump = false;
			player1.sJump = false;
		}

		if (keycode == Keys.Q) {
			player1.shotFired = false;
		}

		// p2

		if (keycode == Keys.LEFT) {
			player2.left = false;
			player2.fLeft = false;
			// if(player2.isTouchingGround)
			// player2.body.setLinearVelocity(-1f,
			// player1.body.getLinearVelocity().y);
		}

		if (keycode == Keys.RIGHT) {
			player2.right = false;
			player2.fRight = false;
			// if(player2.isTouchingGround)
			// player2.body.setLinearVelocity(1f,
			// player1.body.getLinearVelocity().y);
		}

		if (keycode == Keys.UP) {
			player2.jump = false;
			player2.sJump = false;
		}

		if (keycode == Keys.SHIFT_RIGHT) {
			player2.shotFired = false;
		}

		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	public void wipeDestroyed() {
		for (int i = 0; i < contactListener.toBeDestroyed.size(); i++) {
			System.out.println("x");
			world.destroyBody(contactListener.toBeDestroyed.get(i));
			
			contactListener.toBeDestroyed.remove(i);
		}

	}

}
