import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Board extends JFrame
{
	private final int rows = 7;
	private final int columns = 5;
	private ChessLabel cl[][];
	private JPanel totalPanel = null;

	private JPanel mainPanel = null;
	private JPanel northPanel = null;
	private JPanel southPanel = null;
	private JPanel westPanel = null;
	private JPanel eastPanel= null;
	private final int white = 0;
	private final int black = 1;
	private static final double valueChance = 0.95;
	private final double prizeChance = 0.2;
	private static final long serialVersionUID = 1L;
	
	public Board()
	{
		// instantiating the totalPanel
		totalPanel = new JPanel();
		totalPanel.setLayout(new BorderLayout());
		
		// set the main panel
		this.setMainPanel();
		
		// set the chess parts
		this.setChessParts();
		
		// set the prizes
		this.setPrizes();
		
		// set the side panels
		this.setSidePanels();		

		// setting features of the JFrame
		this.setContentPane(totalPanel);
		this.setBounds(140, 0, 550, 700);
		ImageIcon img = new ImageIcon("chess/chessimage.jpg");
		this.setIconImage(img.getImage());
		this.setVisible(true);
		this.setTitle("TUC-Chess");	
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void setMainPanel()
	{
		// creating the main panel of the gui and setting its layout
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
				
		// creating the ChessLabel table
		cl = new ChessLabel[rows][columns];
			
		// constraints of the components of the GridBagLayout
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.insets = new Insets(1,1,1,1);
		gbc.fill = GridBagConstraints.BOTH;
		
		ImageIcon iconWhite = new ImageIcon("./tuc_chess_ai_server22/chess/white.gif");
		ImageIcon iconBlack = new ImageIcon("./tuc_chess_ai_server22/chess/black.gif");
		
		for(int i=0; i<rows; i++)
			for(int j=0; j<columns; j++)
			{
				// creating the setting each ChessLabel
				cl[i][j] = new ChessLabel();
				cl[i][j].setHorizontalAlignment(JLabel.CENTER);
				cl[i][j].setOpaque(true);
				gbc.gridx = j;
				gbc.gridy = i;
				mainPanel.add(cl[i][j], gbc);
				
				// setting the color of the ChessLabel
				if(i%2 == 0)
					if(j%2 == 0)
					{
						cl[i][j].setBackground(Color.WHITE);
						cl[i][j].setIcon(iconWhite);
						cl[i][j].setStandard_icon(iconWhite);
					}
					else
					{
						cl[i][j].setBackground(Color.BLACK);
						cl[i][j].setIcon(iconBlack);
						cl[i][j].setStandard_icon(iconBlack);
					}
				else // i%2 == 1
					if(j%2 == 0)
					{
						cl[i][j].setBackground(Color.BLACK);
						cl[i][j].setIcon(iconBlack);
						cl[i][j].setStandard_icon(iconBlack);
					}
					else
					{
						cl[i][j].setBackground(Color.WHITE);
						cl[i][j].setIcon(iconWhite);
						cl[i][j].setStandard_icon(iconWhite);
					}
			}
		
		totalPanel.add(mainPanel, BorderLayout.CENTER);
	}
	
	private void setChessParts()
	{
		// setting the white ChessParts
		ImageIcon whitePawn = new ImageIcon("./tuc_chess_ai_server22/chess/pawn_white.gif");
		ImageIcon whiteRook = new ImageIcon("./tuc_chess_ai_server22/chess/rook_white.gif");
		ImageIcon whiteKing = new ImageIcon("./tuc_chess_ai_server22/chess/king_white.gif");
		
		// creating the white pawns
		for(int j=0; j<columns; j++)
		{
			cl[rows-2][j].setChess_part(new Pawn(white, whitePawn));
			cl[rows-2][j].setIcon(cl[rows-2][j].getChess_part().getIcon());
		}
		
		cl[rows-1][0].setChess_part(new Pawn(white, whitePawn));
		cl[rows-1][0].setIcon(cl[rows-1][0].getChess_part().getIcon());
		cl[rows-1][columns-1].setChess_part(new Pawn(white, whitePawn));
		cl[rows-1][columns-1].setIcon(cl[rows-1][columns-1].getChess_part().getIcon());
		
		// creating the white rooks
		cl[rows-1][1].setChess_part(new Rook(white, whiteRook));
		cl[rows-1][1].setIcon(cl[rows-1][1].getChess_part().getIcon());
		cl[rows-1][columns-2].setChess_part(new Rook(white, whiteRook));
		cl[rows-1][columns-2].setIcon(cl[rows-1][columns-2].getChess_part().getIcon());
		
		// creating the white king
		cl[rows-1][columns/2].setChess_part(new King(white, whiteKing));
		cl[rows-1][columns/2].setIcon(cl[rows-1][columns/2].getChess_part().getIcon());
		
		//-------------------------------------------------------------
		
		// setting the black ChessParts
		ImageIcon blackPawn = new ImageIcon("./tuc_chess_ai_server22/chess/pawn_black.gif");
		ImageIcon blackRook = new ImageIcon("./tuc_chess_ai_server22/chess/rook_black.gif");
		ImageIcon blackKing = new ImageIcon("./tuc_chess_ai_server22/chess/king_black.gif");
		
		// creating the black pawns
		for(int j=0; j<columns; j++)
		{
			cl[1][j].setChess_part(new Pawn(black, blackPawn));
			cl[1][j].setIcon(cl[1][j].getChess_part().getIcon());
		}
		
		cl[0][0].setChess_part(new Pawn(black, blackPawn));
		cl[0][0].setIcon(cl[0][0].getChess_part().getIcon());
		cl[0][columns-1].setChess_part(new Pawn(black, blackPawn));
		cl[0][columns-1].setIcon(cl[0][columns-1].getChess_part().getIcon());
		
		// creating the black rooks
		cl[0][1].setChess_part(new Rook(black, blackRook));
		cl[0][1].setIcon(cl[0][1].getChess_part().getIcon());
		cl[0][columns-2].setChess_part(new Rook(black, blackRook));
		cl[0][columns-2].setIcon(cl[0][columns-2].getChess_part().getIcon());
		
		// creating the white king
		cl[0][columns/2].setChess_part(new King(black, blackKing));
		cl[0][columns/2].setIcon(cl[0][columns/2].getChess_part().getIcon()); 
	}
	
	private void setPrizes()
	{
		for(int j=0; j<columns; j++)
		{
			Prize prize = new Prize();
			
			cl[rows/2][j].setPrize(prize);
		}
	}
	
	private void setSidePanels()
	{	
		Font font = new Font("Times New Roman", Font.PLAIN, 20);
		
		// setting westPanel
		westPanel = new JPanel();
		westPanel.setLayout(new GridLayout(rows,0));	
		ArrayList<JLabel> numbersLabel1 = new ArrayList<JLabel>();
		for(int i=0; i<rows; i++)
		{
			numbersLabel1.add(new JLabel(""+i,JLabel.CENTER));
			numbersLabel1.get(i).setFont(font);
			westPanel.add(numbersLabel1.get(i));
		}
		westPanel.setBackground(Color.white);
		
		// setting eastPanel
		eastPanel = new JPanel();
		eastPanel.setLayout(new GridLayout(rows,0));	
		numbersLabel1 = new ArrayList<JLabel>();
		for(int i=0; i<rows; i++)
		{
			numbersLabel1.add(new JLabel(""+i,JLabel.CENTER));
			numbersLabel1.get(i).setFont(font);
			eastPanel.add(numbersLabel1.get(i));
		}
		eastPanel.setBackground(Color.white);
		
		// setting northPanel
		northPanel = new JPanel();
		northPanel.setLayout(new GridLayout(0,columns));	
		numbersLabel1 = new ArrayList<JLabel>();
		for(int i=0; i<columns; i++)
		{
			numbersLabel1.add(new JLabel(""+i, JLabel.CENTER));
			numbersLabel1.get(i).setFont(font);
			northPanel.add(numbersLabel1.get(i));
		}
		northPanel.setBackground(Color.white);
		
		// setting southPanel
		southPanel = new JPanel();
		southPanel.setLayout(new GridLayout(0,columns));	
		numbersLabel1 = new ArrayList<JLabel>();
		for(int i=0; i<columns; i++)
		{
			numbersLabel1.add(new JLabel(""+i, JLabel.CENTER));
			numbersLabel1.get(i).setFont(font);
			southPanel.add(numbersLabel1.get(i));
		}
		southPanel.setBackground(Color.white);
		
		// adding the panels in the total panel
		totalPanel.add(northPanel, BorderLayout.NORTH);
		totalPanel.add(southPanel, BorderLayout.SOUTH);
		totalPanel.add(eastPanel, BorderLayout.EAST);
		totalPanel.add(westPanel, BorderLayout.WEST);
	}
	
	public String generateBonus()
	{
		// no bonus == "99"
		String s1 = "99";
		
		// prizeChance == 0.2
		if(Math.random() < prizeChance)
		{
			ArrayList<Integer> xAvailable = new ArrayList<Integer>();
			ArrayList<Integer> yAvailable = new ArrayList<Integer>();
			
			// find every empty ChessLabel
			for(int i=0; i<rows; i++)
				for(int j=0; j<columns; j++)
					if(cl[i][j].getEmpty() && !cl[i][j].getHas_prize())
					{
						xAvailable.add(i);
						yAvailable.add(j);
					}
			
			// pick one at random
			Random ran = new Random();
			int pos = ran.nextInt(xAvailable.size());
			
			int x = xAvailable.get(pos);
			int y = yAvailable.get(pos);
			
			Prize prize = new Prize();
			cl[x][y].setPrize(prize);
			
			s1 = Integer.toString(x) + Integer.toString(y);
		}
		
		return s1;
	}
	
	public ChessLabel[][] getChessLabel()
	{
		return cl;
	}
	
	public int getRows()
	{
		return rows;
	}
	
	public int getColumns()
	{
		return columns;
	}
	
	public static double getValueChance()
	{
		return valueChance;
	}

}
