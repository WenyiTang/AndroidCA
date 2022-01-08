package iss.workshop.ca;

//public class Picture implements Parcelable
public class Picture{

    private String path;
    private int selectCount;
    private int position;

    public Picture() {
    }

    public Picture(String path) {
        this.path = path;
    }


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
