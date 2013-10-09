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

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Assets {
	private static TextureAtlas textures;
	public static BitmapFont timerFont;

	public static Sound fight;
	private static ArrayList<Sound> hitSounds = new ArrayList<Sound>();

	public static void load() {
		textures = new TextureAtlas(Gdx.files.internal("gfx/textures.pack"));
		timerFont = new BitmapFont(Gdx.files.internal("timer.fnt"), textures.findRegion("timer"));
		fight = Gdx.audio.newSound(Gdx.files.internal("sounds/fight.mp3"));

		for (int i = 0; i < 7; i++) {
			hitSounds.add(Gdx.audio.newSound(Gdx.files.internal("sounds/hit" + i + ".mp3")));
		}
	}

	public static void dispose() {
		textures.dispose();
		timerFont.dispose();
		fight.dispose();

		for (int i = 0; i < 7; i++) {
			hitSounds.get(i).dispose();
		}
	}

	public static TextureRegion getTextureRegion(String name) {
		return textures.findRegion(name);
	}

	public static TextureAtlas getTextureAtlas() {
		return textures;
	}

	public static Sound getRandomHitSound() {
		return hitSounds.get(MathUtils.random(hitSounds.size() - 1));
	}
}