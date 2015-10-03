/**
 * ScrollPanel used to control values of alpha_view, beta_view, dist, and size
 * User can use these scrolls to change the values, and the scrolls are automatically adjusted as user
 * moves around in MainPanel
 * Created by user on 2015-07-09.
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class ScrollPanel extends JPanel {

    private JLabel distLabel, alphaLabel, betaLabel, sizeLabel; // Description for each scrolls
    private JLabel distValue, alphaValue, betaValue, sizeValue; // Value of each scroll
    private static JScrollBar distScroll, alphaScroll, betaScroll, sizeScroll; // The scrollbars
    private final int dimension_Width = 100; // The preferred width of the scroll
    private final int dimension_Height = 20;// The preferred height of the scroll
    private static final int distMin = 10, distMax = 300; // The limits of the dist variable

    //The default (starting) values of dist, alpha, beta, size, respectively
    private static int distPreset = 100;
    private static int alphaPreset = 180;
    private static int betaPreset = 180;
    private static int sizePreset = 5;

    private Font titleFont, valueFont;


    //Constructor
    public ScrollPanel() {

        //Set up the panel
        this.setPreferredSize(new Dimension(210, 200));
        this.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(5, 5, 5, 5),
                new LineBorder(Color.BLACK)));

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        //Create the Labels
        titleFont = new Font("Serif", Font.ITALIC, 15);
        valueFont = new Font("Serif", Font.BOLD, 13);

        addLabels();

        //Create the value labels
        createValueLabels();

        addScrollBars();


        distValue.setSize(new Dimension(dimension_Width / 4, dimension_Height));


        //Start drawing the components

        //Draw the label, value, scroll for dist
        drawdistComponents(c);

        //Draw the label, value, scroll for alpha_view
        drawalphaComponents(c);

        //Draw the label, value, scroll for beta_view
        drawbetaComponents(c);

        //Draw the label, value, scroll for size
        drawsizeComponents(c);
    }

    private void drawsizeComponents(GridBagConstraints c) {
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 1;
        this.add(sizeLabel, c);

        c.gridx = 1;
        this.add(sizeValue, c);

        c.gridy = 7;
        c.gridx = 0;
        c.gridwidth = 2;
        this.add(sizeScroll, c);
    }

    private void drawbetaComponents(GridBagConstraints c) {
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 1;
        this.add(betaLabel, c);

        c.gridx = 1;
        this.add(betaValue, c);

        c.gridy = 5;
        c.gridx = 0;
        c.gridwidth = 2;
        this.add(betaScroll, c);
    }

    private void drawalphaComponents(GridBagConstraints c) {
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        this.add(alphaLabel, c);

        c.gridx = 1;
        this.add(alphaValue, c);

        c.gridy = 3;
        c.gridx = 0;
        c.gridwidth = 2;
        this.add(alphaScroll, c);
    }

    private void drawdistComponents(GridBagConstraints c) {
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        this.add(distLabel, c);

        c.gridx = 1;
        this.add(distValue, c);

        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = 2;
        this.add(distScroll, c);
    }

    private void addScrollBars() {
        //Scrollbar that controls the distance
        adddistScroll();

        //Scrollbar that controls the alpha value
        addalphaScroll();

        //Scrollbar that controls the beta value
        addbetaScroll();

        //Scrollbar that controls the size
        addsizeScroll();
    }

    private void addsizeScroll() {
        sizeScroll = new JScrollBar(JScrollBar.HORIZONTAL, sizePreset, 1, 1, 10);
        sizeScroll.addAdjustmentListener(new ScrollbarListener(3));
    }

    private void addbetaScroll() {
        betaScroll = new JScrollBar(JScrollBar.HORIZONTAL, betaPreset, 1, 0, 360);
        betaScroll.addAdjustmentListener(new ScrollbarListener(2));
    }

    private void addalphaScroll() {
        alphaScroll = new JScrollBar(JScrollBar.HORIZONTAL, alphaPreset, 1, 0, 360);
        alphaScroll.addAdjustmentListener(new ScrollbarListener(1));
    }

    private void adddistScroll() {
        distScroll = new JScrollBar(JScrollBar.HORIZONTAL, distPreset, 1, distMin, distMax);
        distScroll.setPreferredSize(new Dimension(dimension_Width, dimension_Height));
        distScroll.addAdjustmentListener(new ScrollbarListener(0));
    }

    private void createValueLabels() {
        distValue = new JLabel(distPreset / 100.0 + " x  ");
        distValue.setFont(valueFont);
        distValue.setForeground(Color.BLACK);
        distValue.setHorizontalAlignment(SwingConstants.RIGHT);
        alphaValue = new JLabel(alphaPreset + "  ");
        alphaValue.setFont(valueFont);
        alphaValue.setForeground(Color.BLACK);
        alphaValue.setHorizontalAlignment(SwingConstants.RIGHT);
        betaValue = new JLabel(betaPreset + "  ");
        betaValue.setFont(valueFont);
        betaValue.setForeground(Color.BLACK);
        betaValue.setHorizontalAlignment(SwingConstants.RIGHT);
        sizeValue = new JLabel(sizePreset + "  ");
        sizeValue.setFont(valueFont);
        sizeValue.setForeground(Color.BLACK);
        sizeValue.setHorizontalAlignment(SwingConstants.RIGHT);
    }

    private void addLabels() {
        distLabel = new JLabel("  Distance");
        distLabel.setFont(titleFont);
        distLabel.setForeground(Color.BLACK);
        alphaLabel = new JLabel("  Alpha View");
        alphaLabel.setFont(titleFont);
        alphaLabel.setForeground(Color.BLACK);
        betaLabel = new JLabel("  Beta View");
        betaLabel.setFont(titleFont);
        betaLabel.setForeground(Color.BLACK);
        sizeLabel = new JLabel("  Size");
        sizeLabel.setFont(titleFont);
        sizeLabel.setForeground(Color.BLACK);
    }

    //Set the scrollBar
    public static void setDistScroll(double val) {
        distScroll.setValue((int) (val * 100));
    }

    public static int getDistMin() {
        return distMin;
    }//Return the minimum distance defined

    public static int getDistMax() {
        return distMax;
    }//Return the maximum distance defined


    //Receive values of new alpha and beta, convert them to degrees, and change
    //the scroll accordingly
    public static void setAlphaBetaScrolls(double alpha, double beta) {

        //Normalize to make it positive
        alpha = (alpha >= 0) ? alpha : (2 * Math.PI + alpha);
        beta = (beta >= 0) ? beta : (2 * Math.PI + beta);

        //Conversion to degrees
        int alpha_new = (int) (alpha * 180.0 / Math.PI) % 360;
        int beta_new = (int) (beta * 180.0 / Math.PI) % 360;

        alphaScroll.setValue(alpha_new);
        betaScroll.setValue(beta_new);
    }

    //Set the values of the scroll bars
    public static void defaultScrollValues() {
        distScroll.setValue(distPreset);
        alphaScroll.setValue(alphaPreset);
        betaScroll.setValue(betaPreset);
        sizeScroll.setValue(sizePreset);
    }

    class ScrollbarListener implements AdjustmentListener {
        private int action;

        public ScrollbarListener(int i) {
            action = i;
        }

        //Displays the value changed by the scrollbar
        public void adjustmentValueChanged(AdjustmentEvent event) {

            switch (action) {
                case 0: //if distvalue is changed
                    double distVal = event.getValue() / 100.0;
                    distValue.setText(distVal + " x  ");
                    Main.changeDist(distVal);
                    LeftPanel.adjust();
                    break;
                case 1: //if alpha_view value is changed
                    int alphaView = event.getValue();
                    alphaValue.setText(alphaView + "  ");
                    Main.changeAlpha(alphaView);
                    break;
                case 2: //if beta_view value is changed
                    int betaView = event.getValue();
                    betaValue.setText(betaView + "  ");
                    Main.changeBeta(betaView);
                    break;
                case 3: //if size value is changed
                    int size = event.getValue();
                    sizeValue.setText(size + "  ");
                    Main.changeSize(size);
                default:
            }
        }
    }
}
