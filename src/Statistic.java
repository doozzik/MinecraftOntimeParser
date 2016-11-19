import javax.swing.*;
import java.util.List;

public class Statistic {
    public Statistic(List<Player> players, List<Date> dates, JTextArea statisticOut){
        // new lost old total

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
}
