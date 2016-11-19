public class Player {
    private final String name;
    private int days;
    private String firstSeen;
    private String lastSeen;

    public Player(String name){
        this.name = name;
        days = 1;
    }

    public void addDays(){
        days++;
    }

    public String getName(){
        return name;
    }

    public int getDays(){
        return days;
    }

    public void setFirstSeen(String firstSeen) {
        this.firstSeen = firstSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getFirstSeen() {
        return firstSeen;
    }

    public String getLastSeen() {
        return lastSeen;
    }
}
