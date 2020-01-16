package com.RenegadeSloth.mike.chipcounter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.RenegadeSloth.mike.chipcounter.DatabaseDescription.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewHistoryDialog extends Dialog implements android.view.View.OnClickListener {

    private ChipCounterDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    Activity activity;

    EditText date_get_text, won_get_text, spent_get_text;

    private Calendar calendar;

    HistoryFragment parent;

    public NewHistoryDialog(Activity a, HistoryFragment parent){
        super(a);
        activity = a;
        this.parent = parent;
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.new_history_dialog);

        dbHelper = new ChipCounterDatabaseHelper(getContext());
        db = dbHelper.getWritableDatabase();

        calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        date_get_text = findViewById(R.id.date_get_text);
        date_get_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(parent.getContext(),date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        won_get_text = findViewById(R.id.won_get_text);
        spent_get_text = findViewById(R.id.spent_get_text);

        Button confirm = findViewById(R.id.confirm_button);
        Button cancel = findViewById(R.id.cancel_button);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    public void updateLabel(){
        String format = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.CANADA);

        date_get_text.setText(sdf.format(calendar.getTime()));
    }

    public void onClick(View v){
        if (v.getId()==R.id.cancel_button){
            parent.reloadScreen();
            InputMethodManager imm = (InputMethodManager)parent.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getDecorView()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            dismiss();
        }
        else{
            addHistory();
            parent.reloadScreen();
            InputMethodManager imm = (InputMethodManager)parent.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getDecorView()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            dismiss();
        }
    }

    public void addHistory(){
        String[] projection = {History.COLUMN_DATE,History.COLUMN_SPENT,History.COLUMN_WON};
        Cursor cursor = db.query(History.TABLE_NAME,projection,null,null,null,null,null,null);

        int size = cursor.getCount()+1;
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(History.COLUMN_ID,size);
        values.put(History.COLUMN_DATE,date_get_text.getText().toString());
        values.put(History.COLUMN_SPENT,spent_get_text.getText().toString());
        values.put(History.COLUMN_WON,won_get_text.getText().toString());
        db.insert(History.TABLE_NAME,null,values);
    }
}
