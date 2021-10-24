package tw.edu.yuntech.app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;



public class Carlist extends BaseAdapter {
    String numText;
    String nameText;
    String priceText;
    private String[] name;
    private int[] num;
    private int[] price;

    public Carlist(String[] name,int[] num,int[] price){
        this.name=name;
        this.num = num;
        this.price = price;
    }

    public Carlist(){}


    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public Object getName(int i) {
        return name[i];
    }
    public Object getNum(int i) {
        return num[i];
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.carlist,null);
        }
        TextView name = (TextView)view.findViewById(R.id.name);
        final TextView num = (TextView)view.findViewById(R.id.num);
        TextView price = (TextView)view.findViewById(R.id.price);
        Button plus = view.findViewById(R.id.plus);
        Button minus = view.findViewById(R.id.minus);
        String name_text = (String)getName(i);
        String num_text = getNum(i).toString();
        String price_text = getPrice(i).toString();
        numText = num_text;
        nameText = name_text;
        priceText = price_text;
        name.setText(nameText);
        num.setText("x"+numText);
        price.setText(priceText+"元");
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numText = String.valueOf((Integer.valueOf(numText)+1));
                num.setText("x"+ numText);
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!numText.equals("1")){
                    numText = String.valueOf((Integer.valueOf(numText)-1));
                    num.setText("x"+ numText);
                }
            }
        });
        return view;
    }
}
