import javax.swing.ImageIcon;

public class Prize
{
	private ImageIcon img = null;
	private int value = 0;
	private int maxVal = 1;
	private int minVal = 0;
	
	public Prize()
	{
		img =  new ImageIcon("TUC_chess_AI_agent/tuc_chess_ai_server22/chess/prize.png");
		
		// valueChance == 0.8
		if(Math.random() > Board.getValueChance())
			value = minVal;
		else
			value = maxVal;	
	}
	
	public ImageIcon getIcon()	{	return img;	}
	
	public int getValue()	{	return value;	}

}
