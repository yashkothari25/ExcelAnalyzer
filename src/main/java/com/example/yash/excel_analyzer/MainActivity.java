package com.example.yash.excel_analyzer;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static final int READ_REQ = 24;
    private static final String TAG = "MainActivity";


    private TextView g;
    private TextView a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        g=(TextView) findViewById(R.id.g);
        a=(TextView)findViewById(R.id.a);
        checkFilePermissions();

        //-----------------------
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.openxls) {

            Intent intentXls = new Intent(Intent.ACTION_GET_CONTENT);
            intentXls.setType("application/vnd.ms-excel");
            intentXls.addCategory(Intent.CATEGORY_OPENABLE);


            startActivityForResult(Intent.createChooser(intentXls,"ChooseFile"), READ_REQ);
           // startActivityForResult(intentXls, READ_REQ);


            // Handle the camera action
        } else if (id == R.id.nav_gallery) {


        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        if (resultCode == Activity.RESULT_OK) {

            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
            }
            if (requestCode == READ_REQ) {
                try {
                    readExcelFile(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InvalidFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    private void readExcelFile(Uri uri) throws IOException, InvalidFormatException{
          {
            try {
                String fn=getFileName(uri);
                a.setText(fn);
                File pdfFile = new File(Environment.getExternalStorageDirectory(), fn);
                Uri path = Uri.fromFile(pdfFile);

                a.setText(path.toString());

                FileInputStream fis = new FileInputStream(pdfFile);
//---------------------------------------------------------------------------------------------------------
                // Using XSSF for xlsx format, for xls use HSSF
                Workbook workbook = WorkbookFactory.create(fis);
                if (workbook == null)
                    Toast.makeText(getApplicationContext(), "no file ", Toast.LENGTH_LONG).show();
//---------------------------------------------------------------------------------------------------------
                // Retrieving the number of sheets in the Workbook
                System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");
                g.setText("WorkBook Has :" + workbook.getNumberOfSheets()+" sheets");
                Sheet sheet = workbook.getSheetAt(0);
//---------------------------------------------------------------------------------------------------------
            // Create a DataFormatter to format and get each cell's value as String
            DataFormatter dataFormatter = new DataFormatter();
            StringBuilder sb=new StringBuilder();
            int nr=sheet.getPhysicalNumberOfRows();
            int max=0;
                for (int r = 1; r < nr; r++) {
                    Row row1 = sheet.getRow(r);
                    int cellsCount = row1.getPhysicalNumberOfCells();
                    if(cellsCount>max)
                        max=cellsCount;
                }

//---------------------------------------------------------------------------------------------------------
                //   you can use a for-each loop to iterate over the rows and columns
            System.out.println("\n\nIterating over Rows and Columns using for-each loop\n");
            for (Row row2: sheet) {
                for(Cell cell: row2) {
                    if (cell.getColumnIndex() == 0) {
                        String cellValue = dataFormatter.formatCellValue(cell);
                        sb.append(cellValue);
                    } else {
                        String cellValue = dataFormatter.formatCellValue(cell);
                        if (cellValue.trim() == "") {
                            sb.append("," + " ");
                        } else {
                            sb.append("," + cellValue);
                        }
                    }
                }
                sb.append(":");
            }
                Intent i = new Intent(MainActivity.this,grid.class);
                i.putExtra("nrows",nr);
                i.putExtra("ncolumns",max);
                i.putExtra("values",sb.toString());
                startActivity(i);
//---------------------------------------------------------------------------------------------------------
                // Closing the workbook
            workbook.close();
//---------------------------------------------------------------------------------------------------------
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }

        }
    }

//---------------------------------------------------------------------------------------------------------

    private void checkFilePermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }
}

/*
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        g=(Button)findViewById(R.id.g);
        a=(TextView)findViewById(R.id.a);
        checkFilePermissions();
//analyze grid button
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MainActivity.this,grid.class);
                startActivity(i);
            }
        });

        //-----------------------
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.openxls) {

            Intent intentXls = new Intent(Intent.ACTION_GET_CONTENT);
            intentXls.setType("application/vnd.ms-excel");
            intentXls.addCategory(Intent.CATEGORY_OPENABLE);


            startActivityForResult(Intent.createChooser(intentXls,"ChooseFile"), READ_REQ);
            // startActivityForResult(intentXls, READ_REQ);


            // Handle the camera action
        } else if (id == R.id.nav_gallery) {


        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        //Toast.makeText(getApplicationContext(),"hi1",Toast.LENGTH_SHORT).show();

        if (resultCode == Activity.RESULT_OK) {

            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                //Toast.makeText(getApplicationContext(),"result ok",Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(getApplicationContext(),"result ok",Toast.LENGTH_LONG).show();
            if (requestCode == READ_REQ) {
                try {
                    //   Toast.makeText(getApplicationContext(),"eequest code match",Toast.LENGTH_SHORT).show();
                    readExcelFile(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InvalidFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    private void readExcelFile(Uri uri) throws IOException, InvalidFormatException{
        //final String FILE_PATH = "./example.xls";
        {
            try {




                String fn=getFileName(uri);
                a.setText(fn);
                //Toast.makeText(getApplicationContext(), "read excel", Toast.LENGTH_SHORT).show();
                File pdfFile = new File(Environment.getExternalStorageDirectory(), fn);
                Uri path = Uri.fromFile(pdfFile);

                //Toast.makeText(getApplicationContext(),path.toString() + " dgshf" + pdfFile.getAbsolutePath(),Toast.LENGTH_SHORT).show();

                //a.setText(path.toString() + " dgshf" + pdfFile.getAbsolutePath());
                a.setText("file exists"+ pdfFile.exists());
                FileInputStream fis = new FileInputStream(pdfFile);

                //HSSFWorkbook workbook = new HSSFWorkbook(fis);
                //FileInputStream fis = null;
                //fis = new FileInputStream(filepath);

                // Using XSSF for xlsx format, for xls use HSSF
                Workbook workbook = WorkbookFactory.create(fis);
                if (workbook == null)
                    Toast.makeText(getApplicationContext(), "no file ", Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(),"read excel file",Toast.LENGTH_SHORT).show();
                g.setText("1");

                // Retrieving the number of sheets in the Workbook
                System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");
                g.setText("shhets" + workbook.getNumberOfSheets());
                //Toast.makeText(getApplicationContext(), String.valueOf(workbook.getNumberOfSheets()), Toast.LENGTH_SHORT).show();


*/
/*
            // Retrieving the number of sheets in the Workbook

        /*
           =============================================================
           Iterating over all the sheets in the workbook (Multiple ways)
           =============================================================
        *//*

*/
/*


            // 3. Or you can use a Java 8 forEach wih lambda
            System.out.println("Retrieving Sheets using Java 8 forEach with lambda");
            workbook.forEach(sheet -> {
                System.out.println("=> " + sheet.getSheetName());
            });
*//*

        */
/*
           ==================================================================
           Iterating over all the rows and columns in a Sheet (Multiple ways)
           ==================================================================
        *//*


                // Getting the Sheet at index zero
                Sheet sheet = workbook.getSheetAt(0);

                // Create a DataFormatter to format and get each cell's value as String
                DataFormatter dataFormatter = new DataFormatter();
                StringBuilder sb=new StringBuilder();
                int nr=sheet.getPhysicalNumberOfRows();
                int max=0;
                for (int r = 1; r < nr; r++) {
                    Row row1 = sheet.getRow(r);
                    int cellsCount = row1.getPhysicalNumberOfCells();
                    if(cellsCount>max)
                        max=cellsCount;
                }


                */
/*

                 *//*

                // 2. Or you can use a for-each loop to iterate over the rows and columns
                System.out.println("\n\nIterating over Rows and Columns using for-each loop\n");
                for (Row row2: sheet) {
                    for(Cell cell: row2) {
                        if (cell.getColumnIndex() == 0) {
                            String cellValue = dataFormatter.formatCellValue(cell);
                            sb.append(cellValue);
                        } else {
                            String cellValue = dataFormatter.formatCellValue(cell);
                            if (cellValue.trim() == "") {
                                sb.append("," + " ");
                            } else {
                                sb.append("," + cellValue);
                                //  System.out.print(cellValue + "\t");
                            }
                        }
                    }
                    sb.append(":");
                    //System.out.println();
                }
                //Toast.makeText(getApplicationContext(),nr +"//"+ max+ "//"+ sb.toString(),Toast.LENGTH_LONG).show();
                Intent i = new Intent(MainActivity.this,grid.class);
                i.putExtra("nrows",nr);
                i.putExtra("ncolumns",max);
                i.putExtra("values",sb.toString());
                startActivity(i);



  */
/*          // 3. Or you can use Java 8 forEach loop with lambda
            System.out.println("\n\nIterating over Rows and Columns using Java 8 forEach with lambda\n");
            sheet.forEach(row -> {
                row.forEach(cell -> {
                    printCellValue(cell);
                });
                System.out.println();
            });
*//*

                // Closing the workbook
                workbook.close();
            }catch (Exception e)
            {
                //e.printStackTrace();
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }

        }


    }
    private void checkFilePermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    private static void printCellValue(Cell cell) {
        switch (cell.getCellTypeEnum()) {
            case BOOLEAN:
                System.out.print(cell.getBooleanCellValue());
                break;
            case STRING:
                System.out.print(cell.getRichStringCellValue().getString());
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    System.out.print(cell.getDateCellValue());
                } else {
                    System.out.print(cell.getNumericCellValue());
                }
                break;
            case FORMULA:
                System.out.print(cell.getCellFormula());
                break;
            case BLANK:
                System.out.print("");
                break;
            default:
                System.out.print("");
        }

        System.out.print("\t");
    }
}
*/
