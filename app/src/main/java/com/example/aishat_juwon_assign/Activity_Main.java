package com.example.aishat_juwon_assign;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Activity_Main extends AppCompatActivity implements ComboInteractionListener {
    private List<ComboItem> comboItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private ComboAdapter adapter;
    private TextView tvCorrectCombos;
    private Button btnRestart;
    private int[][] imagesPerCombo;
    private int correctCombosCount = 0;
    private ComboDatabaseHelper dbHelper;
    private ActivityResultLauncher<Intent> comboDetailLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCorrectCombos = findViewById(R.id.tvCorrectCombos);
        btnRestart = findViewById(R.id.btnRestart);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new ComboDatabaseHelper(this);

        String[] comboNames = {"Reinforce", "Resupply", "Eagle Rearm", "Eagle Airstrike", "Eagle 500kg Bomb"};
        imagesPerCombo = new int[][]{
                {R.drawable.left, R.drawable.right, R.drawable.up, R.drawable.down, R.drawable.left},
                {R.drawable.up, R.drawable.down, R.drawable.left, R.drawable.right},
                {R.drawable.up, R.drawable.up, R.drawable.right, R.drawable.up, R.drawable.left},
                {R.drawable.up, R.drawable.right, R.drawable.down, R.drawable.left},
                {R.drawable.up, R.drawable.right, R.drawable.down, R.drawable.down, R.drawable.down}
        };

        for (int i = 0; i < comboNames.length; i++) {
            comboItems.add(new ComboItem(comboNames[i], imagesPerCombo[i]));
        }

        adapter = new ComboAdapter(this, comboItems, imagesPerCombo, this);
        recyclerView.setAdapter(adapter);

        btnRestart.setOnClickListener(v -> restartGame());

        comboDetailLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Log.d("ActivityResult", "Result received");
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        int position = data.getIntExtra("position", -1);
                        boolean allCorrect = data.getBooleanExtra("allCorrect", false);
                        Log.d("ActivityResult", "Position: " + position + ", All Correct: " + allCorrect);
                        if (position != -1) {
                            updateComboList(position, allCorrect);
                            if (allCorrect) {
                                correctCombosCount++;
                                tvCorrectCombos.setText("Correct Combos: " + correctCombosCount);
                                checkAllCombosAttempted();
                            }
                        }
                    } else {
                        Log.d("ActivityResult", "Result NOT OK or data is null");
                    }
                }
        );
    }

    private void shuffleCombinations() {
        if (imagesPerCombo != null) {
            for (int[] combo : imagesPerCombo) {
                List<Integer> list = new ArrayList<>();
                for (int img : combo) {
                    list.add(img);
                }
                Collections.shuffle(list);
                for (int i = 0; i < combo.length; i++) {
                    combo[i] = list.get(i);
                }
            }
            adapter.notifyDataSetChanged();
        } else {
            Log.e("shuffleCombinations", "imagesPerCombo is null");
        }
    }

    private void restartGame() {
        try {
            shuffleCombinations();
            correctCombosCount = 0;
            tvCorrectCombos.setText("Correct Combos: 0");
            for (ComboItem item : comboItems) {
                item.setAttempted(false);
                item.setResult(false);
                dbHelper.updateComboAttempt(item.getName(), false, false);
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e("restartGame", "Error during game restart", e);
        }
    }

    private void updateComboList(int position, boolean allCorrect) {
        ComboItem item = comboItems.get(position);
        item.setAttempted(true);
        item.setResult(allCorrect);
        dbHelper.updateComboAttempt(item.getName(), true, allCorrect);
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onComboSelected(int position, ComboItem comboItem) {
        if (dbHelper.recordExists(comboItem.getName())) {
            dbHelper.updateComboAttempt(comboItem.getName(), false, false);
        } else {
            dbHelper.insertComboAttempt(comboItem.getName(), false, false);
        }

        Intent intent = new Intent(this, ComboDetailActivity.class);
        intent.putExtra("comboName", comboItem.getName());
        intent.putExtra("imageResources", comboItem.getImageResources());
        intent.putExtra("position", position);
        comboDetailLauncher.launch(intent);
    }

    private void checkAllCombosAttempted() {
        boolean allAttempted = true;
        for (ComboItem item : comboItems) {
            if (!item.isAttempted()) {
                allAttempted = false;
                break;
            }
        }
        if (allAttempted) {
            Intent intent = new Intent(this, CongratulationActivity.class);
            intent.putExtra("totalCorrectCombos", correctCombosCount);
            Log.d("checkAllCombosAttempted", "Starting CongratulationActivity with totalCorrectCombos: " + correctCombosCount);
            startActivity(intent);
        }
    }
}
