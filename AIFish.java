/********************************************************/
/**
 * 
 * @author Administrator
 *
 *这里存储着无数个与你斗争的生命
 *其实他们都和你有同样的咸鱼属性，主要没有强大无敌buff
 *
 */
public class AIFish
{
	//properties
	public int height;					//fish height
	public int width;					//fish width
	public double level;
		
	public int aim_x;
	public int aim_y;
	
	public int location_x;
	public int location_y;
	
	protected double v_x;
	protected double v_y;
	
	public boolean direction;
	private boolean direction_y;
	
	public final boolean LEFT = false;
	public final boolean RIGHT = true;
	
	public int boundary_x;
	public int boundary_y;
	
	//constructor
	//输入为创建鱼的等级和在屏幕上游动的范围
	public AIFish(double lvl, int x, int y)
	{		
		height = 50;
		width  = 80;
		level  = lvl;
		
		boundary_x = x;
		boundary_y = y;
		
		v_x = 0;
		v_y = 0;
		
		Location_Init();
	}

	/**methods**/
	public void Location_Init()
	{
		direction = Math.random()>0.5;
		direction_y = Math.random()>0.5;
		location_x = direction ? -(int)(width*level)-100 : boundary_x+100;
		location_y = (int)(Math.random()*boundary_y);
	}
	
	//**设置游动范围**//
	public void SetBoundary(int x, int y)
	{
		boundary_x = x;
		boundary_y = y;
	}
	
	//设置AI的移动速度和方向
	public void SetVelocity(PlayerFish player, boolean avoid)
	{
		if(avoid)		
		{
			//如果设置了躲避玩家，就会对判断与玩家的距离
			if(player.level>level&&Math.abs(location_x-player.location_x)<level*width*1.5&&Math.abs(location_y-player.location_y)<level*height*1.5)
			{
				//如果与玩家距离够近且等级更低，就会向远离玩家的方向移动，逃离这条咸鱼
				direction = (location_x-player.location_x)>0;
				direction_y = (location_y-player.location_y)>0;
				v_x = Math.random();
				v_y = Math.random();
			}
			else if(player.level<level&&Math.abs(location_x-player.location_x)<level*width*1.5&&Math.abs(location_y-player.location_y)<level*height*1.5)
			{
				//如果与玩家距离够近且等级更低，就会向玩家移动，干掉这条咸鱼
				direction = (location_x-player.location_x)<0;
				direction_y = (location_y-player.location_y)<0;
				v_x = Math.random();
				v_y = Math.random();
			}
			else
			{
				///那咸鱼不在，想干嘛干嘛
				v_x = Math.random();
				v_y = Math.random();
				direction = Math.random()>0.99 ? !direction : direction;			//定了方向就不随便更改了
				direction_y = Math.random()>0.99 ? !direction_y : direction_y;
			}
		}
		else
		{
			//没有设置避免玩家。就不管那咸鱼，想干嘛干嘛
			v_x = Math.random();
			v_y = Math.random();
			direction = Math.random()>0.99 ? !direction : direction;
			direction_y = Math.random()>0.99 ? !direction_y : direction_y;
		}
	}
	
	public void UpdateLocation(int ms)
	{
		location_x += direction ? v_x*ms:-v_x*ms;
		location_y += direction_y ? v_y*ms:-v_y*ms;
		
		if(location_x < -width*level-100)
			Location_Init();
		if(location_x > boundary_x+100)
			Location_Init();
		if(location_y < -height*level)
			Location_Init();
		if(location_y > boundary_y)
			Location_Init();
	}
	
	public boolean CanEat(AIFish fish)
	{
		if(Math.abs(location_x-fish.location_x)<fish.level*fish.width-30&&Math.abs(location_y-fish.location_y)<fish.level*fish.height-10)
			return true;
		else
			return false;
	}
	//**前面有鱼群,准备开饭**//
	public void EatFish(AIFish[] fish)
	{
		for(int i=0; i<fish.length;i++)
		{
			if(CanEat(fish[i]))			//食物到嘴边了吗？
			{
				if(EatFish(fish[i]))	//吞得下吗？
					fish[i].Location_Init();	//目标已消灭
			}
		}
	}
	
	public boolean EatFish(AIFish fish)
	{
		if(level>fish.level)
			return true;
		else
			return false;
	}
}
