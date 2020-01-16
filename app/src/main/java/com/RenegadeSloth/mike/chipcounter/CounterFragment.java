package com.RenegadeSloth.mike.chipcounter;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.RenegadeSloth.mike.chipcounter.DatabaseDescription.*;

public class CounterFragment extends Fragment{

    private EditText twentyFive,oneHundred,fiveHundred,oneThousand,fiveThousand;
    private TextView total;

    private SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.counter_fragment, container, false);

        ChipCounterDatabaseHelper dbHelper = new ChipCounterDatabaseHelper(getActivity().getBaseContext());
        db = dbHelper.getWritableDatabase();

        total = view.findViewById(R.id.total);
        total.setText(getString(R.string.total,0));

        RelativeLayout layout = view.findViewById(R.id.parent);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });

        layout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
               @Override
               public void onFocusChange(View v, boolean hasFocus) {
                   if (hasFocus){
                       hideSoftKeyboard();
                   }
               }
           }
        );

        view.findViewById(R.id.clear_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
            }
        });

        view.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        try{
            ((ImageView)view.findViewById(R.id.twenty_five_image)).setImageDrawable(Drawable.createFromStream(getResources().getAssets().open("twentyFive.png"),null));
            ((ImageView)view.findViewById(R.id.one_hundred_image)).setImageDrawable(Drawable.createFromStream(getResources().getAssets().open("oneHundred.png"),null));
            ((ImageView)view.findViewById(R.id.five_hundred_image)).setImageDrawable(Drawable.createFromStream(getResources().getAssets().open("fiveHundred.png"),null));
            ((ImageView)view.findViewById(R.id.one_thousand_image)).setImageDrawable(Drawable.createFromStream(getResources().getAssets().open("oneThousand.png"),null));
            ((ImageView)view.findViewById(R.id.five_thousand_image)).setImageDrawable(Drawable.createFromStream(getResources().getAssets().open("fiveThousand.png"),null));
        }catch (Exception e){e.printStackTrace();}

        twentyFive = view.findViewById(R.id.twenty_five_value);
        oneHundred = view.findViewById(R.id.one_hundred_value);
        fiveHundred = view.findViewById(R.id.five_hundred_value);
        oneThousand = view.findViewById(R.id.one_thousand_value);
        fiveThousand = view.findViewById(R.id.five_thousand_value);

        Spinner history = view.findViewById(R.id.history_spinner);
        history.setEnabled(false);

        String projection[] = {Amounts.COLUMN_TWENTYFIVE,Amounts.COLUMN_ONEHUNDRED,Amounts.COLUMN_FIVEHUNDRED,Amounts.COLUMN_ONETHOUSAND,Amounts.COLUMN_FIVETHOUSAND};
        Cursor cursor = db.query(Amounts.TABLE_NAME,projection,null,null,null,null,null);
        if (cursor.getCount() > 0){
            history.setEnabled(true);
            if (cursor.getCount() == 1){
                String hStrings[] = new String[1];
                hStrings[0] = "Most Recent";
                ArrayAdapter<String> historyAdapter = new ArrayAdapter<>(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,hStrings);
                historyAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                history.setAdapter(historyAdapter);
                history.setSelection(0);
            }
            if (cursor.getCount() == 2){
                String hStrings[] = new String[2];
                hStrings[0] = "Most Recent";
                hStrings[1] = "1 Back";
                ArrayAdapter<String> historyAdapter = new ArrayAdapter<>(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,hStrings);
                historyAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                history.setAdapter(historyAdapter);
                history.setSelection(0);
            }
            else if (cursor.getCount() == 3){
                String hStrings[] = new String[3];
                hStrings[0] = "Most Recent";
                hStrings[1] = "1 Back";
                hStrings[2] = "2 Back";
                ArrayAdapter<String> historyAdapter = new ArrayAdapter<>(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,hStrings);
                historyAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                history.setAdapter(historyAdapter);
                history.setSelection(0);
            }
        }

        cursor.close();

        history.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String projection[] = {Amounts.COLUMN_TWENTYFIVE,Amounts.COLUMN_ONEHUNDRED,Amounts.COLUMN_FIVEHUNDRED,Amounts.COLUMN_ONETHOUSAND,Amounts.COLUMN_FIVETHOUSAND};
            Cursor cursor = db.query(Amounts.TABLE_NAME,projection,null,null,null,null,null);
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    cursor.moveToLast();
                }
                else if (position == 1){
                    cursor.moveToLast();
                    cursor.moveToPrevious();
                }
                else{
                    cursor.moveToLast();
                    cursor.moveToPrevious();
                    cursor.moveToPrevious();
                }
                if (cursor.getString(cursor.getColumnIndexOrThrow(Amounts.COLUMN_TWENTYFIVE)).equals("0")){
                    twentyFive.setText("");
                }else{twentyFive.setText(cursor.getString(cursor.getColumnIndexOrThrow(Amounts.COLUMN_TWENTYFIVE)));}
                if (cursor.getString(cursor.getColumnIndexOrThrow(Amounts.COLUMN_ONEHUNDRED)).equals("0")){
                    oneHundred.setText("");
                }else{oneHundred.setText(cursor.getString(cursor.getColumnIndexOrThrow(Amounts.COLUMN_ONEHUNDRED)));}
                if (cursor.getString(cursor.getColumnIndexOrThrow(Amounts.COLUMN_FIVEHUNDRED)).equals("0")){
                    fiveHundred.setText("");
                }else{fiveHundred.setText(cursor.getString(cursor.getColumnIndexOrThrow(Amounts.COLUMN_FIVEHUNDRED)));}
                if (cursor.getString(cursor.getColumnIndexOrThrow(Amounts.COLUMN_ONETHOUSAND)).equals("0")){
                    oneThousand.setText("");
                }else{oneThousand.setText(cursor.getString(cursor.getColumnIndexOrThrow(Amounts.COLUMN_ONETHOUSAND)));}
                if (cursor.getString(cursor.getColumnIndexOrThrow(Amounts.COLUMN_FIVETHOUSAND)).equals("0")){
                    fiveThousand.setText("");
                }else{fiveThousand.setText(cursor.getString(cursor.getColumnIndexOrThrow(Amounts.COLUMN_FIVETHOUSAND)));}
                calculateMethod();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Button calculate = view.findViewById(R.id.calculate_button);
        calculate.setOnClickListener(calculateListener);

        hideSoftKeyboard();
        return view;
    }

    public static CounterFragment newInstance(){
        CounterFragment counterFragment = new CounterFragment();
        return counterFragment;
    }

    private View.OnClickListener calculateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            calculateMethod();
        }
    };

    public void saveData(){
        String projection[] = {Amounts.COLUMN_RECORD,Amounts.COLUMN_TWENTYFIVE,Amounts.COLUMN_ONEHUNDRED,Amounts.COLUMN_FIVEHUNDRED,Amounts.COLUMN_ONETHOUSAND,Amounts.COLUMN_FIVETHOUSAND};
        final Cursor cursor = db.query(Amounts.TABLE_NAME,projection,null,null,null,null,null);

        ContentValues values = new ContentValues();

        values.put(Amounts.COLUMN_TWENTYFIVE,twentyFive.getText().toString());
        values.put(Amounts.COLUMN_ONEHUNDRED,oneHundred.getText().toString());
        values.put(Amounts.COLUMN_FIVEHUNDRED,fiveHundred.getText().toString());
        values.put(Amounts.COLUMN_ONETHOUSAND,oneThousand.getText().toString());
        values.put(Amounts.COLUMN_FIVETHOUSAND,fiveThousand.getText().toString());

        if (twentyFive.getText().toString().equals("")){
            values.put(Amounts.COLUMN_TWENTYFIVE,0);
        }
        if (oneHundred.getText().toString().equals("")){
            values.put(Amounts.COLUMN_ONEHUNDRED,0);
        }
        if (fiveHundred.getText().toString().equals("")){
            values.put(Amounts.COLUMN_FIVEHUNDRED,0);
        }
        if (oneThousand.getText().toString().equals("")){
            values.put(Amounts.COLUMN_ONETHOUSAND,0);
        }
        if (fiveThousand.getText().toString().equals("")){
            values.put(Amounts.COLUMN_FIVETHOUSAND,0);
        }

        if (cursor.getCount() == 0){
            values.put(Amounts.COLUMN_RECORD,1);
            db.insert(Amounts.TABLE_NAME,null,values);
        }
        else if (cursor.getCount() > 0 && cursor.getCount() < 3){
            cursor.moveToLast();
            values.put(Amounts.COLUMN_RECORD,cursor.getInt(cursor.getColumnIndexOrThrow(Amounts.COLUMN_RECORD))+1);
            db.insert(Amounts.TABLE_NAME,null,values);
        }
        else{
            cursor.moveToLast();
            ContentValues prev = new ContentValues();
            prev.put(Amounts.COLUMN_TWENTYFIVE,cursor.getString(cursor.getColumnIndexOrThrow(Amounts.COLUMN_TWENTYFIVE)));
            prev.put(Amounts.COLUMN_ONEHUNDRED,cursor.getString(cursor.getColumnIndexOrThrow(Amounts.COLUMN_ONEHUNDRED)));
            prev.put(Amounts.COLUMN_FIVEHUNDRED,cursor.getString(cursor.getColumnIndexOrThrow(Amounts.COLUMN_FIVEHUNDRED)));
            prev.put(Amounts.COLUMN_ONETHOUSAND,cursor.getString(cursor.getColumnIndexOrThrow(Amounts.COLUMN_ONETHOUSAND)));
            prev.put(Amounts.COLUMN_FIVETHOUSAND,cursor.getString(cursor.getColumnIndexOrThrow(Amounts.COLUMN_FIVETHOUSAND)));
            String prevArgs[] = {"2"};
            db.update(Amounts.TABLE_NAME,prev,Amounts.COLUMN_RECORD + " = ?",prevArgs);

            cursor.moveToPrevious();
            ContentValues prev2 = new ContentValues();
            prev2.put(Amounts.COLUMN_TWENTYFIVE,cursor.getString(cursor.getColumnIndexOrThrow(Amounts.COLUMN_TWENTYFIVE)));
            prev2.put(Amounts.COLUMN_ONEHUNDRED,cursor.getString(cursor.getColumnIndexOrThrow(Amounts.COLUMN_ONEHUNDRED)));
            prev2.put(Amounts.COLUMN_FIVEHUNDRED,cursor.getString(cursor.getColumnIndexOrThrow(Amounts.COLUMN_FIVEHUNDRED)));
            prev2.put(Amounts.COLUMN_ONETHOUSAND,cursor.getString(cursor.getColumnIndexOrThrow(Amounts.COLUMN_ONETHOUSAND)));
            prev2.put(Amounts.COLUMN_FIVETHOUSAND,cursor.getString(cursor.getColumnIndexOrThrow(Amounts.COLUMN_FIVETHOUSAND)));
            String prev2Args[] = {"1"};
            db.update(Amounts.TABLE_NAME,prev2,Amounts.COLUMN_RECORD + " = ?",prev2Args);

            String newArgs[] = {"3"};
            db.update(Amounts.TABLE_NAME,values,Amounts.COLUMN_RECORD + " = ?",newArgs);
        }

        cursor.close();
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
    }

    public void clearData(){
        twentyFive.setText("");
        oneHundred.setText("");
        fiveHundred.setText("");
        oneThousand.setText("");
        fiveThousand.setText("");
        total.setText(getString(R.string.total,0));
    }

    public void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (getActivity().getCurrentFocus() != null){
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);
        }
    }

    public void calculateMethod(){
        int value = 0;
        if (!twentyFive.getText().toString().equals("")){
            value += Integer.parseInt(twentyFive.getText().toString())*25;
        }
        if (!oneHundred.getText().toString().equals("")){
            value += Integer.parseInt(oneHundred.getText().toString())*100;
        }
        if (!fiveHundred.getText().toString().equals("")){
            value += Integer.parseInt(fiveHundred.getText().toString())*500;
        }
        if (!oneThousand.getText().toString().equals("")){
            value += Integer.parseInt(oneThousand.getText().toString())*1000;
        }
        if (!fiveThousand.getText().toString().equals("")){
            value += Integer.parseInt(fiveThousand.getText().toString())*5000;
        }
        total.setText(getString(R.string.total,value));
    }
}
