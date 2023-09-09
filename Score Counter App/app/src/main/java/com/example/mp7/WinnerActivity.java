package com.example.mp7;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.widget.ImageView;
import android.widget.TextView;

import android.content.Intent;

import android.net.Uri;

import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WinnerActivity extends AppCompatActivity {

    private ImageView backgroundImageView;
    private TextView winningTeamTextView;
    private Button shareButton;
    private Button callButton;
    private Button messageButton;
    private Button searchButton;
    private Button saveContactInfoNumButton;
    private String winningTeam;
    private int winningScore;
    private int losingScore;
    private String contactInfoNumber;

    private static final int contact_info_pin = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);

        backgroundImageView = findViewById(R.id.background_image_view);

        winningTeamTextView = findViewById(R.id.text_view_for_winning_team);
        shareButton = findViewById(R.id.share_button);
        callButton = findViewById(R.id.call_button);
        messageButton = findViewById(R.id.message_button);
        searchButton = findViewById(R.id.search_button);
        saveContactInfoNumButton = findViewById(R.id.saveContactInfo);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedBackground = preferences.getString("pickedBackground_Winner", "");
        contactInfoNumber = preferences.getString("contact_Info_number", "");

        showWinnerChosenBackground(selectedBackground);

        if (savedInstanceState != null) {
            winningTeam = savedInstanceState.getString("winning_team");
            winningScore = savedInstanceState.getInt("score_of_winning_team");
            losingScore = savedInstanceState.getInt("score_of_losing_team");
            updateWinner();
        } else {
            Intent intent = getIntent();
            winningTeam = intent.getStringExtra("winning_team");
            winningScore = intent.getIntExtra("score_of_winning_team", 0);
            losingScore = intent.getIntExtra("score_of_losing_team", 0);
            updateWinner();
        }

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareChampionInfo();
            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchForArena();
            }
        });

        saveContactInfoNumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WinnerActivity.this, ContactInfoSettingsActivity.class);
                startActivityForResult(intent, contact_info_pin);
            }
        });
    }

    private void updateWinner() {
        String result = winningTeam + " wins by " + (winningScore - losingScore) + " points!";
        winningTeamTextView.setText(result);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("winning_team", winningTeam);
        outState.putInt("score_of_winning_team", winningScore);
        outState.putInt("score_of_losing_team", losingScore);
    }

    private void shareChampionInfo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String contactNumber = sharedPreferences.getString("contact_Info_number", "");

        if (!contactNumber.isEmpty()) {
            String message = "The champion is " + winningTeam + ", they won by "
                    + (winningScore - losingScore) + " points!\nContact: " + contactNumber;
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Champion Announcement");
            intent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(intent, "Share via"));
        }
    }
    private void makePhoneCall() {
        if (contactInfoNumber != null && !contactInfoNumber.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + contactInfoNumber));
            startActivity(intent);
        }
    }
    private void sendMessage() {
        String message = "The champion is " + winningTeam + " with a winning score of "
                + (winningScore - losingScore) + " points!";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("sms:"));
        intent.putExtra("smsMes", message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "SMS", Toast.LENGTH_SHORT).show();
        }
    }

    private void searchForArena() {
        String bb = "basketball arena";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + bb));
        startActivity(intent);
    }

    private void showWinnerChosenBackground(String background) {
        switch (background) {
            case "Medal":
                backgroundImageView.setImageResource(R.drawable.medal_background);
                break;
            case "Cup":
                backgroundImageView.setImageResource(R.drawable.cup_background);
                break;
            case "Thumbs Up":
                backgroundImageView.setImageResource(R.drawable.thumbs_up_background);
                break;
            default:
                backgroundImageView.setImageResource(R.drawable.default_background_winner);
                break;
        }
    }


    @Override
    protected void onActivityResult(int pin, int other, Intent data) {
        super.onActivityResult(pin, other, data);
        if (pin == contact_info_pin && other == RESULT_OK) {
            contactInfoNumber = data.getStringExtra("contact_Info_number");
        }
    }
}


