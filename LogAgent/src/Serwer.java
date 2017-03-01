import java.io.*;
import java.net.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Serwer
{

	private ServerSocket serverSocket;

	public static void main(String[] args) throws IOException
	{
		new Serwer(Integer.parseInt(args[0]));
	}

	public Serwer(int portNumber)
	{
		try
		{
			serverSocket = new ServerSocket(portNumber);
			System.out.println("Serwer wystartowal o: " + new Date());
			DataBaseAccess db = new DataBaseAccess();
			db.crateControlTable();
			db = null;
			while (true)
			{
				Socket socket = serverSocket.accept();
				ClientThread cT = new ClientThread(socket);
				new Thread(cT).start();
			}
		}
		catch (IOException e)
		{
			System.out.println("Blad: " + e);
		}
	}

	class ClientThread implements Runnable
	{
		Socket				threadSocket;
		ObjectOutputStream	outToClient;
		ObjectInputStream	inFromClient;
		DataBaseAccess		dataBase;
		List<String>		infoAboutCurrentTable;
		String				internetAderss;
		String				tableNameForCurrentSession;

		public ClientThread(Socket Socket)
		{
			threadSocket = Socket;
		}

		public void run()
		{
			internetAderss = threadSocket.getInetAddress().toString();
			try
			{
				inFromClient = new ObjectInputStream(threadSocket.getInputStream());
				outToClient = new ObjectOutputStream(threadSocket.getOutputStream());
				dataBase = new DataBaseAccess();
				System.out.println("Polaczenie: " + new Date());

				Log log = null;
				log = (Log) inFromClient.readObject();
				infoAboutCurrentTable = log.getInfo();
				tableNameForCurrentSession = dataBase.crateNewLogTable(Calendar.getInstance(),
						threadSocket.getLocalAddress(), log);
				outToClient.writeObject(tableNameForCurrentSession);
				/// Main server loop
				while (true)
				{
					log = null;
					log = (Log) inFromClient.readObject();
					if (log.getPriority() != -1)
						dataBase.insertLog(tableNameForCurrentSession, log, infoAboutCurrentTable);
				}
			}
			catch (IOException e)
			{
				System.out.println("Blad: " + e);
				dataBase.endSession(tableNameForCurrentSession);
				System.out.println("Konczenie sesji z " + internetAderss);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				System.err.println("Powazny Blad:");
				e.printStackTrace();
			}
		}
	}
}
