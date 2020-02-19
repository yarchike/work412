package ru.netology.lists;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewActivity extends AppCompatActivity {
    static final String KEY1 = "Key1";
    static final String KEY2 = "Key2";
    static final String DATAS = "DataS";
    List<Map<String, String>> simpleAdapterContent = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final SharedPreferences sharedPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor myEditor=sharedPref.edit();
        myEditor.putString(DATAS,getString(R.string.large_text));
        myEditor.apply();

        SwipeRefreshLayout swipeLayout = findViewById(R.id.swiperefresh);

        ListView list = findViewById(R.id.list);

        simpleAdapterContent = prepareContent(sharedPref);

        final BaseAdapter listContentAdapter = createAdapter(simpleAdapterContent);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                simpleAdapterContent.remove(i);
                listContentAdapter.notifyDataSetChanged();
            }
        });
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                simpleAdapterContent = prepareContent(sharedPref);
                listContentAdapter.notifyDataSetChanged();


            }
        });




        list.setAdapter(listContentAdapter);
    }

    @NonNull
    private BaseAdapter createAdapter(List<Map<String, String>> values) {
        return new SimpleAdapter(ListViewActivity.this, values, R.layout.list_layout, new String[]{KEY1, KEY2}, new int[]{R.id.TextOne, R.id.TextTwo});
    }

    @NonNull
    private List<Map<String, String>> prepareContent(SharedPreferences sharedPref) {
        List<Map<String, String>> data = new ArrayList<>();
        String[] arrayContent = sharedPref.getString(DATAS,"").split("\n\n");
        for (int i = 0; i < arrayContent.length; i++) {
            Map<String, String> temp = new HashMap<>();
            temp.put(KEY1, arrayContent[i]);
            temp.put(KEY2, String.valueOf(arrayContent[i].length()));
            data.add(temp);
        }
        return data;
    }
}
