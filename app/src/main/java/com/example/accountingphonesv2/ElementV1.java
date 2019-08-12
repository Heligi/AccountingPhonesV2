package com.example.accountingphonesv2;


import android.widget.Switch;

import java.util.ArrayList;

public class ElementV1 {
    private String dateText;
    private String idText;
    private String modelText;
    private String repairslText;
    private String priceText;
    private String priceDetailText;

    public ElementV1() {
    }

    public ElementV1(String dateText, String idText, String modelText, String repairslText, String priceText, String priceDetailText) {
        this.dateText = dateText;
        this.idText = idText;
        this.modelText = modelText;
        this.repairslText = repairslText;
        this.priceText = priceText;
        this.priceDetailText = priceDetailText;
    }

    public String getDateText() {
        return dateText;
    }

    public void setDateText(String dateText) {
        this.dateText = dateText;
    }

    public String getIdText() {
        return idText;
    }

    public void setIdText(String idText) {
        this.idText = idText;
    }

    public String getModelText() {
        return modelText;
    }

    public void setModelText(String modelText) {
        this.modelText = modelText;
    }

    public String getRepairslText() {
        return repairslText;
    }

    public void setRepairslText(String repairslText) {
        this.repairslText = repairslText;
    }

    public String getPriceText() {
        return priceText;
    }

    public void setPriceText(String priceText) {
        this.priceText = priceText;
    }

    public String getPriceDetailText() {
        return priceDetailText;
    }

    public void setPriceDetailText(String priceDetailText) {
        this.priceDetailText = priceDetailText;
    }

    public float[] someDo(ArrayList<ElementV1> elementV1ArrayList) {
        float in[] = new float[2];
        int check = 0;
        int price = 0;
        int priceDetail = 0;
        String str;
        String str2;
        ElementV1 elementV1;
        for (int i = 0; i < elementV1ArrayList.size(); i++) {
            elementV1 = elementV1ArrayList.get(i);
            str = elementV1.getPriceText();
            if (str.replaceAll("\\s", "")
                    .matches("-?\\d+(.\\d+)?")) {
                str2 = elementV1.getPriceDetailText();
                if (str2.replaceAll("\\s", "")
                        .matches("-?\\d+(.\\d+)?")) {
                    priceDetail += Integer.parseInt(str2.replaceAll("\\s", ""));
                    price += Integer.parseInt(str.replaceAll("\\s", ""));
                }else check++;
            } else check++;
        }
        in[0] = (price - priceDetail) * 30 / 100;
        in[1] = check;
        return in;
    }

    public ArrayList<ElementV1> reads(ArrayList<String> srs) {
        ArrayList<ElementV1> v1ArrayList = new ArrayList<>();
        String date = "";
        String id = "";
        String model = "";
        String repairsl = "";
        String price = "";
        String priceDetail = "";

        for (int i = 0; i < (srs.size() / 7); i++) {
            for (int j = 7 * i; j < 7 + 7 * i; j++) {
                String[] sp = srs.get(j).split("~");
                switch (sp[0]) {
                    case "Дата":
                        if (sp.length == 2) {
                            date = sp[1];
                        } else {
                            date = "";
                        }
                        break;
                    case "Номер акта":
                        if (sp.length == 2) {
                            id = sp[1];
                        } else {
                            id = "";
                        }
                        break;
                    case "Модель":
                        if (sp.length == 2) {
                            model = sp[1];
                        } else {
                            model = "";
                        }
                        break;
                    case "Ремонт":
                        if (sp.length == 2) {
                            repairsl = sp[1];
                        } else {
                            repairsl = "";
                        }
                        break;
                    case "Цена":
                        if (sp.length == 2) {
                            price = sp[1];
                        } else {
                            price = "";
                        }
                        break;
                    case "Цена запчасти":
                        if (sp.length == 2) {
                            priceDetail = sp[1];
                        } else {
                            priceDetail = "";
                        }
                        break;
                }
            }
            v1ArrayList.add(new ElementV1(date, id, model, repairsl, price, priceDetail));
        }
        return v1ArrayList;
    }

    @Override
    public String toString() {
        return "Дата~" + dateText + '\n' +
                "Номер акта~" + idText + '\n' +
                "Модель~" + modelText + '\n' +
                "Ремонт~" + repairslText + '\n' +
                "Цена~" + priceText + '\n' +
                "Цена запчасти~" + priceDetailText + '\n' +
                "---------------------------------------------" + '\n';
    }


}
