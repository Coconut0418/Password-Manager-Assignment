 package my.edu.utar.assinment1;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

 public class StoreActivity extends AppCompatActivity {

    private ListView PasswordListView;
    private Database dbHelper;
    private ArrayList<String> PasswordList;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new Database(this);
        PasswordListView = findViewById(R.id.PasswordListView);

        displayPasswords();

        FloatingActionButton fabAdd = findViewById(R.id.AddPassword);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This will open the input window
                showAddPasswordDialog();
            }
        });

        PasswordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedPassword = PasswordList.get(position);

                String[] lines = selectedPassword.split("\n");
                String website = lines[0].replace("Sites: ", "");
                String username = lines[1].replace("Username: ", "");
                String password = lines[2].replace("Password: ", "");

                showEditPasswordDialog(website,username,password);
            }
        });

        PasswordListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedPassword = PasswordList.get(position);

                String siteName = selectedPassword.split("\n")[0].replace("Sites: ", "");

                new android.app.AlertDialog.Builder(StoreActivity.this)
                        .setTitle("Delete Entry")
                        .setMessage("Are you sure you want to delete " + siteName + "?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            deleteEntry(siteName);
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });

        android.widget.ImageButton btnSettings = findViewById(R.id.setPassword);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePin();
            }
        });
    }

    private void showAddPasswordDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Add Password");

        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(50,40,50,10);

        final android.widget.EditText websiteEditText = new android.widget.EditText(this);
        websiteEditText.setHint("Website");
        layout.addView(websiteEditText);

        final android.widget.EditText usernameEditText = new android.widget.EditText(this);
        usernameEditText.setHint("Username");
        layout.addView(usernameEditText);

        final android.widget.EditText passwordEditText = new android.widget.EditText(this);
        passwordEditText.setHint("Password");
        layout.addView(passwordEditText);

        builder.setView(layout);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String website = websiteEditText.getText().toString().trim();
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (!website.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
                saveNewEntry(website, username, password);
            } else {
                Toast.makeText(StoreActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void saveNewEntry(String website, String username, String password) {
        android.content.ContentValues values = new android.content.ContentValues();
        values.put("website", website);
        values.put("username", username);
        values.put("password", password);

        android.database.sqlite.SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert("passwords", null, values);

        displayPasswords();
        android.widget.Toast.makeText(this, "Saved", android.widget.Toast.LENGTH_SHORT).show();
    }

    private void displayPasswords() {
        PasswordList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM passwords", null);

        if(cursor.moveToFirst()){
            do{
                String entry = "Sites: " + cursor.getString(1) +
                        "\nUsername: " + cursor.getString(2) +
                        "\nPassword: " + cursor.getString(3);
                PasswordList.add(entry);
            }while(cursor.moveToNext());
        }
        cursor.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, PasswordList);
        PasswordListView.setAdapter(adapter);
    }

    private void deleteEntry(String siteName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("passwords", "website = ?", new String[]{siteName});

        displayPasswords();
        Toast.makeText(this, "Delete Successful", Toast.LENGTH_SHORT).show();
    }

    private void showEditPasswordDialog(String Oldwebsite, String Oldusername, String Oldpassword) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Edit Password");

        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(50,40,50,10);

        final android.widget.EditText websiteEditText = new android.widget.EditText(this);
        websiteEditText.setText(Oldwebsite);
        layout.addView(websiteEditText);

        final android.widget.EditText usernameEditText = new android.widget.EditText(this);
        usernameEditText.setText(Oldusername);
        layout.addView(usernameEditText);

        final android.widget.EditText passwordEditText = new android.widget.EditText(this);
        passwordEditText.setText(Oldpassword);
        layout.addView(passwordEditText);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            updateEntry(Oldwebsite, websiteEditText.getText().toString(), usernameEditText.getText().toString(), passwordEditText.getText().toString());
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void updateEntry(String Oldwebsite, String website, String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("website", website);
        values.put("username", username);
        values.put("password", password);

        db.update("passwords", values, "website = ?", new String[]{Oldwebsite});

        displayPasswords();
        Toast.makeText(this, "Update Successful!", Toast.LENGTH_SHORT).show();
    }

     private void showChangePin() {
         android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
         builder.setTitle("Change PIN");

         android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
         layout.setOrientation(android.widget.LinearLayout.VERTICAL);
         layout.setPadding(50, 40, 50, 10);

         final android.widget.EditText newPinInput = new android.widget.EditText(this);
         newPinInput.setHint("Enter New 4-Digit PIN");
         newPinInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD);
         layout.addView(newPinInput);

         builder.setView(layout);

         builder.setPositiveButton("Update PIN", (dialog, which) -> {
             String newPin = newPinInput.getText().toString();
             if (newPin.length() == 4) {
                 android.content.SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                 prefs.edit().putString("MasterPIN", newPin).apply();
                 Toast.makeText(this, "PIN Changed Successfully!", Toast.LENGTH_SHORT).show();
             }else if (newPin.isEmpty()) {
                 Toast.makeText(this, "Please enter PIN", Toast.LENGTH_SHORT).show();
             } else {
                 Toast.makeText(this, "PIN must be 4 digits!", Toast.LENGTH_SHORT).show();
             }
         });
         builder.setNegativeButton("Cancel", null);
         builder.show();
     }
}