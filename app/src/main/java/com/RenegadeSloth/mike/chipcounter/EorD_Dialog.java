package com.RenegadeSloth.mike.chipcounter;

import android.app.Activity;
import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class EorD_Dialog extends Dialog implements android.view.View.OnClickListener {

    private ChipCounterDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private Activity activity;
    private int id;
    HistoryFragment parent;

    public EorD_Dialog(Activity a, int id, HistoryFragment parent){
        super(a);
        activity = a;
        this.id = id;
        this.parent = parent;
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.eord_layout);

        dbHelper = new ChipCounterDatabaseHelper(getContext());
        db = dbHelper.getWritableDatabase();

        TextView delete = findViewById(R.id.delete_button);
        delete.setOnClickListener(this);
        TextView edit = findViewById(R.id.edit_button);
        edit.setOnClickListener(this);
        Button cancel = findViewById(R.id.cancel_button);
        cancel.setOnClickListener(this);
    }

    public void onClick(View v){
        if (v.getId()==R.id.cancel_button){
            dismiss();
        }
        else if (v.getId()==R.id.delete_button){
            DeleteDialog deleteDialog = new DeleteDialog(activity,id,parent);
            deleteDialog.show();
            dismiss();
        }
        else{
            EditDialog editDialog = new EditDialog(activity,id,parent);
            editDialog.show();
            dismiss();
        }
    }
}
