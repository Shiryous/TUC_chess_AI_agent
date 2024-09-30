package tuc_chess_ai_client22.src.src;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;


public class Client
{
	private static final int PORTServer = 9876;
	private DatagramSocket client_socket = null;
	private byte[] send_data = null;
	private byte[] receive_data = null;
	private int size = 200;
	private DatagramPacket send_packet = null;
	private DatagramPacket receive_packet = null;
	private InetAddress host = null;
	
	private String my_name;
	private String agent_logic;
	// private int counterMsg = 0;		optional use
	private String received_messages = "";
	private int my_color = 0;
	private World world = null;
	private int score_white = 0;
	private int score_black = 0;
	private int delay = 1000;		// never set it to 0
	
	public Client(String player_name, String agent_logic)
	{
		// initialization of the fields
		try
		{
			client_socket = new DatagramSocket();
			
			send_data = new byte[size];
			receive_data = new byte[size];
			
			host = InetAddress.getLocalHost();
			
			receive_packet = new DatagramPacket(receive_data, receive_data.length);
			send_packet = new DatagramPacket(send_data, send_data.length, host, PORTServer);
		}
		catch(SocketException | UnknownHostException e)
		{
			// print the occurred exception
			System.out.println(e.getClass().getName() + " : " + e.getMessage());
		}
		this.my_name = player_name;
		this.agent_logic = agent_logic;

		// add a random number from 0 to 19 at the end of the name
		Random random = new Random();
		int x = random.nextInt(20);
		my_name += x;
		
		// create the world of the game
		world = new World();
	}
	
	public void sendName()
	{
		try
		{
			// turn my name into bytes
			send_data = my_name.getBytes("UTF-8");
			send_packet.setData(send_data);
			send_packet.setLength(send_data.length);
			client_socket.send(send_packet);
		}
		catch(IOException e)
		{
			// print the occurred exception
			System.out.println(e.getClass().getName() + " : " + e.getMessage());
		}
	}
	
	private void receiveMessages()
	{
		// keep on receiving messages and act according to their content
		while(true)
		{
			try
			{
				// waiting for a message from the server
				client_socket.receive(receive_packet);
				
				// counterMsg++;
				
				// get the String of the message
				// no need to check for IPAddress and Port of sender, it must be the server of TUC-CHESS
				received_messages = new String(receive_packet.getData(), 0, receive_packet.getLength(), "UTF-8");
				
				System.out.println("Received message from server : " + received_messages);
				
				// get the first letter of the String
				String firstLetter = Character.toString(received_messages.charAt(0));
				
				if(firstLetter.equals("P"))		// received information is about my colour
				{
					// get the second letter of the String
					String secondLetter = Character.toString(received_messages.charAt(1));
					
					if(secondLetter.equals("W"))
						my_color = 0;
					else
						my_color = 1;
					
					world.setMyColor(my_color);
				}				
				else if(firstLetter.equals("G"))	// received information is about the game (begin/end)
				{
					// get the second letter of the String
					String secondLetter = Character.toString(received_messages.charAt(1));
					
					if(secondLetter.equals("B"))
					{
						// beginning of the game
						if(my_color == 0)
						{
							String action = world.selectAction(this.getScore_white(), this.getScore_black(), this.agent_logic);
							
							try
							{
								synchronized(this)
								{
									this.wait(delay);
								}
							}
							catch(InterruptedException e)
							{
								System.out.println(e.getClass().getName() + " : " + e.getMessage());
							}
							
							send_data = action.getBytes("UTF-8");
							send_packet.setData(send_data);
							send_packet.setLength(send_data.length);
							client_socket.send(send_packet);
						}
						else
							continue;
					}
					else	// secondLetter.equals("E") - the game has ended
					{
						score_white = Integer.parseInt(Character.toString(received_messages.charAt(2))
								                    + Character.toString(received_messages.charAt(3)));
						
						score_black = Integer.parseInt(Character.toString(received_messages.charAt(4))
						                            + Character.toString(received_messages.charAt(5)));
						
						if(score_white - score_black > 0)
						{
							if(my_color == 0)
								System.out.println("I won! " + score_white + "-" + score_black);
							else
								System.out.println("I lost. " + score_white + "-" + score_black);
							
							System.out.println("My average branch factor was : " + world.getAvgBFactor());
						}
						else if(score_white - score_black < 0)
						{
							if(my_color == 0)
								System.out.println("I lost. " + score_white + "-" + score_black);
							else
								System.out.println("I won! " + score_white + "-" + score_black);
							System.out.println("My average branch factor was : " + world.getAvgBFactor());
						}
						else
						{
							System.out.println("It is a draw! " + score_white + "-" + score_black);
							
							System.out.println("My average branch factor was : " + world.getAvgBFactor());
						}
							
						break;
					}
				}
				else	// firstLetter.equals("T") - a move has been made
				{
					// decode the rest of the message
					int nextPlayer = Integer.parseInt(Character.toString(received_messages.charAt(1)));
					
					int x1 = Integer.parseInt(Character.toString(received_messages.charAt(2)));
					int y1 = Integer.parseInt(Character.toString(received_messages.charAt(3)));
					int x2 = Integer.parseInt(Character.toString(received_messages.charAt(4)));
					int y2 = Integer.parseInt(Character.toString(received_messages.charAt(5)));
					
					int prizeX = Integer.parseInt(Character.toString(received_messages.charAt(6)));
					int prizeY = Integer.parseInt(Character.toString(received_messages.charAt(7)));
					
					score_white = Integer.parseInt(Character.toString(received_messages.charAt(8)) 
							                      + Character.toString(received_messages.charAt(9)));
					
					score_black = Integer.parseInt(Character.toString(received_messages.charAt(10)) 
												  + Character.toString(received_messages.charAt(11)));
					
					world.makeMove(x1,y1,x2,y2,prizeX,prizeY);
					
					if(nextPlayer==my_color)
					{
						String action = world.selectAction(this.getScore_white(),this.getScore_black(), this.agent_logic);
						
						try
						{
							synchronized(this)
							{
								this.wait(delay);
							}
						}
						catch(InterruptedException e)
						{
							System.out.println(e.getClass().getName() + " : " + e.getMessage());
						}
						
						send_data = action.getBytes("UTF-8");
						send_packet.setData(send_data);
						send_packet.setLength(send_data.length);
						client_socket.send(send_packet);			
					}
					else
					{
						continue;
					}				
				}
				
			}
			catch(IOException e)
			{
				System.out.println(e.getClass().getName() + " : " + e.getMessage());
			}
		}
	}
	
	public int getScore_white()
	{
		return score_white;
	}
	
	public int getScore_black()
	{
		return score_black;
	}
	
	// testing
	public static void main(String[] args)
	{
		Client client = new Client(args[0], args[1]);
		
		// optionally adding delay to response
		//if(args.length == 1)
		//	client.delay = Integer.parseInt(args[0]);
		
		// send the first message - my name
		client.sendName();
		
		// start receiving messages;
		client.receiveMessages();
	}

}
