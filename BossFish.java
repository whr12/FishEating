
public class BossFish extends AIFish {

	/**properties**/
	boolean warntext;
	int text_x;
	int text_y;
	int text_width;
	int text_height;
	
	String text;
	
	/**constructor**/
	public BossFish(int x, int y)
	{
		super(3,x,y);
		v_x = 3;
		text = "Danger!!!";						//boss预警
		text_width = (int) (width*level);
		text_height = (int) (height*level);
	}
	
	/**methods**/
	public void Location_Init()
	{
		super.Location_Init();
		
		warntext = true;
		text_x = location_x;
		text_y = location_y+50;
		
	}
	
	public void SetVelocity(PlayerFish player, boolean avoid)
	{
		v_x = 3;
		v_y = 0;
	}
	
	public void UpdateLocation(int ms)
	{
		if(warntext)
		{
			text_x += direction ? v_x*ms:-v_x*ms;
			if(text_x < -width*level-100)
				warntext = false;
			if(text_x > boundary_x+100)
				warntext = false;
		}
		else
		{
			location_x += direction ? v_x*ms:-v_x*ms;
			if(location_x < -width*level-100&&Math.random()>0.9)	//boss不在频率上就以一定概率刷新出boss
				Location_Init();
			if(location_x > boundary_x+100&&Math.random()>0.9)
				Location_Init();
		}
	}
	
	
}
