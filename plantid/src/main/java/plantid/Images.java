package plantid;

import java.util.Map;

public class Images 
{
	private String organ;
	private Map<String, String> url;
	private String citation;

	public Images()
	{
		
	}
	
	public String getOrgan() 
	{
		return organ;
	}
	
	public void setOrgan(String organ) 
	{
		this.organ = organ;
	}
	
	public Map<String, String> getUrl() 
	{
		return url;
	}
	
	public void setUrl(Map<String, String> url) 
	{
		this.url = url;
	}
	
	public String getCitation() 
	{
		return citation;
	}
	
	public void setCitation(String citation) 
	{
		this.citation = citation;
	}
}
