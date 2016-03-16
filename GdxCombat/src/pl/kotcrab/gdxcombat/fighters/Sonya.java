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

import pl.kotcrab.gdxcombat.core.AnimationUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Sonya extends AbstractFighter {
	private TextureAtlas atlas;

	public Sonya(boolean isPlayer) {
		super(isPlayer);
		atlas = new TextureAtlas(Gdx.files.internal("gfx/fighters/sonya.pack"));
		winsSound = Gdx.audio.newSound(Gdx.files.internal("sounds/sonyawins.mp3"));

		texture = new TextureRegion(atlas.findRegion("walk0"));

		stanceAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "stance", 7, isPlayer));
		walkAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "walk", 8, isPlayer));
		jumpAnim = new Animation(0.3f, AnimationUtils.loadAnim(atlas, "jump", 1, isPlayer));
		jumpFAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "jumpf", 7, isPlayer));
		duckAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "duck", 2, isPlayer));
		punchAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "punch", 4, isPlayer));
		kickAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "kick", 4, isPlayer));
		hitAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "hit", 3, isPlayer));
		winAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "win", 8, isPlayer));
		loseAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "lose", 7, isPlayer));
		blockAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "block", 3, isPlayer));

		stanceAnim.setPlayMode(Animation.LOOP);
		jumpFAnim.setPlayMode(Animation.LOOP);
		punchAnim.setPlayMode(Animation.LOOP);

		switchAnimation(stanceAnim);
	}

	@Override
	public void dispose() {
		super.dispose();
		atlas.dispose();
		winsSound.dispose();
	}

	@Override
	public String getName() {
		return "sonya";
	}

}