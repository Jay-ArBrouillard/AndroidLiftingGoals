package liftinggoals.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.liftinggoals.R;

import liftinggoals.data.DatabaseHelper;

public class RoutinesEditActivity extends AppCompatActivity {
    private EditText routineNameEditText;
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routines_edit);

        routineNameEditText = findViewById(R.id.activity_routines_edit_edit_text);
        db.openDB();

        routineNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //db.updateRoutineName(s.toString());
            }
        });

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.closeDB();
    }
}
