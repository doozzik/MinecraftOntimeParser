package me.doozzik;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends JDialog {
    private JPanel contentPane;
    private JTextField pathTextField;
    private JButton startButton;
    private JLabel statusLabel;
    private JCheckBox createDatabaseCheckBox;
    private JTextArea statisticOut;
    private JComboBox reportType;
    private JButton showGraphButton;
    private Convert convert;
    private Graph g;

    private class DummyFrame extends JFrame {
        DummyFrame(String title) {
            super(title);
            setUndecorated(true);
            setVisible(true);
        }
    }

    public Main() {
        new DummyFrame("Minecraft OnTime parser");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(startButton);

        reportType.addItem("DailyReport");
        reportType.addItem("WeeklyReport");
        reportType.addItem("MonthlyReport");
        reportType.setSelectedIndex(1);
        showGraphButton.setEnabled(false);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onConvertToSqlite();
            }
        });
        showGraphButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graph();
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

    private void graph(){
        if (g == null){
            showGraphButton.setText("Hide graph");
            int max = Integer.MIN_VALUE;
            int min = Integer.MAX_VALUE;
            for (Date date : convert.dates){
                if (date.getpNew() > max){
                    max = date.getpNew();
                }
                if (date.getpLost() > max){
                    max = date.getpLost();
                }
                if (date.getpNew() - date.getpLost() < min){
                    min = date.getpNew() - date.getpLost();
                }
            }
            max = max + max/100*20;
            min = min - max/100*20;

            List<Integer> pNew = new ArrayList<>();
            for (Date date : convert.dates){
                pNew.add(date.getpNew());
            }

            List<Integer> pLost = new ArrayList<>();
            for (Date date : convert.dates){
                pLost.add(date.getpLost());
            }

            List<Integer> pDifference = new ArrayList<>();
            for (Date date : convert.dates){
                pDifference.add(date.getpNew() - date.getpLost());
            }
            g = new Graph(min, max, convert.dates.size(), pNew, pLost, pDifference);
        }else{
            showGraphButton.setText("Show graph");
            g.frame.dispose();
            g = null;
        }
    }

    private void onConvertToSqlite() {
        convert = new Convert(pathTextField.getText(), createDatabaseCheckBox, startButton, statusLabel, reportType, statisticOut, showGraphButton);
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
