import javax.swing.*;
import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Convert implements Runnable {
    private String path;
    private JLabel statusLabel;
    private JCheckBox databaseCheckBox;
    private JButton startButton;
    private List<Player> players;
    private JButton showStatisticButton;
    private List<Date> dates;
    private JComboBox reportType;

    public Convert(String path, JCheckBox databaseCheckBox, JButton startButton, JLabel statusLabel, JButton showStatisticButton, JComboBox reportType) {
        this.path = path;
        this.statusLabel = statusLabel;
        this.databaseCheckBox = databaseCheckBox;
        this.startButton = startButton;
        this.players = new ArrayList<>();
        this.showStatisticButton = showStatisticButton;
        this.dates = new ArrayList<>();
        this.reportType = reportType;
        Thread t = new Thread(this);
        t.start();
    }

    private List<String> listFiles(String path){
        List<String> results = new ArrayList<String>();
        File[] files = new File(path).listFiles();
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
            String pName = line.substring(0, index2);
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
        showStatisticButton.setEnabled(value);
        reportType.setEnabled(value);
    }

    public List<Player> getPlayers(){
        return players;
    }

    public List<Date> getDates(){
        return dates;
    }

    @Override
    public void run() {
        List<String> files = listFiles(path);
        DataBaseManager dbmanager = new DataBaseManager(databaseCheckBox.isSelected());
        controlsToggle(false);

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
                        boolean exist = false;
                        for (Player pl : players){
                            if (pl.getName().equals(p.getName())){
                                pl.addDays();
                                if (pl.getFirstSeen() == null){
                                    pl.setFirstSeen(filePathToTime(file));
                                }
                                pl.setLastSeen(filePathToTime(file));
                                exist = true;
                                break;
                            }
                        }
                        if (!exist){
                            p.setFirstSeen(filePathToTime(file));
                            p.setLastSeen(filePathToTime(file));
                            players.add(p);
                        }
                    }
                    line = in.readLine();
                }
            }

            if (databaseCheckBox.isSelected()){
                for (Player p : players){
                    statusLabel.setText("Adding to base player " + players.indexOf(p) + " / " + players.size());
                    dbmanager.addToBase(p);
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
    }
}
