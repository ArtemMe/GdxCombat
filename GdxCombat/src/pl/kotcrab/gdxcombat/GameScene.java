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

package pl.kotcrab.gdxcombat;

import pl.kotcrab.gdxcombat.arenas.AbstractArena;
import pl.kotcrab.gdxcombat.core.AbstractScene;
import pl.kotcrab.gdxcombat.core.AnimationUtils;
import pl.kotcrab.gdxcombat.core.Touch;
import pl.kotcrab.gdxcombat.fighters.AbstractFighter;
import pl.kotcrab.gdxcombat.fighters.ai.RandomBot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class GameScene extends AbstractScene {
	private AbstractArena arena;
	private AbstractFighter player;
	private AbstractFighter enemy;

	private World world;

	private OrthographicCamera camera;

	private SpriteBatch hudBatch;

	private static int GAMETIME = 99;
	private int time;
	private long startTime;

	private boolean gameover = false;

	private Animation fightTextAnim;

	private Animation nameWinsAnim;
	private int nameWinAnimRenderX;

	private float stateTime;

	// Debug box2d
	private boolean box2dDebug = false;
	private Box2DDebugRenderer debugRenderer;
	private Matrix4 box2dRenderMatrix;

	public GameScene(AbstractArena arena, AbstractFighter player, AbstractFighter enemy) {
		this.arena = arena;
		this.player = player;
		this.enemy = enemy;

		world = new World(new Vector2(0, -20), true);

		camera = Touch.getCamera();

		Matrix4 hudMatrix = new Matrix4();
		hudMatrix.setToOrtho2D(0, 0, 480, 320);
		hudBatch = new SpriteBatch();
		hudBatch.setProjectionMatrix(hudMatrix);

		fightTextAnim = new Animation(0.03f, AnimationUtils.loadAnim(Assets.getTextureAtlas(), "fight", 21));
		startTime = System.currentTimeMillis();

		player.setPosition(30, 150 - player.getHeight());
		enemy.setPosition(350, 150 - enemy.getHeight());

		player.setGroundLevel(arena.getGroundLevel());
		enemy.setGroundLevel(arena.getGroundLevel());

		player.setOponent(enemy);
		enemy.setOponent(player);

		enemy.initAi(new RandomBot());

		arena.initPhys(world);
		player.initPhys(world);
		enemy.initPhys(world);

		createHUD();

		debugRenderer = new Box2DDebugRenderer();
		box2dRenderMatrix = new Matrix4();

		Assets.fight.play();
	}

	private HealthBar playerHealthBar;
	private HealthBar enemyHealthBar;

	private void createHUD() {
		playerHealthBar = new HealthBar(Assets.getTextureRegion("healthbar_full"), Assets.getTextureRegion("healthbar_bg"), Assets.getTextureRegion(player.getName()), 100, 300, false);
		enemyHealthBar = new HealthBar(Assets.getTextureRegion("healthbar_full"), Assets.getTextureRegion("healthbar_bg"), Assets.getTextureRegion(enemy.getName()), 375, 300, true);
	}

	@Override
	public void update() {
		stateTime += Gdx.graphics.getDeltaTime();

		if (box2dDebug) {
			box2dRenderMatrix.set(camera.combined);
			box2dRenderMatrix.scale(10, 10, 0);
		}

		camera.position.x = player.getX() + 180;

		if (camera.position.x < -200)
			camera.position.x = -200;
		if (camera.position.x > 440)
			camera.position.x = 440;

		arena.update();
		player.update();
		enemy.update();

		if (!gameover) {
			playerHealthBar.setPercent(player.getHealth());
			enemyHealthBar.setPercent(enemy.getHealth());

			time = GAMETIME - (int) ((System.currentTimeMillis() - startTime) / 1000);
		}

		if (time == 0 && !gameover) {
			gameover = true;

			if (player.getHealth() == enemy.getHealth()) {
				createWinsText("draw");
				return;
			}

			if (player.getHealth() > enemy.getHealth())
				playerWins();
			else
				enemyWins();
		}

		if (player.getHealth() <= 0 && !gameover) {
			enemyWins();
		}

		if (enemy.getHealth() <= 0 && !gameover) {
			playerWins();
		}
	}

	private void enemyWins() {
		gameover = true;
		createWinsText(enemy.getName() + "wins");
		enemy.getWinsSound().play();
	}

	private void playerWins() {
		gameover = true;
		createWinsText(player.getName() + "wins");
		player.getWinsSound().play();
	}

	private void createWinsText(String animNane) {
		nameWinsAnim = new Animation(0.1f, AnimationUtils.loadAnim(Assets.getTextureAtlas(), animNane, 2));
		nameWinsAnim.setPlayMode(Animation.LOOP);
		nameWinAnimRenderX = (480 - nameWinsAnim.getKeyFrame(0).getRegionWidth()) / 2;
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.begin();

		arena.render(batch);
		player.render(batch);
		enemy.render(batch);

		world.step(1 / 60f, 6, 2); // przy box2d lepiej zrobic krok po wyrenderowaniu wszystkiego

		batch.end();

		if (box2dDebug) {
			debugRenderer.render(world, box2dRenderMatrix);
		}

		hudBatch.begin(); // HUD

		if (!fightTextAnim.isAnimationFinished(stateTime))
			hudBatch.draw(fightTextAnim.getKeyFrame(stateTime), 165, 220);
		if (gameover)
			hudBatch.draw(nameWinsAnim.getKeyFrame(stateTime), nameWinAnimRenderX, 240);
		playerHealthBar.draw(hudBatch);
		enemyHealthBar.draw(hudBatch);
		Assets.timerFont.draw(hudBatch, Integer.toString(time), 228, 325);

		hudBatch.end();

	}

	@Override
	public void dispose() {
		arena.dispose();
		player.dispose();
		enemy.dispose();
		world.dispose();
	}

	@Override
	public boolean keyDown(int key) {
		if (!gameover)
			player.keyDown(key);

		if (key == Keys.F12)
			box2dDebug = !box2dDebug;

		return false;
	}

	@Override
	public boolean keyUp(int key) {
		if (!gameover)
			player.keyUp(key);
		return false;
	}

}