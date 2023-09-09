package com.example.mp7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class BackgroundSettingsActivity extends AppCompatActivity {

    private RadioGroup winner_Radio_buttons;
    private RadioGroup sport_Radio_Buttons;
    private Button setButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_settings);


        winner_Radio_buttons = findViewById(R.id.winnerRadioButton);
        sport_Radio_Buttons = findViewById(R.id.sportRadioButtons);
        setButton = findViewById(R.id.setButton);


        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pickedWinnerBackgroundRadioButtons = winner_Radio_buttons.getCheckedRadioButtonId();
                int pickedSportBackgroundRadioButtons = sport_Radio_Buttons.getCheckedRadioButtonId();

                if (pickedWinnerBackgroundRadioButtons != -1 && pickedSportBackgroundRadioButtons != -1) {
                    RadioButton pickedWinnerBackgroundRadio = findViewById(pickedWinnerBackgroundRadioButtons);
                    String pickedBackgroundWinner = pickedWinnerBackgroundRadio.getText().toString();

                    RadioButton pickedSportRadio = findViewById(pickedSportBackgroundRadioButtons);
                    String pickedSport = pickedSportRadio.getText().toString();

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BackgroundSettingsActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("pickedBackground_Winner", pickedBackgroundWinner);
                    editor.apply();

                    editor.putString("pickedBackgroundSport", pickedSport);
                    editor.apply();

                    Intent intent = new Intent();
                    intent.putExtra("pickedBackground_Winner", pickedBackgroundWinner);
                    intent.putExtra("pickedBackgroundSport", pickedSport);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}


