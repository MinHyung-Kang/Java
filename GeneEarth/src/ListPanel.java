/**
 * Panel that lists all the genes that the specified gene is connected to.
 * Used Indexer,NameIndexer to sort the results in terms of R, GR, or Names.
 * Created by user on 2015-07-27.
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;


public class ListPanel extends JPanel implements ActionListener {

    private JLabel desLabel; // "Connected to : " label
    private JPanel listPanel; // Actual panel of list of the data
    private GridBagConstraints d; // GridBagConstraint to draw the result
    private Font defaultFont; // Default font that is followed
    private static Indexer comparator; // Indexer used to sort by R, GR
    private static NameIndexer nameComparator; // NameINdexer used to sort by name
    private static ArrayList<Data> datas; // ArrayList of Data which will be
    private static int sortOption = 0; // 0 (by R) 1 (by GR) 2 (by Name)
    private static Integer[] indices; // New set of indices used for sorthing
    private static int valIndex = -1; // keeps track of which gene was clicked

    private JRadioButton byR, byGR, byName;
    private JLabel elem_rLabel, elem_grLabel, elem_nameLabel;
    private JTextField elem_rBox, elem_grBox, elem_nameBox;

    // Inner class that represents drawing components for one gene
    public class Data {
        public JTextField nameBox, rBox, grBox;
    }

    //Constructor
    public ListPanel() {

        //Set the borders
        this.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(5, 5, 5, 5),
                new LineBorder(Color.BLACK)));

        this.setPreferredSize(new Dimension(300, 600));

        /*
            Creates button panel where we have radiobuttons so that
            user can choose how to order the results
         */
        JPanel buttonPanel = new JPanel();
        JLabel filterLabel = new JLabel("Order by : ");
        defaultFont = new Font("Serif", Font.BOLD, 13);
        filterLabel.setFont(defaultFont);

        createButtons();

        buttonPanel.add(filterLabel);
        buttonPanel.add(byR);
        buttonPanel.add(byGR);
        buttonPanel.add(byName);

        this.add(buttonPanel, BorderLayout.NORTH);

        //Create the lowerPanel for the label
        JPanel lowerPanel = new JPanel(new BorderLayout());
        desLabel = new JLabel("Connected to : ");

        /*
            Create a list of genes that the selected gene is connected to
         */
        listPanel = new JPanel();
        listPanel.setLayout(new GridBagLayout());
        d = new GridBagConstraints();

        //Create arraylist of components
        datas = new ArrayList<Data>(MainPanel.getN());

        //Add the necessary amount of components
        for (int i = 0; i < MainPanel.getN(); i++) {
            addComponents(i);
        }

        //Create ScrollPane
        JScrollPane scrollListPanel = new JScrollPane(listPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollListPanel.setPreferredSize(new Dimension(280, 500));
        lowerPanel.add(desLabel, BorderLayout.NORTH);
        lowerPanel.add(scrollListPanel, BorderLayout.CENTER);

        this.add(lowerPanel, BorderLayout.CENTER);
    }

    private void createButtons() {
        byR = new JRadioButton("R");
        byR.setActionCommand("R");
        byR.setSelected(true);
        byGR = new JRadioButton("GR");
        byGR.setActionCommand("GR");
        byName = new JRadioButton("Name");
        byName.setActionCommand("Name");
        ButtonGroup options = new ButtonGroup();
        options.add(byR);
        options.add(byGR);
        options.add(byName);

        byR.addActionListener(this);
        byGR.addActionListener(this);
        byName.addActionListener(this);
    }

    //Determines what to do when each of radiobutton is clicked
    public void actionPerformed(ActionEvent e) {
        //Filter by chosen component
        if (e.getActionCommand().equals("R")) {
            sortOption = 0;
            if (valIndex != -1)
                setValue();
        } else if (e.getActionCommand().equals("GR")) {
            sortOption = 1;
            if (valIndex != -1)
                setValue();
        } else if (e.getActionCommand().equals("Name")) {
            sortOption = 2;
            if (valIndex != -1)
                setValue();
        }
    }

    //Add the components (genes they are connected to)
    private void addComponents(int index) {
        Data temp = new Data();
        addLabels();

        addTextFields(temp);
        datas.add(index, temp);

        /*Add the elements correspondingly
            GENENAME : [       ]
         R : [      ]  GR : [       ]

        */
        addelem_nameLabel(index);
        addelem_nameBox();
        addelem_rLabel(index);
        addelem_rBox();
        addelem_grLabel();
        addelem_grBox();
    }

    private void addelem_grBox() {
        d.gridx = 4;
        d.gridwidth = 2;
        d.weightx = 1.0;
        d.anchor = GridBagConstraints.EAST;
        listPanel.add(elem_grBox, d);
    }

    private void addelem_grLabel() {
        d.gridx = 3;
        d.gridwidth = 1;
        d.weightx = 0;
        listPanel.add(elem_grLabel, d);
    }

    private void addelem_rBox() {
        d.anchor = GridBagConstraints.WEST;
        d.insets = new Insets(3, 0, 0, 0);
        d.gridx = 1;
        d.gridwidth = 2;
        d.weightx = 1.0;
        listPanel.add(elem_rBox, d);
    }

    private void addelem_rLabel(int index) {
        d.anchor = GridBagConstraints.WEST;
        d.insets = new Insets(3, 5, 0, 0);
        d.gridy = 2 * index + 1;
        d.gridx = 0;
        d.gridwidth = 1;
        d.weightx = 0;
        listPanel.add(elem_rLabel, d);
    }

    private void addelem_nameBox() {
        d.anchor = GridBagConstraints.WEST;
        d.insets = new Insets(10, 0, 0, 0);
        d.gridx = 1;
        d.gridwidth = 4;
        listPanel.add(elem_nameBox, d);
    }

    private void addelem_nameLabel(int index) {
        d.insets = new Insets(10, 5, 0, 0);
        d.anchor = GridBagConstraints.WEST;
        d.weightx = 0.5;
        d.gridy = 2 * index;
        d.gridx = 0;
        d.gridwidth = 1;
        listPanel.add(elem_nameLabel, d);
    }

    private void addLabels() {
        elem_nameLabel = new JLabel("Gene : ");
        elem_nameLabel.setFont(defaultFont);
        elem_rLabel = new JLabel("R : ");
        elem_rLabel.setFont(defaultFont);
        elem_grLabel = new JLabel("GR : ");
        elem_grLabel.setFont(defaultFont);
    }

    private void addTextFields(Data temp) {
        elem_nameBox = new JTextField(10);
        temp.nameBox = elem_nameBox;
        elem_nameBox.setEditable(false);
        elem_rBox = new JTextField(7);
        temp.rBox = elem_rBox;
        elem_rBox.setEditable(false);
        elem_grBox = new JTextField(7);
        temp.grBox = elem_grBox;
        elem_grBox.setEditable(false);
    }

    //Set the values of the components
    public static void setValue() {
        //Get the data to display
        ArrayList<Double> cor = MainPanel.getCor(valIndex);
        ArrayList<Double> gr = MainPanel.getGR();
        String[] names = MainPanel.getNames();

        //Sort according to the comparator
        switch (sortOption) {
            case 0:
                comparator = new Indexer(cor);
                indices = comparator.createIndices();
                Arrays.sort(indices, comparator);
                break;
            case 1:
                comparator = new Indexer(gr);
                indices = comparator.createIndices();
                Arrays.sort(indices, comparator);
                break;
            case 2:
                nameComparator = new NameIndexer(names);
                indices = nameComparator.createIndices();
                Arrays.sort(indices, nameComparator);
                break;
            default:
                return;
        }

        //Set the list accordingly by going through each textfields and changing values
        for (int i = 0; i < indices.length; i++) {
            datas.get(i).nameBox.setText(names[indices[i]]);
            datas.get(i).nameBox.setCaretPosition(0);
            datas.get(i).grBox.setText("" + gr.get(indices[i]));
            datas.get(i).grBox.setCaretPosition(0);
            datas.get(i).rBox.setText("" + cor.get(indices[i]));
            datas.get(i).rBox.setCaretPosition(0);
        }
    }

    //Deletes all the current values stored in the list
    public static void clearValues() {
        for (int i = 0; i < datas.size(); i++) {
            datas.get(i).nameBox.setText("");
            datas.get(i).grBox.setText("");
            datas.get(i).rBox.setText("");
        }
    }

    //Set the value of valIndex, which keeps track of which gene was clicked
    public static void setValIndex(int n) {
        valIndex = n;
    }

    //Returns the value of valIndex
    public static int getValIndex() {
        return valIndex;
    }
}
