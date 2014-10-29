package hu.safi.friendsfootball.server.jdo;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Match {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private Key place;

    @Persistent
    private String day;

    @Persistent
    private String startTime;
    
    @Persistent
    private Integer money;    
   
    public Match(Key place, String day, String startTime, Integer money) {
        this.place = place;
        this.day = day;
        this.startTime = startTime;     
        this.money = money;     
    }

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Key getPlace() {
		return place;
	}

	public void setPlace(Key place) {
		this.place = place;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}
}
