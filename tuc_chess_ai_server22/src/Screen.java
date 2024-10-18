import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import java.awt.Color;

public class Screen extends JFrame
{
	JTextArea text_area = null;
	JScrollPane scroll_pane = null;
	private static final long serialVersionUID = 1L-100;
	
	public Screen(double timeLimit)
	{
		text_area = new JTextArea("Welcome to TUC-Chess");
		this.print("Time limit: " + timeLimit + " minutes");
		text_area.setBackground(Color.WHITE);
		text_area.setEditable(false);
		
		scroll_pane = new JScrollPane();
		scroll_pane.setViewportView(text_area);
		
		this.add(scroll_pane);
				
		this.setBounds(750, 100, 400, 500);
		ImageIcon img = new ImageIcon("TUC_chess_AI_agent/tuc_chess_ai_server22/chess/chessimage.jpg");
		this.setIconImage(img.getImage());
		this.setVisible(true);
		this.setTitle("TUC-Chess Screen");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void print(String s1)
	{
		text_area.append("\n" + s1);
		
		int length = text_area.getText().length();
		text_area.setCaretPosition(length);
	}
	
}
