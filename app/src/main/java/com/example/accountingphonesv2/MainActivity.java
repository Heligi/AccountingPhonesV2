package com.example.accountingphonesv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ListView listViewFile;
    File[] files;
    FloatingActionButton floatingActionButton;
    private LinearLayout pdf_layout;
    private static final int STORAGE_CODE = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton1);

        listViewFile = (ListView) findViewById(R.id.listFiles);
        addItemFile();

        listViewFile.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //выполнение нажания с задержкой
                return false;
            }
        });
        listViewFile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                popupMenuShow(view, i);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, addFile.class);
                startActivity(intent);
            }
        });

    }

    //всплывающее меню
    private void popupMenuShow(View view, final int i) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.poupup_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.one:
                        Intent intent = new Intent(MainActivity.this, fileC.class);
                        intent.putExtra("nameFile", files[i].getName());
                        startActivity(intent);
                        //Toast.makeText(getApplicationContext(), files[i].getName().toString(), Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.two:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Важное сообщение!")
                                .setMessage("Точно удалить!")
                                .setCancelable(false)
                                .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        File file = new File(String.valueOf(files[i]));
                                        file.delete();
                                        addItemFile();
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        return true;
                    case R.id.three:
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                        builder2.setTitle("Rename");
                        View viewInflated = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_signin, null);
                        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                        builder2.setView(viewInflated);

                        builder2.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                String m_Text = "001";
                                m_Text = input.getText().toString();
                                renameFile(i, m_Text);
                            }
                        });
                        builder2.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder2.show();                        //renameFile(i);
                        // action
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                Toast.makeText(getApplicationContext(), "onDismiss", Toast.LENGTH_SHORT).show();
            }
        });
        popupMenu.show();
    }

    //переипенование файла
    private void renameFile(int i, String str) {
        ArrayList<String> strings = new ArrayList<>();
        FileInputStream file = null;
        try {
            file = openFileInput(files[i].getName());
            BufferedReader br = new BufferedReader(new InputStreamReader(file));
            String strLine;
            strings.clear();
            while ((strLine = br.readLine()) != null) {
                strings.add(strLine);
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
        FileOutputStream fos = null;
        try {
            for (int j = 0; j < strings.size(); j++) {
                fos = openFileOutput(str, MODE_APPEND);
                String string = strings.get(j) + "\n";
                fos.write(string.getBytes());
            }
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
        File filee = new File(String.valueOf(files[i]));
        filee.delete();
        addItemFile();
    }

    //отображение имеющихся файлов
    private void addItemFile() {
        searchFile();
        ArrayList<String> arrayFiles = new ArrayList<>();
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayFiles);
        listViewFile.setAdapter(adapter);
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                arrayFiles.add(files[i].getName());
            }
        } // else arrayFiles.add("Файлы пока не созданны.");
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
            Arrays.sort(files, new Comparator() {
                @Override
                public int compare(Object f1, Object f2) {
                    return ((File) f1).getName().compareTo(((File) f2).getName());
                }
            });
            //Toast.makeText(this, "Название первого файла: " + files[0].getName(), Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "Файлы ещё не созданны.", Toast.LENGTH_SHORT).show();
    }

    // Создание PDF
    private void savePdf() {
        Document mDoc = new Document();
        String mFileName = new SimpleDateFormat("yyyy_MM_dd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        String mFilePath = Environment.getExternalStorageDirectory() + "/" + mFileName + ".pdf";
        try {
            PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath));
            mDoc.open();
            String mText = "Text tttt ttt tttt";
            mDoc.addAuthor("Atif Prvaiz");
            mDoc.add(new Paragraph(mText));
            mDoc.close();
            Toast.makeText(this, mFileName + ".pdf\nis save to\n" + mFilePath, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    savePdf();
                } else {
                    Toast.makeText(this, "Permissino denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



    // МЕНЮ --
    /*@Override
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
        }
        return super.onOptionsItemSelected(item);
    }*/
}

