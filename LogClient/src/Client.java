import java.awt.AWTException;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

public class Client
{

	static ClientFrame		cI;
	Socket					serverSocket;
	ObjectOutputStream		outToServer;
	ObjectInputStream		inFromServer;
	String					userInput;
	String					hostName;
	int						portNumber;
	private TrayIcon		trayIcon	= null;
	private SystemTray		tray		= null;
	private String			nameOfCurrentSession;
	private double			counter		= 0;

	private static boolean	run			= true;

	public Client(String[] hostInfo)
	{
		if (hostInfo.length != 2)
		{
			System.err.println("Usage: java EchoClient <host name> <port number>");
			System.exit(1);
		}
		hostName = hostInfo[0];
		portNumber = Integer.parseInt(hostInfo[1]);
	}

	public static void main(String[] args) throws IOException
	{

		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			// UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		}
		catch (UnsupportedLookAndFeelException ex)
		{
			ex.printStackTrace();
		}
		catch (IllegalAccessException ex)
		{
			ex.printStackTrace();
		}
		catch (InstantiationException ex)
		{
			ex.printStackTrace();
		}
		catch (ClassNotFoundException ex)
		{
			ex.printStackTrace();
		}

		SetupFrame cS = new SetupFrame();
		cS.setVisible(true);

	}
	
	public void start(Client client)
	{
		client.addToSystemTray(client);
		cI = new ClientFrame(client);
		cI.frmLogclient.setVisible(true);
	}

	public void commit(List<String> info, String typeName)
	{
		try
		{
			serverSocket = new Socket(hostName, portNumber);
			outToServer = new ObjectOutputStream(serverSocket.getOutputStream());
			inFromServer = new ObjectInputStream(serverSocket.getInputStream());

			Log log = new Log();
			log.setDate(getDate());
			log.setInternetAddress(typeName);
			log.setPriority(Double.POSITIVE_INFINITY);
			info.add(0, "Priorytet");
			info.add(0, "AdresZrodlowy");
			info.add(0, "DataLog");
			log.setInfo(info);

			outToServer.writeObject(log);
			outToServer.reset();

			try
			{
				nameOfCurrentSession = (String) inFromServer.readObject();
				if (nameOfCurrentSession.equals(""))
				{
					JOptionPane.showMessageDialog(null, "Problem connecting to the Serwer");
					System.exit(1);
				} else
					cI.setSessionName(nameOfCurrentSession);
				System.out.println(nameOfCurrentSession);
			}
			catch (ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		catch (UnknownHostException e)
		{
			System.err.println("Don't know about host " + hostName);
			System.exit(1);
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, "Problem connecting to " + hostName);
			System.err.println("Couldn't get I/O for the connection to " + hostName);
			System.exit(1);
		}
	}

	public void hartbeat()
	{
		Log logbeat = new Log();
		logbeat.setPriority(-1);
		try
		{
			outToServer.writeObject(logbeat);
			outToServer.reset();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Serwer has malfunctioned");
			System.exit(0);
		}

	}

	private void send(Date date, InetAddress internetAddress, double priority, List<String> info)
	{
		Log log = new Log();

		log.setDate(date);
		log.setInternetAddress(internetAddress.toString());
		log.setPriority(priority);
		log.setInfo(info);

		try
		{
			outToServer.writeObject(log);
			outToServer.reset();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	class KeyHookThread implements Runnable
	{
		private Thread t;

		public void run()
		{
			GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();

			System.out.println("Global keyboard hook successfully started.");
			List<String> key = new ArrayList<String>();
			keyboardHook.addKeyListener(new GlobalKeyAdapter()
			{
				@Override
				public void keyPressed(GlobalKeyEvent event)
				{
					System.out.println(event);
					if (cI.getList().size() != 0)
					{
						if (Character.getName(event.getKeyChar()) != null)
							key.add(Character.getName(event.getKeyChar()));
						else
							key.add("" + event.getVirtualKeyCode());
						send(getDate(), serverSocket.getLocalAddress(), 0, key);
						key.clear();
					} else
					{
						send(getDate(), serverSocket.getLocalAddress(), event.getVirtualKeyCode(), null);
					}
				}

				@Override
				public void keyReleased(GlobalKeyEvent event)
				{
					/* nothing to do here */ 
					// System.out.println(event);
				}
			});

			try
			{
				while (run)
					Thread.sleep(128);
			}
			catch (InterruptedException e)
			{
				/* nothing to do here */ 
			}
			finally
			{
				keyboardHook.shutdownHook();
			}
		}

		public void start()
		{
			if (t == null)
			{
				t = new Thread(this);
				t.start();
			}
		}

		public void interrupt()
		{
			if (t != null)
			{
				t.interrupt();
			}
		}
	}

	KeyHookThread keyHookHandle;

	public void startSendingKeybordHook()
	{
		keyHookHandle = new KeyHookThread();
		keyHookHandle.start();
	}

	class TimeLogThread implements Runnable
	{
		private Thread	t;
		private long	delay;

		TimeLogThread(long d)
		{
			delay = d;
		}

		public void run()
		{
			List<String> counterList = new ArrayList<String>();
			try
			{
				while (true)
				{
					if (cI.getList().size() != 0)
					{
						counterList.add("" + counter++);
						send(getDate(), serverSocket.getLocalAddress(), 0, counterList);
						counterList.clear();
					} else
					{
						send(getDate(), serverSocket.getLocalAddress(), counter++, null);
					}

					Thread.sleep(delay);
				}
			}
			catch (InterruptedException e)
			{
				// System.out.println("Thread " + threadName + " interrupted.");
			}
		}

		public void start()
		{
			if (t == null)
			{
				t = new Thread(this);
				t.start();
			}
		}

		public void interrupt()
		{
			if (t != null)
			{
				t.interrupt();
			}
		}
	}

	TimeLogThread timeLogThreadHandle;

	public void startSendingTimeLog(long delay)
	{
		timeLogThreadHandle = new TimeLogThread(delay);
		timeLogThreadHandle.start();
	}

	public void startSendingHybridLog(long delay)
	{
		timeLogThreadHandle = new TimeLogThread(delay);
		timeLogThreadHandle.start();
		System.out.println("Option: sending hybrid logs");
	}

	public void sendManualLog(double priority, List<String> info)
	{
		send(getDate(), serverSocket.getLocalAddress(), priority, info);
		try
		{
			Thread.sleep(127);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stopSending()
	{
		run = false;
		if (timeLogThreadHandle != null)
			timeLogThreadHandle.interrupt();
		if (keyHookHandle != null)
			keyHookHandle.interrupt();
	}

	public Date getDate()
	{
		return new Date(Calendar.getInstance().getTimeInMillis());
	}

	public InetAddress getInetAdress()
	{
		return serverSocket.getLocalAddress();
	}

	/// System Tray Functions

	protected static Image createImage(String path, String description)
	{
		ImageIcon ico = new ImageIcon(path, description);
		return ico.getImage();
	}

	public void addToSystemTray(Client c)
	{
		if (!SystemTray.isSupported())
		{
			System.out.println("SystemTray is not supported");
			return;
		}
		final PopupMenu popup = new PopupMenu();
		trayIcon = new TrayIcon(createImage("log.png", "tray icon"));
		tray = SystemTray.getSystemTray();

		trayIcon.setToolTip("LogClinet");
		trayIcon.setImageAutoSize(true);

		MenuItem aboutItem = new MenuItem("About");
		Menu displayMenu = new Menu("Display");
		MenuItem minDisItem = new MenuItem("Minimal");
		MenuItem defDisItem = new MenuItem("Default");
		MenuItem exitItem = new MenuItem("Exit");

		// Add components to pop-up menu
		popup.add(aboutItem);
		popup.addSeparator();
		popup.add(displayMenu);
		displayMenu.add(minDisItem);
		displayMenu.add(defDisItem);
		popup.add(exitItem);

		aboutItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(null, "Projekt stworzony jako zadanie domowe na przedmiot: Zarzadzanie sieciami telekomunikacyjnymi. ");
			}
		});
		
		minDisItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				cI.frmLogclient.setVisible(true);
				cI.frmLogclient.setSize(370, 210);
				tray.remove(trayIcon);
			}
		});
		
		defDisItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				cI.frmLogclient.setVisible(true);
				cI.frmLogclient.setSize(450, 300);
				tray.remove(trayIcon);
			}
		});
		
		exitItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					serverSocket.close();
				}
				catch (IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				finally
				{
					tray.remove(trayIcon);
					System.exit(1);
				}
			}
		});

		trayIcon.setPopupMenu(popup);

		trayIcon.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				cI.frmLogclient.setVisible(true);
				tray.remove(trayIcon);
			}
		});
	}

	public void addTrayListener()
	{
		try
		{
			tray.add(trayIcon);
		}
		catch (AWTException e)
		{
			System.out.println("TrayIcon could not be added.");
		}
	}
}
