package com.example.mp7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ContactInfoSettingsActivity extends AppCompatActivity {

    private EditText contactNumberEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info_settings);

        contactNumberEditText = findViewById(R.id.editTextForContactNumber);
        saveButton = findViewById(R.id.buttonToSave);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContactInfoNumber();
            }
        });
    }

    private void saveContactInfoNumber() {
        String contactNum = contactNumberEditText.getText().toString().trim();

        if (!contactNum.isEmpty()) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("contact_Info_number", contactNum);
            editor.apply();

            Intent intent = new Intent();
            intent.putExtra("contact_Info_number", contactNum);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            contactNumberEditText.setError("Enter Contact Number");
        }
    }
}


