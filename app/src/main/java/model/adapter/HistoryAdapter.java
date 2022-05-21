package model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.projectandroid.R;

import java.util.List;

import model.HistoryEntity;

public class HistoryAdapter extends BaseAdapter {
    Context context;
    public List<HistoryEntity> list;

    public  HistoryAdapter(Context context, List<HistoryEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row =inflater.inflate(R.layout.home_page_list_view, null);
        TextView categoryName =row.findViewById(R.id.list_item_category);
        TextView description = row.findViewById(R.id.list_item_description);
        TextView payment = row.findViewById(R.id.list_item_payment);
        TextView date = row.findViewById(R.id.list_item_date);
         HistoryEntity historyEntity = list.get(i);
         categoryName.setText(historyEntity.getCategoryName());
         description.setText(historyEntity.getDescription());
         payment.setText(historyEntity.getPayment().toString()+".vnđ");
         date.setText(historyEntity.getDate());
        return row;
    }
}
