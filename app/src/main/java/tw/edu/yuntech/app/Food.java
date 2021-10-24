package tw.edu.yuntech.app;


import java.util.List;
//firebase資料
public class Food {
    private List<String> name;  //商品名
    private List<Integer> price; //商品價格

    public Food(){};

    public Food(List<String> name,List<Integer>price) {
        this.name = name;
        this.price = price;
    }


    public List<String> getName() {
        return name;
    }

    public List<Integer> getPrice(){
        return price;
    }
}

