/**
 *  Class used to determine how to filter the displayed results according to r values
 * Created by user on 2015-07-22.
 *
 *  User can specify among three options to limit r(correlation) values : by boundary, distance, show all
 *  1. By boundary : user can specify a boundary for the result.
 *                  Use can set boundaries, and use logicla statement (and/or) to further specify limits.
 *  2. By distance : The default behavior, Correlation with higher values are shown when distance is far,
 *                  and more correlations are shown when distance is low and close
 *  3. Show all : show all the correlation lines
 *
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class FilterRPanel extends JPanel implements ItemListener {

    private JLabel filterLabel; // Label used as title for this panel
    private JTextField bound1_Text,bound2_Text; // Textfield to represent the boundaries of correlation
    private JRadioButton dist_RB, cor_RB,all_RB; // Radio button to choose btw different options
    private JRadioButton gt1_RB,lt1_RB,gt2_RB,lt2_RB, and_RB, or_RB,blank_RB; // More options to specify limits
    private JButton applyButton; // apply the current filter

    //Default constructor
    public FilterRPanel() {

        //Set the size, border, and layout
        this.setPreferredSize(new Dimension(210, 180));
        this.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(5, 5, 5, 5),
                new LineBorder(Color.BLACK)));
        this.setLayout(new GridBagLayout());

        createRadioButtons();

        //create textfield components
        bound1_Text = new JTextField(7);
        bound2_Text = new JTextField(7);

        setButtonGroups();


        //Button to apply changes
        applyButton = new JButton("Apply");

        //Set the default font
        setDefaultFont();

        //Create and add the title
        Font titleFont = new Font("Serif", Font.ITALIC, 15);
        filterLabel = new JLabel("Filter Correlation : ");
        filterLabel.setFont(titleFont);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        addComponents(c);

        //Add itemlisteners
        addItemlisteners();


        //Action to be done if enter is pressed or search is clicked
        Action apply = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //If distance radiobutton is selected, disable extra features
                if(cor_RB.isSelected()){

                    double firstLow=-1.0, firstHigh = 1.0, secondLow=-1.0, secondHigh=1.0;
                    int choice=0; //set the default option to be AND

                    //Check if the first textfield is input and makes sense
                    if(bound1_Text.getText().equals("")){ // set as -1
                    }else{
                        try{
                            if(gt1_RB.isSelected()) {
                                firstLow = Double.parseDouble(bound1_Text.getText());
                            }else{
                                firstHigh = Double.parseDouble(bound1_Text.getText());
                            }
                        }catch(NumberFormatException err){
                            JOptionPane.showMessageDialog(null, "Please put numeric value between 0 and 1");
                            return;
                        }
                    }

                    //Check if the second textfield is input and makes sense
                    if(bound2_Text.getText().equals("")){ // set as -1
                    }else{
                        try{
                            if(gt2_RB.isSelected()) {
                                secondLow = Double.parseDouble(bound2_Text.getText());
                            }else{
                                secondHigh = Double.parseDouble(bound2_Text.getText());
                            }
                        }catch(NumberFormatException err){
                            JOptionPane.showMessageDialog(null, "Please put numeric value between -1 and 1");
                            return;
                        }
                    }

                    //Determine if we are using AND or Or
                    if(or_RB.isSelected()){
                        choice = 1;
                    }

                    //Check if the input values make sense
                    if( Math.abs(firstLow) > 1.0 || Math.abs(firstHigh) > 1.0 || Math.abs(secondLow) > 1.0 || Math.abs(secondHigh) > 1.0){
                        JOptionPane.showMessageDialog(null, "Please put value between -1 and 1");
                        return;
                    }

                    //Process the input
                    MainPanel.changer_Thresh(firstLow,firstHigh,secondLow,secondHigh, choice);

                }else if(dist_RB.isSelected()){ //if dist is selected, return to default
                    MainPanel.defaultr_Thresh();
                }else if(all_RB.isSelected()){
                    MainPanel.changer_Thresh(-1,1,-1,1,1);
                }
            }
        };

        //Make it so that if textfield gets an input the checkbox is selected
        bound1_Text.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                blank_RB.setEnabled(true);
                and_RB.setEnabled(true);
                or_RB.setEnabled(true);
            }

            public void removeUpdate(DocumentEvent e) {
            }

            public void insertUpdate(DocumentEvent e) {
                blank_RB.setEnabled(true);
                and_RB.setEnabled(true);
                or_RB.setEnabled(true);
            }
        });

        applyButton.addActionListener(apply);
    }

    private void addItemlisteners() {
        dist_RB.addItemListener(this);
        cor_RB.addItemListener(this);
        gt1_RB.addItemListener(this);
        lt1_RB.addItemListener(this);
        blank_RB.addItemListener(this);
        and_RB.addItemListener(this);
        or_RB.addItemListener(this);
        gt2_RB.addItemListener(this);
        lt2_RB.addItemListener(this);
        all_RB.addItemListener(this);
    }

    private void addComponents(GridBagConstraints c) {
        addFilterLabel(c);

        //Add the distance RadioButton
        adddist_RB(c);

        //Add the Correlation RadioButton
        addcor_RB(c);

        //Add gt1,lt1, bound1_textfield
        addFirstGroup(c);

        //Add the "blank", "and", "or" buttons
        addBlankAndOr(c);

        //Add gt2,lt2, bound2_textfield
        addSecondGroup(c);

        //Add the all RadioButton
        addall_RB(c);

        //Add the apply button
        addapplyButton(c);
    }

    private void addapplyButton(GridBagConstraints c) {
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5,10,5,0);
        c.weightx = 0.5;
        c.gridwidth = 2;
        c.gridx = 3;
        c.gridy = 6;
        this.add(applyButton, c);
    }

    private void addall_RB(GridBagConstraints c) {
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 6;
        this.add(all_RB, c);
    }

    private void addSecondGroup(GridBagConstraints c) {
        c.weightx = 0.1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.insets = new Insets(0,10,0,0);
        c.gridy = 5;
        this.add(gt2_RB, c);

        c.weightx = 0.1;
        c.gridx = 1;
        this.add(lt2_RB, c);

        c.weightx = 1;
        c.gridwidth = 3;
        c.gridx = 2;
        c.insets = new Insets(0,0,0,0);
        this.add(bound2_Text, c);
    }

    private void addBlankAndOr(GridBagConstraints c) {
        c.weightx = 0.1;
        c.gridwidth = 1;
        c.insets = new Insets(0,20,0,0);
        c.gridx = 0;
        c.gridy = 4;
        this.add(blank_RB,c);

        c.gridx = 1;
        this.add(and_RB, c);

        c.gridx = 3;
        c.gridy = 4;
        this.add(or_RB, c);
    }

    private void addFirstGroup(GridBagConstraints c) {
        c.weightx = 0.1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.insets = new Insets(0,10,0,0);
        c.gridy = 3;
        this.add(gt1_RB, c);

        c.weightx = 0.1;
        c.gridx = 1;
        this.add(lt1_RB, c);

        c.weightx = 0.5;
        c.gridwidth = 3;
        c.gridx = 2;
        c.insets = new Insets(0,0,0,0);
        this.add(bound1_Text, c);
    }

    private void addcor_RB(GridBagConstraints c) {
        c.weightx = 0.0;
        c.gridwidth = 5;
        c.gridx = 0;
        c.gridy = 2;
        this.add(cor_RB, c);
    }

    private void adddist_RB(GridBagConstraints c) {
        c.weightx = 0.0;
        c.gridwidth = 5;
        c.gridx = 0;
        c.gridy = 1;
        this.add(dist_RB, c);
    }

    private void addFilterLabel(GridBagConstraints c) {
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 5;
        this.add(filterLabel, c);
    }

    private void setDefaultFont() {
        Font defaultFont = dist_RB.getFont();
        defaultFont = new Font(defaultFont.getFontName(),defaultFont.getStyle(),defaultFont.getSize()-2);
        gt1_RB.setFont(defaultFont);
        lt1_RB.setFont(defaultFont);
        gt2_RB.setFont(defaultFont);
        lt2_RB.setFont(defaultFont);
    }

    private void setButtonGroups() {
        ButtonGroup options_BG = new ButtonGroup();
        options_BG.add(dist_RB);
        options_BG.add(cor_RB);
        options_BG.add(all_RB);
        ButtonGroup bound1_BG = new ButtonGroup();
        bound1_BG.add(gt1_RB);
        bound1_BG.add(lt1_RB);
        ButtonGroup bound2_BG = new ButtonGroup();
        bound2_BG.add(gt2_RB);
        bound2_BG.add(lt2_RB);
        ButtonGroup andor_BG = new ButtonGroup();
        andor_BG.add(blank_RB);
        andor_BG.add(and_RB);
        andor_BG.add(or_RB);
    }

    private void createRadioButtons() {
        dist_RB = new JRadioButton("By distance (default)");
        dist_RB.setSelected(true);
        cor_RB = new JRadioButton("Set correlation boundary");
        all_RB = new JRadioButton("Show All");
        gt1_RB = new JRadioButton(">");
        gt1_RB.setEnabled(false);
        gt1_RB.setSelected(true);
        lt1_RB = new JRadioButton("<");
        lt1_RB.setEnabled(false);
        gt2_RB = new JRadioButton(">");
        gt2_RB.setEnabled(false);
        gt2_RB.setSelected(true);
        lt2_RB = new JRadioButton("<");
        lt2_RB.setEnabled(false);
        blank_RB = new JRadioButton("");
        blank_RB.setEnabled(false);
        blank_RB.setSelected(true);
        and_RB = new JRadioButton("AND");
        and_RB.setEnabled(false);
        or_RB = new JRadioButton("OR");
        or_RB.setEnabled(false);
    }

    //What to do when item is checked
    public void itemStateChanged(ItemEvent e){
        Object source = e.getItemSelectable();
        int change = e.getStateChange();

        //For different items, take different actions
        if(source == dist_RB){
            if (change == ItemEvent.SELECTED) {
                processDistSelected();
            }
        }else if(source == cor_RB){
            if (change == ItemEvent.SELECTED) {
                processCorSelected();
            }
        }else if(source == all_RB){
            if (change == ItemEvent.SELECTED) {
                processDistSelected();
            }
        }else if(source == and_RB){
            if (change == ItemEvent.SELECTED) {
                processBlankAndOr(true);
            }
        }else if(source == or_RB){
            if (change == ItemEvent.SELECTED) {
                processBlankAndOr(true);
            }
        }else if(source == blank_RB){
            if (change == ItemEvent.SELECTED) {
                processBlankAndOr(false);
            }
        }
    }

    private void processBlankAndOr(boolean state) {
        gt2_RB.setEnabled(state);
        lt2_RB.setEnabled(state);
        bound2_Text.setEnabled(state);
    }

    private void processCorSelected() {
        gt1_RB.setEnabled(true);
        lt1_RB.setEnabled(true);
        blank_RB.setEnabled(true);
        and_RB.setEnabled(true);
        or_RB.setEnabled(true);
        bound1_Text.setEnabled(true);
        if(and_RB.isSelected() || or_RB.isSelected()) {
            processBlankAndOr(true);
        }
    }


    private void processDistSelected() {
        gt1_RB.setEnabled(false);
        lt1_RB.setEnabled(false);
        gt2_RB.setEnabled(false);
        lt2_RB.setEnabled(false);
        blank_RB.setEnabled(false);
        and_RB.setEnabled(false);
        or_RB.setEnabled(false);
        bound1_Text.setEnabled(false);
        bound2_Text.setEnabled(false);
    }
}
