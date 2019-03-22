----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
------------------------APP NAME:  EXCEL-ANALYZER---------------------------------------------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

PROBLEM DEFINITION:-Design an application to accept data from source [.xls] file. The application will perform analysis like
					finding minimum and maximum value from given row or column. The row and column value will combine to 
					generate one value,which can be used as a map address. The application will generate similar row column 
					attribute, with threshold +/- [2] from minimum and maximum generated.
					
					
TECHNOLOGY STACK:-	 Android Studio, Java

LIBRARY USED:-		 Apache POI 4.0.1

DESCRIPTION:-		Given an .xls file which contains only data values int(here) can change it to float also if required making considerable changes in the code.
			Find the minimum and maximum value for the given row/column and then finding if the same minimum or maximum values exits at any other 
			position in the nearby rows/columns respectively (threshold given: [2])

FUNCTIONS USED:-

----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--------MainActivity.java-----------------------------
----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


Here, I created an activity with navigation panel ,the required IMPORT option is placed here . 
This option helps to select only the excel files in the phones memory or any other external media attached by intent.
BY setting the type of the intent object as 		.setType("application/vnd.ms-excel");
													.addCategory(Intent.CATEGORY_OPENABLE);  


On selecting the .xls file the result is receiveed back by the method .
The goal is achieved in two steps [A] and [B] given below:-

[A] DATA EXTRACTION FROM .xls file:


1)	onActivityResult(int requestCode, int resultCode,Intent resultData)

	Uri uri = resultData.getData(); 
	// uri object contains information about the file.


2)	String getFileName(Uri uri)
	//This method is used to retrieve the name of the file by the passed object(uri).


3)	void readExcelFile(Uri uri)
	Make sure it throws IOException, InvalidFormatException for proper catching of the exception.

	Java.io.File Class is used.
	This function helps to create a File object for it to be passed to be processed to the FileInputStream 
	A File object is created by passing in a String that represents the name of a file (from function(2)), or a String or another File object.
	Java FileInputStream class obtains input bytes from a file

	Class WorkbookFActory is used to create Workbook object.
	Required package: org.apache.poi.hssf.usermodel  

	Workbook method -> getSheetAt(int index);   //return Sheet object to work on.


	Class: DataFormatter //is used for formatting the value stored in an Cell values as String.

	Class: StringBuilder // is also used to create mutable (modifiable) string object.

	getPhysicalNumberOfRows();// returns int as the output, it is the last active row nummber in the current sheet.
	getPhysicalNumberOfCells();// returns int as the output, it is the maximum number of columns in the given row in the current sheet.
	getRow(r);  //it returns the row object to access the data(cells) in the row. 
	formatCellValue(Cell cell);	//Returns the formatted value of a cell as a String regardless of the cell type
										

MAPPING OF ROWS AND COLUMNS:
									
		Here ,the mapping of row and column is done by Iterating over Rows and Columns using for-each loop and 
		appending it to StringBuilder object created using .append();


Finally we have thus gathered our data from excel sheet and thus we pass it to grid.java for further analysis.


----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--------grid.java-----------------------------
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

[B] DATA ANALYSIS :

The user has to input the row or/and the column he want to analyze as per the statement  
Three Buttons  1) Row Analyze : It will apply the given business logic to find the similar values as of maximum and minimum in the rows above and below it with a given threshold.
			   2) Column Analyze : It will apply the given business logic to find the similar values as of maximum and minimum in the columns before and after it with a given threshold.
			   3) RELOAD: It is used to restart the given activity is user wants to.
			   
The data is extracted and assigned to similar row column generated using buttons(set as clickable=false)and grid layout with scrolling functionality both horizontally and vertically.

[Here, the GREEN color represents the minimum value  &  BLUE color represents the maximum value.]

----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------