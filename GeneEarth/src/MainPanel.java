

/**
 * Main Program that deals with the visualization.
 * All the other panels are in the end connected to this panel : changing values in other panels
 * afftect this panel, hence causing change in the view.
 */

import javax.swing.*;
import java.awt.Graphics;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.awt.geom.GeneralPath;

class MainPanel extends JPanel {


    //Words in Brackets indicate the variable name in R code
    private static String[] names; // Names of the genes [namgene]
    private static ArrayList<ArrayList<Double>> cor; // Correlation data [R]
    private static ArrayList<Double> gr; // Gene Rank Data [GR]
    private static ArrayList<Coordinate> coords_3d; // Coordinates of the genes in 3d [x,y,z]
    private static ArrayList<Coordinate> coords_2d; // Coordinates of the genes in 2d [xy]
    private static ArrayList<Double> cond; // The conditional values that determine if genes are visible from the viewing angle [cond]
    private static ArrayList<Double> filterCond; // Represents if the genes should be filtered or not
    private ArrayList<Ellipse2D> points; // arraylist of points to recognize the click
    private ArrayList<LineStruct> lines, permanentLines; // List of correlation lines and permanent correlation lines
    private ArrayList<Double> ch; // Used to draw correlation lines [ch]
    private ArrayList<Coordinate> cor_line; // A Correlation line[xyc]

    private GeneralPath currentLine; // Represents current line

    private static double dist = 1; // Distance from the view to the earth (higher = farther) [dist]
    private static double alpha_view = 180.0 / 180.0 * Math.PI; // Angle with respect to x-axis [alpha.view]
    private static double beta_view = 180.0 / 180.0 * Math.PI; // Angle with respect to z-axis[beta.view]
    private static double thresh = 1 - Math.exp(-dist / 10); // Threshold that represents if correlation should be drawn or not [r2.thresh]
    //4 Values that will be used to filter the correlations
    private static double r_thresh1_low = -1, r_thresh1_high = 1, r_thresh2_low = -1, r_thresh2_high = 1;
    //Threshold of GR that represents if a gene should be drawn or not
    private static double gr_thresh = 1 - Math.exp(-dist / 10); // [r2.thresh]

    private static double x_view, y_view, z_view; // Three views
    private static double GR_high = 1, GR_low = 0; // 2 Values that will be used to filter GR

    private static int TRANSLATEX_INITIAL = 300, TRANSLATEY_INITIAL = 300;
    private static int translateX = TRANSLATEX_INITIAL;
    private static int translateY = TRANSLATEY_INITIAL;
    private static int n; // Number of genes
    private static int gr_option = 1; // (0) by specified boundary (1) by dist (2) show all
    private static int r_option = -1; // Neither(Default) = -1 ; AND = 0 ;  OR = 1
    private int m = 100; // Number of points used to draw a correlation line
    private int radius; // The radius of the points; also used to determine size of the letters
    private static final int MULTIPLIER = 250; // Multiplier used to fit the size of the screen
    private int radius_default = 5; // The starting (default) size of radius
    private int fromIndex = -1, toIndex = -1; //keeps track of the correlation line of interest
    private PopUpWindow popWindow; // Popup window to tell the user info about the correlation line


    //Get the click interval to calculate the time to determine double click
    private static final int CLICK_INTERVAL = (Integer) Toolkit.getDefaultToolkit().
            getDesktopProperty("awt.multiClickInterval");

    //Private class to save the correlation lines in
    private class LineStruct {
        private int fromIndex, toIndex; // from which gene to which gene is correlation line connected
        private GeneralPath path; // The path of the correlation line

        //constructor for private class
        public LineStruct(GeneralPath p, int i, int j) {
            this.fromIndex = i;
            this.toIndex = j;
            this.path = p;
        }

        //Accessor methods
        public GeneralPath getLine() {
            return path;
        }

        public int getFromIndex() {
            return fromIndex;
        }

        public int getToIndex() {
            return toIndex;
        }
    }

    //Popupwindow used to display information regarding correlation between two genes
    private class PopUpWindow extends JFrame {

        private JLabel toLabel, rLabel; //

        //Constructor
        public PopUpWindow() {

            //Sets up the components
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setUndecorated(true);
            toLabel = new JLabel("Information");
            rLabel = new JLabel("Information2");
            toLabel.setPreferredSize(new Dimension(100, 20));
            rLabel.setPreferredSize(new Dimension(100, 20));
            this.setPreferredSize(new Dimension(100, 40));
            Box b1 = Box.createVerticalBox();
            b1.add(toLabel);
            b1.add(rLabel);
            this.add(b1);
            this.pack();
        }

        //Update the labels as needed, and make the window appear
        public void set(int x, int y, String s1, String s2) {
            this.setLocation(x, y);
            toLabel.setText(s1);
            rLabel.setText(s2);
            this.setVisible(true);
        }

        // Make the window disappear
        public void clear() {
            this.setVisible(false);
        }
    }

    //Constructor : reads in the data
    public MainPanel(File RFile, File GRFile, File angleFile) {
        //Read R data
        try {
            readR(RFile);//Read R data
            readGR(GRFile);//Read GR data
            readAngles(angleFile);//Read angle data
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        computeViewAngles(); // computes x_view, y_view, z_view
        compute2dCoords(); // compute 2d coordinates

        //Generate the ch arrayList
        ch = new ArrayList<Double>();
        for (int i = 0; i < m; i++) {
            ch.add(i / (m - 1.0));
        }

        MovingAdapter ma = new MovingAdapter();

        //Add mouselisteners
        addMouseListener(ma);
        addMouseMotionListener(ma);
        addMouseWheelListener(new ScaleAdapter());

        //Create a new popup window
        popWindow = new PopUpWindow();

        //New ArrayList to keep track of the permanent lines
        permanentLines = new ArrayList<>();

    }


    //Projects a 3D point (x,y,z) to a point on the plane from the camera angle
    private static Coordinate td(double alpha_view, double beta_view, Coordinate point) {

        double x = point.getX() * Math.cos(alpha_view) * Math.sin(beta_view) + point.getY() * Math.sin(alpha_view) * Math.sin(beta_view) - point.getZ() * Math.cos(beta_view);
        double y = point.getX() * Math.sin(alpha_view) - point.getY() * Math.cos(alpha_view);
        return new Coordinate(x, y);
    }

    //Read a GR file
    private void readGR(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        gr = new ArrayList<Double>();

        scanner.nextLine();//Skip the header

        //Add the GR values to the arraylist
        while (scanner.hasNext()) {
            gr.add(Double.valueOf(scanner.next()));
        }
        scanner.close();
    }

    //Read r data and save them to arraylist
    private void readR(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        scanner.useDelimiter(",");

        //Get the names
        if (scanner.hasNextLine()) {
            names = scanner.nextLine().split(",");
        } else {
            JOptionPane.showMessageDialog(null, "File Empty");
        }

        n = names.length;

        //Take out all the double quotes
        for (int i = 0; i < n; i++) {
            names[i] = names[i].replaceAll("\"", "");
        }

        cor = new ArrayList<>(n);

        //Save the rest of the data to the arraylist
        while (scanner.hasNextLine()) {
            String[] temp = scanner.nextLine().split(",");
            ArrayList<Double> tempList = new ArrayList<Double>(n);
            for (String tempElem : temp) {
                tempList.add(Double.valueOf(tempElem));
            }
            cor.add(tempList);
        }
    }

    //Read the angle data and add onto the list
    private void readAngles(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        scanner.useDelimiter(",");
        coords_3d = new ArrayList<>();

        scanner.nextLine();//Skip the header

        //For each set of angles, compute the 3d coordinate
        while (scanner.hasNextLine()) {
            String[] tempLine = scanner.nextLine().split(",");

            //Read in degrees : use this instead if the input is in degrees
            /*double alpha = Double.valueOf(tempLine[0]) /180.0 * Math.PI;
            double beta = Double.valueOf(tempLine[1]) /180.0 * Math.PI;*/

            //Read in radians
            double alpha = Double.valueOf(tempLine[0]);
            double beta = Double.valueOf(tempLine[1]);

            Coordinate temp = new Coordinate(Math.cos(alpha) * Math.cos(beta), Math.sin(alpha) * Math.cos(beta), Math.sin(beta));
            coords_3d.add(temp);
        }
    }

    //Computes the viewing angles
    private static void computeViewAngles() {
        x_view = Math.cos(alpha_view) * Math.cos(beta_view);
        y_view = Math.sin(alpha_view) * Math.cos(beta_view);
        z_view = Math.sin(beta_view);
    }

    //compute 2d coordinates from given 3d coordinates
    private static void compute2dCoords() {
        coords_2d = new ArrayList<>();
        cond = new ArrayList<>();
        filterCond = new ArrayList<>();
        //Compute the condition and 2-d projection for each coordinate
        for (Coordinate point : coords_3d) {

            //Project the 3d coordinate to 2d and add them to arrayList
            coords_2d.add(td(alpha_view, beta_view, point));

            //If we don't care about condition values
            if (gr_option == 2 || gr_option == 0) {
                cond.add(1.0);
            } else {
                double condVal = x_view * point.getX() + y_view * point.getY() + z_view * point.getZ();
                cond.add(condVal);
            }
        }
        //Check GR to determine whether or not to display the gene
        evaluateGRBoundary();
    }

    //Determines if the gene is visible from current viewing angle
    public static boolean isVisible(double x, double y, double z) {
        return (x_view * x + y_view * y + z_view * z > 0);
    }

    //Draw the components
    private void draw(Graphics g) {

        //Keeps track of the time it took to draw each component
        /*
        long startTime,endTime;
        startTime = System.currentTimeMillis();
        System.out.println("Started at : " + startTime);*/

        //Set up the stroke type
        this.setBackground(Color.BLACK);
        Graphics2D g2d = (Graphics2D) g.create();
        Stroke baseStroke = g2d.getStroke();
        BasicStroke thickStroke = new BasicStroke(Math.round(radius / (10 * dist)), BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_BEVEL);
        g2d.translate(translateX, translateY);

        //Draw the Earth
        g2d.setPaint(Color.BLUE);
        g2d.fillOval((int) (-MULTIPLIER / dist), (int) (-MULTIPLIER / dist), (int) (2 * MULTIPLIER / dist), (int) (2 * MULTIPLIER / dist));
        g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        g2d.setPaint(Color.YELLOW);
        g2d.drawOval((int) (-MULTIPLIER / dist), (int) (-MULTIPLIER / dist), (int) (2 * MULTIPLIER / dist), (int) (2 * MULTIPLIER / dist));

        /*endTime = System.currentTimeMillis();
        System.out.println("Drawing Earth took : " + (endTime - startTime));
        startTime = endTime;*/

        //Set up for drawing the coordinates
        g2d.setStroke(baseStroke);
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Serif", Font.PLAIN, (int) (10.0 / dist)));

        //Draw the coordinates (set variables)
        radius = (int) Math.round(radius_default / dist);
        points = new ArrayList<>(coords_2d.size());
        lines = new ArrayList<>(coords_2d.size());
        double magf = 3 * Math.exp(-dist);
        double xGR, yGR, zGR; // Coordinate for endpoint of GR bar

        int currentIndex = ListPanel.getValIndex();

        //Draw the coordinates
        for (int k = 0; k < coords_2d.size(); k++) {
            //Check if (A point was clicked and is visible from current viewing angle)
            //    OR (Visible from current viewing angle, if it was filtered out, and if GR is greater than threshold)
            if ((k == currentIndex && cond.get(k) > 0) || (cond.get(k) > 0 && filterCond.get(k) > 0 && gr.get(k) > gr_thresh)) {

                //Draw the points
                g2d.setColor(Color.RED);
                Ellipse2D point;

                //If current gene was clicked, make the point bigger and black
                if (k == currentIndex) {
                    g2d.setColor(Color.BLACK);
                    point = new Ellipse2D.Double((int) (MULTIPLIER / dist * (coords_2d.get(k).getX()) - radius),
                            (int) (MULTIPLIER / dist * (coords_2d.get(k).getY()) - radius), radius * 2, radius * 2);
                } else {
                    point = new Ellipse2D.Double((int) (MULTIPLIER / dist * (coords_2d.get(k).getX()) - radius / 2),
                            (int) (MULTIPLIER / dist * (coords_2d.get(k).getY()) - radius / 2), radius, radius);
                }
                points.add(point);//add the point to the list
                g2d.fill(point);


                g2d.setStroke(thickStroke);
                g2d.setPaint(Color.YELLOW);

                //Draw the GR with a yellow bar
                if (k == currentIndex) {
                    magf = magf * 2.0;
                }//If selected draw longer lines
                //Comput the coordinate of enddpoint of the GR bar
                xGR = coords_3d.get(k).getX() * (1 + gr.get(k) * magf);
                yGR = coords_3d.get(k).getY() * (1 + gr.get(k) * magf);
                zGR = coords_3d.get(k).getZ() * (1 + gr.get(k) * magf);
                Coordinate endPoint = td(alpha_view, beta_view, new Coordinate(xGR, yGR, zGR));

                g2d.drawLine((int) (MULTIPLIER / dist * (coords_2d.get(k).getX())), (int) (MULTIPLIER / dist * (coords_2d.get(k).getY())),
                        (int) (MULTIPLIER / dist * (endPoint.getX())), (int) (MULTIPLIER / dist * (endPoint.getY())));

                //Draw the names of the genes
                double xString = (endPoint.getX() > 0) ? -Math.sqrt(radius) / 2.0 : -Math.sqrt(radius);
                double yString = (endPoint.getY() > 0) ? Math.sqrt(radius) / 2.0 : -Math.sqrt(radius) / 2.0;

                //If current gene was clicked, draw the name bigger
                if (k == currentIndex) {
                    g2d.setFont(new Font("Serif", Font.BOLD, (int) (2.0 * radius / Math.sqrt(dist))));
                    magf = magf / 2.0; // return the magf to regular level
                } else {
                    g2d.setFont(new Font("Serif", Font.PLAIN, (int) (radius / Math.sqrt(dist))));
                }

                g2d.drawString(names[k], (int) (MULTIPLIER / dist * (endPoint.getX()) + xString), (int) (MULTIPLIER / dist * (endPoint.getY()) + yString));
            } else {
                points.add(null);
            }
        }

        /*endTime = System.currentTimeMillis();
        System.out.println("Drawing Coordinates took : " + (endTime - startTime));
        startTime = endTime;*/

        double ch1, ch2, ch3, nn, cons;

        //Define stroke to have dotted lines
        float[] dashVal = {2f, 0f, 2f};
        BasicStroke dashStroke = new BasicStroke(1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND, 1.0f, dashVal, 2f);


        BasicStroke thinStroke = new BasicStroke(1, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_BEVEL);
        BasicStroke semiThickStroke = new BasicStroke(Math.round(1 / dist), BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_BEVEL);

        g2d.setStroke(thinStroke);

        //Compute and draw the correlation lines
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int inPermanentLine = inPermanentLine(i, j);
                // Show gene's connections according to filters, thresholds,etc
                // If it should be a permanent line, compute it.
                // If both genes are visible and GR value of both are greater than threshold, compute the correlation line
                if (inPermanentLine == 1 || (filterCond.get(i) > 0 && filterCond.get(j) > 0 && gr.get(i) > gr_thresh && gr.get(j) > gr_thresh)) {
                    cor_line = new ArrayList<>();

                    //Compute the points that, when linked, create the correlation line
                    for (double elem : ch) {
                        ch1 = elem * coords_3d.get(i).getX() + (1.0 - elem) * coords_3d.get(j).getX();
                        ch2 = elem * coords_3d.get(i).getY() + (1.0 - elem) * coords_3d.get(j).getY();
                        ch3 = elem * coords_3d.get(i).getZ() + (1.0 - elem) * coords_3d.get(j).getZ();
                        nn = Math.sqrt(ch1 * ch1 + ch2 * ch2 + ch3 * ch3);
                        ch1 /= nn;
                        ch2 /= nn;
                        ch3 /= nn;
                        cons = ch1 * x_view + ch2 * y_view + ch3 * z_view;
                        if (cons > 0) {
                            cor_line.add(td(alpha_view, beta_view, new Coordinate(ch1, ch2, ch3)));
                        }
                    }

                    GeneralPath line = new GeneralPath();

                    double corVal = cor.get(i).get(j);

                    //draw a correlation line between points of cor_line
                    if (cor_line.size() > 0) { // sanity check
                        //Determine if we should draw the correlation or not

                        /*
                            1. (If the correlation line is a permanent line) OR (no gene was selected or one of the genes in this correlation was selected)
                         AND 2. (a) Correlation is greater than threshold OR (b) Correlation is between the limits (c) correlation is between the limits
                         */
                        if (inPermanentLine == 1 ||
                                (currentIndex == i || currentIndex == j || currentIndex == -1) && ((r_option == -1 && Math.pow(corVal, 2) > thresh) ||
                                        (r_option == 0 && isBetween(corVal, r_thresh1_low, r_thresh1_high) && isBetween(corVal, r_thresh2_low, r_thresh2_high)) ||
                                        (r_option == 1 && (isBetween(corVal, r_thresh1_low, r_thresh1_high) || isBetween(corVal, r_thresh2_low, r_thresh2_high))))) {


                            //Change color depending on if the correlation is positive or negative
                            if (corVal > 0) {
                                g2d.setPaint(Color.CYAN);
                            } else if (corVal < 0) {
                                g2d.setPaint(Color.PINK);
                            }


                            line.moveTo(MULTIPLIER / dist * (cor_line.get(0).getX()), MULTIPLIER / dist * (cor_line.get(0).getY()));
                            for (Coordinate point : cor_line) {
                                line.lineTo(MULTIPLIER / dist * (point.getX()), MULTIPLIER / dist * (point.getY()));
                            }

                            //Add the correlation line to the list to the matching spot
                            if (currentIndex != -1) {
                                g2d.setStroke(semiThickStroke);
                                if (currentIndex == i) {
                                    lines.add(new LineStruct(line, i, j));
                                } else {
                                    lines.add(new LineStruct(line, j, i));
                                }
                            } else {
                                g2d.setStroke(thinStroke);
                            }

                            //If the line is a permanent line, draw the line in dashed line
                            if (inPermanentLine == 1) {
                                g2d.setStroke(dashStroke);
                            }

                            g2d.draw(line);

                        }
                    }
                }
            }
        }
        /*endTime = System.currentTimeMillis();
        System.out.println("Drawing correlation lines took : " + (endTime - startTime));*/

        g2d.dispose();
    }

    //Check if the pair should be permanently drawn
    private int inPermanentLine(int from, int to) {
        for (LineStruct line : permanentLines) {
            //If there is a match, we return 1
            if ((line.getFromIndex() == from && line.getToIndex() == to) ||
                    (line.getFromIndex() == to && line.getToIndex() == from)) {
                return 1;
            }
        }
        return 0;
    }

    //Change the value of distance
    public void changeDist(double distance) {
        dist = 1.0 / distance;
        thresh = 1 - Math.exp(-dist / 10);
        gr_thresh = (gr_thresh == 0) ? 0 : 1 - Math.exp(-dist / 10);
    }

    //Accessor for the names
    public static String[] getNames() {
        return names;
    }

    //Accessor for the GR
    public static ArrayList<Double> getGR() {
        return gr;
    }

    //Accessor for the nth line of correlation
    public static ArrayList<Double> getCor(int n) {
        return cor.get(n);
    }

    //Change the value of GR_Thresh only if necessary
    public static void changeGR_Thresh(int option) {
        gr_thresh = (option == 0) ? 0 : 1 - Math.exp(-dist / 10);
    }

    //Change the r threshhold (choice = OR(1), AND(0))
    public static void changer_Thresh(double bound1_low, double bound1_high, double bound2_low, double bound2_high, int choice) {
        r_thresh1_low = bound1_low;
        r_thresh1_high = bound1_high;
        r_thresh2_low = bound2_low;
        r_thresh2_high = bound2_high;
        r_option = choice;
        Main.redrawMain(gr_option);
    }

    //Make the threshhold default
    public static void defaultr_Thresh() {
        r_thresh1_low = -1;
        r_thresh1_high = 1;
        r_thresh2_low = -1;
        r_thresh2_high = 1;
        r_option = -1;
        Main.redrawMain(gr_option);
    }

    //Change the value of alpha_view
    public void changeAlpha(double alpha) {
        alpha_view = alpha / 180.0 * Math.PI;
        computeViewAngles();
        compute2dCoords();
    }


    //Set GR_high
    public static void setGR_high(double newHigh) {
        GR_high = newHigh;
    }

    //Set GR_Low
    public static void setGR_low(double newLow) {
        GR_low = newLow;
    }

    //Check GR to determine whether or not to display the gene
    public static void evaluateGRBoundary() {
        for (int i = 0; i < cond.size(); i++) {
            //If they are out of boundary, set their condition to be < 0
            if (gr.get(i) > GR_high || gr.get(i) < GR_low) {
                filterCond.add(-1.0);
            } else {
                filterCond.add(1.0);
            }
        }
    }

    //Change the value of beta_view
    public void changeBeta(double beta) {
        beta_view = beta / 180.0 * Math.PI;
        computeViewAngles();
        compute2dCoords();
    }

    //Change the size of font
    public void changeSize(int size) {
        radius_default = size;
    }

    //Determines if the value is between low and high
    private boolean isBetween(double val, double low, double high) {
        return ((val > low) && (val < high));
    }


    //Called by Main to apply GROption
    public static void setGROPtion(int n) {
        gr_option = n;
        compute2dCoords();
    }

    //Get number of Genes
    public static int getN() {
        return n;
    }

    //Accessor method for dist
    public static double getDist() {
        return dist;
    }

    //Accesor method for alpha_view
    public static double getAlpha_view() {
        return alpha_view;
    }

    //Accesor method for beta_view
    public static double getBeta_view() {
        return beta_view;
    }

    //Accessor Methods for translateX
    public static int getTranslateX() {
        return translateX;
    }

    //Accessor Methods for translateY
    public static int getTranslateY() {
        return translateY;
    }

    //Accessor Methods for translateXInitial
    public static int getTranslateXInitial() {
        return TRANSLATEX_INITIAL;
    }

    //Accessor Methods for translateYInitial
    public static int getTranslateYInitial() {
        return TRANSLATEY_INITIAL;
    }

    public static int getMultiplier() {
        return MULTIPLIER;
    }

    //Move the view
    public void move(double updown, double rightleft) {
        translateX += rightleft;
        translateY += updown;
    }

    //Go to the standard view
    public void goHome() {
        dist = 1.0;
        alpha_view = Math.PI;
        beta_view = Math.PI;
        translateX = TRANSLATEX_INITIAL;
        translateY = TRANSLATEY_INITIAL;
        computeViewAngles();
        compute2dCoords();
    }

    //Tries to find the specified gene
    //Returns -1 if none found, returns index if gene was found
    public static int findGene(String query) {
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(query)) {
                DataPanel.setGeneBox(names[i]);
                DataPanel.setGRBox(gr.get(i).toString());
                ListPanel.setValIndex(i);
                ListPanel.setValue();
                return i;
            }
        }
        ListPanel.setValIndex(-1);

        //UNCOMMENT this if the information of list should disappear when user clicks somewhere else
        //ListPanel.clearValues();
        return -1;
    }

    //Go to specified gene by finding the necessary alpha_view and beta_view
    public void goToGene(int index) {
        Coordinate gene = coords_3d.get(index);
        double x = gene.getX();
        double y = gene.getY();
        double z = gene.getZ();

        centerLocation(x, y, z);

        translateX = TRANSLATEX_INITIAL;
        translateY = TRANSLATEY_INITIAL;
        LeftPanel.goToGene(dist);
        compute2dCoords();
    }

    //Called to center the whole diagram on the gene
    private void centerLocation(double x, double y, double z) {
        if (x == 0) {
            alpha_view = Math.PI / 2.0;
            if (y == 0) {
                beta_view = Math.PI / 2.0;
            } else {
                beta_view = Math.atan(z / y);
            }
        } else {
            alpha_view = Math.atan(y / x);
            double tempVal = z / (x * Math.cos(alpha_view) + y * Math.sin(alpha_view));
            if (tempVal == 0) {
                beta_view = Math.PI / 2.0;
            } else {
                beta_view = Math.atan(tempVal);
            }
        }
        computeViewAngles();

        //Take into account that tangent gives range of -pi/2 to pi/2
        //Makes sure the gene is facing forward, not backward in the globe
        if (x_view * x + y_view * y + z_view * z < 0) {
            if (beta_view <= 0) {
                beta_view += Math.PI;
            } else {
                beta_view -= Math.PI;
            }
            computeViewAngles();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    //Adapter that will move the location when clicked and dragged
    class MovingAdapter extends MouseAdapter {

        private int x_origin;
        private int y_origin;

        //In case of a double click, go to that spot
        public void doubleClick(MouseEvent e) {

            //New clicked point
            double x_new = (e.getX() - translateX) * dist / MULTIPLIER;
            double y_new = (e.getY() - translateY) * dist / MULTIPLIER;

            if (Math.pow(x_new, 2) + Math.pow(y_new, 2) > 1)
                return;

            //Get the new Center
            Coordinate center = new Coordinate(0, 0, 1);
            try {
                center = Calculator.convert2dto3d(x_new, y_new, alpha_view, beta_view);
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(null, "ERROR OCCURED WHILE CONVERTING 2d COORDINATE to 3D coordinate");
            }

            //Change the center to the location
            centerLocation(center.getX(), center.getY(), center.getZ());

            LeftPanel.adjust(); // adjust the preview
            ScrollPanel.setAlphaBetaScrolls(alpha_view, beta_view); // change the scrolls

            //Zoom in accordingly
            dist -= 0.05;
            thresh = 1 - Math.exp(-dist / 10);
            gr_thresh = (gr_thresh == 0) ? 0 : 1 - Math.exp(-dist / 10);
            ScrollPanel.setDistScroll(1.0 / dist);

        }

        //Determine what to do if the program was clicked
        @Override
        public void mouseClicked(MouseEvent e) {

            //Deal with double click : focus
            if (e.getClickCount() > 2) return;
            if (e.getClickCount() == 2)
                doubleClick(e);


            //Compute the translated coordinates
            int x = e.getX() - translateX;
            int y = e.getY() - translateY;

            //Make certain lines permanent or not permanent
            int deleted = 0;

            if (currentLine != null && currentLine.contains(x, y)) {
                for (int i = 0; i < permanentLines.size(); i++) {

                    //If there was already a permanent line, remove it from the list
                    if ((permanentLines.get(i).getFromIndex() == fromIndex &&
                            permanentLines.get(i).getToIndex() == toIndex) ||
                            (permanentLines.get(i).getFromIndex() == toIndex &&
                                    permanentLines.get(i).getToIndex() == fromIndex)) {
                        permanentLines.remove(i);
                        deleted = 1;
                        break;
                    }
                }
                //Add a new permanent line
                if (deleted == 0)
                    permanentLines.add(new LineStruct(null, fromIndex, toIndex));

                Main.redrawMain(gr_option);
                return;
            }

            //Check if any of the points were clicked
            for (int i = 0; i < coords_2d.size(); i++) {
                //If the point was clicked
                if (points.get(i) != null && points.get(i).contains(x, y)) {
                    DataPanel.setGeneBox(names[i]);
                    DataPanel.setGRBox(gr.get(i).toString());
                    ListPanel.setValIndex(i);
                    ListPanel.setValue();

                    Main.redrawMain(gr_option);

                    return;
                }
            }

            ListPanel.setValIndex(-1);
            Main.redrawMain(gr_option);

            //UNCOMMENT this if the information of list should disappear when user clicks somewhere else
            //ListPanel.clearValues();

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (ListPanel.getValIndex() != -1) {
                int x = e.getX() - translateX;
                int y = e.getY() - translateY;

                DecimalFormat numberFormat = new DecimalFormat("#.000");


                //Check if any of the points were passed
                for (int i = 0; i < lines.size(); i++) {
                    LineStruct line = lines.get(i);
                    //If any of the point in line was clicked
                    if (line != null && line.getLine().contains(x, y)) {
                        Point p = MouseInfo.getPointerInfo().getLocation();
                        fromIndex = line.getFromIndex();
                        toIndex = line.getToIndex();
                        currentLine = line.getLine();
                        popWindow.set((int) p.getX() + 10, (int) p.getY() + 10, "To : " + names[toIndex],
                                " R  : " + numberFormat.format(cor.get(fromIndex).get(toIndex)));
                        return;
                    }
                }

                fromIndex = -1;
                toIndex = -1;
                currentLine = null;

                popWindow.clear();
            }

        }

        //When mouse is pressed, we get current coordinates
        @Override
        public void mousePressed(MouseEvent e) {
            x_origin = e.getX();
            y_origin = e.getY();
        }

        //When mouse is dragged, we move the window accordingly
        @Override
        public void mouseReleased(MouseEvent e) {

            double dx = (x_origin - e.getX()) * dist / MULTIPLIER;
            double dy = (y_origin - e.getY()) * dist / MULTIPLIER;

            //Check if the mouse moved enough
            if (Math.abs(dx) < 0.005 || Math.abs(dy) < 0.005)
                return;

            //If the person dragged too much, only move the appropriate amount
            if (Math.pow(dx, 2) + Math.pow(dy, 2) > 1) {
                double length = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
                dx = dx / length;
                dy = dy / length;
            }

            //Get the new Center
            Coordinate center = new Coordinate(0, 0, 1);
            try {
                center = Calculator.convert2dto3d(dx, dy, alpha_view, beta_view);
            } catch (NullPointerException ex) {
                System.out.println("ERROR OCCURRED WHILE CONVERTING 2d COORDINATE to 3D coordinate");
            }

            //Change the center to the location
            centerLocation(center.getX(), center.getY(), center.getZ());

            LeftPanel.adjust(); // adjust the preview
            ScrollPanel.setAlphaBetaScrolls(alpha_view, beta_view); // change the scrolls
            compute2dCoords(); // recompute the 2d coordinates
        }
    }

    //Adapter that will deal with scaling
    class ScaleAdapter extends MouseAdapter {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            doScale(e);
        }

        //Called to scale
        private void doScale(MouseWheelEvent e) {
            if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                dist = dist + e.getWheelRotation() * 0.1;

                if (1.0 / dist > ScrollPanel.getDistMax() / 100.0) {
                    dist = 1.0 / (ScrollPanel.getDistMax() / 100.0);
                } else if (1.0 / dist < ScrollPanel.getDistMin() / 100.0) {
                    dist = 1.0 / (ScrollPanel.getDistMin() / 100.0);
                }

                thresh = 1 - Math.exp(-dist / 10);
                gr_thresh = (gr_thresh == 0) ? 0 : 1 - Math.exp(-dist / 10);
                ScrollPanel.setDistScroll(1.0 / dist);
            }
        }
    }
}