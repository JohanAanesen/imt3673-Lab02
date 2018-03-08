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

    static final String SPINNER_CHOICE = "spinner-choice";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        //setup spinner //
        spinnerSelect();

        //url saved?
        URLSelect();
    }


    private void spinnerSelect(){

        //spinner
        final Spinner spinner =  findViewById(R.id.prefSpinner);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.max_Items,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Get spinner shared state
        final SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        final int userChoice = shared.getInt(SPINNER_CHOICE, -1);

        if (userChoice == -1) {
            //Error?
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
                    editor.putInt(SPINNER_CHOICE, position); // Storing integer
                    editor.apply(); // commit changes

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                //Error?
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
        startActivity(intent);
    }
}
