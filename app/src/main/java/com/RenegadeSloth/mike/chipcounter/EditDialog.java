package com.RenegadeSloth.mike.chipcounter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditDialog extends Dialog implements android.view.View.OnClickListener {

    private ChipCounterDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private EditText date_get_text, won_get_text, spent_get_text;

    private Calendar calendar;

    private int id;
    HistoryFragment historyFragment;

    public EditDialog(Activity a, int id, HistoryFragment frag){
        super(a);
        this.id = id;
        historyFragment = frag;
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
                new DatePickerDialog(historyFragment.getContext(),date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        won_get_text = findViewById(R.id.won_get_text);
        spent_get_text = findViewById(R.id.spent_get_text);

        date_get_text.setText(historyFragment.getDate());
        won_get_text.setText(historyFragment.getWon());
        spent_get_text.setText(historyFragment.getSpent());

        Button confirm = findViewById(R.id.confirm_button);
        confirm.setOnClickListener(this);
        Button cancel = findViewById(R.id.cancel_button);
        cancel.setOnClickListener(this);
    }

    public void updateLabel(){
        String format = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.CANADA);

        date_get_text.setText(sdf.format(calendar.getTime()));
    }

    public void onClick(View v){
        if (v.getId()==R.id.cancel_button){
            InputMethodManager imm = (InputMethodManager)historyFragment.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getDecorView()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            dismiss();
        }
        else{
            updateHistory();
            historyFragment.reloadScreen();
            InputMethodManager imm = (InputMethodManager)historyFragment.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getDecorView()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            dismiss();

        }
    }

    public void updateHistory(){
        ContentValues values = new ContentValues();
        values.put(DatabaseDescription.History.COLUMN_ID,id);
        values.put(DatabaseDescription.History.COLUMN_DATE,date_get_text.getText().toString());
        values.put(DatabaseDescription.History.COLUMN_SPENT,spent_get_text.getText().toString());
        values.put(DatabaseDescription.History.COLUMN_WON,won_get_text.getText().toString());

        String selection = DatabaseDescription.History.COLUMN_ID + " = ?";
        String[] selection_args = {String.valueOf(id)};
        db.update(DatabaseDescription.History.TABLE_NAME,values,selection,selection_args);
    }
}
