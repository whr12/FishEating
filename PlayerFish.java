/****************************************************/
/**
 * 
 * @author Administrator
 *
 *这里是咸鱼成长记录，我会将他的旅途经历记录下来，决定让他变回小咸鱼还是成长为
 *更大的咸鱼！
 *
 */
public class PlayerFish
{
	//properties
	public int height;					//fish height
	public int width;					//fish width
	public double level;				//游戏还是要有等级的
	
	public int aim_x;					//这只咸鱼想去那，帮他记下来，
	public int aim_y;					//毕竟只有7s的记忆
	
	public int location_x;				//然而这只咸鱼还在这
	public int location_y;
	
	private double v_x;					//看看他要跑多快
	private double v_y;
	
	public boolean direction;			//哦，不帮他记录方向，都会迷路

	public final boolean LEFT = false;	//虽然写了这么个东西，好像并没有什么用
	public final boolean RIGHT = true;
	
	public int boundary_x;				//不能让这只咸鱼跑出我的视野
	public int boundary_y;
	
	public int exp;						//获取经验才能升级
	public int lvlupneed;				//你的路还很漫长，这么多经验才能让你变身
	public int score;					//希望你的经历能够保留
	public int life;					//残酷的大自然其实不该有生命值的存在
	public boolean invincible;			//无敌光环?
	//constructor
	//输入为创建的鱼在屏幕上游动的范围
	public PlayerFish(int x,int y)
	{		
		height = 50;					//这是你的基础能力值，其实每条咸鱼都一样
		width  = 80;
		level  = 1.2;					//我多好，起码你不是新手村出来的经验宝宝
		
		boundary_x = x;
		boundary_y = y;
		aim_x = boundary_x/2;			//给你给初始小目标
		aim_y = boundary_y/2;
		location_x = boundary_x/2;		//给你给出生地点
		location_y = boundary_y/2;
		
		exp = 0;						//脚踏实地，从0开始
		lvlupneed = 80;					//其实升级要的也不多
		score = 0;						//新的人生，新的轨迹
		life = 3;						//1才是正在的人生，为什么我的人生不能让这个数大于1呢？
		invincible = false;				//怎么能给你无敌，这是给我通不了关准备的
		direction = RIGHT;				//咸鱼头先看看右边吧
	}
	
	public void MissionInit()
	{	
		//咸鱼准备开始闯关了
		level  = 1.2;					//新的关卡从头开始吧
		
		aim_x = boundary_x/2;			
		aim_y = boundary_y/2;
		location_x = boundary_x/2;
		location_y = boundary_y/2;
		
		exp = 0;

		direction = RIGHT;
	}
	
	public void SetLvlneed(int lvlupexp)
	{
		lvlupneed = lvlupexp;				//设置你成长要的经验值
	}

	//**设置游动范围**//
	public void SetBoundary(int x, int y)
	{
		boundary_x = x;
		boundary_y = y;
	}
	
	public void SetVelocity()				//咸鱼快跑，目标还很远呢
	{
		v_x = (aim_x-location_x)*0.02;		//根据目标位置和当前位置的距离来设定咸鱼奔跑的速度
		v_y = (aim_y-location_y)*0.02;
		if(v_x>0)							
			direction = RIGHT;				//往右跑，头朝右
		else if(v_x<0)
			direction = LEFT;				//往左跑，头朝左
	}
	
	public void UpdateLocation(int ms)		//ms虽然开始准备代表定时器的定时周期，其实改变这个值，可以改变每次鱼移动的距离，也就是说，这个越大，体现的是鱼的速度越快
	{
		//我们家的咸鱼终于要动了
		location_x += v_x*ms;		//下个位置=当前位置+速度*时间
		location_y += v_y*ms;
		
		if(location_x < 0)
			location_x = 0;
		if(location_x > boundary_x)
			location_x = boundary_x;
		if(location_y < 0)
			location_y = 0;
		if(location_y > boundary_y)
			location_y = boundary_y;
	}
	
	//**我们的咸鱼想吃饭了，我们先看看食物在哪**//
	public boolean CanEat(AIFish fish)
	{
		if(Math.abs(location_x-fish.location_x)<fish.level*fish.width-20&&Math.abs(location_y-fish.location_y)<fish.level*fish.height-10)
			return true;			//好的，食物在旁边
		else
			return false;			//食物都没找到，还吃饭，快去找！！！
	}
	
	//**咸鱼想吃这条鱼，我看看让不让他吃**//
	public boolean EatFish(AIFish fish)
	{
		if(level>fish.level)		//等级够高吗？
		{
			//**吃吧吃吧，别噎着了**//
			exp += fish.level*10;
			score += fish.level*100;
			if(exp>lvlupneed)		//升级了？
			{
				exp -= lvlupneed;
				level += 0.5;		//别小看只有0.5
			}
			return true;
		}
		else
			return false;
	}
	
	public void Hurt()
	{
		life--;					//你受伤了，生命值减小了
		invincible = true;		//给你个无敌缓缓
	}
	
	public boolean Alive()
	{
		return life>0;			//你还有生命值，继续你的咸鱼生活
	}
	
	public void NoBuffNow()
	{
		invincible = false;		//够了，无敌时间到了
	}
}
