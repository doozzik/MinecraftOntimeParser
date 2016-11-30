package me.doozzik;

class Player {
    private final String name;
    private String firstSeen;
    private String lastSeen;

    Player(String name){
        this.name = name;
    }

    String getName(){
        return name;
    }

    void setFirstSeen(String firstSeen) {
        this.firstSeen = firstSeen;
    }

    void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    String getFirstSeen() {
        return firstSeen;
    }

    String getLastSeen() {
        return lastSeen;
    }
}
