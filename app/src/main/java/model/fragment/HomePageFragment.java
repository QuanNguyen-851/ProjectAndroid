package model.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.projectandroid.DetailHistoryActivity;
import com.example.projectandroid.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import model.HistoryEntity;
import model.adapter.HistoryAdapter;

public class HomePageFragment extends Fragment {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View viewActivity = lf.inflate(R.layout.activity_hone_page,container,false );
        TextView account = viewActivity.findViewById(R.id.home_page_account);
        account.setText(getAccount().toString());

        EditText search = viewActivity.findViewById(R.id.home_page_searchBox);


        List<HistoryEntity> listHistory = getListHistory(search.getText().toString());
        HistoryAdapter historyAdapter =new HistoryAdapter(getContext(), listHistory);
        ListView listView = viewActivity.findViewById(R.id.home_page_list);
        listView.setAdapter(historyAdapter);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listHistory.clear();
                listHistory.addAll(getListHistory(search.getText().toString()));
                historyAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), DetailHistoryActivity.class);
                HistoryEntity entity = listHistory.get(i);
                Bundle itemBun = new Bundle();
                itemBun.putSerializable("history", entity);
                intent.putExtras(itemBun);
                startActivity(intent);
                return true;
            }
        });



        return viewActivity;

    }
    private Long getAccount(){
        SQLiteDatabase db = getActivity().openOrCreateDatabase("projectAndroid", MODE_PRIVATE, null);

        SharedPreferences viewPreferences = getActivity().getSharedPreferences("ProjectAndroidSetting", MODE_PRIVATE);
        String phone = viewPreferences.getString("profile_phone", null);
        String sql = "select * from profile where " +
                " phone = '" + phone + "'" +
                " LIMIT 1";
        Cursor rs = db.rawQuery(sql, null);
        rs.moveToFirst();
        return rs.getLong(8);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<HistoryEntity> getListHistory(String search){
        LocalDate now = LocalDate.now().minusDays(30);
        List<HistoryEntity> historyEntities = new ArrayList<>();
        SQLiteDatabase db = getActivity().openOrCreateDatabase("projectAndroid", MODE_PRIVATE, null);

        SharedPreferences viewPreferences = getActivity().getSharedPreferences("ProjectAndroidSetting", MODE_PRIVATE);
        String phone = viewPreferences.getString("profile_phone", null);
        String sql = "select history.*, category.name as categoryName from history" +
                " inner join category on category.id = history.categoryId" +
                " inner join profile on profile.id = history.profileId" +
                "   where " +
                " profile.phone = '" + phone + "'" +
                " and history.date > '" + now + "'" +
                " and ( Lower(history.description) LIKE '%"+search+"%' " +
                " or Lower(category.name) LIKE '%"+search+"%' " +
                " or Lower(history.date) LIKE '%"+search+"%' " +
                " or Lower(history.payment) LIKE '%"+search+"%' ) " +
                " Order by history.id desc ";
        Cursor rs = db.rawQuery(sql, null);
        rs.moveToFirst();
        while (!rs.isAfterLast()) {
            LocalDate today = LocalDate.parse(rs.getString(5));
            HistoryEntity entity = new HistoryEntity();
            entity.setId(rs.getInt(0));
            entity.setCategoryId(rs.getInt(1));
            entity.setPayment(rs.getLong(3));
            entity.setDescription(rs.getString(4));
            entity.setDate(today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            entity.setCategoryName(rs.getString(6));
            historyEntities.add(entity);
            rs.moveToNext();
        }
        return historyEntities;
    }
}
