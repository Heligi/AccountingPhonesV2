package com.example.accountingphonesv2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class file extends AppCompatActivity {
    String nameFile;
    Button backHome;
    ArrayList<String> actsList = new ArrayList<>();
    ListView listViewPhones;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        listViewPhones = (ListView) findViewById(R.id.listViewPhones);

        backHome = (Button) findViewById(R.id.backHome);
        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(file.this, MainActivity.class);
                startActivity(intent);
            }
        });

        getNameFIle();
        CreateListActs();
        WorkListView();
    }

    //заполнение ListView из списка
    public void WorkListView() {
        final ArrayList<String> Acts = new ArrayList<>();
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Acts);
        listViewPhones.setAdapter(adapter);
        for (int i = 0; i < actsList.size(); i++) {
            Acts.add(0, actsList.get(i));
        }
        adapter.notifyDataSetChanged();
    }

    //чтение списка из файла и запись в арЛист
    public void CreateListActs() {
        FileInputStream file = null;
        try {
            file = openFileInput(nameFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(file));
            String strLine;
            actsList.clear();
            while ((strLine = br.readLine()) != null) {
                actsList.add(strLine);
            }

        } catch (IOException ex) {

            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {

            try {
                if (file != null)
                    file.close();
            } catch (IOException ex) {

                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getNameFIle() {
        Bundle arguments = getIntent().getExtras();
        nameFile = Objects.requireNonNull(Objects.requireNonNull(arguments).get("nameFile")).toString();
    }
}
