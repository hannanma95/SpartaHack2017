package edu.msu.hannanma.spartahack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ShoppingActivity extends AppCompatActivity {

    private CatalogAdapter adapter;

    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        // Find the list view
        list = (ListView)findViewById(R.id.shopItems);

        // Create an adapter
        adapter = new CatalogAdapter(list);
        list.setAdapter(adapter);
    }

    public void onFinish(View view) {
        EditActivity.Item item;
        for (int i = 0; i < adapter.checkedBoxes.size(); i++) {
            if (adapter.checkedBoxes.get(i)) {
                item = adapter.getItem(i);

                Date date = new Date();
                SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
                String today = format1.format(date);
                item.purchaseDate = today;
                DbTools db = new DbTools(view.getContext());
                db.updateItem(item);
            }
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onCancel(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * An adapter so that list boxes can display a list of filenames from
     * the cloud server.
     */
    public static class CatalogAdapter extends BaseAdapter {

        public List<Boolean> checkedBoxes;

        private View view1;
        /**
         * Constructor
         */
        public CatalogAdapter(final View view) {
            view1 = view;

            // Create a thread to load the catalog
            new Thread(new Runnable() {

                @Override
                public void run() {
                    List<EditActivity.Item> newItems = getCatalog();
                    items = newItems;

                    view.post(new Runnable() {

                        @Override
                        public void run() {
                            // Tell the adapter the data set has been changed
                            notifyDataSetChanged();
                        }

                    });
                }

            }).start();

        }

        /**
         * Get the catalog items from the server
         * @return Array of items or null if failed
         */
        public List<EditActivity.Item> getCatalog() {
            /**
             * select all from database, parse json into items, and return list of items
             */
            DbTools db = new DbTools(view1.getContext());
            items = db.getNeededItems();

            checkedBoxes = new ArrayList<>(Arrays.asList(new Boolean[items.size()]));
            Collections.fill(checkedBoxes, Boolean.FALSE);

            return items;
        }

        /**
         * The items we display in the list box. Initially this is
         * null until we get items from the server.
         */
        private List<EditActivity.Item> items = new ArrayList<>();

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public EditActivity.Item getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            if(view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopitem, parent, false);
            }

            TextView tv = (TextView)view.findViewById(R.id.textItem2);
            tv.setText(items.get(position).name);

            TextView tv2 = (TextView)view.findViewById(R.id.textPrice2);
            tv2.setText("$" + items.get(position).price);

            CheckBox cb = (CheckBox)view.findViewById(R.id.checkBox);
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkedBoxes.set(position, !checkedBoxes.get(position));
                }
            });

            return view;
        }

        public String getName(int position) {
            return getItem(position).name;
        }
    }
}
