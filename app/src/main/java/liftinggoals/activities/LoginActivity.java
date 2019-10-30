package liftinggoals.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.liftinggoals.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import liftinggoals.data.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {
    private Button button;
    private EditText username;
    private EditText password;
    private CheckBox registering;
    private Toast toastMsg;
    private DatabaseHelper databaseHelper;
    private final String URL_REGISTER = "http://3.221.56.60/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Setup database
        databaseHelper = new DatabaseHelper(this.getApplicationContext());
        databaseHelper.openDB();

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

    public void loginOrRegister (View view) {
        final String userNameInput = username.getEditableText().toString();
        final String passwordInput = password.getEditableText().toString();

        //Added code login using remote database
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = String.format("http://3.221.56.60/register.php?username=%s&password=%s", userNameInput, passwordInput);
        StringRequest stringRequest = new StringRequest (Request.Method.GET, url, new
                Response.Listener<String>() {
                    public void onResponse(String response) {
                        if (response.equals("1"))
                        {
                            Toast.makeText(getApplicationContext(), "Successfully registered: " + userNameInput, Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Error registering: " + userNameInput, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "onErrorResponse: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);


        /* JsonArrayRequest
        JsonArrayRequest jsObjRequest = new JsonArrayRequest (Request.Method.POST, url, null, new
                        Response.Listener<JSONArray>() {
                    public void onResponse(JSONArray response) {

                        try {
                            for(int i = 0; i < response.length(); i++){
                                JSONObject jso = response.getJSONObject(i);
                                int id = jso.getInt("user_id");
                                String username = jso.getString("username");

                                Toast.makeText(getApplicationContext(), "Successfully registered: " + username + "(" + id + ")", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "onErrorResponse: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
        });
        queue.add(jsObjRequest);
        */


         //End Added code

        /*
        //Login using local databases
        if (registering.isChecked()) {  //user is registering
            boolean validUsername = validateUserReq();
            boolean validPassword = validatePassReq();
            if (databaseHelper.getUsername(userNameInput) != null)
            {
                Toast.makeText(getApplicationContext(),userNameInput + " is already registered", Toast.LENGTH_SHORT).show();
            }
            else if (validUsername && validPassword)
            {
                databaseHelper.insert(userNameInput, passwordInput, null, null);

                //Ensure new user was successfully added to database
                if (databaseHelper.getUser(userNameInput, passwordInput) != null)
                {
                    Toast.makeText(getApplicationContext(),"Successfully added new user to Database!", Toast.LENGTH_SHORT).show();

                    //Lab 5
                    SharedPreferences sp = getSharedPreferences("com.example.Brouillard.lab5", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("username", userNameInput);
                    editor.commit();
                    //Lab 5 end

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Error adding user to database", Toast.LENGTH_SHORT).show();
                }
            }

        }
        else { //user is logging in

            if (databaseHelper.getUsername(userNameInput) == null)
            {
                Toast.makeText(getApplicationContext(),"Username doesn't exist", Toast.LENGTH_SHORT).show();
            }
            else
            {
                if (databaseHelper.getUser(userNameInput, passwordInput) != null) {
                    Toast.makeText(getApplicationContext(),"Successful login", Toast.LENGTH_SHORT).show();

                    //Lab 5
                    SharedPreferences sp = getSharedPreferences("com.example.Brouillard.lab5", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("username", userNameInput);
                    editor.commit();
                    //Lab 5 end

                    Intent startMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(startMainActivity);
                    finish();   //Prevent user from pressing back button and going to login page
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Username or password is incorrect", Toast.LENGTH_LONG).show();
                }
            }

        }*/
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
            Toast.makeText(getApplicationContext(),"Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            password.setError("Password must be at least 6 characters long");
        } else if (!digitCasePattern.matcher(passwordInput).find()) {
            Toast.makeText(getApplicationContext(),"Password must contain a number", Toast.LENGTH_SHORT).show();
            password.setError("Password must contain a number");
        } else if (!lowerCasePattern.matcher(passwordInput).find()) {
            Toast.makeText(getApplicationContext(),"Password must contain a lower case letter", Toast.LENGTH_SHORT).show();
            password.setError("Password must contain a lower case letter");
        } else if (!upperCasePattern.matcher(passwordInput).find()) {
            Toast.makeText(getApplicationContext(),"Password must contain an upper case letter", Toast.LENGTH_SHORT).show();
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

    public void onDestroy()
    {
        super.onDestroy();
        databaseHelper.closeDB();
    }
}
