package hu.safi.friendsfootball.server.jdo;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Participant {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private Key match;

    @Persistent
    private Key player;  
    
    @Persistent
    private Boolean potential;
    
    @Persistent
    private Integer money;    

    @Persistent
    private String right;

    public Participant(Key match, Key player, Boolean potential, Integer money, String right) {
        this.match = match;
        this.player = player;
        this.potential = potential;     
        this.money = money;
        this.right = right;     
    }
    
    public Key getKey() {
        return key;
    }

	public Key getMatch() {
		return match;
	}

	public void setMatch(Key match) {
		this.match = match;
	}

	public Key getPlayer() {
		return player;
	}

	public void setPlayer(Key player) {
		this.player = player;
	}

	public Boolean getPotential() {
		return potential;
	}

	public void setPotential(Boolean potential) {
		this.potential = potential;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public String getRight() {
		return right;
	}

	public void setRight(String right) {
		this.right = right;
	}
    
}
