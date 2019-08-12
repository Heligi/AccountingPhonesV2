package com.example.accountingphonesv2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class fileC extends AppCompatActivity {
    String nameFile;
    ArrayList<String> actsList = new ArrayList<>();
    ArrayList<ElementV1> elementV1ArrayList = new ArrayList<>();
    ListView listViewPhones;
    FloatingActionButton floatingActionButtonAddPhone;
    FloatingActionButton floatingActionButtonBack;
    FloatingActionButton floatingActionButtonReports;
    private static final int STORAGE_CODE = 1000;
    FloatingActionButton floatingActionButtonSomeDy;

    public fileC() {
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);


        floatingActionButtonBack = (FloatingActionButton) findViewById(R.id.floatingActionButtonBack);
        floatingActionButtonAddPhone = (FloatingActionButton) findViewById(R.id.floatingActionButtonAddPhone);
        floatingActionButtonReports = (FloatingActionButton) findViewById(R.id.floatingActionButtonReports);

        floatingActionButtonSomeDy = (FloatingActionButton) findViewById(R.id.floatingActionButtonSomeDy);

        listViewPhones = (ListView) findViewById(R.id.listViewPhones);

        floatingActionButtonAddPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fileC.this, addPhone.class);
                intent.putExtra("nameFile", nameFile);
                startActivity(intent);
            }
        });

        floatingActionButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fileC.this, MainActivity.class);
                startActivity(intent);
            }
        });
        floatingActionButtonReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        // permission was not granted
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, STORAGE_CODE);
                    } else {
                        //permission already granted, call method savePdf
                        savePdf();
                    }
                } else {
                    // System OS < Marshmallow
                    savePdf();
                }
            }
        });


        //отправка файла по почте --
        floatingActionButtonSomeDy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // впихнуть потом вызов функции создания этого файла!!!!
                //-------------------------------------

                /*Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("application/pdf");
                shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "abc@gmail.com" });
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Отчёт " +    nameFile);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Отчёт");
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("/PDF/"+nameFile+".pdf"));
                if (shareIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(shareIntent);
                }*/

//                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//                StrictMode.setVmPolicy(builder.build());
//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setType("application/pdf");
//                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF/" + nameFile + ".pdf");
//                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                startActivity(Intent.createChooser(shareIntent, "Share PDF using.."));

                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                Intent shareIntent = new Intent(Intent.ACTION_SEND);

                File file = new File(Environment.getExternalStorageDirectory(),"/tut.pdf");
                Uri bmpUri = (Uri) FileProvider.getUriForFile(fileC.this,"com.example.accountingphonesv2", file);
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                shareIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, "Share.."));

            }
        });

        getNameFIle();
        CreateListActs();
        WorkListViewArray(); // Заполнение по строчно()старый способ = WorkListView();

        listViewPhones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                popupMenuShow(view, i);
            }
        });
    }


    //заполнение ListView из списка
    public void WorkListView() {
        final ArrayList<String> Acts = new ArrayList<>();
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Acts);
        listViewPhones.setAdapter(adapter);
        if (actsList != null) {
            for (int i = 0; i < actsList.size(); i++) {
                Acts.add(0, actsList.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }

    //заполнение listView из массива
    public void WorkListViewArray() {

        ElementV1 elementV1 = new ElementV1();
        if (actsList != null) {
            elementV1ArrayList = elementV1.reads(actsList);
        }
        final ArrayList<String> Acts = new ArrayList<>();
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Acts);
        listViewPhones.setAdapter(adapter);
        if (elementV1ArrayList != null) { //if (actsList != null) {
            for (int i = 0; i < elementV1ArrayList.size(); i++) {
                Acts.add(i, elementV1ArrayList.get(i).toString());
            }
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
        // Убрать или потом скрыть (кол-во записей в файле)
        //Toast.makeText(this, "actsList.size()=" + actsList.size(), Toast.LENGTH_SHORT).show();
        if (actsList.size() != 0) {
            Toast.makeText(this, "Кол-во ремонтов=" + (actsList.size() / 7), Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getNameFIle() {
        Bundle arguments = getIntent().getExtras();
        nameFile = Objects.requireNonNull(Objects.requireNonNull(arguments).get("nameFile")).toString();
    }

    // Создание PDF
    private void savePdf() {
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        BaseFont bfComic = null;
        try {
            //Шрифтик для PDF оставить пока такой, потдерживает 2 языка (лежит в assets/fonts/)(не проебать!)
            bfComic = BaseFont.createFont("assets/fonts/VOMono.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            ///system/fonts/DancingScript-Regular.ttf
            //bfComic = BaseFont.createFont("assets/fonts/comic.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            //assets/fonts/VOMono.ttf
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Font font1 = new Font(bfComic, 12);
        Document mDoc = new Document();
        //-- старое название файла с указанием тады, времени и имени файла (не удолять! пригодиться :3)
        String mFileName = new SimpleDateFormat("yyyy_MM_dd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        String mFilePath = dir.getAbsolutePath() + "/" + mFileName + "_" + nameFile + ".pdf";
        String mFilePath2 = dir.getAbsolutePath() + "/" + nameFile + ".pdf";
        //--
        try {
            PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath2));
            mDoc.open();
            String mText = "Text tttt ttt tttt";
            mDoc.addAuthor("QQQ");
            for (int i = 0; i < actsList.size(); i++) {
                //Acts.add(0, actsList.get(i));
                mDoc.add(new Paragraph(actsList.get(i), font1));
            }
            ElementV1 elementV1 = new ElementV1();
            float in[] = new float[2];
            in = elementV1.someDo(elementV1ArrayList);
            mDoc.add(new Paragraph("Процент по ремонтам: " + in[0], font1));
            mDoc.add(new Paragraph("Кол-во не учтённых ремонтов в расчёте процента: " + in[1], font1));
            mDoc.close();
            Toast.makeText(this, mFileName + ".pdf\nis save to\n" + mFilePath, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //всплывающее меню
    private void popupMenuShow(View view, final int i) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.pmcp);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                ElementV1 elementV1;
                FileOutputStream fos = null;
                switch (menuItem.getItemId()) {
                    case R.id.oneFP:
                        elementV1 = elementV1ArrayList.get(i);
                        String string = nameFile
                                + "~ " + elementV1.getDateText()
                                + "~ " + elementV1.getIdText()
                                + "~ " + elementV1.getModelText()
                                + "~ " + elementV1.getRepairslText()
                                + "~ " + elementV1.getPriceText()
                                + "~ " + elementV1.getPriceDetailText();

                        elementV1ArrayList.remove(i);
                        try {
                            fos = openFileOutput(nameFile, MODE_PRIVATE);
                            for (int i = 0; i < elementV1ArrayList.size(); i++) {
                                fos.write(elementV1ArrayList.get(i).toString().getBytes());
                            }
                        } catch (IOException ex) {
                            //--
                        } finally {
                            try {
                                if (fos != null)
                                    fos.close();
                            } catch (IOException ex) {
                                //-
                            }
                        }

                        Intent intent = new Intent(fileC.this, addPhone.class);
                        intent.putExtra("nameFile", string);
                        startActivity(intent);
                        return true;
                    case R.id.twoFP:
                        elementV1ArrayList.remove(i);
                        try {
                            fos = openFileOutput(nameFile, MODE_PRIVATE);
                            for (int i = 0; i < elementV1ArrayList.size(); i++) {
                                fos.write(elementV1ArrayList.get(i).toString().getBytes());
                            }
                        } catch (IOException ex) {
                            //--
                        } finally {
                            try {
                                if (fos != null)
                                    fos.close();
                            } catch (IOException ex) {
                                //-
                            }
                        }
                        CreateListActs();
                        WorkListViewArray(); // Заполнение по строчно ()старый способ = WorkListView();
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
}
