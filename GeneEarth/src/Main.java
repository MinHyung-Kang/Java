
/**
 * Main class that initiates the whole program
 * It first loads the FileChooserPanel to allow user to choose the files
 * then it loads the geneEarth program to display the graphics
 *
 */
import javax.swing.*;
import java.awt.Container;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

//Main class(program)
public class Main extends JFrame implements WindowListener{

    //Fixed window size => ideally will be changed in the future
    private static final int WINDOW_WIDTH = 1400;
    private static final int WINDOW_HEIGHT = 600;

    private static MainPanel mainP; // displays the gene earth
    private static LeftPanel leftP; // control settings, preview of the whole earth
    private static RightPanel rightP; // displays information
    private ListPanel listP; // displays the genes that selected gene is connected to


    //constructor
    public Main(File RFile, File GRFile, File angleFile ){
        //Try to set the UI look like system's UI
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }

        //Set the components
        setComponents();

        //Add the different panels
        mainP = new MainPanel(RFile, GRFile, angleFile);
        mainP.setPreferredSize(new Dimension(600, WINDOW_HEIGHT));
        addPanels();

        Container pane = this.getContentPane();

        pane.setLayout(new FlowLayout());

        pane.add(leftP);
        pane.add(mainP);
        pane.add(rightP);
        pane.add(listP);
    }

    private void addPanels() {
        leftP = new LeftPanel();
        leftP.setPreferredSize(new Dimension(200,WINDOW_HEIGHT));
        rightP = new RightPanel();
        rightP.setPreferredSize(new Dimension(200,WINDOW_HEIGHT));
        listP = new ListPanel();
        listP.setPreferredSize(new Dimension(300,WINDOW_HEIGHT));
    }

    private void setComponents() {
        setTitle("Gene Earth V1.0");
        setVisible(true);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    //Called by MovePanel to move the Main globe to move
    public static void move(double updown, double leftright){
        mainP.move(updown, leftright);
        mainP.repaint();
    }

    //Change the distance in MainPanel
    public static void changeDist(double dist){
        mainP.changeDist(dist);
        mainP.repaint();
    }

    //Change the alpha in MainPanel
    public static void changeAlpha(double alpha){
        mainP.changeAlpha(alpha);
        mainP.repaint();
    }

    //Change the beta in MainPanel
    public static void changeBeta(double beta){
        mainP.changeBeta(beta);
        mainP.repaint();
    }

    //Change the size in MainPanel
    public static void changeSize(int size){
        mainP.changeSize(size);
        mainP.repaint();
    }

    //Called by MovePanel to return to home state
    public static void goHome(){
        mainP.goHome();
        mainP.repaint();
    }

    //Called by searchPanel to change the view
    public static void goToGene(int i){
        mainP.goToGene(i);
        mainP.repaint();
    }

    /*
        1. Called by FilterPanel to repaint the mainPanel
            =>Sets the option to display gene rank
        2. Called by MainPanel to repaint after clicking a gene
     */
    public static void redrawMain(int option){
        MainPanel.setGROPtion(option);
        mainP.repaint();
    }

    //Action for the window
    public void windowClosing(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowOpened(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}

    //Main Program
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable(){

            @Override
            public void run() {
                //Start with choosing the FileChooserPanel, then move onto actual program.
                FileChooserPanel another = new FileChooserPanel();
                FileChooserPanel.showWindow();
            }
        });

    }
}
