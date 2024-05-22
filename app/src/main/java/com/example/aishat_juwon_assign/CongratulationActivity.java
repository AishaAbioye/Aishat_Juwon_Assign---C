package com.example.aishat_juwon_assign;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CongratulationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulation);

        TextView tvCongratulationMessage = findViewById(R.id.tvCongratulationMessage);
        TextView tvTotalCorrectCombinations = findViewById(R.id.tvTotalCorrectCombinations);


        int totalCorrectCombinations = getIntent().getIntExtra("totalCorrectCombinations", 0);


        tvCongratulationMessage.setText("Congratulations – You have completed all combinations!");
        tvTotalCorrectCombinations.setText("Total Correct Combinations: " + totalCorrectCombinations);
    }

    public void onCloseClick(View view) {
        finish();
    }
}
