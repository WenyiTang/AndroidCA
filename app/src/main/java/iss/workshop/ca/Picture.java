package iss.workshop.ca;

import android.graphics.Bitmap;

import java.io.File;
import java.io.Serializable;

public class Picture implements Serializable {
    private Bitmap bitmap;
    private String id;
    private File file;
    private int selectCount;
    private int position;
  
    public Picture() {
    }

    public Picture (File file) {
        this.file = file;
    }

    public Picture (Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Picture (Bitmap bitmap, String id) {
        this(bitmap);
        this.id = id;
    }

    public Picture (File file, int positions) {
        this(file);
        this.position= position;
    }

    public Picture (Bitmap bitmap, File file) {
        this(bitmap);
        this.file = file;
    }

    public int getSelectCount() {
        return selectCount;
    }
    
    public void setSelectCount(int selectCount) {
    this.selectCount = selectCount;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getId() {
        return id;
    }

    public File getFile() {return file;}

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFile(File file) {this.file = file;}
}
