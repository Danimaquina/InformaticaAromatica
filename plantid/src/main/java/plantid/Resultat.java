package plantid;

import java.util.List;

public class Resultat 
{
	private Double score;
	private Especie species;
	private List<Images> images;
	
	public Resultat() 
	{

	}
	
	public Double getScore() 
	{
		return score;
	}
	
	public void setScore(Double score) 
	{
		this.score = score;
	}
	
	public Especie getSpecies() 
	{
		return species;
	}
	
	public void setSpecies(Especie species) 
	{
		this.species = species;
	}

	public List<Images> getImages() 
	{
		return images;
	}

	public void setImages(List<Images> images) 
	{
		this.images = images;
	}
}

