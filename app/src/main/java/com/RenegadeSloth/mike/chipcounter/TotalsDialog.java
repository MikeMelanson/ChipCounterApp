package com.RenegadeSloth.mike.chipcounter;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.RenegadeSloth.mike.chipcounter.DatabaseDescription.*;

public class TotalsDialog extends Dialog implements android.view.View.OnClickListener {

    private ChipCounterDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public TotalsDialog(Activity a){
        super(a);
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.totals_dialog);

        dbHelper = new ChipCounterDatabaseHelper(getContext());
        db = dbHelper.getWritableDatabase();

        float total_spent=0;
        float total_won=0;

        //query the database to
        String[] projection = {History.COLUMN_SPENT,History.COLUMN_WON};
        Cursor cursor = db.query(History.TABLE_NAME,projection,null,null,null,null,null);
        cursor.moveToFirst();
        int size = cursor.getCount();

        for (int i = 0;i<size;i++){
            total_won += cursor.getFloat(cursor.getColumnIndexOrThrow(History.COLUMN_WON));
            total_spent += cursor.getFloat(cursor.getColumnIndexOrThrow(History.COLUMN_SPENT));
            cursor.moveToNext();
        }

        cursor.close();

        TextView spent = findViewById(R.id.spent_text);
        TextView won = findViewById(R.id.won_text);
        TextView total_ = findViewById(R.id.total_text);

        spent.setText("Spent: $"+total_spent);
        won.setText("Won: $"+total_won);
        total_.setText("Total: \n$"+(total_won-total_spent));

        Button confirm = findViewById(R.id.ok_button);
        confirm.setOnClickListener(this);
    }

    public void onClick(View v){
        dismiss();
    }
}
