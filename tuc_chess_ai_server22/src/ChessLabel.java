import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class ChessLabel extends JLabel
{
	private boolean empty = true;	// empty of a chess part, it may still contain a prize
	private boolean has_prize = false;
	private ImageIcon standard_icon = null;
	private ChessPart chess_part = null;
	private Prize prize = null;
	private static final long serialVersionUID = 1L - 200;
	
	// empty constructor - auto generated
	
	public void setStandard_icon(ImageIcon standard_image)
	{
		this.standard_icon = standard_image;
	}
	
	public void resetImage()	{	this.setIcon(standard_icon);	}
	
	public void setChess_part(ChessPart chess_part)
	{
		this.chess_part = chess_part;
		this.setIcon(this.chess_part.getIcon());
		this.empty = false;
	}
	
	public void removeCp()
	{
		this.chess_part = null;
		this.resetImage();
		this.empty = true;
	}
	
	public void setPrize(Prize prize)
	{
		this.prize = prize;
		this.setIcon(this.prize.getIcon());
		this.has_prize = true;
	}
	
	public void removePrize()
	{
		this.prize = null;
		this.resetImage();
		this.has_prize = false;
	}
	
	public ChessPart getChess_part()	{	return chess_part;		}
	
	public boolean getEmpty()	{	return empty;	}
	
	public Prize getPrize()		{	return prize;	}
	
	public boolean getHas_prize(){	return has_prize;}
	
}
