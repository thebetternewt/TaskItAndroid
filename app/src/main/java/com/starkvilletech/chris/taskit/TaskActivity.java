package com.starkvilletech.chris.taskit;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskActivity extends AppCompatActivity {

    public static final String EXTRA = "TaskExtra";
    private static final String TAG = "TaskActivity";

    private Calendar mCal;
    private Task mTask;
    private Button mDateButton;
    private EditText mTaskNameInput;
    private CheckBox mDoneBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        // Get Intent
        mTask = (Task)getIntent().getSerializableExtra(EXTRA);

        mCal = Calendar.getInstance();

        // Get UI elements and store in variables
        mTaskNameInput = (EditText)findViewById(R.id.task_name);
        mDateButton = (Button)findViewById(R.id.task_date);
        mDoneBox = (CheckBox)findViewById(R.id.task_done);
        Button saveButton = (Button)findViewById(R.id.save_button);

        // Assign values to variables/UI elements
        mTaskNameInput.setText(mTask.getName());

        if (mTask.getDueDate() == null) {
            mCal.setTime(new Date());
            mDateButton.setText(getResources().getString(R.string.no_date));
        } else{
            mCal.setTime(mTask.getDueDate());
            updateDateButton();
        }
        mDoneBox.setChecked(mTask.isDone());

        // Set on click listeners for buttons
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(TaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mCal.set(year, monthOfYear, dayOfMonth);
                        updateDateButton();
                    }
                }, mCal.get(Calendar.YEAR), mCal.get(Calendar.MONTH), mCal.get(Calendar.DAY_OF_MONTH));

                dpd.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTask.setName(mTaskNameInput.getText().toString());
                mTask.setDone(mDoneBox.isChecked());
                mTask.setDueDate(mCal.getTime());

                Intent i = new Intent();
                i.putExtra(EXTRA, mTask);
                setResult(RESULT_OK, i);
                finish();
            }
        });

    }

    private void updateDateButton(){
        DateFormat df = DateFormat.getDateInstance();
        mDateButton.setText(df.format(mCal.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task, menu);
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
}
