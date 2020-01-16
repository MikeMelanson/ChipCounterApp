package com.RenegadeSloth.mike.chipcounter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

public class DeleteDialog extends Dialog implements android.view.View.OnClickListener {

    private ChipCounterDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private int id;
    HistoryFragment historyFragment;

    public DeleteDialog(Activity a, int id, HistoryFragment frag){
        super(a);
        this.id = id;
        historyFragment = frag;
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.delete_dialog_layout);

        dbHelper = new ChipCounterDatabaseHelper(getContext());
        db = dbHelper.getWritableDatabase();

        Button confirm = findViewById(R.id.confirm_button);
        confirm.setOnClickListener(this);
        Button cancel = findViewById(R.id.cancel_button);
        cancel.setOnClickListener(this);
    }

    public void onClick(View v){
        if (v.getId()==R.id.cancel_button){
            dismiss();
        }
        else{
            deleteHistory();
            dismiss();
            historyFragment.reloadScreen();
        }
    }

    public void deleteHistory(){
        String selection = DatabaseDescription.History.COLUMN_ID + " = ?";
        String[] selection_args = {String.valueOf(id)};
        db.delete(DatabaseDescription.History.TABLE_NAME,selection,selection_args);
    }
}
