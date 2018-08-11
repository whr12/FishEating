/******************************************************************************************/
/**
 * 
 * File Name: FishEating
 * 
 * ����������
 * �������ʵ����һ�������С���С��Ϸ��
 * ���׽��棬��������ť���ֱ�Ϊ��ʼ��Ϸ����Ϸ��߷�
 * ����Ϸ�У���ҿ��ԳԵ����Լ�С���㣬��Ҳ���ܵ����Լ������Ĺ�������3�α������Ļ���
 * ���������ж��ݵ�ʱ���޵�
 * ��MISSION3��ʼ��AI���Զ��ܿ����߽ӽ����
 * MISSION5�У����о޴��boss�ᴩ��Ļ�������е�����ɻ����ԵĴ��
 * 
 * Ϊ�����Ѷȣ������û�гɳ�֮ǰ����MISSION4�У�AI�����Զ���ܻ�׷�����
 * 
 * �����㷨����λ�õĸ��²�����ͨ����������𣿵�λʱ��ĵ�λ�Ƽ����ϴε�λ�õõ���λ��
 * 
 * �������ͱ��ǵ�ͼƬû�ҵ����Ͼ�Ҫ���ǿ��ϣ���һ��ͼƬͦ���ѵģ���Ǹ�����Ĺ�û������
 * 
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.*;

public class FishEating extends JComponent implements ActionListener, MouseMotionListener
{	
	/**properties**/
	PlayerFish player;							//���
	AIFish[] littlefish;						//С��
	AIFish[] middlefish;						//����
	AIFish[] bigfish;							//����
	BossFish boss;								//�����㡪���ռ�boss��ǿ���޵�
	
	Image player_imgL;							//��Ҳ���������ͼƬ��������
	Image player_imgR;							//��Ҳ���������ͼƬ��������		
	Image littlefish_imgL;						//С��ͼƬ��������
	Image littlefish_imgR;						//С��ͼƬ��������
	Image middlefish_imgL;						//����ͼƬ��������
	Image middlefish_imgR;						//����ͼƬ��������
	Image bigfish_imgL;							//����ͼƬ��������
	Image bigfish_imgR;							//����ͼƬ��������
	Image boss_imgL;							//������ͼƬ��������
	Image boss_imgR;							//������ͼƬ��������
	Image background_img;						//���󱳾�ͼƬ

	public int boundary_x;						//���淶Χx
	public int boundary_y;						//���淶Χy
	
	public boolean avoid_player;				//�Ƿ�����ұ�־
	public int fishspeed;						//AI�ζ��ٶ�
	
	JButton start;								//��ť�ؼ�
	JButton rule;								//��ť�ؼ�
	
	private Timer timer;						//��ʱ��
	int cnt;									//��������
	
	int flag;									//��Ϸ״̬��־
	int mission;								//��Ϸ���йؿ���־
	private final static int MENU = 0;			
	private final static int INTRODUCTION = 1;
	private final static int MISSION1 = 2;
	private final static int MISSION2 = 3;
	private final static int MISSION3 = 4;
	private final static int MISSION4 = 5;
	private final static int MISSION5 = 6;
	private final static int WIN      = 7;
	private final static int MISSION_INTERFACE = 8;
	private final static int DEAD = 9;
	private final static int HIGHSCORE = 10;
	
	public int highscore;						//��߷�
	
	/**constructor**/
	public FishEating()
	{
		/***��ȡͼƬ��Ϣ***/
		ImageIcon icon = new ImageIcon("playerL.png");
		player_imgL = icon.getImage();
		icon = new ImageIcon("littlefishL.png");
		littlefish_imgL = icon.getImage();
		icon = new ImageIcon("middlefishL.png");
		middlefish_imgL = icon.getImage();
		icon = new ImageIcon("bigfishL.png");
		bigfish_imgL = icon.getImage();
		icon = new ImageIcon("bossL.png");
		boss_imgL = icon.getImage();
		
		icon = new ImageIcon("playerR.png");
		player_imgR = icon.getImage();
		icon = new ImageIcon("littlefishR.png");
		littlefish_imgR = icon.getImage();
		icon = new ImageIcon("middlefishR.png");
		middlefish_imgR = icon.getImage();
		icon = new ImageIcon("bigfishR.png");
		bigfish_imgR = icon.getImage();
		icon = new ImageIcon("bossR.png");
		boss_imgR = icon.getImage();
				
		icon = new ImageIcon("sea.png");
		background_img = icon.getImage();
		
		setBounds(0, 0, 1000, 600);			//�趨�߽�
		
		timer = new Timer(50,this);			//ʵ����ʱ������ʱ����50ms
		timer.start();						//��ʼ��ʱ
		timer.setActionCommand("Time");		//���ö�ʱ����Ӧ��ʶ��
		
//		boundary_x = getWidth();			//��ý�����
//		boundary_y = getHeight();			//��ý���߶�
		
		highscore = 0;						//��߷�����
		
		Init();								//��Ϸ��ʼ��
		
	}
	
	//**methods**//
	/**��Ϸ�����ʼ��������������Ϸ����ʼ�ĳ�ʼ����**/
	private void Init()
	{
		flag = MENU;						//����MENU����
		timer.stop();						//��ʱֹͣ
		
		start = new JButton("Strat");		//ʵ����ť
		rule = new JButton("Rule");			//ʵ����ť
		
		
	    start.addActionListener(this);		//��Ӱ�ť��Ӧ�¼�
	    start.setActionCommand("start");	//��Ӱ�ť��Ӧ��ʶ��
	    rule.addActionListener(this);
	    rule.setActionCommand("rule"); 

	}
	/**��Ϸ��ʼǰ�ĳ�ʼ�������ڿ�ʼ��Ϸʱ������ĳ�ʼ��**/
	private void GameInit()
	{
		flag = MISSION_INTERFACE;
		mission = MISSION1;
		player = new PlayerFish(getWidth(),getHeight());
		
		boss = null;
		timer.start();
		addMouseMotionListener(this);
		
	}
	/**�ؿ���ʼ��������ÿ�ؿ�ʼǰAI���ݺ���Ҿ���ֵ�ĳ�ʼ��**/
	private void MissionInit()
	{
		player.MissionInit();
		switch (mission)
		{
		case MISSION1:
		{
			avoid_player = false;						//�趨�ùؿ�AI�Ƿ��ܻ�׷�����
			littlefish = new AIFish[15];				//����С������
			for(int i=0; i<littlefish.length; i++)		
				littlefish[i] = new AIFish(1,getWidth(),getHeight());			//ʵ����С��
			middlefish = new AIFish[0];					//������������
			for(int i=0; i<middlefish.length; i++)		
				middlefish[i] = new AIFish(1.5,getWidth(),getHeight());		//ʵ��������
			bigfish = new AIFish[0];					//���ô�������
			for(int i=0; i<bigfish.length; i++)			
				bigfish[i] = new AIFish(2,getWidth(),getHeight());				//ʵ��������
			player.SetLvlneed(80);						//��������ڱ����������辭��ֵ
			fishspeed = 10;								//����AI�ٶ�
			break;
		}
		case MISSION2:
		{
			avoid_player = false;
			littlefish = new AIFish[12];
			for(int i=0; i<littlefish.length; i++)
				littlefish[i] = new AIFish(1,getWidth(),getHeight());
			middlefish = new AIFish[3];
			for(int i=0; i<middlefish.length; i++)
				middlefish[i] = new AIFish(1.5,getWidth(),getHeight());
			bigfish = new AIFish[0];
			for(int i=0; i<bigfish.length; i++)
				bigfish[i] = new AIFish(2,getWidth(),getHeight());
			player.SetLvlneed(80);
			fishspeed = 10;
			break;
		}
		case MISSION3:
		{
			avoid_player = true;
			littlefish = new AIFish[10];
			for(int i=0; i<littlefish.length; i++)
				littlefish[i] = new AIFish(1,getWidth(),getHeight());
			middlefish = new AIFish[3];
			for(int i=0; i<middlefish.length; i++)
				middlefish[i] = new AIFish(1.5,getWidth(),getHeight());
			bigfish = new AIFish[1];
			for(int i=0; i<bigfish.length; i++)
				bigfish[i] = new AIFish(2,getWidth(),getHeight());
			player.SetLvlneed(80);
			fishspeed = 10;
			break;
		}
		case MISSION4:
		{
			avoid_player = true;
			littlefish = new AIFish[8];
			for(int i=0; i<littlefish.length; i++)
				littlefish[i] = new AIFish(1,getWidth(),getHeight());
			middlefish = new AIFish[3];
			for(int i=0; i<middlefish.length; i++)
				middlefish[i] = new AIFish(1.5,getWidth(),getHeight());
			bigfish = new AIFish[2];
			for(int i=0; i<bigfish.length; i++)
				bigfish[i] = new AIFish(2,getWidth(),getHeight());
			player.SetLvlneed(80);
			fishspeed = 15;
			break;
		}
		case MISSION5:
		{
			avoid_player = true;
			littlefish = new AIFish[6];
			for(int i=0; i<littlefish.length; i++)
				littlefish[i] = new AIFish(1,getWidth(),getHeight());
			middlefish = new AIFish[3];
			for(int i=0; i<middlefish.length; i++)
				middlefish[i] = new AIFish(1.5,getWidth(),getHeight());
			bigfish = new AIFish[2];
			for(int i=0; i<bigfish.length; i++)
				bigfish[i] = new AIFish(2,getWidth(),getHeight());
			player.SetLvlneed(100);
			fishspeed = 10;
			boss = new BossFish(getWidth(),getHeight());							//boss is coming
			break;
		}
		default:
		{
			flag = MENU;
			mission = MISSION1;
		}
		}
	}
	
	public void paint(Graphics g)
	{
		//paint background
		super.paint(g);
		g.drawImage(background_img, 0, 0, getWidth(), getHeight(), this);
		switch(flag)						//����flag�ı�ʶ��ȷ��Ҫ��ʾ��ͼ��
		{
		case MENU:
		{
			//Game start interface
			/**���ñ���**/
			g.setColor(Color.RED);
			g.setFont(new Font("Serif",Font.ITALIC,52));
			g.drawString("FishEating Game", getWidth()/2-180, getHeight()/4);
			/**��Ӱ�ť**/
			add(start);
			add(rule);
			start.setText("Start");
			rule.setText("Highscore");
			start.setLocation (getWidth()/2-150, getHeight()*3/4-100);
		    start.setSize (100, 50) ;
		    rule.setLocation (getWidth()/2+50, getHeight()*3/4-100);
		    rule.setSize (100, 50) ;
			
		    break;
		}
		case INTRODUCTION:
		{
			//The rule of game
			//No design by now
		}
		case DEAD:
		{
			//��ӡ����֪ͨ
			g.setColor(Color.RED);
			g.setFont(new Font("Serif",Font.ITALIC,52));
			g.drawString("You become food!", getWidth()/2-180, getHeight()/2-100);
			//��ӡ�����ɾ�
			g.setColor(Color.RED);
			g.setFont(new Font("Serif", Font.BOLD, 28));
			g.drawString("Score: "+String.valueOf(player.score), getWidth()/2-100, getHeight()/2);
			g.drawString("HighScore: "+String.valueOf(highscore), getWidth()/2-100, getHeight()/2+50);
			//����ֻذ�ť��������ͷ����
			add(start);
			start.setText("restart");
			start.setLocation (getWidth()/2-150, getHeight()*3/4);
		    start.setSize (100, 50) ;
		    //��ӷ��ذ�ť������ܿ������
		    add(rule);
			rule.setText("Back");		    
		    rule.setLocation (getWidth()/2+50, getHeight()*3/4);
		    rule.setSize (100, 50) ;
			break;
		}
		case MISSION_INTERFACE:
		{
			//�ؿ���ͷ��ʾ����ӡ�����Ϣ
			g.setColor(Color.RED);
			g.setFont(new Font("Serif",Font.ITALIC,52));
			g.drawString("Mission "+(mission-1), getWidth()/2-100, getHeight()/2);
			break;
		}
		case WIN:
		{
			//ʤ����ʶ�����㻹�Ǹ�����
			g.setColor(Color.RED);
			g.setFont(new Font("Serif",Font.ITALIC,52));
			g.drawString("Congradulation!", getWidth()/2-180, getHeight()/2-100);
			g.setColor(Color.RED);
			g.setFont(new Font("Serif", Font.BOLD, 28));
			g.drawString("Score: "+String.valueOf(player.score), getWidth()/2-100, getHeight()/2);
			g.drawString("HighScore: "+String.valueOf(highscore), getWidth()/2-100, getHeight()/2+50);
			//��ӷ��ذ�ť����������һ��
			add(rule);
			rule.setText("Back");
			rule.setLocation (getWidth()/2-50, getHeight()*3/4);
		    rule.setSize (100, 50) ;
			break;
		}
		case HIGHSCORE:
		{
			//��ʾ��߷�
			g.setColor(Color.RED);
			g.setFont(new Font("Serif", Font.BOLD, 52));
			g.drawString("HighScore", getWidth()/2-100, getHeight()/2-50);
			g.setFont(new Font("Serif", Font.BOLD, 26));
			g.drawString(String.valueOf(highscore), getWidth()/2, getHeight()/2+50);
			start.setLocation (0, 0);
		    start.setSize (0, 0) ;
			add(rule);
			rule.setText("Back");
			rule.setLocation (getWidth()/2-50, getHeight()*3/4);
		    rule.setSize (100, 50) ;
			break;
		}
		default:
		{
			/**Game interface**/
			//ע�⣬��Ϸ������ʾ��������
			/**��ʾС��**/
			for(int i=0;i<littlefish.length;i++)
			{
				if(littlefish[i].direction)		//�ж���ͷ����
					g.drawImage(littlefish_imgR, littlefish[i].location_x, littlefish[i].location_y, (int)(littlefish[i].width*littlefish[i].level), (int)(littlefish[i].height*littlefish[i].level), this);
				else
					g.drawImage(littlefish_imgL, littlefish[i].location_x, littlefish[i].location_y, (int)(littlefish[i].width*littlefish[i].level), (int)(littlefish[i].height*littlefish[i].level), this);
			}
			//**��ʾ����**//
			for(int i=0;i<middlefish.length;i++)
			{
				if(middlefish[i].direction)
					g.drawImage(middlefish_imgR, middlefish[i].location_x, middlefish[i].location_y, (int)(middlefish[i].width*middlefish[i].level), (int)(middlefish[i].height*middlefish[i].level), this);
				else
					g.drawImage(middlefish_imgL, middlefish[i].location_x, middlefish[i].location_y, (int)(middlefish[i].width*middlefish[i].level), (int)(middlefish[i].height*middlefish[i].level), this);
			}
			//**��ʾ����**//
			for(int i=0;i<bigfish.length;i++)
			{
				if(bigfish[i].direction)
					g.drawImage(bigfish_imgR, bigfish[i].location_x, bigfish[i].location_y, (int)(bigfish[i].width*bigfish[i].level), (int)(bigfish[i].height*bigfish[i].level), this);
				else
					g.drawImage(bigfish_imgL, bigfish[i].location_x, bigfish[i].location_y, (int)(bigfish[i].width*bigfish[i].level), (int)(bigfish[i].height*bigfish[i].level), this);
			}
			//draw player's fish
			if(!player.invincible||cnt%2==0)		//����и�ǿ���buff����ͻ᲻�ϵ���˸
			{
				if(player.direction)
					g.drawImage(player_imgR, player.location_x, player.location_y, (int)(player.width*player.level), (int)(player.height*player.level), this);
				else
					g.drawImage(player_imgL, player.location_x, player.location_y, (int)(player.width*player.level), (int)(player.height*player.level), this);
			}
			//boss������
			if(boss!=null)
			{
				//����Ԥ��
				g.setColor(Color.RED);
				g.setFont(new Font("Serif", Font.BOLD, 60));
				g.drawString(boss.text, boss.text_x, boss.text_y);
				//boss is coming
				if(boss.direction)
					g.drawImage(boss_imgR, boss.location_x, boss.location_y, (int)(boss.width*boss.level), (int)(boss.height*boss.level), this);
				else
					g.drawImage(boss_imgL, boss.location_x, boss.location_y, (int)(boss.width*boss.level), (int)(boss.height*boss.level), this);
			}
			//��ʾ��ǰ����
			String str = "score:" + String.valueOf(player.score);
			g.setColor(Color.RED);
			g.setFont(new Font("Serif",Font.ITALIC,30));
			//��ʾ��ǰ����ֵ
			g.drawString(str, 20, 50);
			str = "life: "+String.valueOf(player.life);
			g.drawString(str, getWidth()-100, 50);
			
			break;
		}
		}
	}

	//��������λ���е�С��ְ�
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("FishEating Game");
		frame.setSize(1000, 600);
		frame.add(new FishEating());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// ��������ƶ�Ŀ��
		player.aim_x = e.getX()-(int)(player.width*player.level/2);
		player.aim_y = e.getY()-(int)(player.height*player.level/2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand()=="start")		//�жϰ��µİ�ť
		{	
			flag = MISSION_INTERFACE;			//��ʼ��Ϸ����־ת�Ƶ��ؿ���ӭλ��
			mission = MISSION1;					//��һ�أ�����
			//��ť�Ȳ�Ҫ�ˣ���С����С����
			start.setLocation (0, 0);			
		    start.setSize (0, 0) ;
		    rule.setLocation (0, 0);
		    rule.setSize (0, 0) ;
			cnt = 0;							//�������㣬׼����ʱ
			GameInit();							//��Ϸ��ʼ��
		}
		if(e.getActionCommand()=="rule")
		{
			if(flag == MENU)					//�жϰ��µİ�ť
				flag = HIGHSCORE;				//������ʾ��߷ֵ�״̬
			else	
				flag = MENU;					//������ҳ
			timer.stop();						//ֹͣ��ʱ
			repaint();							//�ػ�����
		}
		if(e.getActionCommand()=="Time")		//�жϼ�ʱ���ж�
		{
			if(flag==mission)					//�ж��Ƿ��ڹؿ���
			{
				player.SetBoundary(getWidth(), getHeight());
				player.SetVelocity();			//������ҵ������ٶ�
				player.UpdateLocation(20);		//�������λ��
				//**����С������**//
				for(int i=0;i<littlefish.length;i++)
				{
					littlefish[i].SetBoundary(getWidth(), getHeight());
					littlefish[i].SetVelocity(player,avoid_player); //����С���ƶ��ٶ�
					littlefish[i].UpdateLocation(fishspeed);		//����С��λ��
					if(player.CanEat(littlefish[i]))				//�ж�����ܷ��С��
						if(player.EatFish(littlefish[i]))			//�ж�����ܲ��ܳ�С��
							littlefish[i].Location_Init();			//С�㱻���̵�ɱ���ˣ���а������ְ���������
					if(boss!=null)									//boss���ˣ�
						if(littlefish[i].CanEat(boss))				//С���ܲ��ܳ�boss
							littlefish[i].Location_Init();			//��Ȼ������������֪����Ϊʲô���ǰ��Ǳ�����С�㸴����
				}
				//**������������**//
				for(int i=0;i<middlefish.length;i++)
				{
					//���뽲�����ˣ��Լ���ɣ��޷���һ��ɵ���ܰ��ظ�����ʹ��
					middlefish[i].SetBoundary(getWidth(), getHeight());
					middlefish[i].SetVelocity(player,avoid_player);	
					middlefish[i].UpdateLocation(fishspeed);
					if(player.CanEat(middlefish[i]))
						if(player.EatFish(middlefish[i]))
							middlefish[i].Location_Init();
						else
						{
							if(!player.invincible)			//�޵У���������˼��
							{
								cnt = 0;
								player.Hurt();				//��Ӯ���ϣ����ǲ���ɵ����׷�㣿�㲻���ܣ�
								if(!player.Alive())
								{
									flag = DEAD;			//�����糬����������ҵ�������ʲô��������
									highscore = player.score>highscore ? player.score:highscore;		//�����������������µ�ʲô�ɣ���Ȼ���Ǹ����㡣Ŷ����̫���ˣ��Ҷ������¼��
									timer.stop();			//���ˣ�ʱ��Ҳ��ֹ�ˣ�����
								}
							}
						}
					if(boss!=null)							//boss���˾��ܰ�
						if(middlefish[i].CanEat(boss))
							middlefish[i].Location_Init();
					middlefish[i].EatFish(littlefish);		//��Ȼ����ʳ�����ϸ�һ���������в������������
				}
				//**���´�����Ϣ**//
				for(int i=0;i<bigfish.length;i++)
				{
					//�������Ա��������ͷ������д��
					bigfish[i].SetBoundary(getWidth(), getHeight());
					bigfish[i].SetVelocity(player,avoid_player);
					bigfish[i].UpdateLocation(fishspeed);
					if(player.CanEat(bigfish[i]))
						if(player.EatFish(bigfish[i]))
							bigfish[i].Location_Init();
						else
						{
							if(!player.invincible)
							{
								cnt = 0;
								player.Hurt();
								if(!player.Alive())
								{
									flag = DEAD;
									highscore = player.score>highscore ? player.score:highscore;
									timer.stop();
								}
							}
						}
					if(boss!=null)
					{
						boss.SetBoundary(getWidth(), getHeight());
						if(bigfish[i].CanEat(boss))
							bigfish[i].Location_Init();
					}
					//�ף����ܳ��������У�����һ����������˰ɣ��Ҳ��Ḵ������
					bigfish[i].EatFish(littlefish);
					bigfish[i].EatFish(middlefish);
				}
				//���boss�ڵĻ���������
				if(boss!=null)
				{
					boss.UpdateLocation(fishspeed);	
					if(player.CanEat(boss))			//�����������boss��
					{
						if(!player.invincible)		//�����ˣ��һ�����޵й⻷���㲻������
						{
							cnt = 0;				
							player.Hurt();			//��ʵ���������㱻��ɱ�ģ���̫�ʴ���
							if(!player.Alive())	
							{
								flag = DEAD;		//��ʵboss�������ǣ���������Ҫ�޵й⻷
								highscore = player.score>highscore ? player.score:highscore;
								timer.stop();		
							}
						}
					}
				}
				if(player.level>2.5)				//���������ȥ��һ��������ˣ�ˣ�����û��ô��ͼƬ��������
				{
					flag = MISSION_INTERFACE;		//�����ؿ���ӭ���滺����
					mission = mission+1;			//�ؿ��ݽ�
					cnt = 0;
					if(mission == WIN)				//�ף����㷭����
					{
						flag = WIN;					//���ò�˵����ɹ������������ս��ȥ������ʲô�ڵȴ���ɡ���ʵû����
						highscore = player.score>highscore ? player.score:highscore;		//��¼�����ȴ���һ����������Խ�ɡ��ף�û���£��㻹���������е�����
						timer.stop();				//ɢ��ɢ��
					}
				}				
			}
			
			if(flag==MISSION_INTERFACE)
			{
				//��ʹ20*50=1000ms
				cnt++;
				if(cnt>20)
				{
					cnt = 0;
					flag = mission;  //��ʼ����֮�ð�
					MissionInit();
				}
			}
			if(player.invincible)
			{
				//���㱻Ű����ô�ң������޵�ʱ���
				cnt++;
				if(cnt>40)			//40*50=2000ms
				{
					player.NoBuffNow();
					cnt = 0;
				}
			}
			
			if(mission>MISSION2)
			{
				if(player.level>1.5)			//���ǵ�����ɳ����𣿳ɳ��˾͸ý�����������ս��
					avoid_player = true;
			}
			
			repaint(); 	//�ػ�
		}
	}
}
