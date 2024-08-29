package com.pub.testmaps;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
    private int id;
    private boolean isSelected;
    private int imageResource;
    private String textViewNameText;
    private String textViewText;
    private int imageButtonResourceInfo;
    private int imageButtonResourceMap;

    public Item(int id, String name, int imageResource){
        this.id = id;
        this.isSelected = false;
        this.imageResource = imageResource;
        this.textViewNameText = name;
        this.textViewText = "Centro de São Carlos";
        this.imageButtonResourceInfo = R.drawable.info;
        this.imageButtonResourceMap = R.drawable.map;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getTextViewNameText() {
        return textViewNameText;
    }

    public void setTextViewNameText(String textViewNameText) {
        this.textViewNameText = textViewNameText;
    }

    public String getTextViewText() {
        return textViewText;
    }

    public void setTextViewText(String textViewText) {
        this.textViewText = textViewText;
    }

    public int getImageButtonResourceInfo() {
        return imageButtonResourceInfo;
    }

    public void setImageButtonResourceInfo(int imageButtonResourceInfo) {
        this.imageButtonResourceInfo = imageButtonResourceInfo;
    }

    public int getImageButtonResourceMap() {
        return imageButtonResourceMap;
    }

    public void setImageButtonResourceMap(int imageButtonResourceMap) {
        this.imageButtonResourceMap = imageButtonResourceMap;
    }

            ///////* Métodos necessários para a implementação da interface Parcelable *///////

    // Construtor usado para criar um objeto Item a partir de um Parcel
    protected Item(Parcel in) {
        id = in.readInt();
        isSelected = in.readByte() != 0;
        imageResource = in.readInt();
        textViewNameText = in.readString();
        textViewText = in.readString();
        imageButtonResourceInfo = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeInt(imageResource);
        dest.writeString(textViewNameText);
        dest.writeString(textViewText);
        dest.writeInt(imageButtonResourceInfo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
