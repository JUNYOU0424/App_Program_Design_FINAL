package tw.edu.yuntech.app;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MList extends BaseAdapter {
    private String[] name;
    private int[] price;
    private int[] pic;

    public MList(String[] name,int[] price,int[] pic){
        this.name=name;
        this.price = price;
        this.pic=pic;
    }

    public MList(){}


    @Override
    public int getCount() {

        return name.length;
    }

    @Override
    public Object getItem(int i) {
        return name[i];
    }

    public Object getPrice(int i) {
        return price[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.foodlist,null);
        }
        TextView name = (TextView)view.findViewById(R.id.food);
        TextView price = (TextView)view.findViewById(R.id.price);
        ImageView img = (ImageView)view.findViewById(R.id.img);
        String name_text = (String)getItem(i);
        String price_text = String.valueOf(getPrice(i));
        name.setText(name_text);
        price.setText(price_text+"元");
        img.setImageResource(pic[i]);
        return view;
    }
}
