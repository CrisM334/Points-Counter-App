package com.example.mp7;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private ImageView backgroundImageView;
    private TextView textViewForTeamAScore;
    private TextView textViewForTeamBScore;
    private Button buttonForTeamA;
    private Button buttonForTeamB;
    private Button buttonPickBackground;

    private int score_for_team_A = 0;
    private int score_for_team_B = 0;

    private String chosenSport;
    private static final int selected_sport = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        backgroundImageView = findViewById(R.id.background_image_view);
        textViewForTeamAScore = findViewById(R.id.text_view_for_team_a_score);
        textViewForTeamBScore = findViewById(R.id.text_view_for_team_b_score);
        buttonForTeamA = findViewById(R.id.button_for_team_A);
        buttonForTeamB = findViewById(R.id.button_for_team_B);
        buttonPickBackground = findViewById(R.id.choseBackground);


        buttonForTeamA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementScore(true);
            }
        });

        buttonForTeamB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementScore(false);
            }
        });

        buttonPickBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBackgroundSettingsActivity();
            }
        });

        if (savedInstanceState != null) {
            score_for_team_A = savedInstanceState.getInt("teamAScore");
            score_for_team_B = savedInstanceState.getInt("teamBScore");
            updateScores();
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        chosenSport = preferences.getString("pickedBackgroundSport", "");

        setBackgroundForSport(chosenSport);
    }

    private void incrementScore(boolean ifTeamA) {
        if (ifTeamA) {
            score_for_team_A++;
            textViewForTeamAScore.setText(String.valueOf(score_for_team_A));
            if (score_for_team_A == 5) {
                declareWinner("Team A", score_for_team_A, score_for_team_B);
            }
        } else {
            score_for_team_B++;
            textViewForTeamBScore.setText(String.valueOf(score_for_team_B));
            if (score_for_team_B == 5) {
                declareWinner("Team B", score_for_team_B, score_for_team_A);
            }
        }
    }

    private void declareWinner(String winningTeam, int scoreOfWinningTeam, int scoreOfLosingTeam) {
        Intent intent = new Intent(MainActivity.this, WinnerActivity.class);
        intent.putExtra("winning_team", winningTeam);
        intent.putExtra("score_of_winning_team", scoreOfWinningTeam);
        intent.putExtra("score_of_losing_team", scoreOfLosingTeam);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("teamAScore", score_for_team_A);
        outState.putInt("teamBScore", score_for_team_B);
    }

    private void updateScores() {
        textViewForTeamAScore.setText(String.valueOf(score_for_team_A));
        textViewForTeamBScore.setText(String.valueOf(score_for_team_B));
    }

    private void setBackgroundForSport(String sport) {
        switch (sport) {
            case "Football":
                backgroundImageView.setImageResource(R.drawable.football_background);
                break;
            case "Basketball":
                backgroundImageView.setImageResource(R.drawable.basketball_background);
                break;
            case "Baseball":
                backgroundImageView.setImageResource(R.drawable.baseball_background);
                break;
            case "Soccer":
                backgroundImageView.setImageResource(R.drawable.soccer_background);
                break;
            default:
                backgroundImageView.setImageResource(R.drawable.default_background);
                break;
        }
    }


    private void showBackgroundSettingsActivity() {
        Intent intent = new Intent(MainActivity.this, BackgroundSettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int sportPin, int other, Intent data) {
        super.onActivityResult(sportPin, other, data);
        if (sportPin == selected_sport && other == RESULT_OK) {
            if (data != null && data.hasExtra("pickedBackgroundSport")) {
                String selectedSport = data.getStringExtra("pickedBackgroundSport");
                setBackgroundForSport(selectedSport);
            }
        }
    }

    private void saveFavoriteTeamName(String teamName) {
        getSharedPreferences("preference", MODE_PRIVATE)
                .edit()
                .putString("favoriteTeamName", teamName)
                .apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.main_menu, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mI) {
        int id = mI.getItemId();
        if (id == R.id.action_save_favorite) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.activity_favorites_setting, null);
            builder.setView(view);
            final EditText editTextName = view.findViewById(R.id.editTextName);

            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int fav) {
                    String name = editTextName.getText().toString();
                    saveFavoriteTeamName(name);
                    Toast.makeText(MainActivity.this, "Favorite Team: " + name, Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Cancel", null);

            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(mI);
    }

}


