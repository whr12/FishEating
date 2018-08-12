/********************************************************/
/**
 * 
 * @author Administrator
 *
 *����洢�����������㶷��������
 *��ʵ���Ƕ�������ͬ�����������ԣ���Ҫû��ǿ���޵�buff
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
	//����Ϊ������ĵȼ�������Ļ���ζ��ķ�Χ
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
	
	//**�����ζ���Χ**//
	public void SetBoundary(int x, int y)
	{
		boundary_x = x;
		boundary_y = y;
	}
	
	//����AI���ƶ��ٶȺͷ���
	public void SetVelocity(PlayerFish player, boolean avoid)
	{
		if(avoid)		
		{
			//��������˶����ң��ͻ���ж�����ҵľ���
			if(player.level>level&&Math.abs(location_x-player.location_x)<level*width*1.5&&Math.abs(location_y-player.location_y)<level*height*1.5)
			{
				//�������Ҿ��빻���ҵȼ����ͣ��ͻ���Զ����ҵķ����ƶ���������������
				direction = (location_x-player.location_x)>0;
				direction_y = (location_y-player.location_y)>0;
				v_x = Math.random();
				v_y = Math.random();
			}
			else if(player.level<level&&Math.abs(location_x-player.location_x)<level*width*1.5&&Math.abs(location_y-player.location_y)<level*height*1.5)
			{
				//�������Ҿ��빻���ҵȼ����ͣ��ͻ�������ƶ����ɵ���������
				direction = (location_x-player.location_x)<0;
				direction_y = (location_y-player.location_y)<0;
				v_x = Math.random();
				v_y = Math.random();
			}
			else
			{
				///�����㲻�ڣ���������
				v_x = Math.random();
				v_y = Math.random();
				direction = Math.random()>0.99 ? !direction : direction;			//���˷���Ͳ���������
				direction_y = Math.random()>0.99 ? !direction_y : direction_y;
			}
		}
		else
		{
			//û�����ñ�����ҡ��Ͳ��������㣬��������
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
	//**ǰ������Ⱥ,׼������**//
	public void EatFish(AIFish[] fish)
	{
		for(int i=0; i<fish.length;i++)
		{
			if(CanEat(fish[i]))			//ʳ�ﵽ�������
			{
				if(EatFish(fish[i]))	//�̵�����
					fish[i].Location_Init();	//Ŀ��������
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
