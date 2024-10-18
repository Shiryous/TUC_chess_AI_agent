import javax.swing.ImageIcon;

public class Prize
{
	private ImageIcon image = null;
	private int value = 0;
	private int max_value = 1;
	private int min_value = 0;
	
	public Prize()
	{
		image =  new ImageIcon("./tuc_chess_ai_server22/chess/prize.png");
		
		if(Math.random() > Board.getValueChance()) // Default value chance is 0.95
			value = min_value;
		else
			value = max_value;	
	}
	
	public ImageIcon getIcon()	{	return image;	}
	
	public int getValue()	{	return value;	}

}
