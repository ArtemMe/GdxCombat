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
	TextureAtlas atlas;

	public Sonya(boolean player) {
		super(player);
		atlas = new TextureAtlas(Gdx.files.internal("gfx/fighters/sonya.pack"));
		winsSound = Gdx.audio.newSound(Gdx.files.internal("sounds/sonyawins.mp3"));

		texture = new TextureRegion(atlas.findRegion("walk0"));

		if (player) {
			stanceAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "stance", 7));
			walkAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "walk", 8));
			jumpAnim = new Animation(0.3f, AnimationUtils.loadAnim(atlas, "jump", 1));
			jumpFAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "jumpf", 7));
			duckAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "duck", 2));
			punchAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "punch", 4));
			kickAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "kick", 4));
			hitAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "punch", 3));
			winAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "win", 8));
			loseAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "lose", 7));
			blockAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "block", 3));
		} else {
			stanceAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "stance", 7, true));
			walkAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "walk", 8, true));
			jumpAnim = new Animation(0.3f, AnimationUtils.loadAnim(atlas, "jump", 1, true));
			jumpFAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "jumpf", 7, true));
			duckAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "duck", 2, true));
			punchAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "punch", 4, true));
			kickAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "kick", 4, true));
			hitAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "hit", 3, true));
			winAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "win", 8, true));
			loseAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "lose", 7, true));
			blockAnim = new Animation(0.1f, AnimationUtils.loadAnim(atlas, "block", 3, true));
		}

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