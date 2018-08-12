/****************************************************/
/**
 * 
 * @author Administrator
 *
 *����������ɳ���¼���һὫ������;������¼�����������������С���㻹�ǳɳ�Ϊ
 *��������㣡
 *
 */
public class PlayerFish
{
	//properties
	public int height;					//fish height
	public int width;					//fish width
	public double level;				//��Ϸ����Ҫ�еȼ���
	
	public int aim_x;					//��ֻ������ȥ�ǣ�������������
	public int aim_y;					//�Ͼ�ֻ��7s�ļ���
	
	public int location_x;				//Ȼ����ֻ���㻹����
	public int location_y;
	
	private double v_x;					//������Ҫ�ܶ��
	private double v_y;
	
	public boolean direction;			//Ŷ����������¼���򣬶�����·

	public final boolean LEFT = false;	//��Ȼд����ô������������û��ʲô��
	public final boolean RIGHT = true;
	
	public int boundary_x;				//��������ֻ�����ܳ��ҵ���Ұ
	public int boundary_y;
	
	public int exp;						//��ȡ�����������
	public int lvlupneed;				//���·������������ô�ྭ������������
	public int score;					//ϣ����ľ����ܹ�����
	public int life;					//�п�Ĵ���Ȼ��ʵ����������ֵ�Ĵ���
	public boolean invincible;			//�޵й⻷?
	//constructor
	//����Ϊ������������Ļ���ζ��ķ�Χ
	public PlayerFish(int x,int y)
	{		
		height = 50;					//������Ļ�������ֵ����ʵÿ�����㶼һ��
		width  = 80;
		level  = 1.2;					//�Ҷ�ã������㲻�����ִ�����ľ��鱦��
		
		boundary_x = x;
		boundary_y = y;
		aim_x = boundary_x/2;			//�������ʼСĿ��
		aim_y = boundary_y/2;
		location_x = boundary_x/2;		//����������ص�
		location_y = boundary_y/2;
		
		exp = 0;						//��̤ʵ�أ���0��ʼ
		lvlupneed = 80;					//��ʵ����Ҫ��Ҳ����
		score = 0;						//�µ��������µĹ켣
		life = 3;						//1�������ڵ�������Ϊʲô�ҵ��������������������1�أ�
		invincible = false;				//��ô�ܸ����޵У����Ǹ���ͨ���˹�׼����
		direction = RIGHT;				//����ͷ�ȿ����ұ߰�
	}
	
	public void MissionInit()
	{	
		//����׼����ʼ������
		level  = 1.2;					//�µĹؿ���ͷ��ʼ��
		
		aim_x = boundary_x/2;			
		aim_y = boundary_y/2;
		location_x = boundary_x/2;
		location_y = boundary_y/2;
		
		exp = 0;

		direction = RIGHT;
	}
	
	public void SetLvlneed(int lvlupexp)
	{
		lvlupneed = lvlupexp;				//������ɳ�Ҫ�ľ���ֵ
	}

	//**�����ζ���Χ**//
	public void SetBoundary(int x, int y)
	{
		boundary_x = x;
		boundary_y = y;
	}
	
	public void SetVelocity()				//������ܣ�Ŀ�껹��Զ��
	{
		v_x = (aim_x-location_x)*0.02;		//����Ŀ��λ�ú͵�ǰλ�õľ������趨���㱼�ܵ��ٶ�
		v_y = (aim_y-location_y)*0.02;
		if(v_x>0)							
			direction = RIGHT;				//�����ܣ�ͷ����
		else if(v_x<0)
			direction = LEFT;				//�����ܣ�ͷ����
	}
	
	public void UpdateLocation(int ms)		//ms��Ȼ��ʼ׼������ʱ���Ķ�ʱ���ڣ���ʵ�ı����ֵ�����Ըı�ÿ�����ƶ��ľ��룬Ҳ����˵�����Խ�����ֵ�������ٶ�Խ��
	{
		//���Ǽҵ���������Ҫ����
		location_x += v_x*ms;		//�¸�λ��=��ǰλ��+�ٶ�*ʱ��
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
	
	//**���ǵ�������Է��ˣ������ȿ���ʳ������**//
	public boolean CanEat(AIFish fish)
	{
		if(Math.abs(location_x-fish.location_x)<fish.level*fish.width-20&&Math.abs(location_y-fish.location_y)<fish.level*fish.height-10)
			return true;			//�õģ�ʳ�����Ա�
		else
			return false;			//ʳ�ﶼû�ҵ������Է�����ȥ�ң�����
	}
	
	//**������������㣬�ҿ����ò�������**//
	public boolean EatFish(AIFish fish)
	{
		if(level>fish.level)		//�ȼ�������
		{
			//**�԰ɳ԰ɣ���ҭ����**//
			exp += fish.level*10;
			score += fish.level*100;
			if(exp>lvlupneed)		//�����ˣ�
			{
				exp -= lvlupneed;
				level += 0.5;		//��С��ֻ��0.5
			}
			return true;
		}
		else
			return false;
	}
	
	public void Hurt()
	{
		life--;					//�������ˣ�����ֵ��С��
		invincible = true;		//������޵л���
	}
	
	public boolean Alive()
	{
		return life>0;			//�㻹������ֵ�����������������
	}
	
	public void NoBuffNow()
	{
		invincible = false;		//���ˣ��޵�ʱ�䵽��
	}
}
