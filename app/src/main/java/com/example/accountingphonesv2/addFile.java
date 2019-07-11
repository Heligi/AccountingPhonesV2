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

public class addFile extends AppCompatActivity {

    Button saveFileButton;
    EditText nameFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_file);

        saveFileButton = (Button) findViewById(R.id.saveFileButton);
        nameFile = (EditText) findViewById(R.id.nameFile);

        saveFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameFile.getText() != null) {
                    someDo();
                    Intent intent = new Intent(addFile.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void someDo() {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(nameFile.getText().toString(), MODE_APPEND);
            String text = "Дата|Номер акта|Модель|Ремонт|Цена|Цена запчасти";
            fos.write(text.getBytes());
            Toast.makeText(this, "Файл создан", Toast.LENGTH_SHORT).show();
        } catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
