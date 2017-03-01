import java.sql.Date;
import java.util.List;

public class Log implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public String getInternetAddress()
	{
		return internetAddress;
	}

	public void setInternetAddress(String internetAddress)
	{
		this.internetAddress = internetAddress;
	}

	public double getPriority()
	{
		return priority;
	}

	public void setPriority(double priority)
	{
		this.priority = priority;
	}

	public List<String> getInfo()
	{
		return info;
	}

	public void setInfo(List<String> info)
	{
		this.info = info;
	}

	private Date			date;
	private String			internetAddress;
	private double			priority;
	private List<String>	info;
}
