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

package pl.kotcrab.gdxcombat.core;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AnimationUtils {
	public static Array<TextureRegion> loadAnim(TextureAtlas atlas, String name, int framesNumber, boolean flip) {
		Array<TextureRegion> frames = new Array<TextureRegion>();

		for (int i = 0; i < framesNumber; i++) {
			TextureRegion region = new TextureRegion(atlas.findRegion(name + i));
			if (flip)
				region.flip(true, false);
			frames.add(region);
		}

		return frames;
	}

	public static Array<TextureRegion> loadAnim(TextureAtlas atlas, String name, int framesNumber) {
		return loadAnim(atlas, name, framesNumber, false);
	}
}