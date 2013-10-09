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

import pl.kotcrab.gdxcombat.arenas.PitArena;
import pl.kotcrab.gdxcombat.core.AbstractScene;
import pl.kotcrab.gdxcombat.core.Touch;
import pl.kotcrab.gdxcombat.fighters.LiuKang;
import pl.kotcrab.gdxcombat.fighters.Sonya;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;

public class GdxCombat implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;

	private AbstractScene activeScene;

	private FPSLogger fpsLogger = new FPSLogger();

	@Override
	public void create() {
		Assets.load();

		camera = new OrthographicCamera(480, 320);
		camera.position.x = 240;
		camera.position.y = 160;
		Touch.setCamera(camera);
		batch = new SpriteBatch();

		changeScene(new GameScene(new PitArena(), new LiuKang(true), new Sonya(false)));
	}

	@Override
	public void dispose() {
		batch.dispose();
		activeScene.dispose();

		Assets.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		update();

		batch.setProjectionMatrix(camera.combined);
		activeScene.render(batch);
	}

	public void update() {
		camera.update();
		activeScene.update();
		fpsLogger.log();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	public void changeScene(AbstractScene scene) {
		if (activeScene != null)
			activeScene.dispose();

		activeScene = scene;

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(new GestureDetector(activeScene));
		multiplexer.addProcessor(activeScene);

		Gdx.input.setInputProcessor(multiplexer);
	}


}
