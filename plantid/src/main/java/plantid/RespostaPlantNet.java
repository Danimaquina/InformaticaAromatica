package plantid;

import java.util.List;

public class RespostaPlantNet 
{
	private String language;
	private String bestMatch;
	private String preferedReferential;
	private String version;
	private int remainingIdentificationRequests;

	private List<Resultat> results;

	public RespostaPlantNet()
	{

	}

	public String getLanguage() 
	{
		return language;
	}



	public void setLanguage(String language) 
	{
		this.language = language;
	}



	public String getPreferedReferential() 
	{
		return preferedReferential;
	}



	public void setPreferedReferential(String preferedReferential) 
	{
		this.preferedReferential = preferedReferential;
	}



	public String getVersion() 
	{
		return version;
	}



	public void setVersion(String version) 
	{
		this.version = version;
	}



	public int getRemainingIdentificationRequests() 
	{
		return remainingIdentificationRequests;
	}



	public void setRemainingIdentificationRequests(int remainingIdentificationRequests) 
	{
		this.remainingIdentificationRequests = remainingIdentificationRequests;
	}



	public String getBestMatch()
	{
		return bestMatch;
	}

	public void setBestMatch(String bestMatch)
	{
		this.bestMatch = bestMatch;
	}

	public List<Resultat> getResults() 
	{
		return results;
	}
	public void setResults(List<Resultat> results) 
	{
		this.results = results;
	}
}
