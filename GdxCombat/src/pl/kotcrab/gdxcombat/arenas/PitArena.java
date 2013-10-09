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

package pl.kotcrab.gdxcombat.arenas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class PitArena extends AbstractArena {
	private Texture pit;

	public PitArena() {
		pit = new Texture(Gdx.files.internal("gfx/arenas/thepit.png"));
	}

	@Override
	public void update() {

	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(pit, -442, 0);
	}

	@Override
	public void dispose() {
		pit.dispose();
	}

	@Override
	public void initPhys(World world) {
		// Do³
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(20, 6.5f);
		Body groundBody = world.createBody(groundBodyDef);

		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(70, 1.0f);
		groundBody.createFixture(groundBox, 0.0f);
		groundBox.dispose();

		// Lewa œcianka
		BodyDef leftBodyDef = new BodyDef();
		leftBodyDef.position.set(-45, 6.5f);
		Body leftBody = world.createBody(leftBodyDef);

		PolygonShape leftBox = new PolygonShape();
		leftBox.setAsBox(1.0f, 70f);
		leftBody.createFixture(leftBox, 0.0f);
		leftBox.dispose();

		// Prawa œcianka
		BodyDef rightBodyDef = new BodyDef();
		rightBodyDef.position.set(69, 6.5f);
		Body rightBody = world.createBody(rightBodyDef);

		PolygonShape rightBox = new PolygonShape();
		rightBox.setAsBox(1.0f, 70f);
		rightBody.createFixture(rightBox, 0.0f);
		rightBox.dispose();
	}

	@Override
	public int getGroundLevel() {
		return 76;
	}

}