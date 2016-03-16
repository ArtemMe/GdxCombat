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

package pl.kotcrab.gdxcombat.fighters.ai;

import com.badlogic.gdx.math.MathUtils;

import pl.kotcrab.gdxcombat.fighters.AbstractFighter;

public class RandomBot implements BotInterface {
	// Klawisze
	private static final int KEY_JUMP = 0;
	private static final int KEY_DUCK = 1;
	private static final int KEY_LEFT = 2;
	private static final int KEY_RIGHT = 3;
	private static final int KEY_PUNCH = 4;
	private static final int KEY_KICK = 5;
	private static final int KEY_BLOCK = 6;

	@Override
	public void update(boolean[] keys, AbstractFighter controlled, AbstractFighter oponent) {
		if (controlled.getState() == AbstractFighter.State.JUMP) {
			keys[KEY_JUMP] = false;
			keys[KEY_RIGHT] = false;
			keys[KEY_LEFT] = false;
		}

		if (controlled.getState() == AbstractFighter.State.BLOCK)
			keys[KEY_BLOCK] = false;

		if (controlled.getState() == AbstractFighter.State.KICK)
			keys[KEY_KICK] = false;

		if (controlled.getState() == AbstractFighter.State.DUCK && MathUtils.random(10) == 10)
			keys[KEY_DUCK] = false;

		if (controlled.getState() == AbstractFighter.State.PUNCH) {
			if (controlled.isCurrentAnimFinished() && MathUtils.random(100) == 10) {
				keys[KEY_PUNCH] = false;
			}
		}

		keys[KEY_LEFT] = !controlled.isOponentInRange();

		if (controlled.isOponentInRange()) {
			if (oponent.getState() == AbstractFighter.State.BLOCK || oponent.getState() == AbstractFighter.State.DUCK)
				return;

			switch (MathUtils.random(10)) {
			case 2:
				keys[KEY_PUNCH] = true;
				break;
			case 5:
				keys[KEY_KICK] = true;
				break;
			default:
				break;

			}
		}
	}

	@Override
	public void beingHit(boolean[] keys, AbstractFighter controlled) {
		switch (MathUtils.random(7)) {
		case 2:
			keys[KEY_BLOCK] = true;
			break;
		case 3:
			keys[KEY_JUMP] = true;
			break;
		case 4:
			keys[KEY_JUMP] = true;
			keys[KEY_RIGHT] = true;
			break;
		case 5:
			keys[KEY_DUCK] = true;
			break;
		default:
			break;

		}
	}

}