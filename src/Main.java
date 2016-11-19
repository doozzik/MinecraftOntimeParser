import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JDialog {
    private JPanel contentPane;
    private JTextField pathTextField;
    private JButton startButton;
    private JLabel statusLabel;
    private JCheckBox createDatabaseCheckBox;
    private JButton showStatisticButton;
    private JTextArea statisticOut;
    private JComboBox reportType;
    private Convert convert;

    public Main() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(startButton);

        reportType.addItem("DailyReport");
        reportType.addItem("WeeklyReport");
        reportType.addItem("MonthlyReport");
        reportType.setSelectedIndex(1);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onConvertToSqlite();
            }
        });

        showStatisticButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onShowStatistic();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onConvertToSqlite() {
        convert = new Convert(pathTextField.getText(), createDatabaseCheckBox, startButton, statusLabel, showStatisticButton, reportType);
    }

    private void onShowStatistic(){
        Statistic statistic = new Statistic(convert.getPlayers(), convert.getDates(), statisticOut);
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        Main dialog = new Main();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
