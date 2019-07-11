package com.example.accountingphonesv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listViewFile;
    File[] files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewFile = (ListView) findViewById(R.id.listFiles);
        addItemFile();

        listViewFile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, file.class);
                intent.putExtra("nameFile", files[i].getName());
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), files[i].getName().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //отображение имеющихся файлов
    private void addItemFile() {
        searchFile();
        final ArrayList<String> arrayFiles = new ArrayList<>();
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayFiles);
        listViewFile.setAdapter(adapter);
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                arrayFiles.add(files[i].getName());
            }
        } else arrayFiles.add("Файлы пока не созданны.");
        adapter.notifyDataSetChanged();
    }

    //создание списка файлов
    private void searchFile() {
        // путь к файлам -> /data/data/com.example.accountingphonesv2/files/
        String path = "/data/data/com.example.accountingphonesv2/files/";
        File file = new File(path);
        files = file.listFiles();
        if (files != null) {
            Toast.makeText(this, "Количество файлов: " + files.length, Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, "Название первого файла: " + files[0].getName(), Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "Файлы ещё не созданны.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.createFile_settings:
                intent = new Intent(MainActivity.this, addFile.class);
                startActivity(intent);
                return true;
            case R.id.deleteFile_settings:
                return true;
            case R.id.options_settings:
                return true;
            case R.id.add:
                intent = new Intent(MainActivity.this, addPhone.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
