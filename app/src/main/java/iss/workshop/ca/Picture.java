package iss.workshop.ca;

import android.graphics.Bitmap;

import java.io.File;
import java.io.Serializable;

public class Picture implements Serializable {
    private Bitmap bitmap;
    private String id;
    private File file;

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
