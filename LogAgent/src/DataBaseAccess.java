import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

public class DataBaseAccess
{
	/**
	 * Ciag zawierajacy adres pakietu w ktorym znajduje sie sterownik JDBC
	 */
	private static String	driver;
	/**
	 * Ciag zawierajacy adres url do bazy danych
	 */
	private static String	url;
	/**
	 * Ciag zawierajacy login do bazy danych (domyslnie root)
	 */
	private static String	login;
	/**
	 * Ciag zawierajacy haslo do bazy danych (domyslnie brak)
	 */
	private static String	password;

	/**
	 * BLOK STATYCZNY pobierajacy sterownik JDBC/ODBC przy pierwszym uzyciu
	 * DataBaseAccesor
	 */
	static
	{
		try
		{
			Configuration();
			System.out.println("Configurated!");
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}

		try
		{
			Class.forName(driver);
		}
		catch (ClassNotFoundException e)
		{
			System.err.println("£adowanie sterownika JDBC/ODBC zakoñczy³o siê niepowodzeniem.");
		}
	}

	private static void Configuration() throws IOException
	{

		File file = new File("dbconfig.properties");
		if (!file.exists())
		{
			file.createNewFile();
			String defaultData = "#Driver for JDBC\n" + "driver=com.mysql.jdbc.Driver\n"
					+ "#Data Base adress (default on your localhost on 80 port)\n"
					+ "url=jdbc:mysql://localhost/loghandler?useUnicode=true&characterEncoding=UTF-8\n"
					+ "#login on your database\n" + "login=root\n" + "#your password on database\n" + "password=";
			FileWriter fileWritter = new FileWriter(file.getName(), true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(defaultData);
			bufferWritter.close();
		}
		try (FileReader reader = new FileReader("dbconfig.properties"))
		{
			Properties prop = new Properties();
			prop.load(reader);
			driver = prop.getProperty("driver");
			url = prop.getProperty("url");
			login = prop.getProperty("login");
			password = prop.getProperty("password");
		}
	}

	private Connection createConnection() throws SQLException
	{
		return DriverManager.getConnection(url, login, password);
	}

	/** Metoda zamykajaca polaczenie z lokalna baza danych */
	private void endConnection(Connection connection)
	{
		if (connection == null)
		{
			return;
		}
		try
		{
			connection.close();
		}
		catch (SQLException e)
		{
			System.err.println("Zamkniêcie po³¹czenia zakoñczy³o siê niepowodzeniem.");
		}
	}

	public void crateControlTable()
	{
		String controlQuery = "CREATE TABLE IF NOT EXISTS control (id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT, "
				+ "tableName VARCHAR(50), " + "typeName VARCHAR(50), " + "source VARCHAR(20), " + "dateStart DATETIME, "
				+ "dateStop DATETIME, " + "edited  TINYINT(1) NOT NULL DEFAULT 0)";
		Connection c = null;

		try
		{
			c = createConnection();
			PreparedStatement controlStatement = null;
			controlStatement = c.prepareStatement(controlQuery);
			controlStatement.executeUpdate();
		}
		catch (SQLException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.err.println("Stworzenie tablicy kontrolnej sie nie powiodlo.");
		}
		finally
		{
			endConnection(c);
		}
	}

	public String crateNewLogTable(Calendar cal, InetAddress internetAddressOfSource, Log log)
	{
		String tableName;
		List<String> list = log.getInfo();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SS");
		String partOfQuery = list.get(0);
		partOfQuery = partOfQuery + " DATETIME";
		String partOfValues = "?";
		for (int i = 1; i < list.size(); i++)
		{
			partOfQuery = partOfQuery + ", " + list.get(i);
			if (i == 1)
			{
				partOfQuery = partOfQuery + " VARCHAR(20)";
			} else if (i == 2)
			{
				partOfQuery = partOfQuery + " DOUBLE";
			} else
			{
				partOfQuery = partOfQuery + " VARCHAR(200)";
			}
			partOfValues = partOfValues + "," + "?";
		}
		String internetAdress = internetAddressOfSource.toString().replace("/", "_");
		java.util.Date currentTime = cal.getTime();
		tableName = "logFrom" + internetAdress.replace(".", "_") + "_" + format1.format(currentTime);
		String query = "CREATE TABLE " + tableName + " " + "(" + "ID INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT, "
				+ partOfQuery + ")";

		String controlQuery = "INSERT INTO control (tableName, typeName, source, dateStart) VALUES (?,?,?,?)";

		Connection c = null;
		PreparedStatement statement = null;
		PreparedStatement controlStatement = null;
		try
		{
			c = createConnection();
			statement = c.prepareStatement(query);
			statement.executeUpdate();

			controlStatement = c.prepareStatement(controlQuery);
			controlStatement.setString(1, tableName);
			controlStatement.setString(2, log.getInternetAddress());
			controlStatement.setString(3, internetAdress.replace("_", ""));
			controlStatement.setTimestamp(4, new Timestamp(currentTime.getTime()));
			controlStatement.executeUpdate();

		}
		catch (SQLException e)
		{
			System.err.println("Utworzenie nowej tabeli zakonczylo sie nie powodzeniem.");
			tableName = "";
		}
		catch (Exception e)
		{
			e.printStackTrace();
			tableName = "";
		}
		finally
		{
			endConnection(c);
		}
		return tableName;
	}

	public void insertLog(String tableName, Log log, List<String> list)
	{
		System.out.println("Doszedl log z " + log.getInternetAddress());
		String partOfQuery = list.get(0);
		String partOfValues = "?";
		for (int i = 1; i < list.size(); i++)
		{
			partOfQuery = partOfQuery + ", " + list.get(i);
			partOfValues = partOfValues + "," + "?";

		}
		String query = "INSERT INTO " + tableName + "(" + partOfQuery + ") " + "VALUES (" + partOfValues + ")";
		Connection c = null;
		PreparedStatement statement = null;
		try
		{
			c = createConnection();

			statement = c.prepareStatement(query);
			statement.setTimestamp(1, new java.sql.Timestamp(log.getDate().getTime()));
			statement.setString(2, log.getInternetAddress().toString());
			statement.setDouble(3, log.getPriority());

			if (list.size() != 3)
				for (int i = 0; i < list.size() - 3; i++)
				{
					// System.out.println(i);
					if (log.getInfo() != null)
						statement.setString(i + 4, log.getInfo().get(i));
					else
						statement.setString(i + 4, "");
				}
			statement.executeUpdate();
		}
		catch (SQLException e)
		{
			System.err.println("Wstawianie wydarzenia zakoñczy³o siê niepowodzeniem.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			endConnection(c);
		}
	}

	public void endSession(String tableName)
	{
		String query = "UPDATE `control` SET dateStop = ? WHERE tableName='" + tableName + "'";
		Connection c = null;
		PreparedStatement statement = null;
		try
		{
			c = createConnection();
			statement = c.prepareStatement(query);
			statement.setTimestamp(1, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			statement.executeUpdate();
		}
		catch (SQLException e)
		{
			System.err.println("Koñczenie sesji zakoñczy³o siê niepowodzeniem.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			endConnection(c);
		}
	}
}
