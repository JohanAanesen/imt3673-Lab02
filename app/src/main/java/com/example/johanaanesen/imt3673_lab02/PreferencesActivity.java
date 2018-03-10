package com.example.johanaanesen.imt3673_lab02;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class PreferencesActivity extends AppCompatActivity {

    static final String ITEM_SPINNER = "item-spinner";
    static final String REFRESH_SPINNER = "refresh-spinner";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        //setup spinners //
        spinnerSelect((Spinner)findViewById(R.id.itemSpinner), ITEM_SPINNER, R.array.max_Items);
        spinnerSelect((Spinner)findViewById(R.id.refreshSpinner), REFRESH_SPINNER, R.array.refresh_Items);

        //url saved?
        URLSelect();
    }


    private void spinnerSelect(View view, String save, int itemArray){

        final String SAVE = save;

        //spinner
        final Spinner spinner =  (Spinner)view;
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                itemArray,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Get spinner shared state
        final SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        final int userChoice = shared.getInt(SAVE, -1);

        if (userChoice == -1) {
            //Error, set to 0
            spinner.setSelection(0);
        } else {
            spinner.setSelection(userChoice);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {


                Object item = adapterView.getItemAtPosition(position);
                if (item != null)
                {
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putInt(SAVE, position); // Storing integer
                    editor.apply(); // commit changes

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                SharedPreferences.Editor editor = shared.edit();
                editor.putInt(SAVE, 0); // Storing default integer
                editor.apply(); // commit changes
            }
        });
    }

    private void URLSelect(){
        EditText editText = findViewById(R.id.prefUrl);

        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        String userChoice = shared.getString("URL", "https://www.vg.no/rss/feed");
        editText.setText(userChoice);
    }

    public void save(View view){
        EditText editText = findViewById(R.id.prefUrl);
        String text = editText.getText().toString();

        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = shared.edit();


        editor.putString("URL", text);
        editor.apply();

        Intent intent = new Intent(this, HomeActivity.class);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, HomeActivity.class);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
