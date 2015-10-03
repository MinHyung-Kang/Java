/**
 * Panel used to move the viewp
 * User can click in one of the four directions to shift left/right/up/down,
 * or click on home button go default
 * Created by user on 2015-07-09.
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MovePanel extends JPanel {

    private static final int arrowSize = 20; // Size of the arrow
    private final int translateX = 70, translateY = 60; // To set the center and make calculation easier
    private static final int translateHome = arrowSize * 5; // Size of home button
    private GeneralPath upArrow, downArrow, leftArrow, rightArrow, home; //Paths that will describe the butons
    private static int size = 200; // Size of the panel

    //Constructor
    public MovePanel() {
        this.setPreferredSize(new Dimension(size + 10, 120));

        this.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(5, 5, 5, 5),
                new LineBorder(Color.BLACK)));

        addMouseListener(new arrowClicker());
    }

    //Draw the components
    private void draw(Graphics g) {

        //Draw the earth (the sphere)(the smaller version)
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setPaint(Color.BLACK);
        g2d.translate(translateX, translateY);

        //Coordinates that will define each arrow
        int upCoords[][] = {
                {-arrowSize / 2, -arrowSize / 2}, {arrowSize / 2, -arrowSize / 2},
                {arrowSize / 2, -3 * arrowSize / 2}, {arrowSize, -3 * arrowSize / 2},
                {0, -5 * arrowSize / 2}, {-arrowSize, -3 * arrowSize / 2},
                {-arrowSize / 2, -3 * arrowSize / 2}
        };
        int downCoords[][] = {
                {-arrowSize / 2, arrowSize / 2}, {arrowSize / 2, arrowSize / 2},
                {arrowSize / 2, 3 * arrowSize / 2}, {arrowSize, 3 * arrowSize / 2},
                {0, 5 * arrowSize / 2}, {-arrowSize, 3 * arrowSize / 2},
                {-arrowSize / 2, 3 * arrowSize / 2}
        };
        int leftCoords[][] = {
                {-arrowSize, -arrowSize / 2}, {-arrowSize, arrowSize / 2},
                {-2 * arrowSize, arrowSize / 2}, {-2 * arrowSize, arrowSize},
                {-3 * arrowSize, 0}, {-2 * arrowSize, -arrowSize},
                {-2 * arrowSize, -arrowSize / 2}
        };
        int rightCoords[][] = {
                {arrowSize, -arrowSize / 2}, {arrowSize, arrowSize / 2},
                {2 * arrowSize, arrowSize / 2}, {2 * arrowSize, arrowSize},
                {3 * arrowSize, 0}, {2 * arrowSize, -arrowSize},
                {2 * arrowSize, -arrowSize / 2}
        };

        //Draw the arrows
        upArrow = new GeneralPath();
        downArrow = new GeneralPath();
        leftArrow = new GeneralPath();
        rightArrow = new GeneralPath();

        upArrow.moveTo(upCoords[0][0], upCoords[0][1]);
        downArrow.moveTo(downCoords[0][0], downCoords[0][1]);
        leftArrow.moveTo(leftCoords[0][0], leftCoords[0][1]);
        rightArrow.moveTo(rightCoords[0][0], rightCoords[0][1]);

        for (int i = 1; i < upCoords.length; i++) {
            upArrow.lineTo(upCoords[i][0], upCoords[i][1]);
            downArrow.lineTo(downCoords[i][0], downCoords[i][1]);
            leftArrow.lineTo(leftCoords[i][0], leftCoords[i][1]);
            rightArrow.lineTo(rightCoords[i][0], rightCoords[i][1]);
        }
        upArrow.closePath();
        downArrow.closePath();
        leftArrow.closePath();
        rightArrow.closePath();

        g2d.fill(upArrow);
        g2d.fill(downArrow);
        g2d.fill(leftArrow);
        g2d.fill(rightArrow);

        //Define the coordinates for the home
        double homeSize = arrowSize * 0.8;
        double homeCoords[][] = {
                {translateHome - homeSize, -homeSize / 2}, {translateHome - 2 * homeSize, -homeSize / 2},
                {translateHome + 0, -2 * homeSize}, {translateHome + 2 * homeSize, -homeSize / 2},
                {translateHome + homeSize, -homeSize / 2}, {translateHome + homeSize, homeSize},
                {translateHome + homeSize / 5, homeSize}, {translateHome + homeSize / 5, homeSize / 4},
                {translateHome - homeSize / 5, homeSize / 4}, {translateHome - homeSize / 5, homeSize},
                {translateHome - homeSize, homeSize}
        };

        //Draw the home button
        home = new GeneralPath();
        home.moveTo(homeCoords[0][0], homeCoords[0][1]);
        for (int i = 1; i < homeCoords.length; i++) {
            home.lineTo(homeCoords[i][0], homeCoords[i][1]);
        }
        home.closePath();
        g2d.fill(home);
        g2d.setFont(new Font("Serif", Font.BOLD, (int) (homeSize * 4.0 / 5.0)));
        g2d.drawString("HOME", (int) (translateHome - homeSize), (int) (2 * homeSize));

        g2d.dispose();
    }


    //Defines what will be done when arrows are clicked
    class arrowClicker extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {

            int x = e.getX() - translateX;
            int y = e.getY() - translateY;

            double mainMove = 20;

            //Change the mainPanel accordingly
            if (upArrow.contains(x, y)) {
                Main.move(mainMove, 0);
                LeftPanel.adjust();
            }

            if (downArrow.contains(x, y)) {
                Main.move(-mainMove, 0);
                LeftPanel.adjust();
            }

            if (leftArrow.contains(x, y)) {
                Main.move(0, mainMove);
                LeftPanel.adjust();
            }

            if (rightArrow.contains(x, y)) {
                Main.move(0, -mainMove);
                LeftPanel.adjust();
            }

            if (home.contains(x, y)) {
                LeftPanel.goHome(1);
                Main.goHome();
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
}
