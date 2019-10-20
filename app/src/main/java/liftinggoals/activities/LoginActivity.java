package liftinggoals.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.liftinggoals.R;

import java.util.regex.Pattern;

import liftinggoals.data.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {
    private Button button;
    private EditText username;
    private EditText password;
    private CheckBox registering;
    private Toast toastMsg;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Setup database
        databaseHelper = new DatabaseHelper(this.getApplicationContext());
        databaseHelper.openDB();
        //databaseHelper.insert("Erik", "Krohn1", null, null);

        button = findViewById(R.id.login_button);
        username = findViewById(R.id.username_edit_text);
        password = findViewById(R.id.password_edit_text);
        registering = findViewById(R.id.login_checkbox);

        registering.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    button.setText("Registering");
                } else {
                    button.setText("Login");
                }
            }
        });
    }

    public void onDestroy()
    {
        super.onDestroy();
        databaseHelper.closeDB();
    }

    public void loginOrRegister (View view) {
        if (registering.isChecked()) {  //user is registering
            boolean validUser = validateUser();
            boolean validPassword = validatePass();
            if (validUser && validPassword) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        } else { //user is logging in
            String userNameInput = username.getEditableText().toString();
            String passwordInput = password.getEditableText().toString();

            if (databaseHelper.getUser(userNameInput, passwordInput) != null) {
                Intent startMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                startMainActivity.putExtra("username", "Jay-Ar");
                startActivity(startMainActivity);
                finish();   //Prevent user from pressing back button and going to login page
            } else {
                toastMsg = Toast.makeText(getApplicationContext(),
                        "Incorrect Username and/or password",
                        Toast.LENGTH_LONG);

                toastMsg.show();

                Cursor c = databaseHelper.getAllUsers();

                while(c.moveToNext()){
                    System.out.println("Username in db: " + c.getString(c.getColumnIndexOrThrow(DatabaseHelper.UserEntry.COLUMN_USERNAME)));
                }

            }

        }
    }

    private boolean validatePass() {
        String passwordInput = password.getEditableText().toString().trim();
        boolean valid = false;
        Pattern upperCasePattern = Pattern.compile("[A-Z]");
        Pattern lowerCasePattern = Pattern.compile("[a-z]");
        Pattern digitCasePattern = Pattern.compile("[0-9]");

        if (passwordInput.isEmpty()) {
            password.setError("Field can't be empty");
        } else if (passwordInput.length() < 6) {
            toastMsg = Toast.makeText(getApplicationContext(),
                    "Password must be at least 6 characters long",
                    Toast.LENGTH_SHORT);

            toastMsg.show();
            password.setError("Password must be at least 6 characters long");
        } else if (!digitCasePattern.matcher(passwordInput).find()) {
            toastMsg = Toast.makeText(getApplicationContext(),
                    "Password must contain a number",
                    Toast.LENGTH_SHORT);

            toastMsg.show();
            password.setError("Password must contain a number");
        } else if (!lowerCasePattern.matcher(passwordInput).find()) {
            toastMsg = Toast.makeText(getApplicationContext(),
                    "Password must contain a lower case letter",
                    Toast.LENGTH_SHORT);

            toastMsg.show();
            password.setError("Password must contain a lower case letter");
        } else if (!upperCasePattern.matcher(passwordInput).find()) {
            toastMsg = Toast.makeText(getApplicationContext(),
                    "Password must contain an upper case letter",
                    Toast.LENGTH_SHORT);

            toastMsg.show();
            password.setError("Password must contain an upper case letter");
        } else {
            password.setError(null);
            valid = true;
        }

        return valid;
    }

    private boolean validateUser() {
        String userNameInput = username.getEditableText().toString().trim();
        boolean valid = false;

        if (userNameInput.isEmpty()) {
            username.setError("Field can't be empty");
        } else if (userNameInput.length() < 4) {
            toastMsg = Toast.makeText(getApplicationContext(),
                    "UserName must be at least 4 characters long",
                    Toast.LENGTH_SHORT);

            toastMsg.show();
            username.setError("UserName must be at least 4 characters long");
        } else {
            username.setError(null);
            valid = true;
        }

        return valid;
    }
}
