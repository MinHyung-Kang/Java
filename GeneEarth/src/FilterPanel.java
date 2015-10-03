
/**
 * Class used to determine how to filter the displayed results
 * Created by user on 2015-07-22.
 * <p>
 * User can filter by 3 options : GR, distance, and show all
 * 1. GR : Show tha genes according to the spccified limis of GR.
 * Use can set either the lower limit, upper limit, or both
 * 2. distance : the default behavior. The further away one is from the genes,
 * one will see fewer genes.
 * 3. Show all : Show all the genes regardless of distance.
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

public class FilterPanel extends JPanel implements ItemListener {

    private JLabel filterLabel; // Title for the panel
    private JTextField ltText, gtText; // Textfield which will represent upper and lower limit
    private JRadioButton GRButton, distButton, showAllButton; // Buttons to enable different filter
    private JCheckBox ltCheck, gtCheck; // Checkbox to enable different limits for GR
    private JButton applyButton; // Button to apply the filter

    //Default constructor
    public FilterPanel() {
        this.setPreferredSize(new Dimension(210, 160));

        //Set border and layout
        this.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(5, 5, 5, 5),
                new LineBorder(Color.BLACK)));
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        addFilterLabel(c);

        createRadioButtons();

        Font defaultFont = GRButton.getFont();
        defaultFont = new Font(defaultFont.getFontName(), defaultFont.getStyle(), defaultFont.getSize() - 2);

        createComponents(defaultFont);
        addComponents(c);

        GRButton.addItemListener(this);
        distButton.addItemListener(this);
        showAllButton.addItemListener(this);
        ltCheck.addItemListener(this);
        gtCheck.addItemListener(this);


        //Action to be done if enter is pressed or search is clicked
        Action apply = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Perform this if GRbutton was selected
                if (GRButton.isSelected()) {
                    double high = 1.0, low = 0.0;//set default values

                    //Check if gtText textfield is blank or not
                    if (!gtText.getText().equals("")) {
                        try {
                            low = Double.parseDouble(gtText.getText());
                        } catch (NumberFormatException err) {
                            JOptionPane.showMessageDialog(null, "Please put numeric value between 0 and 1");
                            return;
                        }
                    }

                    //Perform this if ltText textfield is blank or not
                    if (!ltText.getText().equals("")) {
                        try {
                            high = Double.parseDouble(ltText.getText());
                        } catch (NumberFormatException err) {
                            JOptionPane.showMessageDialog(null, "Please put numeric value between 0 and 1");
                            return;
                        }
                    }

                    //Check if the input values make sense
                    checkInputValues(high, low);
                } else if (distButton.isSelected()) { // If filter by distance is selected
                    MainPanel.changeGR_Thresh(1);
                    Main.redrawMain(1);
                } else if (showAllButton.isSelected()) { // if show all (no filter) is selected
                    showAll();
                }
            }
        };

        //Make it so that if textfield gets an input the checkbox is selected
        ltText.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                ltCheck.setSelected(true);
                GRButton.setSelected(true);
            }

            public void removeUpdate(DocumentEvent e) {
            }

            public void insertUpdate(DocumentEvent e) {
                ltCheck.setSelected(true);
                GRButton.setSelected(true);
            }
        });

        //Make it so that if textfield gets an input the checkbox is selected
        gtText.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                gtCheck.setSelected(true);
                GRButton.setSelected(true);
            }

            public void removeUpdate(DocumentEvent e) {
            }

            public void insertUpdate(DocumentEvent e) {
                gtCheck.setSelected(true);
                GRButton.setSelected(true);
            }
        });

        applyButton.addActionListener(apply);
    }

    private void showAll() {
        MainPanel.changeGR_Thresh(0);
        MainPanel.setGR_high(1);
        MainPanel.setGR_low(0);
        Main.redrawMain(2);
    }

    private void checkInputValues(double high, double low) {
        if (high > 1.0 || high < 0 || low > 1.0 || low < 0) {
            JOptionPane.showMessageDialog(null, "Please put value between 0 and 1");
        } else if (high <= low) {
            JOptionPane.showMessageDialog(null, "Upper limit should be greater than lower limit");
        } else {
            MainPanel.setGR_high(high);
            MainPanel.setGR_low(low);
            MainPanel.changeGR_Thresh(0);
            Main.redrawMain(0);
        }
    }

    private void addComponents(GridBagConstraints c) {
        addTheDistCheckbox(c);
        addTheGRCheckboxes(c);
        addltCheck(c);
        addltText(c);
        addgtCheck(c);
        addgtText(c);
        addshowAllCheckBox(c);
        addTheApplyButton(c);
    }

    private void addTheApplyButton(GridBagConstraints c) {
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 10, 5, 0);
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 5;
        this.add(applyButton, c);
    }

    private void addshowAllCheckBox(GridBagConstraints c) {
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 5;
        this.add(showAllButton, c);
    }

    private void addgtText(GridBagConstraints c) {
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.gridwidth = 2;
        c.insets = new Insets(5, 0, 0, 5);
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 1;
        c.gridy = 4;
        this.add(gtText, c);
    }

    private void addgtCheck(GridBagConstraints c) {
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.insets = new Insets(5, 10, 0, 0);
        c.gridy = 4;
        this.add(gtCheck, c);
    }

    private void addltText(GridBagConstraints c) {
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(5, 0, 0, 5);
        c.weightx = 1.0;
        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 3;
        this.add(ltText, c);
    }

    private void addltCheck(GridBagConstraints c) {
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.insets = new Insets(5, 10, 0, 0);
        c.gridy = 3;
        this.add(ltCheck, c);
    }

    private void addTheGRCheckboxes(GridBagConstraints c) {
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        this.add(GRButton, c);
    }

    private void addTheDistCheckbox(GridBagConstraints c) {
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 0, 0);
        c.weightx = 0.0;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 1;
        this.add(distButton, c);
    }

    private void createComponents(Font defaultFont) {
        ltCheck = new JCheckBox("less than : ", false);
        ltCheck.setFont(defaultFont);
        gtCheck = new JCheckBox("greater than : ", false);
        gtCheck.setFont(defaultFont);
        ltText = new JTextField(7);
        gtText = new JTextField(7);
    }

    private void addFilterLabel(GridBagConstraints c) {
        Font titleFont = new Font("Serif", Font.ITALIC, 15);
        filterLabel = new JLabel("Filter by : ");
        filterLabel.setFont(titleFont);

        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        this.add(filterLabel, c);
    }

    private void createRadioButtons() {
        GRButton = new JRadioButton("Gene Rank (between 0 and 1)");
        distButton = new JRadioButton("Distance (Default)");
        distButton.setSelected(true);
        showAllButton = new JRadioButton("Show All");
        ButtonGroup buttons = new ButtonGroup();
        buttons.add(GRButton);
        buttons.add(distButton);
        buttons.add(showAllButton);
        applyButton = new JButton("Apply");
    }

    //What to do when item is checked
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getItemSelectable();
        int change = e.getStateChange();

        //For different items, match the corresponding behavior
        if (source == distButton) {
            if (change == ItemEvent.SELECTED) {
                ltCheck.setSelected(false);
                gtCheck.setSelected(false);
            }
        } else if (source == showAllButton) {
            if (change == ItemEvent.SELECTED) {
                ltCheck.setSelected(false);
                gtCheck.setSelected(false);
            }
        } else if (source == ltCheck) {
            if (change == ItemEvent.SELECTED) {
                GRButton.setSelected(true);
            } else if (change == ItemEvent.DESELECTED) {
                ltText.setText("");
            }
        } else if (source == gtCheck) {
            if (change == ItemEvent.SELECTED) {
                GRButton.setSelected(true);
            } else if (change == ItemEvent.DESELECTED) {
                gtText.setText("");
            }
        }
    }
}
