package rdxcabs.com.rdxcabs;

/**
 * Created by arung on 19/4/16.
 */
public class TripsBean {

    private String username;

    private String sourceLoc;

    private String destLoc;

    private String date;

    private String time;

    private String cabType;

    private String fare;

    private String comments;

    public TripsBean(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSourceLoc() {
        return sourceLoc;
    }

    public void setSourceLoc(String sourceLoc) {
        this.sourceLoc = sourceLoc;
    }

    public String getDestLoc() {
        return destLoc;
    }

    public void setDestLoc(String destLoc) {
        this.destLoc = destLoc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCabType() {
        return cabType;
    }

    public void setCabType(String cabType) {
        this.cabType = cabType;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
