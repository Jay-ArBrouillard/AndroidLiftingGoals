package liftinggoals.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.liftinggoals.R;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import liftinggoals.data.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private Button registerButton;
    private EditText username;
    private EditText password;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Setup database
        databaseHelper = new DatabaseHelper(this.getApplicationContext());
        databaseHelper.openDB();

        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);
        username = findViewById(R.id.username_edit_text);
        password = findViewById(R.id.password_edit_text);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    public void loginEvent (View view) {
        boolean validUsername = validateUserReq();
        boolean validPassword = validatePassReq();

        if (validUsername && validPassword)
        {
            LoadToast loadToast = new LoadToast(this);
            loadToast.setText("Login...");
            loadToast.setTranslationY(50);
            loadToast.show();

            loginUserDatabase(loadToast);
        }
    }

    public void registerEvent(View view)
    {
        boolean validUsername = validateUserReq();
        boolean validPassword = validatePassReq();

        if (validUsername && validPassword)
        {
            LoadToast loadToast = new LoadToast(this);
            loadToast.setText("Register...");
            loadToast.setTranslationY(50);
            loadToast.show();
            registerUserDatabase(loadToast);
        }
    }

    private void loginUserDatabase(final LoadToast loadToast)
    {
        final String userNameInput = username.getEditableText().toString();
        final String passwordInput = password.getEditableText().toString();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = String.format("http://3.221.56.60/login.php?username=%s&password=%s", userNameInput, passwordInput);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest (Request.Method.GET, url, null, new
                Response.Listener<JSONArray>() {
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject jso = response.getJSONObject(0);
                            if (jso.has("ErrorStatus")) {
                                String errorStatus = jso.getString("ErrorStatus");
                                if (errorStatus.equals("UserNonExisting")) {
                                    loadToast.error();
                                    Toast.makeText(getApplicationContext(), "The username you typed in doesn't exist", Toast.LENGTH_LONG).show();
                                } else if (errorStatus.equals("PasswordIncorrect")) {
                                    loadToast.error();
                                    Toast.makeText(getApplicationContext(), "Password is incorrect", Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                loadToast.success();
                                Toast.makeText(getApplicationContext(), "Successful login: " + userNameInput, Toast.LENGTH_LONG).show();
                                Intent startMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(startMainActivity);
                                finish();   //Prevent user from pressing back button and going to login page
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                loadToast.error();
                error.printStackTrace();
            }
        });

        queue.add(jsObjRequest);

    }

    private void registerUserDatabase(final LoadToast loadToast)
    {
        final String userNameInput = username.getEditableText().toString();
        final String passwordInput = password.getEditableText().toString();
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = String.format("http://3.221.56.60/register.php?username=%s&password=%s", userNameInput, passwordInput);
        StringRequest stringRequest = new StringRequest (Request.Method.GET, url, new
                Response.Listener<String>() {
                    public void onResponse(String response) {
                        if (response.equals("success"))
                        {
                            loadToast.success();
                            Toast.makeText(getApplicationContext(), "Successfully registered " + userNameInput, Toast.LENGTH_LONG).show();
                        }
                        else if (response.equals("UserExists"))
                        {
                            loadToast.error();
                            Toast.makeText(getApplicationContext(), "Username \"" + userNameInput + "\" already exists!", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            loadToast.error();
                            Toast.makeText(getApplicationContext(), "Error registering", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                loadToast.error();
                error.printStackTrace();
            }
        });
        queue.add(stringRequest);

    }

    private boolean validatePassReq() {
        String passwordInput = password.getEditableText().toString().trim();
        boolean valid = false;
        Pattern upperCasePattern = Pattern.compile("[A-Z]");
        Pattern lowerCasePattern = Pattern.compile("[a-z]");
        Pattern digitCasePattern = Pattern.compile("[0-9]");

        if (passwordInput.isEmpty()) {
            password.setError("Field can't be empty");
        } else if (passwordInput.length() < 6) {
            password.setError("Password must be at least 6 characters long");
        } else if (!digitCasePattern.matcher(passwordInput).find()) {
            password.setError("Password must contain a number");
        } else if (!lowerCasePattern.matcher(passwordInput).find()) {
            password.setError("Password must contain a lower case letter");
        } else if (!upperCasePattern.matcher(passwordInput).find()) {
            password.setError("Password must contain an upper case letter");
        } else {
            password.setError(null);
            valid = true;
        }

        return valid;
    }

    private boolean validateUserReq() {
        String userNameInput = username.getEditableText().toString().trim();

        if (userNameInput.isEmpty()) {
            username.setError("Field can't be empty");
            return false;
        }

        if (userNameInput.length() < 4) {
            Toast.makeText(getApplicationContext(),"UserName must be at least 4 characters long", Toast.LENGTH_SHORT).show();
            username.setError("UserName must be at least 4 characters long");
            return false;
        }

        username.setError(null);
        return true;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("username", username.getEditableText().toString());
        outState.putString("password", password.getEditableText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        username.setText(savedInstanceState.getString("username"));
        password.setText(savedInstanceState.getString("password"));
    }

    public void onDestroy()
    {
        super.onDestroy();
        databaseHelper.closeDB();
    }
}
