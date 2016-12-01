package me.doozzik;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

class Graph extends JPanel {
    private int padding = 25;
    private int labelPadding = 25;
    private final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private int min;
    private int max;
    private int size;
    private List<Integer> pNew;
    private List<Integer> pLost;
    private List<Integer> pDifference;
    JFrame frame;

    Graph(int min, int max, int size, List<Integer> pNew, List<Integer> pLost, List<Integer> pDifference) {
        this.setPreferredSize(new Dimension(800, 400));
        frame = new JFrame("blue - new players, red - lost players, yellow - difference, black - zero line");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        this.min = min;
        this.max = max;
        this.size = size;
        this.pNew = pNew;
        this.pLost = pLost;
        this.pDifference = pDifference;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // draw white background
        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLACK);

        drawLinesAndNames(g2);
        drawLine(g2, pNew, new Color(63, 178, 230, 180));
        drawLine(g2, pLost, new Color(230, 21, 23, 180));
        drawLine(g2, pDifference, new Color(229, 230, 35, 180));

        List<Integer> zero = new ArrayList<>();
        for (int i = 0; i < size; i++){
            zero.add(0);
        }
        drawLine(g2, zero, new Color(0, 0, 0, 180));

    }

    private void drawLinesAndNames(Graphics2D g2){
        Color gridColor = new Color(200, 200, 200, 200);
        int pointWidth = 4;
        int numberYDivisions = 10;

        // create hatch marks and grid lines for y axis.
        for (int i = 0; i < numberYDivisions + 1; i++) {
            int x0 = padding + labelPadding;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
            g2.setColor(gridColor);
            g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y0);
            g2.setColor(Color.BLACK);
            String yLabel = ((int) ((min + (max - min) * ((i * 1.0) / numberYDivisions)) * 100)) / 100.0 + "";
            FontMetrics metrics = g2.getFontMetrics();
            int labelWidth = metrics.stringWidth(yLabel);
            g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
        }

        // and for x axis
        for (int i = 0; i < size; i++) {
            if (size > 1) {
                int x0 = i * (getWidth() - padding * 2 - labelPadding) / (size - 1) + padding + labelPadding;
                int y0 = getHeight() - padding - labelPadding;
                if ((i % ((int) ((size / 20.0)) + 1)) == 0) {
                    g2.setColor(gridColor);
                    g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x0, padding);
                    g2.setColor(Color.BLACK);
                    String xLabel = i + "";
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                }
            }
        }
    }

    private void drawLine(Graphics2D g2, List<Integer> p, Color lineColor){
        double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (size - 1);
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (max - min);

        List<Point> graphPoints = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int x1 = (int) (i * xScale + padding + labelPadding);
            int y1 = (int) ((max - p.get(i)) * yScale + padding);
            graphPoints.add(new Point(x1, y1));
        }

        g2.setColor(lineColor);
        g2.setStroke(GRAPH_STROKE); // bold line
        for (int i = 0; i < graphPoints.size() - 1; i++) {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }
    }
}