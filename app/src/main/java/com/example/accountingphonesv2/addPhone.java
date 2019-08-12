package com.example.accountingphonesv2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class addPhone extends AppCompatActivity {

    String nameFile;
    Button buttonSavePhone;
    EditText dateText;
    EditText idText;
    EditText modelText;
    EditText repairslText;
    EditText priceText;
    EditText priceDetailText;
    ElementV1 elementV1;
    FloatingActionButton floatingActionButtonBackInAddPhone;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
        floatingActionButtonBackInAddPhone = findViewById(R.id.floatingActionButtonBackInAddPhone);

        buttonSavePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveText(view);
                Intent intent = new Intent(addPhone.this, fileC.class);
                intent.putExtra("nameFile", nameFile);
                startActivity(intent);
            }
        });

        floatingActionButtonBackInAddPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(addPhone.this, fileC.class);
                intent.putExtra("nameFile", nameFile);
                startActivity(intent);
            }
        });

        getNameFIle();
    }

    // сохранение файла
    public void saveText(View view) {
        FileOutputStream fos = null;
        try {
/*            String text = dateText.getText().toString() +" | "+ idText.getText().toString() + " | " + modelText.getText().toString() +
                    " | "+ repairslText.getText().toString() + " | " + priceText.getText().toString() +
                    " | "+ priceDetailText.getText().toString() + "\n";*/
            elementV1 = new ElementV1(dateText.getText().toString(), idText.getText().toString(), modelText.getText().toString(),
                    repairslText.getText().toString(), priceText.getText().toString(), priceDetailText.getText().toString());

            fos = openFileOutput(nameFile, MODE_APPEND);
            //fos.write(text.getBytes());
            fos.write(elementV1.toString().getBytes());
            Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getNameFIle() {
        Bundle arguments = getIntent().getExtras();
        //nameFile = Objects.requireNonNull(Objects.requireNonNull(arguments).get("nameFile")).toString();
        String string = Objects.requireNonNull(Objects.requireNonNull(arguments).get("nameFile")).toString();
        String[] sp = string.split("~");
        if (sp.length == 1){
            nameFile = sp[0];
        }
        if(sp.length == 7){
            nameFile = sp[0];
             dateText.setText(sp[1]);
             idText.setText(sp[2]);
             modelText.setText(sp[3]);
             repairslText.setText(sp[4]);
             priceText.setText(sp[5]);
             priceDetailText.setText(sp[6]);
        }
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
    }
}
