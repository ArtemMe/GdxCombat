/*******************************************************************************
 * Copyright 2013 Pawel Pastuszak
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package pl.kotcrab.gdxcombat.fighters;

import pl.kotcrab.gdxcombat.Assets;
import pl.kotcrab.gdxcombat.fighters.ai.BotInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public abstract class AbstractFighter {
	public enum State {
		IDLE, LEFT, RIGHT, JUMP, DUCK, PUNCH, HIT, KICK, END, BLOCK
	}

	private AbstractFighter oponent; // przeciwnik

	private static final int movementForceAmount = 4; // predkosc poruszania sie

	protected TextureRegion texture; // jedna klatka z g³ownej animacji dla obliczeñ box2d
	protected Sound winsSound; // dzwiek odtwarzany przy wygraniu

	// Animacje
	protected Animation currentAnim; // aktualna animacja

	protected Animation stanceAnim; // idle
	protected Animation walkAnim; // chodzenie
	protected Animation jumpAnim; // skok w gore
	protected Animation jumpFAnim; // skok na boki
	protected Animation duckAnim; // kucanie
	protected Animation punchAnim; // piesci
	protected Animation kickAnim; // kopniak
	protected Animation hitAnim; // gdy uderzony
	protected Animation blockAnim; // blok
	protected Animation winAnim; // przy wygraniu
	protected Animation loseAnim; // przy przegraniu

	private float stateTime; // czas aktualnej animacji
	private boolean blockedAnim; // zmienna u¿ywane przez niektóre animacje

	private int groundLevel; // poziom pod³ogi
	private boolean player = false; // czy ten obiekt jest sterowany przez gracza

	private BotInterface bot;

	protected float x, y;
	protected Body body;

	private int health = 100; // ¿ycie

	// Klawisze
	private static final int KEY_JUMP = 0;
	private static final int KEY_DUCK = 1;
	private static final int KEY_LEFT = 2;
	private static final int KEY_RIGHT = 3;
	private static final int KEY_PUNCH = 4;
	private static final int KEY_KICK = 5;
	private static final int KEY_BLOCK = 6;
	private boolean[] keys = { false, false, false, false, false, false, false };
	private State state = State.IDLE;

	long startTime; // dla ciosów

	public abstract String getName();

	public AbstractFighter(boolean player) {
		this.player = player;
	}

	public void render(SpriteBatch batch) {
		if (!player && (state == State.PUNCH || state == State.KICK))
			batch.draw(currentAnim.getKeyFrame(stateTime), x - 30, y);
		else
			batch.draw(currentAnim.getKeyFrame(stateTime), x, y);
	}

	public void update() {
		stateTime += Gdx.graphics.getDeltaTime();

		switch (state) {
		case IDLE:
			stateIdle();
			break;
		case RIGHT:
			stateRight();
			break;
		case LEFT:
			stateLeft();
			break;
		case JUMP:
			stateJump();
			break;
		case DUCK:
			stateDuck();
			break;
		case PUNCH:
			statePunch();
			break;
		case KICK:
			stateKick();
			break;
		case HIT:
			stateHit();
			break;
		case BLOCK:
			stateBlock();
			break;
		default:
			break;

		}
		if (bot != null)
			bot.update(keys, this, oponent);

		x = body.getPosition().x * 10 - texture.getRegionWidth() / 2;
		y = body.getPosition().y * 10 - texture.getRegionHeight() / 2;

	}

	private void stateRight() {
		if (keys[KEY_RIGHT]) {
			body.setLinearVelocity(movementForceAmount, body.getLinearVelocity().y);
		} else {
			state = State.IDLE;
			switchAnimation(stanceAnim);
		}

		if (keys[KEY_JUMP] && isOnGround()) {
			state = State.JUMP;
			switchAnimation(jumpFAnim);
			if (player)
				jumpFAnim.setPlayMode(Animation.LOOP);
			else
				jumpFAnim.setPlayMode(Animation.LOOP_REVERSED);
			body.applyLinearImpulse(new Vector2(0, 2300), body.getWorldCenter(), true);
			return;
		}
	}

	private void stateLeft() {
		if (keys[KEY_LEFT]) {
			body.setLinearVelocity(-1 * movementForceAmount, body.getLinearVelocity().y);
		} else {
			state = State.IDLE;
			switchAnimation(stanceAnim);
		}

		if (keys[KEY_JUMP] && isOnGround()) {
			state = State.JUMP;
			switchAnimation(jumpFAnim);
			if (player)
				jumpFAnim.setPlayMode(Animation.LOOP_REVERSED);
			else
				jumpFAnim.setPlayMode(Animation.LOOP);

			body.applyLinearImpulse(new Vector2(0, 2300), body.getWorldCenter(), true);
			return;
		}
	}

	private void stateDuck() {

		if (blockedAnim && duckAnim.isAnimationFinished(stateTime)) {
			state = State.IDLE;
			switchAnimation(stanceAnim);
			blockedAnim = false;
			return;

		}

		if (blockedAnim)
			return;

		if (!keys[KEY_DUCK]) {

			switchAnimation(duckAnim);
			duckAnim.setPlayMode(Animation.REVERSED);
			blockedAnim = true;
			return;
		}

	}

	private void stateBlock() {
		if (blockedAnim && blockAnim.isAnimationFinished(stateTime)) {
			state = State.IDLE;
			switchAnimation(stanceAnim);
			blockedAnim = false;
			return;
		}

		if (blockedAnim)
			return;

		if (!keys[KEY_BLOCK]) {
			switchAnimation(blockAnim);
			blockAnim.setPlayMode(Animation.REVERSED);
			blockedAnim = true;
			return;
		}
	}

	private void statePunch() {
		if (!keys[KEY_PUNCH]) {
			state = State.IDLE;
			switchAnimation(stanceAnim);
			return;
		}

		if (System.currentTimeMillis() - startTime > 400) {
			startTime = System.currentTimeMillis();

			if (isOponentInRange()) {
				oponent.hit(5);
				isOponentDead();
			}
		}
	}

	private void stateKick() {
		if (currentAnim.isAnimationFinished(stateTime) && !blockedAnim) {
			blockedAnim = true;
			currentAnim.setPlayMode(Animation.REVERSED);
			switchAnimation(currentAnim);
			
			if (isOponentInRange()) {
				oponent.hit(10);
				isOponentDead();
			}
			return;
		}

		if (currentAnim.isAnimationFinished(stateTime) && blockedAnim) {
			blockedAnim = false;
			state = State.IDLE;
			switchAnimation(stanceAnim);
		}
	}

	private void stateJump() {
		if (isOnGround() && body.getLinearVelocity().y == 0) {
			state = State.IDLE;
			switchAnimation(stanceAnim);
		}
	}

	private void stateHit() {
		if (currentAnim.isAnimationFinished(stateTime)) {
			state = State.IDLE;
			switchAnimation(stanceAnim);
		}

	}

	private void stateIdle() {

		if (keys[KEY_LEFT]) {
			state = State.LEFT;
			switchAnimation(walkAnim);
			if (player)
				walkAnim.setPlayMode(Animation.LOOP_REVERSED);
			else
				walkAnim.setPlayMode(Animation.LOOP);

			return;
		}

		if (keys[KEY_RIGHT]) {
			state = State.RIGHT;
			switchAnimation(walkAnim);
			if (player)
				walkAnim.setPlayMode(Animation.LOOP);
			else
				walkAnim.setPlayMode(Animation.LOOP_REVERSED);

			return;
		}

		if (keys[KEY_DUCK]) {
			state = State.DUCK;
			switchAnimation(duckAnim);
			duckAnim.setPlayMode(Animation.NORMAL);
			return;
		}

		if (keys[KEY_BLOCK]) {
			state = State.BLOCK;
			switchAnimation(blockAnim);
			blockAnim.setPlayMode(Animation.NORMAL);
			return;
		}

		if (keys[KEY_PUNCH]) {
			state = State.PUNCH;
			switchAnimation(punchAnim);
			startTime = System.currentTimeMillis();
			return;
		}

		if (keys[KEY_JUMP] && isOnGround()) {
			state = State.JUMP;
			switchAnimation(jumpAnim);
			jumpAnim.setPlayMode(Animation.NORMAL);
			body.setLinearVelocity(0, 0);
			body.applyLinearImpulse(new Vector2(0, 2300), body.getWorldCenter(), true);

			return;
		}

		if (keys[KEY_KICK]) {
			state = State.KICK;
			kickAnim.setPlayMode(Animation.NORMAL);
			switchAnimation(kickAnim);
		}
	}

	public void switchAnimation(Animation newAnim) {
		currentAnim = newAnim;
		stateTime = 0;
	}

	public void hit(int damage) {
		if (state == State.JUMP || state == State.DUCK) { // 2
			return;
		}

		Assets.getRandomHitSound().play(); // 1

		if (bot != null) // 3
			bot.beingHit(keys, this);

		if (state == State.BLOCK) { // 4
			damage /= 2;
		}

		health -= damage; // 5

		if (health > 0) { // 6
			state = State.HIT;
			switchAnimation(hitAnim);
		} else {
			state = State.END;
			switchAnimation(loseAnim);
		}
	}

	public void initPhys(World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set((x + texture.getRegionWidth()) / 10f, (y + texture.getRegionHeight()) / 10f); // samo x i y oznaczalo by dolny lewy rog sprite'a

		body = world.createBody(bodyDef);
		body.setFixedRotation(true);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(76 / 2 / 10f, 136 / 2 / 10f);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;
		fixtureDef.friction = 100f; // w ogóle nie bedzie sie slizgac
		fixtureDef.restitution = 0f;

		body.createFixture(fixtureDef);

		shape.dispose();
	}

	public void initAi(BotInterface bot) {
		this.bot = bot;
	}

	public boolean isOponentInRange() {
		int distance = (int) Math.abs(x - oponent.getX());

		return distance < 86;
	}

	private void isOponentDead() {
		if (oponent.getHealth() <= 0) {
			state = State.END;
			switchAnimation(winAnim);
		}
	}

	private boolean isOnGround() {
		return y < groundLevel;
	}

	public boolean isCurrentAnimFinished() {
		return currentAnim.isAnimationFinished(stateTime);
	}

	public void setGroundLevel(int groundLevel) {
		this.groundLevel = groundLevel;
	}

	public void setOponent(final AbstractFighter oponent) {
		this.oponent = oponent;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public int getWidth() {
		return texture.getRegionHeight();
	}

	public int getHeight() {
		return texture.getRegionHeight();

	}

	public Sound getWinsSound() {
		return winsSound;
	}

	public int getHealth() {
		return health;
	}

	public void keyDown(int key) {
		if (player) {
			switch (key) {
			case Keys.UP:
				keys[KEY_JUMP] = true;
				break;
			case Keys.DOWN:
				keys[KEY_DUCK] = true;
				break;
			case Keys.LEFT:
				keys[KEY_LEFT] = true;
				break;
			case Keys.RIGHT:
				keys[KEY_RIGHT] = true;
				break;
			case Keys.A:
				keys[KEY_PUNCH] = true;
				break;
			case Keys.S:
				keys[KEY_KICK] = true;
				break;
			case Keys.Q:
				keys[KEY_BLOCK] = true;
				break;
			default:
				break;

			}
		}
	}

	public void keyUp(int key) {
		if (player) {
			switch (key) {
			case Keys.UP:
				keys[KEY_JUMP] = false;
				break;
			case Keys.DOWN:
				keys[KEY_DUCK] = false;
				break;
			case Keys.LEFT:
				keys[KEY_LEFT] = false;
				break;
			case Keys.RIGHT:
				keys[KEY_RIGHT] = false;
				break;
			case Keys.A:
				keys[KEY_PUNCH] = false;
				break;
			case Keys.S:
				keys[KEY_KICK] = false;
				break;
			case Keys.Q:
				keys[KEY_BLOCK] = false;
				break;
			default:
				break;

			}
		}
	}

	public void dispose() {
	}
}