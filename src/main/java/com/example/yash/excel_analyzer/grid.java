package com.example.yash.excel_analyzer;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;

public class grid extends AppCompatActivity
{
    private int i,j,a=0,b=0;
    private int colcount ;
    private int rowcount ;
    private String values;
    //RadioButton[] rb = new RadioButton[2];
    private int threshold=2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        rowcount = getIntent().getIntExtra("nrows", 0);
        colcount = getIntent().getIntExtra("ncolumns", 0);
        values = getIntent().getStringExtra("values");

        ScrollView svv = new ScrollView(this);
        svv.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT, ScrollView.LayoutParams.WRAP_CONTENT));
        LinearLayout linLayout = new LinearLayout(this);
        linLayout.setOrientation(LinearLayout.VERTICAL);
        LayoutParams linLayoutParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        GridLayout grd;
        GridLayout.LayoutParams gllop;
        HorizontalScrollView svh;

//        -------------------------------Upper Layout-----------------

      /*  RadioGroup rg = new RadioGroup(this); //create the RadioGroup
        rg.setOrientation(RadioGroup.HORIZONTAL);//or RadioGroup.VERTICAL
        rb[0]=new RadioButton(this);
        rb[1]=new RadioButton(this);

        rb[0].setText("Row SEARCH");

        rb[1].setText("Column Search");


        for(int i=0; i<rb.length; i++)
        {
            rg.addView(rb[i]);
        }
        linLayout.addView(rg);
*/
        EditText row_no = new EditText(this);
        row_no.setHint("Enter row no");
        row_no.setTextSize(25);
        row_no.setTextColor(Color.BLUE);
        linLayout.addView(row_no);
        EditText col_no = new EditText(this);
        col_no.setHint("Enter column no");
        col_no.setTextSize(25);
        col_no.setTextColor(Color.BLUE);
        linLayout.addView(col_no);

        /*if (rb[0].isSelected()) {
            col_no.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), rb[0].getText().toString() +"is selected", Toast.LENGTH_LONG).show();
        }
        if (rb[1].isSelected()) {
            row_no.setVisibility(View.GONE);
        }*/




    Button analyze = new Button(this);
    analyze.setText("ROW ANALYZE");
    analyze.setTextSize(19);
    analyze.setTextColor(Color.RED);
    linLayout.addView(analyze);

    Button canalyze = new Button(this);
    canalyze.setText("COLUMN ANALYZE");
    canalyze.setTextSize(19);
    canalyze.setTextColor(Color.RED);
    linLayout.addView(canalyze);

    Button[][] btn = new Button[rowcount][colcount];

//--------------------------------------------------------------------------
    String[] row_val = values.split(":");
    String[] cell_val;

    for (i = 0; i < rowcount; i++) {
        cell_val = row_val[i].split(",");
        grd = new GridLayout(this);
        grd.setColumnCount(colcount);
        grd.setRowCount(rowcount);
        grd.setBackgroundColor(Color.YELLOW);
        for (j = 0; j < colcount; j++) {

            btn[i][j] = new Button(this);
            btn[i][j].setText((i + 1) + "," + (j + 1));
            btn[i][j].setText(cell_val[j]);
            btn[i][j].setClickable(false);

            grd.addView(btn[i][j]);
        }
        svh = new HorizontalScrollView(this);
        svh.addView(grd);
        linLayout.addView(svh);
    }

    svv.addView(linLayout);
    setContentView(svv, linLayoutParam);

    Button nextPage = new Button(this);
    nextPage.setText("RELOAD");
    nextPage.setTextSize(25);
    nextPage.setTextColor(Color.BLUE);
    linLayout.addView(nextPage);

    TextView tv = new TextView(this);
    tv.setTextSize(19);
    tv.setTextColor(Color.DKGRAY);
    linLayout.addView(tv);

    TextView tv1 = new TextView(this);
    tv1.setTextSize(19);
    tv1.setTextColor(Color.DKGRAY);
    linLayout.addView(tv1);

    nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
    });

    analyze.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------
    //--------------------------ROW WISE ANALYSIS--------------------------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------
                try {
                    int analyze_row = Integer.parseInt(row_no.getText().toString());


                   int max1 = 0;
                   int min1 = 0;
                   if (analyze_row <= rowcount) {
                       min1 = Integer.parseInt(btn[analyze_row - 1][0].getText().toString());
                       max1 = Integer.parseInt(btn[analyze_row - 1][0].getText().toString());

                       for (int l = 1; l < colcount; l++) {
                           if (Integer.parseInt(btn[analyze_row - 1][l].getText().toString()) > max1) {
                               max1 = Integer.parseInt(btn[analyze_row - 1][l].getText().toString());

                           }
                           if (Integer.parseInt(btn[analyze_row - 1][l].getText().toString()) < min1) {
                               min1 = Integer.parseInt(btn[analyze_row - 1][l].getText().toString());

                           }
                       }
                       for (int l = 0; l < colcount; l++) {
                           if (Integer.parseInt(btn[analyze_row - 1][l].getText().toString()) == max1) {
                               btn[analyze_row - 1][l].setBackgroundColor(Color.CYAN);
                               btn[analyze_row - 1][l].setTextColor(Color.WHITE);
                           }
                           if (Integer.parseInt(btn[analyze_row - 1][l].getText().toString()) == min1) {
                               btn[analyze_row - 1][l].setBackgroundColor(Color.GREEN);
                               btn[analyze_row - 1][l].setTextColor(Color.WHITE);
                           }
                       }

                       tv.setText(analyze_row + " ROW: \n"+ "Minimum: " + min1 +"\n"+ "Maximum: " + max1  );


                       for (int t = 1; t <= threshold; t++) {
                           if ((analyze_row - 1 - t) >= 0) {
                               for (int c = 0; c < colcount; c++) {
                                   if (Integer.parseInt(btn[analyze_row - 1 - t][c].getText().toString()) == max1) {
                                       btn[analyze_row - 1 - t][c].setBackgroundColor(Color.CYAN);
                                       btn[analyze_row - 1 - t][c].setTextColor(Color.WHITE);
                                   }
                                   if (Integer.parseInt(btn[analyze_row - 1 - t][c].getText().toString()) == min1) {
                                       btn[analyze_row - 1 - t][c].setBackgroundColor(Color.GREEN);
                                       btn[analyze_row - 1 - t][c].setTextColor(Color.WHITE);
                                   }
                               }

                           }
                           if ((analyze_row - 1 + t) < rowcount) {
                               if ((analyze_row - 1 + t) <= (analyze_row - 1 + threshold)) {   //+1
                                   for (int c = 0; c < colcount; c++) {
                                       if (Integer.parseInt(btn[analyze_row - 1 + t][c].getText().toString()) == max1) {
                                           btn[analyze_row - 1 + t][c].setBackgroundColor(Color.CYAN);
                                           btn[analyze_row - 1 + t][c].setTextColor(Color.WHITE);
                                       }
                                       if (Integer.parseInt(btn[analyze_row - 1 + t][c].getText().toString()) == min1) {
                                           btn[analyze_row - 1 + t][c].setBackgroundColor(Color.GREEN);
                                           btn[analyze_row - 1 + t][c].setTextColor(Color.WHITE);
                                       }
                                   }
                               }
                           }
                       }


                   } else {
                       row_no.setError("Enter row no within 1-" + rowcount);
                   }
                }
                catch (Exception e){
                    row_no.setError("Only Integers are allowed");
                }

               }
           });


    //---------------------------------------------------------------------------------------------------------------------------------------------------------------
    //-----------------------COLUMN WISE ANALYSIS--------------------------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------
canalyze.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        try {

            int analyze_column = Integer.parseInt(col_no.getText().toString());

            int max1 = 0;
            int min1 = 0;
            if (analyze_column <= colcount) {
                min1 = Integer.parseInt(btn[0][analyze_column - 1].getText().toString());
                max1 = Integer.parseInt(btn[0][analyze_column - 1].getText().toString());

                for (int l = 1; l < rowcount; l++) {
                    if (Integer.parseInt(btn[l][analyze_column - 1].getText().toString()) > max1) {
                        max1 = Integer.parseInt(btn[l][analyze_column - 1].getText().toString());

                    }
                    if (Integer.parseInt(btn[l][analyze_column - 1].getText().toString()) < min1) {
                        min1 = Integer.parseInt(btn[l][analyze_column - 1].getText().toString());

                    }
                }
                for (int l = 0; l < rowcount; l++) {
                    if (Integer.parseInt(btn[l][analyze_column - 1].getText().toString()) == max1) {
                        btn[l][analyze_column - 1].setBackgroundColor(Color.CYAN);
                        btn[l][analyze_column - 1].setTextColor(Color.WHITE);
                    }
                    if (Integer.parseInt(btn[l][analyze_column - 1].getText().toString()) == min1) {
                        btn[l][analyze_column - 1].setBackgroundColor(Color.GREEN);
                        btn[l][analyze_column - 1].setTextColor(Color.WHITE);
                    }
                }

                tv1.setText(analyze_column + " COLUMN: \n" + "Minimum: " + min1 + "\n" + "Maximum: " + max1);


                for (int t = 1; t <= threshold; t++) {
                    if ((analyze_column - 1 - t) >= 0) {
                        for (int c = 0; c < rowcount; c++) {
                            if (Integer.parseInt(btn[c][analyze_column - 1 - t].getText().toString()) == max1) {
                                btn[c][analyze_column - 1 - t].setBackgroundColor(Color.CYAN);
                                btn[c][analyze_column - 1 - t].setTextColor(Color.WHITE);
                            }
                            if (Integer.parseInt(btn[c][analyze_column - 1 - t].getText().toString()) == min1) {
                                btn[c][analyze_column - 1 - t].setBackgroundColor(Color.GREEN);
                                btn[c][analyze_column - 1 - t].setTextColor(Color.WHITE);
                            }
                        }

                    }
                    if ((analyze_column - 1 + t) < colcount) {
                        if ((analyze_column - 1 + t) <= (analyze_column - 1 + threshold)) {   //+1
                            for (int c = 0; c < rowcount; c++) {
                                if (Integer.parseInt(btn[c][analyze_column - 1 + t].getText().toString()) == max1) {
                                    btn[c][analyze_column - 1 + t].setBackgroundColor(Color.CYAN);
                                    btn[c][analyze_column - 1 + t].setTextColor(Color.WHITE);
                                }
                                if (Integer.parseInt(btn[c][analyze_column - 1 + t].getText().toString()) == min1) {
                                    btn[c][analyze_column - 1 + t].setBackgroundColor(Color.GREEN);
                                    btn[c][analyze_column - 1 + t].setTextColor(Color.WHITE);
                                }
                            }
                        }
                    }
                }


            } else {
                col_no.setError("Enter row no within 1-" + colcount);
            }


        } catch (Exception e) {
            col_no.setError("Only Integers are allowed");
        }
    }

});

    }
}

