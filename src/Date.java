class Date {
    private String date;
    private int pNew;
    private int pLost;

    Date(String date){
        this.date = date;
    }

    void addNew(){
        pNew++;
    }

    void addLost(){
        pLost++;
    }

    String getDate() {
        return date;
    }

    int getpNew() {
        return pNew;
    }

    int getpLost() {
        return pLost;
    }
}
