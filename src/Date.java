public class Date {
    private String date;
    private int pNew;
    private int pLost;
    private int pOld;
    private int pTotal;

    public Date(String date){
        this.date = date;
    }

    public void addNew(){
        pNew++;
    }

    public void addLost(){
        pLost++;
    }

    public void addOld(){
        pOld++;
    }

    public void addTotal(){
        pTotal++;
    }

    public String getDate() {
        return date;
    }

    public int getpNew() {
        return pNew;
    }

    public int getpLost() {
        return pLost;
    }
}
