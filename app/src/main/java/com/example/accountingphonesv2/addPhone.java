package com.example.accountingphonesv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;

public class addPhone extends AppCompatActivity {

    private final static String FILE_NAME = "content.txt";
    Button buttonSavePhone;
    EditText dateText;
    EditText idText;
    EditText modelText;
    EditText repairslText;
    EditText priceText;
    EditText priceDetailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phone);

        buttonSavePhone = findViewById(R.id.savePhoneB);
        dateText = findViewById(R.id.dateText);
        idText = findViewById(R.id.idText);
        modelText = findViewById(R.id.modelText);
        repairslText = findViewById(R.id.repairslText);
        priceText = findViewById(R.id.priceText);
        priceDetailText = findViewById(R.id.priceDetailText);

        buttonSavePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveText(view);
                Intent intent = new Intent(addPhone.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    // сохранение файла
    public void saveText(View view){

        FileOutputStream fos = null;
        try {

            String text = dateText.getText().toString() +" | "+ idText.getText().toString() + " | " + modelText.getText().toString() +
                    " | "+ repairslText.getText().toString() + " | " + priceText.getText().toString() +
                    " | "+ priceDetailText.getText().toString() + "\n";

            fos = openFileOutput(FILE_NAME, MODE_APPEND);
            fos.write(text.getBytes());
            Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
        }
        catch(IOException ex) {

            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally{
            try{
                if(fos!=null)
                    fos.close();
            }
            catch(IOException ex){

                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
