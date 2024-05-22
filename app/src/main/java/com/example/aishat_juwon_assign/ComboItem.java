package com.example.aishat_juwon_assign;
public class ComboItem {
    private String name;
    private int[] imageResources;
    private boolean attempted;
    private boolean result;

    public ComboItem(String name, int[] imageResources) {
        this.name = name;
        this.imageResources = imageResources;
        this.attempted = false;
        this.result = false;
    }

    public String getName() {
        return name;
    }

    public int[] getImageResources() {
        return imageResources;
    }

    public boolean isAttempted() {
        return attempted;
    }

    public void setAttempted(boolean attempted) {
        this.attempted = attempted;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}

