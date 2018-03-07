package marchMadness.objects;

/*
 * This class is used to represent a team
 *
 * @author kr06pern - Koushik Pernati
 * @author te21wals - Tom Walsh
 */
public class Team {
	
	//Instance variables
	private String name;
	private int ID;
	private int rating;
	
	/*
	 * Constructor for marchMadness.objects.Team class
	 * @param name - The name of the team
	 * @param ID - The ID of the team
	 */
	public Team(String name, int number){
		this.name = name;
		this.ID = number;
		this.rating = 1200;
	}
	
	/*
	 * Gets the name of the team
	 * @return name of the team
	 */
	public String getName(){
		return this.name;
	}
	
	/*
	 * Gets the ID of the team
	 * @return the ID of the team
	 */
	public int getID(){
		return this.ID;
	}
	
	/*
	 * Gets the rating of the team
	 * @return the rating of the team
	 */
	public int getEloRating(){
		return this.rating;
	}
	
	/*
	 * Sets the rating of the team
	 * @param rating - rating of the team to be set
	 */
	public void setEloRating(int rating){
		this.rating = rating;
	}
        @Override 
        public String toString(){
            return("[ "+"Name:"+name+" Number:"+ ID +" ELOrating:"
                    +rating+" ]");
        }
}
