package com.example.administrator.testscrollitem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView lvListView;
    private List<MyBean> beans;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        lvListView = (ListView) findViewById(R.id.lv_listView);
        beans = new ArrayList<>();
        initData();
        myAdapter = new MyAdapter();
        lvListView.setAdapter(myAdapter);
    }

    private void initData() {
        for (int i = 0; i < 50; i++) {
            beans.add(new MyBean("Content" + i));
        }
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return beans.size();
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            MyViewHolder myViewHolder;
            if (view == null) {
                view = View.inflate(MainActivity.this, R.layout.listview_item, null);
                myViewHolder = new MyViewHolder();
                myViewHolder.item_content = (TextView) view.findViewById(R.id.item_content);
                myViewHolder.item_menu = (TextView) view.findViewById(R.id.item_menu);
                view.setTag(myViewHolder);
            } else {
                myViewHolder = (MyViewHolder) view.getTag();
            }

            final MyBean myBean = beans.get(i);
            myViewHolder.item_content.setText(myBean.getContent());

            myViewHolder.item_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyBean temp = beans.get(i);
                    Toast.makeText(MainActivity.this, "" + temp.getContent(), Toast.LENGTH_SHORT).show();
                }
            });

            myViewHolder.item_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SlideItemLayout slideItemLayout = (SlideItemLayout) view.getParent();
                    slideItemLayout.closeMenu();
                    beans.remove(myBean);
                    myAdapter.notifyDataSetChanged();
                }
            });

            SlideItemLayout slideItemLayout = (SlideItemLayout) view;
            slideItemLayout.setOnStateChangedListener(new MyStateChangedListener());
            return view;
        }
    }

    private SlideItemLayout slideLayout;

    class MyStateChangedListener implements SlideItemLayout.OnStateChangedListener{

        @Override
        public void onClose(SlideItemLayout slideItemLayout) {
            if (slideLayout == slideItemLayout){
                slideLayout = null;
            }
        }

        @Override
        public void onDown(SlideItemLayout slideItemLayout) {
            if (slideLayout != null && slideLayout != slideItemLayout){
                slideLayout.closeMenu();
            }
        }

        @Override
        public void onOpen(SlideItemLayout slideItemLayout) {
            slideLayout = slideItemLayout;
        }
    }

    static class MyViewHolder {
        TextView item_content, item_menu;
    }
}
