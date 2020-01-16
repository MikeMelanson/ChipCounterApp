package com.RenegadeSloth.mike.chipcounter;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.RenegadeSloth.mike.chipcounter.DatabaseDescription.*;

public class HistoryFragment extends Fragment{

    private ChipCounterDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private LinearLayout histories;

    private HistoryFragment thisFrag;

    private TextView dateView,spentView,wonView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.history_fragment, container, false);

        dbHelper = new ChipCounterDatabaseHelper(getActivity().getBaseContext());
        db = dbHelper.getWritableDatabase();

        thisFrag = this;

        histories = view.findViewById(R.id.histories);

        Button newButton = view.findViewById(R.id.newButton);
        Button totalsButton = view.findViewById(R.id.totalsButton);
        ScrollView scrollView = view.findViewById(R.id.scrollView);
        scrollView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewHistoryDialog newHistoryDialog = new NewHistoryDialog(getActivity(),thisFrag);
                newHistoryDialog.show();
                reloadScreen();
            }
        });

        totalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TotalsDialog(getActivity()).show();
            }
        });
        reloadScreen();
        return view;
    }

    public void reloadScreen(){
        hideSoftKeyboard();
        histories.removeAllViews();

        String[] projection = {History.COLUMN_ID,History.COLUMN_DATE,History.COLUMN_SPENT,History.COLUMN_WON};
        Cursor cursor = db.query(History.TABLE_NAME,projection,null,null,null,null,null,null);
        cursor.moveToFirst();

        int size = cursor.getCount();
        for (int i=0;i<size;i++){
            final int id = cursor.getInt(cursor.getColumnIndexOrThrow(History.COLUMN_ID));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(History.COLUMN_DATE));
            float spent = cursor.getFloat(cursor.getColumnIndexOrThrow(History.COLUMN_SPENT));
            float won = cursor.getFloat(cursor.getColumnIndexOrThrow(History.COLUMN_WON));

            View history = getLayoutInflater().inflate(R.layout.history_entry,null);

            dateView = history.findViewById(R.id.date);
            spentView = history.findViewById(R.id.spent);
            wonView = history.findViewById(R.id.won);

            dateView.setText(getString(R.string.date,date));
            spentView.setText(getString(R.string.spent,spent));
            wonView.setText(getString(R.string.won,won));

            history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EorD_Dialog eorD_dialog = new EorD_Dialog(getActivity(),id,thisFrag);
                    eorD_dialog.show();
                }
            });

            histories.addView(history);
            cursor.moveToNext();
        }
        cursor.close();
        hideSoftKeyboard();
    }

    public static HistoryFragment newInstance(){
        HistoryFragment historyFragment = new HistoryFragment();
        return historyFragment;
    }

    public void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (getActivity().getCurrentFocus() != null){
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);
        }
    }

    public String getDate(){
        String date = (String)dateView.getText();
        date = date.replace("Date: ","");
        return date;
    }

    public String getSpent(){
        String spent = (String)spentView.getText();
        spent = spent.replace("Spent: ","");
        return spent;
    }

    public String getWon(){
        String won = (String)wonView.getText();
        won = won.replace("Won: ","");
        return won;
    }
}
