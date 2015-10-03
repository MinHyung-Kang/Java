/**
 * Panel used to choose the files needed to run the program
 * Created by user on 2015-07-21.
 * <p>
 * Methods :
 * //Method to show the window
 * public static void showWindow()
 */

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FileChooserPanel extends JPanel implements ActionListener {

    static JFrame frame; // background frame
    File RFile = null, GRFile = null, angleFile = null; //Files that will be loaded

    //Buttons to open different files
    JButton openRButton, openGRButton, openAngleButton;
    JTextField RText, GRText, angleText;
    JButton defaultButton, startButton;

    //Chooser for different files
    JFileChooser fc;

    JPanel RPanel, GRPanel, anglePanel, optionPanel;

    //Default constructor
    public FileChooserPanel() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        createButtons();
        addActionListeners();
        createTextfields();
        createPanels();
        addComponents();

        this.add(RPanel);
        this.add(GRPanel);
        this.add(anglePanel);
        this.add(optionPanel);

        fc = new JFileChooser();
    }

    private void addComponents() {
        RPanel.add(RText);
        RPanel.add(openRButton);
        GRPanel.add(GRText);
        GRPanel.add(openGRButton);
        anglePanel.add(angleText);
        anglePanel.add(openAngleButton);
        optionPanel.add(defaultButton);
        optionPanel.add(startButton);
    }

    private void createPanels() {
        RPanel = new JPanel();
        GRPanel = new JPanel();
        anglePanel = new JPanel();
        optionPanel = new JPanel();
    }

    private void createTextfields() {
        RText = new JTextField("Choose R Data", 40);
        RText.setEditable(false);
        GRText = new JTextField("Choose GR Data", 40);
        GRText.setEditable(false);
        angleText = new JTextField("Choose Angle Data", 40);
        angleText.setEditable(false);
    }

    private void addActionListeners() {
        openRButton.addActionListener(this);
        openGRButton.addActionListener(this);
        openAngleButton.addActionListener(this);
        defaultButton.addActionListener(this);
        startButton.addActionListener(this);
    }

    private void createButtons() {
        openRButton = new JButton("Open R Data");
        openGRButton = new JButton("Open GR Data");
        openAngleButton = new JButton("Open Angle Data");
        openRButton.setPreferredSize(new Dimension(200, 30));
        openGRButton.setPreferredSize(new Dimension(200, 30));
        openAngleButton.setPreferredSize(new Dimension(200, 30));
        defaultButton = new JButton("Default");
        startButton = new JButton("Start");
    }


    //Open each file
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openRButton) {//Open the file with R Data
            getRData();
        } else if (e.getSource() == openGRButton) {//Open the file with GR Data
            getGRData();
        } else if (e.getSource() == openAngleButton) {//Open the file with Angle Data
            getAngleData();
        } else if (e.getSource() == defaultButton) {
            getDefaultFiles();
        } else if (e.getSource() == startButton) { // start the program with chosen files
            startProgram();
        }
    }

    private void startProgram() {
        if (RFile != null && GRFile != null && angleFile != null) {
            if (RFile.exists() && GRFile.exists() && angleFile.exists()) {
                frame.dispose();
                new Main(RFile, GRFile, angleFile).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(frame, "Please check default settings.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select the data file(s).");
        }
    }

    private void getDefaultFiles() {
        //Choose the default files (mainly for testing, but could be used for convenience)
        RFile = new File("R.txt");
        GRFile = new File("GR.txt");
        angleFile = new File("tf.txt");

        //Open default files
        if (RFile.exists() && GRFile.exists() && angleFile.exists()) {
            frame.dispose();
            new Main(RFile, GRFile, angleFile).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(frame, "Please check default settings.");
        }
    }

    private void getAngleData() {
        int val = fc.showOpenDialog(FileChooserPanel.this);
        if (val == JFileChooser.APPROVE_OPTION) {
            angleFile = fc.getSelectedFile();
            angleText.setText(angleFile.getName());
        }
    }

    private void getGRData() {
        int val = fc.showOpenDialog(FileChooserPanel.this);
        if (val == JFileChooser.APPROVE_OPTION) {
            GRFile = fc.getSelectedFile();
            GRText.setText(GRFile.getName());
        }
    }

    private void getRData() {
        int val = fc.showOpenDialog(FileChooserPanel.this);
        if (val == JFileChooser.APPROVE_OPTION) {
            RFile = fc.getSelectedFile();
            RText.setText(RFile.getName());
        }
    }

    //Method to show the GUI
    public static void showWindow() {
        frame = new JFrame("Choose Data Files");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Add the panel
        frame.add(new FileChooserPanel());
        frame.pack();
        //Show the frame
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
