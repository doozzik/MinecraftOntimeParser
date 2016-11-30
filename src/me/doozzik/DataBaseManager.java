package me.doozzik;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class DataBaseManager {
    private String baseName;
    private String baseTableName;

    DataBaseManager(boolean addToBase){
        baseName = "ontime.db";
        baseTableName = "PLAYERS";

        if (addToBase){
            try {
                createBase();
                createTable();
            } catch (SQLException | ClassNotFoundException e) {
                if (!e.getMessage().contains("already exists")){
                    e.printStackTrace();
                }
            }
        }
    }

    private void createBase() throws SQLException, ClassNotFoundException {
        java.sql.Connection con = null;
        Class.forName("org.sqlite.JDBC");
        con = DriverManager.getConnection("jdbc:sqlite:" + baseName);
        con.close();
    }

    private void createTable() throws ClassNotFoundException, SQLException {
        java.sql.Connection con = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");
        con = DriverManager.getConnection("jdbc:sqlite:" + baseName);

        stmt = con.createStatement();
        String sql = "CREATE TABLE " + baseTableName +
                " (NAME TEXT PRIMARY KEY     NOT NULL," +
                " FIRSTSEEN         TEXT, " +
                " LASTSEEN         TEXT, " +
                " DAYS         INTEGER)";
        stmt.executeUpdate(sql);

        stmt.close();
        con.close();
    }

    void addToBase(Player p) throws ClassNotFoundException, SQLException {
        java.sql.Connection con = null;
        Statement stmt = null;

        Class.forName("org.sqlite.JDBC");con = DriverManager.getConnection("jdbc:sqlite:" + baseName);
        con.setAutoCommit(false);

        stmt = con.createStatement(); // replace("\"", "\"\"")
        String sql = "INSERT INTO " + baseTableName + " (NAME,FIRSTSEEN,LASTSEEN) " +
                "VALUES ('" + p.getName() + "','" + p.getFirstSeen() + "','" + p.getLastSeen() + "');";
        try{
            stmt.executeUpdate(sql);
        }finally{
            stmt.close();
            con.commit();
            con.close();
        }
    }
}
