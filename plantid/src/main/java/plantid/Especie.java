package plantid;

import java.util.List;

public class Especie {
	private String scientificName;
	private List<String> commonNames;

	public Especie() 
	{

	}

	public String getScientificName() 
	{
		return scientificName;
	}
	public void setScientificName(String scientificName) 
	{
		this.scientificName = scientificName;
	}
	public List<String> getCommonNames() 
	{
		return commonNames;
	}

	public void setCommonNames(List<String> commonNames) 
	{
		this.commonNames = commonNames;
	}
}