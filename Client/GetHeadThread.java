import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class GetHeadThread implements Runnable {
	final static int CLIENT_PORT = 5656;
	final static int SERVER_PORT = 5556;

	public void run() {
		
		try {
			
			while(true) {
				Socket s = new Socket("localhost", SERVER_PORT);
				System.out.println("get head thread");
				
				//Initialize data stream to send data out
				OutputStream outstream = s.getOutputStream();
				PrintWriter out = new PrintWriter(outstream); 
				
				String command = "GETHEADS\n";
				out.println(command);
				out.flush();
				s.close();
				
				//pause
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
		        }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//pause
		try {
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
        }
	}
}
