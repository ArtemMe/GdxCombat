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

import pl.kotcrab.gdxcombat.core.TexturePart;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;

public class HealthBar {
	private TexturePart foreground;
	private TextureRegion background;
	private TextureRegion name;
	private int percent = 100;
	private int x, y;

	boolean flip;

	Matrix4 mx4Flip = new Matrix4();

	public HealthBar(TextureRegion foreground, TextureRegion background, TextureRegion name, int x, int y, boolean flip) {
		this.background = background;
		this.foreground = new TexturePart(foreground, x, y);
		this.name = name;
		this.x = x;
		this.y = y;
		this.flip = flip;

		if (flip) {
			mx4Flip = new Matrix4();
			mx4Flip.rotate(0, 0, 1, 180);
			mx4Flip.translate(2 * -x - 1, 2 * -y, 0);
		}
	}

	public void draw(SpriteBatch batch) {
		batch.draw(background, x - background.getRegionWidth() / 2, y - background.getRegionHeight() / 2);

		if (flip) {
			Matrix4 old = batch.getTransformMatrix().cpy();
			batch.setTransformMatrix(mx4Flip);
			foreground.draw(batch);
			batch.setTransformMatrix(old);
		} else {
			foreground.draw(batch);
		}

		if (flip) {
			batch.draw(name, x + background.getRegionWidth() / 2 - name.getRegionWidth() - 20, y - name.getRegionHeight() / 2 - 1);
		} else {
			batch.draw(name, x - background.getRegionWidth() / 2 + 20, y - name.getRegionHeight() / 2 - 1);

		}
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		if (percent < 0)
			percent = 0;

		if (this.percent == percent)
			return;

		this.percent = percent;

		foreground.SetEnd(percent, 100);

	}

}