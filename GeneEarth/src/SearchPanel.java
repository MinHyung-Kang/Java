

/**
 * Panel used to search a gene.
 * User can type in a gene's name and find it if it exists, or is told that it doesn't exist.
 * Created by user on 2015-07-13.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SearchPanel extends JPanel{

    private JLabel searchLabel; // "Search Gene" Label
    private JTextField searchBox; // The box where user type in the gene's name to search
    private JButton searchButton; // Button one clicks to search

    //Constructor
    public SearchPanel(){

        //Set the size,border,layout of the panel
        this.setPreferredSize(new Dimension(200,100));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setLayout(new GridLayout(3, 1));

        //Create search label
        Font titleFont = new Font("Serif", Font.ITALIC, 15);
        searchLabel = new JLabel("Search Gene : ");
        searchLabel.setFont(titleFont);
        searchLabel.setForeground(Color.BLACK);

        //Create search Box
        searchBox = new JTextField(30);

        //Create the find Button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        searchButton = new JButton("Find");
        buttonPanel.add(searchButton);

        //Action to be done if enter is pressed or search is clicked
        Action findGene = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = MainPanel.findGene(searchBox.getText());
                if(index >= 0){
                    Main.goToGene(index);
                    searchBox.setText("");
                }else{
                    JOptionPane.showMessageDialog(null , "No such Gene was found. Please check your query.");
                }
            }
        };

        //Adds the listener to the textfield and button
        searchBox.addActionListener(findGene);
        searchButton.addActionListener(findGene);

        //Add the components
        this.add(searchLabel);
        this.add(searchBox);
        this.add(searchButton);
    }
}
