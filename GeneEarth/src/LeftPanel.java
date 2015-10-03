/**
 * Left panel of the whole program : contains ScrollPanel, MovePanel, and PreviewPanel.
 * Works as intermediary between main program and above mentioned panels, calling main program to repaint when needed.
 * Created by user on 2015-07-09.
 */

import javax.swing.*;
import java.awt.*;

public class LeftPanel extends JPanel {

    private static PreviewPanel previewP; // PreviewPanel used to see where the view window is located with respect to earth
    private static ScrollPanel scrollP; // Scroll Panel used to control distance, alpha_view, beta_view, size
    private MovePanel moveP; // Movepanel used to move the view in 4 directions or to default view

    //Constructor
    public LeftPanel() {

        this.setPreferredSize(new Dimension(200, 600));

        //Create and add the elements
        scrollP = new ScrollPanel();
        moveP = new MovePanel();
        previewP = new PreviewPanel();
        this.add(scrollP);
        this.add(moveP);
        this.add(previewP);
    }

    //Called by movePanel to shift the previewPanel
    public static void adjust() {
        previewP.adjust();
        previewP.repaint();
    }

    //Called by movePanel to make the previewPanel go center, as well as change the scroll values to default
    public static void goHome(double dist) {
        previewP.goHome(dist);
        previewP.repaint();
        scrollP.defaultScrollValues();
    }

    //Called by mainPanel to go to the searched gene
    public static void goToGene(double dist) {
        previewP.goHome(dist);
        previewP.repaint();
    }

}
