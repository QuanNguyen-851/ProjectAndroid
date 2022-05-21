package model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.projectandroid.R;

import java.util.List;

import model.CategoryEntity;
import model.HistoryEntity;

public class CategoryAdapter extends BaseAdapter {
    Context context;
    public List<CategoryEntity> list;

    public CategoryAdapter(Context context, List<CategoryEntity> list) {
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
        View row =inflater.inflate(R.layout.category_list_view, null);
        TextView categoryName =row.findViewById(R.id.categoryItem);
        CategoryEntity categoryEntity = list.get(i);
        categoryName.setText(categoryEntity.getName());
        return row;
    }
}
