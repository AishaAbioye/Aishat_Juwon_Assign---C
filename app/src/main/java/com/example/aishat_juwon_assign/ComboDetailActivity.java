package com.example.aishat_juwon_assign;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class ComboDetailActivity extends AppCompatActivity {
    private int currentInputIndex = 0;
    private int[] expectedSequence;
    private LinearLayout llImagesContainer;
    private TextView tvComboName;
    private boolean[] inputResults;
    int currentPosition = 0;

    private ComboDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combo_detail);

        dbHelper = new ComboDatabaseHelper(this);

        tvComboName = findViewById(R.id.tvComboName);
        tvComboName.setText(getIntent().getStringExtra("comboName"));
        llImagesContainer = findViewById(R.id.llImagesContainer);

        expectedSequence = getIntent().getIntArrayExtra("imageResources");
        setupImageSequence();

        setupButtonListeners();
    }

    private void setupImageSequence() {
        llImagesContainer.removeAllViews();
        for (int id : expectedSequence) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(id);
            imageView.setLayoutParams(getDefaultLayoutParams());
            llImagesContainer.addView(imageView);
        }
    }

    private LinearLayout.LayoutParams getDefaultLayoutParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (int) getResources().getDimension(R.dimen.image_size_small),
                (int) getResources().getDimension(R.dimen.image_size_small)
        );
        params.setMargins(4, 4, 4, 4);
        return params;
    }

    private void setupButtonListeners() {
        int[] buttonIds = {R.id.imgbtnup, R.id.imgbtndown, R.id.imgbtnright, R.id.imgbtnleft};
        int[] drawableIds = {R.drawable.up, R.drawable.down, R.drawable.right, R.drawable.left};

        for (int i = 0; i < buttonIds.length; i++) {
            ImageButton button = findViewById(buttonIds[i]);
            final int drawableId = drawableIds[i];
            button.setOnClickListener(v -> processButtonClick(drawableId));
        }
    }

    private void processButtonClick(int drawableId) {
        if (currentPosition < expectedSequence.length) {
            boolean isCorrect = expectedSequence[currentPosition] == drawableId;
            ImageView imageView = (ImageView) llImagesContainer.getChildAt(currentPosition);
            imageView.setImageResource(isCorrect ? R.drawable.btn_star_big_on : R.drawable.btn_star_big_off);
            currentPosition++;
            if (currentPosition == expectedSequence.length) {
                onComboComplete();
            }
        }
    }

    private void onComboComplete() {
        String comboName = getIntent().getStringExtra("comboName");
        boolean allCorrect = checkAllInputsCorrect();
        dbHelper.updateComboResult(comboName, allCorrect);  // Update the result in the database

        Intent returnIntent = new Intent();
        returnIntent.putExtra("position", getIntent().getIntExtra("position", -1));
        returnIntent.putExtra("allCorrect", allCorrect);
        setResult(RESULT_OK, returnIntent);

        finish();
    }

    private boolean checkAllInputsCorrect() {
        for (int i = 0; i < expectedSequence.length; i++) {
            ImageView imageView = (ImageView) llImagesContainer.getChildAt(i);
            if (imageView.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.btn_star_big_on).getConstantState()) {
                return false;
            }
        }
        return true;
    }


}
