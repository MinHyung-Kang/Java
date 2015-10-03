/**
 * Right (2nd from right) panel that has the following components :
 *      SearchPanel, FilterPanel, FilterRpanel, DataPanel
 * Acts as an intermediary between these panel, Main and MainPanel
 * Created by user on 2015-07-09.
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.awt.geom.GeneralPath;

public class RightPanel extends JPanel {

    //Different panels
    private SearchPanel searcher;
    private FilterPanel filter;
    private FilterRPanel filterR;
    private DataPanel data;

    //Constructor
    public RightPanel(){

        //Add the components
        data = new DataPanel();
        searcher = new SearchPanel();
        filter = new FilterPanel();
        filterR = new FilterRPanel();

        this.add(data);
        this.add(searcher);
        this.add(filter);
        this.add(filterR);
    }
}
