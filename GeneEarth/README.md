# GeneEarth V. 1.0 (August, 2015)

## Information

Developed by  : Daniel Kang, Dartmouth College

Advisor : Professor Eugene Demidenko, Geisel School of Medicine, Dartmouth College

## Summary : 

GeneEarth is a program that allows visualization of connectivity of genes. It uses GeneRank, a recursive definition of gene connectivity developed by Professor Demidenko. The concept is similar to that of PageRank algorithm of Google. The goal behind this program is to provide a method for researchers to utilize GeneRank and visually study the connections of the genes.
	
For more information regarding GeneRank, refer to original paper by Professor Demidenko : http://www.biodatamining.org/content/8/1/2
	
For detailed video instructions, refer to the following video : 
http://youtu.be/i2JEsUUrgcs

## Compiling

The program was developed using IDEA IntelliJ.
	
### Running using command prompt : 
One can compile the code by calling "javac -sourcepath src ./\*.java" from the src directory (where the files are)
One can run the code by calling "java Main"
Note that three files R.txt, GR.txt, tf.txt were also supplied for testing cases.
These are sample datafile with 100 genes, and are loaded if one clicks on "default"	

## R-Code
At this version, one has to supply three data files. 
One can create these files by running the provided r-code CreateFiles.r
One supplies the Gene Expression Data by specifying the parameters :
- dr : the directory of the gene expression data
- fileName : the name of gene expression data you want to read from
- target : the directory where you want the target files to be created
	
The script will then create three files : 
- R.txt : Correlation data
- GR.txt : Gene Rank Data
- tf.txt : Angle data used to create 3-d coordinates
		
Supply these three files upon running the java program.

## Description : 
		
1. Providing data files	
Upon running the files, you will be given option of choosing data files. You can find three files as described above (R,GR,tf) or choose default option to use sample data
		
2. Running the program
Upon clicking the start or default button, user will see the main program.
		
A. Scrolls : One can use scrolls on left top to change certain values
- Dist : Changes the distance from the earth to the camera
- Alpha_view : Changes the alpha_view. Refer to documentation for calculation details.
- Beta_view : Changes the beta_view. Refer to documentation for calculation details.
- Size : Used to change size of each point and the letters. Change this for optimal view
		
B. Moving around : One can click arrows or home button on left side to change the view
- Clicking on the arrows will shift the view (rather than rotate) in that direction
- Clicking on the home button will move the view to default view
			
C. Preview : One can see the location of the view with respect to whole earth on the left bottom. One can see how much one has zoomed in as well as how much one has moved. 
			
D. Mouse Options : The center panel shows the GeneEarth. One can use mouse for different operations.
	a. Single click
	- On a gene : Shows the gene's information and the genes it is connected to on the right Only displays the correlation lines of that gene
	- On a correlation line : One can click on a correlation line when a single gene is selected to make the correlation line permanent. That is, it will always appear. One can cancel this by clicking on the line again.
	b. Double click
	- On a gene : Does single click operation, and make the gene the center of earth, and zooms in
	- On a location on earth : Make that point center of earth, and zooms in
	c. Mouse moved
	- If mouse moves over a correlation line when a single gene is selected, shows information regarding that correlation. Otherwise, does nothing.
	d. MouseScroll
	- One can use mouse scroll to zoom in or zoom out. The program will zoom in/out with respect to the center of the view.
	e. Mouse Dragged
	-If one location is clicked, dragged to another point, and released, the earth rotates in that direction. (Note that rotation is not so stable. Calculation should be fixed - one can always doubleclick on locations to move around)
					
E. Filter option : One can choose to filter the view by GR from right-middle panel.
	a. By dist : Genes are displayed according to their GeneRank and threshold computed by distance. That is, the further the view, genes with higher GR are displayed. (Default)
	b. By boundary : One can specify the boundary of GR and only portray the genes that match the condition
	c. Show all : display all genes. Not recommended when there is a large dataset.
			
F. FilterR option : One can choose to filter the view by correlation value from right-bottom panel.
	a. By dist : Correlation lines are displayed according to R and threshold computed by distance. That is, the further the view, correlation lines with higher R are displayed. (Default)
	b. By boundary : One can specify the boundary of R and only portray the lines that match the condition
	c. Show all : display all correlation. Not recommended when there is a large dataset.

G. List : When one clicks on a gene, the genes it is connected to are displayed on the very right panel. One can choose to sort them in decreasing order of GR, R or name. 