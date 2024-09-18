import src.Client;

class TaskStart implements Runnable {
    private String threadName;

    public TaskStart(String name){
        this.threadName = name;
    }

    @Override
    public void run() {
        System.out.println(threadName + " is running" );
        if (threadName == "Server"){
            UDPServer.main(null);
        }
        else {
            Client.main(null);
        }   
    }
}

public class chessSetup {
    public static void main(String[] args) throws InterruptedException {

        TaskStart serverTask = new TaskStart("Server");
        TaskStart player1Task = new TaskStart("Player 1");
        TaskStart player2Task = new TaskStart("Player 2");

        Thread serverThread = new Thread(serverTask);
        Thread player1Thread = new Thread(player1Task);
        Thread player2Thread = new Thread(player2Task);

        serverThread.start();
        Thread.sleep(2000);
        player1Thread.start();
        Thread.sleep(2000);
        player2Thread.start();
        Thread.sleep(2000);

    }
	
}
// There is a possibility that it needs different threads to pull this off.
//ANSWER: It did need different threads