package todoList;


public class Categorie
{
	protected String nom;

	public Categorie()
	{
    		nom="Sans_Categorie";
  	}
	public Categorie(String s)
	{
		nom=s;
	}

	public String get()
	{
		return nom;
	}
	public void set(String s)
	{
		nom=s;
	}

	public boolean equals(String s){
		return nom.equals(s);
	}
	public boolean equals(Categorie c){
		return this==c;
	}
}
