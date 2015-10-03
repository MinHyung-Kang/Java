/**
 * Panel that shows where the current view is with respect to the whole earth
 * Created by user on 2015-07-09.
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class PreviewPanel extends JPanel {

    private static int size = 95; // size of the panel
    private static final double rectRatio = 1.20; // original ratio between the earth and the panel

    // size of the rectangle that will describe the current view
    private static double rectSize = 300.0 * size / (rectRatio * 250.0);
    private static int TRANSLATEX_INITIAL = size + 5, TRANSLATEY_INITIAL = size + 5; // Center of the panel
    private static int translateX = 0, translateY = 0; // How much the view has moved

    //ratio between the mainPanel's graphics and previewpanel's
    private static double ratio = (double) size / (rectRatio * 300.0);

    //Constructor
    public PreviewPanel() {
        this.setPreferredSize(new Dimension(2 * size + 10, 2 * size + 10));
        this.setBorder(new LineBorder(Color.BLACK));
    }

    //Draw the components
    private void draw(Graphics g) {


        //Draw the boundary rectangle
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.fillRect(5, 5, 2 * size, 2 * size);

        //Draw the earth (the sphere)(the smaller version)
        g2d.setPaint(Color.BLUE);
        g2d.translate(TRANSLATEX_INITIAL, TRANSLATEY_INITIAL);
        g2d.fillOval(-(int) (size / rectRatio), -(int) (size / rectRatio), (int) (2 * size / rectRatio), (int) (2 * size / rectRatio));
        g2d.setPaint(Color.YELLOW);
        g2d.drawOval(-(int) (size / rectRatio), -(int) (size / rectRatio), (int) (2 * size / rectRatio), (int) (2 * size / rectRatio));

        //Defines dashed lines
        float[] dash = {4f, 0f, 2f};
        BasicStroke dashStroke = new BasicStroke(1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND, 1.0f, dash, 2f);

        g2d.translate(translateX, translateY);

        g2d.setPaint(Color.RED);
        g2d.setStroke(dashStroke);

        //Draw the rectangle view
        g2d.drawRect(-(int) (rectSize), -(int) (rectSize),
                (int) (2 * rectSize), (int) (2 * rectSize));

        g2d.dispose();
    }

    //Adjust the values to fit the preview panel to the mainpanel
    public static void adjust() {
        ratio = (double) size / rectRatio * MainPanel.getDist() / MainPanel.getMultiplier();
        translateX = (int) (ratio * (MainPanel.getTranslateXInitial() - MainPanel.getTranslateX()));
        translateY = (int) (ratio * (MainPanel.getTranslateYInitial() - MainPanel.getTranslateY()));
        rectSize = MainPanel.getTranslateXInitial() * ratio;
    }

    //Focus goes back home
    public static void goHome(double dist) {
        translateX = 0;
        translateY = 0;
        ratio = (double) size / (rectRatio * MainPanel.getTranslateXInitial());
        rectSize = rectRatio * size;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
}
