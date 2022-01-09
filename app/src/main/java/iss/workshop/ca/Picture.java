package iss.workshop.ca;

import android.graphics.Bitmap;

//public class Picture implements Parcelable
public class Picture{

    private String path;
    private Bitmap bitmap;
    private int selectCount;
    private int position;

    public Picture() {
    }

    public Picture(String path) {
        this.path = path;
    }
    public Picture(Bitmap bitmap) {this.bitmap = bitmap;}

    public Bitmap getBitmap() {return bitmap;}

    public void setBitmap(Bitmap bitmap) { this.bitmap = bitmap; }


    public String getPath() {
        return path;
    }
//
//    public void setPath(String path) {
//        this.path = path;
//    }
//
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


}
