package pl.kotcrab.gdxcombat;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AnimationUtils
{
	public static Array<TextureRegion> loadAnim(TextureAtlas atlas, String name, int framesNumber, boolean flip)
	{
		Array<TextureRegion> frames = new Array<TextureRegion>();
		
		for (int i = 0; i < framesNumber; i++)
		{
			TextureRegion region = new TextureRegion(atlas.findRegion(name + i));
			if(flip) region.flip(true, false);
			frames.add(region);
		}
		
		return frames;
	}
	
	public static Array<TextureRegion> loadAnim(TextureAtlas atlas, String name, int framesNumber)
	{
		return loadAnim(atlas, name, framesNumber, false);
	}
}