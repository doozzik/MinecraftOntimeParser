package me.doozzik;

import javax.swing.*;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class Convert implements Runnable {
    private String path;
    private JLabel statusLabel;
    private JCheckBox databaseCheckBox;
    private JButton startButton;
    private List<Player> players;
    private List<Date> dates;
    private JComboBox reportType;
    private JTextArea statisticOut;

    Convert(String path, JCheckBox databaseCheckBox, JButton startButton, JLabel statusLabel, JComboBox reportType, JTextArea statisticOut) {
        this.path = path;
        this.statusLabel = statusLabel;
        this.databaseCheckBox = databaseCheckBox;
        this.startButton = startButton;
        this.players = new ArrayList<>();
        this.dates = new ArrayList<>();
        this.reportType = reportType;
        this.statisticOut = statisticOut;
        Thread t = new Thread(this);
        t.start();
    }

    private List<String> listFiles(String path){
        List<String> results = new ArrayList<>();
        File[] files = new File(path).listFiles();
        if (files == null){
            statusLabel.setText("Error: Directory not exist or is empty.");
        }
        for (File file : files) {
            if (file.isFile()) {
                if (file.getName().contains(reportType.getSelectedItem().toString())){
                    results.add(file.getAbsolutePath());
                    dates.add(new Date(file.getName().substring(0, 10)));
                }
            }
        }
        return results;
    }

    private Player convertToPlayer(String line){
        if (line.charAt(0) == '#'){
            line = line.trim();
            int index1 = line.indexOf(" ");
            line = line.substring(index1);
            line = line.trim();
            int index2 = line.indexOf(" ");
            String pName = line.substring(0, index2).toLowerCase();
            //line = line.substring(index2);
            //line = line.trim();
            return new Player(pName);
        }
        return null;
    }

    private String filePathToTime(String path){
        path = path.substring(path.lastIndexOf('\\') + 1);
        path = path.substring(0, path.indexOf(' '));
        return path;
    }

    private void controlsToggle(boolean value){
        //databaseCheckBox.setEnabled(value);
        startButton.setEnabled(value);
        reportType.setEnabled(value);
    }

    private void statistic(){
        for (Player p : players){
            for (Date d : dates){
                if (d.getDate().equals(p.getFirstSeen())){
                    d.addNew();
                }
                if (d.getDate().equals(p.getLastSeen())){
                    d.addLost();
                }
            }
        }

        String text = "Date / new players / lost players / difference\n";
        for (Date d : dates){
            text += d.getDate() + " / " + d.getpNew() + " / " + d.getpLost() + " / " + (d.getpNew() - d.getpLost()) + "\n";
        }
        statisticOut.setText(text);
    }

    @Override
    public void run() {
        List<String> files = listFiles(path);
        DataBaseManager dbManager = new DataBaseManager(databaseCheckBox.isSelected());
        controlsToggle(false);

        ArrayList<ArrayList<Player>> allPlayers = new ArrayList<>();

        String alphabet = "abcdefghijklmnopqrstuvwxyz1234567890-=[];',./!@#$%^&*()_+{}:\"/<>?`~";
        for (int i = 0; i < alphabet.length(); i++){
            allPlayers.add(new ArrayList<>());
        }

        try{
            for (String file : files){
                statusLabel.setText("Parsing file " + files.indexOf(file) + " / " + files.size());
                String line;
                BufferedReader in;

                in = new BufferedReader(new FileReader(file));
                line = in.readLine();

                while(line != null)
                {
                    Player p = convertToPlayer(line);
                    if (p != null){
                        findPlayer(p, file, allPlayers.get(alphabet.indexOf(p.getName().charAt(0))));
                    }
                    line = in.readLine();
                }
            }

            for (List<Player> list : allPlayers){
                players.addAll(list);
            }

            if (databaseCheckBox.isSelected()){
                for (Player p : players){
                    statusLabel.setText("Adding to base player " + players.indexOf(p) + " / " + players.size());
                    dbManager.addToBase(p);
                }
            }
        } catch (IOException | ClassNotFoundException | SQLException e) {
            if (!e.getMessage().contains("constraint failed")){
                statusLabel.setText(e.getMessage());
                e.printStackTrace();
            }
        }

        controlsToggle(true);
        statusLabel.setText("Job done.");
        statistic();
    }

    private void findPlayer(Player p, String file, List<Player> players){
        boolean exist = false;
        for (Player pl : players){
            if (pl.getName().equals(p.getName())){
                if (pl.getFirstSeen() == null){
                    pl.setFirstSeen(filePathToTime(file));
                }
                pl.setLastSeen(filePathToTime(file));
                exist = true;
            }
        }
        if (!exist){
            p.setFirstSeen(filePathToTime(file));
            p.setLastSeen(filePathToTime(file));
            players.add(p);
        }
    }
}
