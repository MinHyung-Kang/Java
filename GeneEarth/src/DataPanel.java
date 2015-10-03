/**
 * Represents the DataPanel, which tells us informationa bout the gene's namd and gene rank
 * Created by user on 2015-07-27.
 * <p>
 * Methods :
 * //Set the genes name
 * public static void setGeneBox(String name)
 * <p>
 * //Set the gene rank
 * public static void setGRBox(String gr)
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class DataPanel extends JPanel {

    private JLabel geneLabel, GRLabel; // each represents the label "gene" and "gene rank"
    private static JTextField geneBox, GRBox; // each represents the value of gene and its GR

    //Constructor
    public DataPanel() {

        //Set border, size, and layout
        this.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(5, 5, 5, 5),
                new LineBorder(Color.BLACK)));
        this.setPreferredSize(new Dimension(210, 80));
        this.setLayout(new GridBagLayout());

        setupLayout();
    }

    private void setupLayout() {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        //Set up the labels
        Font titleFont = new Font("Serif", Font.ITALIC, 15);
        geneLabel = new JLabel("Gene : ");
        geneLabel.setFont(titleFont);
        GRLabel = new JLabel("G R  :");
        GRLabel.setFont(titleFont);

        //Set up the boxes
        geneBox = new JTextField(10);
        geneBox.setEditable(false);
        GRBox = new JTextField(10);
        GRBox.setEditable(false);

        addComponents(c);
    }

    private void addComponents(GridBagConstraints c) {
        //Add the components
        c.insets = new Insets(0, 5, 0, 0);
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        this.add(geneLabel, c);

        c.insets = new Insets(5, 5, 0, 5);
        c.gridx = 1;
        c.gridwidth = 2;
        this.add(geneBox, c);

        c.weighty = 1;
        c.insets = new Insets(0, 5, 0, 0);
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = 1;
        this.add(GRLabel, c);

        c.weighty = 1;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 1;
        c.gridwidth = 2;
        this.add(GRBox, c);
    }

    //Set the genes name
    public static void setGeneBox(String name) {
        geneBox.setText(name);
    }

    //Set the gene rank
    public static void setGRBox(String gr) {
        GRBox.setText(gr);
    }
}
