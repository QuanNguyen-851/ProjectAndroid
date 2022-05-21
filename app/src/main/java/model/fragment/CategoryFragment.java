package model.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;

import com.example.projectandroid.DrawerActivity;
import com.example.projectandroid.R;
import com.example.projectandroid.databinding.ActivityDrawerBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import model.CategoryEntity;
import model.HistoryEntity;
import model.adapter.CategoryAdapter;
import model.adapter.HistoryAdapter;

public class CategoryFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View viewActivity = lf.inflate(R.layout.activity_category_list,container,false );
        List<CategoryEntity> list = new ArrayList<>();
        list.addAll(getAllCategory());
        ActivityDrawerBinding binding = ActivityDrawerBinding.inflate(getLayoutInflater());
        CategoryAdapter categoryAdapter =new CategoryAdapter(getContext(), list);
        ListView listView = viewActivity.findViewById(R.id.list_category);
        listView.setAdapter(categoryAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                CategoryEntity entity = list.get(i);
                final EditText input = new EditText(getActivity());
                input.setHint("Tên danh mục");
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setPadding(100, 50,100,50);
                input.setText(entity.getName());
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setView(input);
                alert.setMessage("Vui lòng nhập danh mục ");
                alert.setNegativeButton("Trở lại", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_drawer);

                alert.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (input.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "Vui lòng điền tên danh mục", Toast.LENGTH_SHORT).show();
                        } else {
                            updateCategory(entity.getId(), input.getText().toString());
                            NavGraph gr =  navController.getGraph();
                            gr.setStartDestination(R.id.nav_category);
                            navController.setGraph(gr);
                        }
                    }
                });
                alert.show();
                return true;
            }
        });

        return viewActivity;

    }
    private Void updateCategory(Integer id, String name){
        SQLiteDatabase db = getActivity().openOrCreateDatabase("projectAndroid", MODE_PRIVATE, null);
        ContentValues value = new ContentValues();
        value.put("name", name);
        db.update("category", value, "id = " + id , null);
        return null;

    }

    private List<CategoryEntity> getAllCategory(){
        List<CategoryEntity> list = new ArrayList<>();
        SQLiteDatabase db = getActivity().openOrCreateDatabase("projectAndroid", MODE_PRIVATE, null);
        String sql = " select * from category Order by id DESC";
        Cursor rs = db.rawQuery(sql, null);
        rs.moveToFirst();
        while (!rs.isAfterLast()) {
            list.add(new CategoryEntity(rs.getInt(0), rs.getString(1)));
            rs.moveToNext();
        }
        return list;
    }
}
