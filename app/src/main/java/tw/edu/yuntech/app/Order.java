package tw.edu.yuntech.app;

public class Order {
    private String[] name;
    private int[] num;
    private float lat;
    private float longt;
    private String status;

    public Order(){};

    public Order(int[] num,String[] name,float lat,float longt,String status) {
        this.name = name;
        this.num = num;
        this.lat = lat;
        this.longt = longt;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
