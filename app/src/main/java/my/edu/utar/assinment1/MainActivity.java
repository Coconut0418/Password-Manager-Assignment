package my.edu.utar.assinment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{

    private EditText InputPassword;
    private Button LoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputPassword = findViewById(R.id.InputPassword);
        LoginButton = findViewById(R.id.LoginButton);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = InputPassword.getText().toString();

                android.content.SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                String correctPin = prefs.getString("MasterPIN", "1234");

                if (password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                } else if (password.equals(correctPin)) {
                    Intent intent = new Intent(MainActivity.this, StoreActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}