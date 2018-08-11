/******************************************************************************************/
/**
 * 
 * File Name: FishEating
 * 
 * 功能描述：
 * 这个程序实现了一个大鱼吃小鱼的小游戏。
 * 在首界面，有两个按钮，分别为开始游戏和游戏最高分
 * 在游戏中，玩家可以吃掉比自己小的鱼，但也会受到比自己大的鱼的攻击，共3次被攻击的机会
 * 被攻击后，有短暂的时间无敌
 * 从MISSION3开始，AI会自动避开或者接近玩家
 * MISSION5中，会有巨大的boss横穿屏幕，对所有的鱼造成毁灭性的打击
 * 
 * 为降低难度，在玩家没有成长之前，在MISSION4中，AI不会自动规避或追赶玩家
 * 
 * 迭代算法：鱼位置的更新不就是通过迭代获得吗？单位时间的的位移加上上次的位置得到新位置
 * 
 * 最后珍珠和贝壳的图片没找到，毕竟要贝壳开合，找一组图片挺困难的，抱歉，第四关没珍珠了
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
	PlayerFish player;							//玩家
	AIFish[] littlefish;						//小鱼
	AIFish[] middlefish;						//中鱼
	AIFish[] bigfish;							//大鱼
	BossFish boss;								//大鲨鱼——终极boss，强、无敌
	
	Image player_imgL;							//玩家操作的咸鱼图片，朝向左
	Image player_imgR;							//玩家操作的咸鱼图片，朝向右		
	Image littlefish_imgL;						//小鱼图片，朝向左
	Image littlefish_imgR;						//小鱼图片，朝向右
	Image middlefish_imgL;						//中鱼图片，朝向左
	Image middlefish_imgR;						//中鱼图片，朝向右
	Image bigfish_imgL;							//大鱼图片，朝向左
	Image bigfish_imgR;							//大鱼图片，朝向右
	Image boss_imgL;							//大鲨鱼图片，朝向左
	Image boss_imgR;							//大鲨鱼图片，朝向右
	Image background_img;						//海洋背景图片

	public int boundary_x;						//界面范围x
	public int boundary_y;						//界面范围y
	
	public boolean avoid_player;				//是否躲避玩家标志
	public int fishspeed;						//AI游动速度
	
	JButton start;								//按钮控件
	JButton rule;								//按钮控件
	
	private Timer timer;						//定时器
	int cnt;									//计数缓存
	
	int flag;									//游戏状态标志
	int mission;								//游戏进行关卡标志
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
	
	public int highscore;						//最高分
	
	/**constructor**/
	public FishEating()
	{
		/***读取图片信息***/
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
		
		setBounds(0, 0, 1000, 600);			//设定边界
		
		timer = new Timer(50,this);			//实例定时器，定时周期50ms
		timer.start();						//开始定时
		timer.setActionCommand("Time");		//设置定时器相应标识符
		
//		boundary_x = getWidth();			//获得界面宽度
//		boundary_y = getHeight();			//获得界面高度
		
		highscore = 0;						//最高分置零
		
		Init();								//游戏初始化
		
	}
	
	//**methods**//
	/**游戏程序初始化，用于整个游戏程序开始的初始操作**/
	private void Init()
	{
		flag = MENU;						//进入MENU界面
		timer.stop();						//计时停止
		
		start = new JButton("Strat");		//实例按钮
		rule = new JButton("Rule");			//实例按钮
		
		
	    start.addActionListener(this);		//添加按钮响应事件
	    start.setActionCommand("start");	//添加按钮响应标识符
	    rule.addActionListener(this);
	    rule.setActionCommand("rule"); 

	}
	/**游戏开始前的初始化，用于开始游戏时相关量的初始化**/
	private void GameInit()
	{
		flag = MISSION_INTERFACE;
		mission = MISSION1;
		player = new PlayerFish(getWidth(),getHeight());
		
		boss = null;
		timer.start();
		addMouseMotionListener(this);
		
	}
	/**关卡初始化，用于每关开始前AI数据和玩家经验值的初始化**/
	private void MissionInit()
	{
		player.MissionInit();
		switch (mission)
		{
		case MISSION1:
		{
			avoid_player = false;						//设定该关卡AI是否躲避或追赶玩家
			littlefish = new AIFish[15];				//设置小鱼数量
			for(int i=0; i<littlefish.length; i++)		
				littlefish[i] = new AIFish(1,getWidth(),getHeight());			//实例化小鱼
			middlefish = new AIFish[0];					//设置中鱼数量
			for(int i=0; i<middlefish.length; i++)		
				middlefish[i] = new AIFish(1.5,getWidth(),getHeight());		//实例化中鱼
			bigfish = new AIFish[0];					//设置大鱼数量
			for(int i=0; i<bigfish.length; i++)			
				bigfish[i] = new AIFish(2,getWidth(),getHeight());				//实例化大鱼
			player.SetLvlneed(80);						//设置玩家在本关升级所需经验值
			fishspeed = 10;								//设置AI速度
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
		switch(flag)						//根据flag的标识，确定要显示的图像
		{
		case MENU:
		{
			//Game start interface
			/**设置标题**/
			g.setColor(Color.RED);
			g.setFont(new Font("Serif",Font.ITALIC,52));
			g.drawString("FishEating Game", getWidth()/2-180, getHeight()/4);
			/**添加按钮**/
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
			//打印死亡通知
			g.setColor(Color.RED);
			g.setFont(new Font("Serif",Font.ITALIC,52));
			g.drawString("You become food!", getWidth()/2-180, getHeight()/2-100);
			//打印鱼生成就
			g.setColor(Color.RED);
			g.setFont(new Font("Serif", Font.BOLD, 28));
			g.drawString("Score: "+String.valueOf(player.score), getWidth()/2-100, getHeight()/2);
			g.drawString("HighScore: "+String.valueOf(highscore), getWidth()/2-100, getHeight()/2+50);
			//添加轮回按钮，鱼生重头来过
			add(start);
			start.setText("restart");
			start.setLocation (getWidth()/2-150, getHeight()*3/4);
		    start.setSize (100, 50) ;
		    //添加返回按钮，告别受苦的世间
		    add(rule);
			rule.setText("Back");		    
		    rule.setLocation (getWidth()/2+50, getHeight()*3/4);
		    rule.setSize (100, 50) ;
			break;
		}
		case MISSION_INTERFACE:
		{
			//关卡开头提示，打印相关信息
			g.setColor(Color.RED);
			g.setFont(new Font("Serif",Font.ITALIC,52));
			g.drawString("Mission "+(mission-1), getWidth()/2-100, getHeight()/2);
			break;
		}
		case WIN:
		{
			//胜利标识，但你还是个咸鱼
			g.setColor(Color.RED);
			g.setFont(new Font("Serif",Font.ITALIC,52));
			g.drawString("Congradulation!", getWidth()/2-180, getHeight()/2-100);
			g.setColor(Color.RED);
			g.setFont(new Font("Serif", Font.BOLD, 28));
			g.drawString("Score: "+String.valueOf(player.score), getWidth()/2-100, getHeight()/2);
			g.drawString("HighScore: "+String.valueOf(highscore), getWidth()/2-100, getHeight()/2+50);
			//添加返回按钮，回忆戎马一生
			add(rule);
			rule.setText("Back");
			rule.setLocation (getWidth()/2-50, getHeight()*3/4);
		    rule.setSize (100, 50) ;
			break;
		}
		case HIGHSCORE:
		{
			//显示最高分
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
			//注意，游戏主体显示终于来了
			/**显示小鱼**/
			for(int i=0;i<littlefish.length;i++)
			{
				if(littlefish[i].direction)		//判断鱼头方向
					g.drawImage(littlefish_imgR, littlefish[i].location_x, littlefish[i].location_y, (int)(littlefish[i].width*littlefish[i].level), (int)(littlefish[i].height*littlefish[i].level), this);
				else
					g.drawImage(littlefish_imgL, littlefish[i].location_x, littlefish[i].location_y, (int)(littlefish[i].width*littlefish[i].level), (int)(littlefish[i].height*littlefish[i].level), this);
			}
			//**显示中鱼**//
			for(int i=0;i<middlefish.length;i++)
			{
				if(middlefish[i].direction)
					g.drawImage(middlefish_imgR, middlefish[i].location_x, middlefish[i].location_y, (int)(middlefish[i].width*middlefish[i].level), (int)(middlefish[i].height*middlefish[i].level), this);
				else
					g.drawImage(middlefish_imgL, middlefish[i].location_x, middlefish[i].location_y, (int)(middlefish[i].width*middlefish[i].level), (int)(middlefish[i].height*middlefish[i].level), this);
			}
			//**显示大鱼**//
			for(int i=0;i<bigfish.length;i++)
			{
				if(bigfish[i].direction)
					g.drawImage(bigfish_imgR, bigfish[i].location_x, bigfish[i].location_y, (int)(bigfish[i].width*bigfish[i].level), (int)(bigfish[i].height*bigfish[i].level), this);
				else
					g.drawImage(bigfish_imgL, bigfish[i].location_x, bigfish[i].location_y, (int)(bigfish[i].width*bigfish[i].level), (int)(bigfish[i].height*bigfish[i].level), this);
			}
			//draw player's fish
			if(!player.invincible||cnt%2==0)		//如果有个强大的buff，你就会不断的闪烁
			{
				if(player.direction)
					g.drawImage(player_imgR, player.location_x, player.location_y, (int)(player.width*player.level), (int)(player.height*player.level), this);
				else
					g.drawImage(player_imgL, player.location_x, player.location_y, (int)(player.width*player.level), (int)(player.height*player.level), this);
			}
			//boss来了吗？
			if(boss!=null)
			{
				//高能预警
				g.setColor(Color.RED);
				g.setFont(new Font("Serif", Font.BOLD, 60));
				g.drawString(boss.text, boss.text_x, boss.text_y);
				//boss is coming
				if(boss.direction)
					g.drawImage(boss_imgR, boss.location_x, boss.location_y, (int)(boss.width*boss.level), (int)(boss.height*boss.level), this);
				else
					g.drawImage(boss_imgL, boss.location_x, boss.location_y, (int)(boss.width*boss.level), (int)(boss.height*boss.level), this);
			}
			//显示当前分数
			String str = "score:" + String.valueOf(player.score);
			g.setColor(Color.RED);
			g.setFont(new Font("Serif",Font.ITALIC,30));
			//显示当前生命值
			g.drawString(str, 20, 50);
			str = "life: "+String.valueOf(player.life);
			g.drawString(str, getWidth()-100, 50);
			
			break;
		}
		}
	}

	//主方法，位置有点小奇怪啊
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
		// 更新玩家移动目标
		player.aim_x = e.getX()-(int)(player.width*player.level/2);
		player.aim_y = e.getY()-(int)(player.height*player.level/2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand()=="start")		//判断按下的按钮
		{	
			flag = MISSION_INTERFACE;			//开始游戏，标志转移到关卡欢迎位置
			mission = MISSION1;					//第一关！！！
			//按钮先不要了，变小，变小！！
			start.setLocation (0, 0);			
		    start.setSize (0, 0) ;
		    rule.setLocation (0, 0);
		    rule.setSize (0, 0) ;
			cnt = 0;							//计数清零，准备计时
			GameInit();							//游戏初始化
		}
		if(e.getActionCommand()=="rule")
		{
			if(flag == MENU)					//判断按下的按钮
				flag = HIGHSCORE;				//进入显示最高分的状态
			else	
				flag = MENU;					//返回首页
			timer.stop();						//停止计时
			repaint();							//重画界面
		}
		if(e.getActionCommand()=="Time")		//判断计时器中断
		{
			if(flag==mission)					//判断是否在关卡内
			{
				player.SetBoundary(getWidth(), getHeight());
				player.SetVelocity();			//设置玩家的咸鱼速度
				player.UpdateLocation(20);		//更新玩家位置
				//**更新小鱼数据**//
				for(int i=0;i<littlefish.length;i++)
				{
					littlefish[i].SetBoundary(getWidth(), getHeight());
					littlefish[i].SetVelocity(player,avoid_player); //更新小鱼移动速度
					littlefish[i].UpdateLocation(fishspeed);		//更新小鱼位置
					if(player.CanEat(littlefish[i]))				//判断玩家能否吃小鱼
						if(player.EatFish(littlefish[i]))			//判断玩家能不能吃小鱼
							littlefish[i].Location_Init();			//小鱼被残忍的杀害了，但邪恶的你又把他复活了
					if(boss!=null)									//boss来了？
						if(littlefish[i].CanEat(boss))				//小鱼能不能吃boss
							littlefish[i].Location_Init();			//当然不能啦，但不知道你为什么还是把那笨笨的小鱼复活了
				}
				//**更新中鱼数据**//
				for(int i=0;i<middlefish.length;i++)
				{
					//不想讲故事了，自己编吧，无非是一个傻逼总爱重复他人痛苦
					middlefish[i].SetBoundary(getWidth(), getHeight());
					middlefish[i].SetVelocity(player,avoid_player);	
					middlefish[i].UpdateLocation(fishspeed);
					if(player.CanEat(middlefish[i]))
						if(player.EatFish(middlefish[i]))
							middlefish[i].Location_Init();
						else
						{
							if(!player.invincible)			//无敌？开挂有意思吗？
							{
								cnt = 0;
								player.Hurt();				//打不赢还上，你是不是傻，他追你？你不会跑？
								if(!player.Alive())
								{
									flag = DEAD;			//早死早超生，这个悲惨的世界有什么好留恋的
									highscore = player.score>highscore ? player.score:highscore;		//让你在这世界上留下点什么吧，虽然你是个咸鱼。哦，你太弱了，我都不想记录你
									timer.stop();			//死了，时间也静止了，是吗？
								}
							}
						}
					if(boss!=null)							//boss来了就跑吧
						if(middlefish[i].CanEat(boss))
							middlefish[i].Location_Init();
					middlefish[i].EatFish(littlefish);		//虽然你在食物链上高一级，但拜托不了咸鱼的命运
				}
				//**更新大鱼信息**//
				for(int i=0;i<bigfish.length;i++)
				{
					//这个程序员很懒，开头都不想写了
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
					//咦？你能吃两？拜托，把另一个大咸鱼吃了吧，我不会复活他的
					bigfish[i].EatFish(littlefish);
					bigfish[i].EatFish(middlefish);
				}
				//如果boss在的话，别惹他
				if(boss!=null)
				{
					boss.UpdateLocation(fishspeed);	
					if(player.CanEat(boss))			//你这咸鱼想吃boss？
					{
						if(!player.invincible)		//别想了，我会给你无敌光环，你不是主角
						{
							cnt = 0;				
							player.Hurt();			//其实很想这咸鱼被秒杀的，我太仁慈了
							if(!player.Alive())	
							{
								flag = DEAD;		//其实boss才是主角，他都不需要无敌光环
								highscore = player.score>highscore ? player.score:highscore;
								timer.stop();		
							}
						}
					}
				}
				if(player.level>2.5)				//这咸鱼可以去另一个池塘玩耍了，但我没那么多图片换背景啊
				{
					flag = MISSION_INTERFACE;		//先驱关卡欢迎界面缓和下
					mission = mission+1;			//关卡递进
					cnt = 0;
					if(mission == WIN)				//咦？咸鱼翻身了
					{
						flag = WIN;					//不得不说，你成功完成了所有挑战，去看看有什么在等待你吧。其实没奖励
						highscore = player.score>highscore ? player.score:highscore;		//记录下来等待下一条咸鱼来超越吧。咦？没更新，你还真是咸鱼中的咸鱼
						timer.stop();				//散了散了
					}
				}				
			}
			
			if(flag==MISSION_INTERFACE)
			{
				//计使20*50=1000ms
				cnt++;
				if(cnt>20)
				{
					cnt = 0;
					flag = mission;  //开始咸鱼之旅吧
					MissionInit();
				}
			}
			if(player.invincible)
			{
				//看你被虐的这么惨，给点无敌时间吧
				cnt++;
				if(cnt>40)			//40*50=2000ms
				{
					player.NoBuffNow();
					cnt = 0;
				}
			}
			
			if(mission>MISSION2)
			{
				if(player.level>1.5)			//我们的咸鱼成长了吗？成长了就该接受真正的挑战了
					avoid_player = true;
			}
			
			repaint(); 	//重画
		}
	}
}
