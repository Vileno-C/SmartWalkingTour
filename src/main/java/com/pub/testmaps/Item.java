package com.pub.testmaps;

public class Item {
    private int id;
    private boolean isSelected;
    private int imageResource;
    private String textViewNameText;
    private String textViewText;

    public Item(int id, String name, int imageResource) {
        this.id = id;
        this.isSelected = false;
        this.imageResource = imageResource;
        this.textViewNameText = name;
        this.textViewText = "Centro de São Carlos";
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
}