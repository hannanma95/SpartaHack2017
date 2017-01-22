package edu.msu.hannanma.spartahack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Find the list view
        ListView list = (ListView)findViewById(R.id.items);

        // Create an adapter
        final CatalogAdapter adapter = new CatalogAdapter(list);
        list.setAdapter(adapter);
    }

    public void onDone(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onAdd(View view) {
        Intent intent = new Intent(this, AddItemActivity.class);
        startActivity(intent);
    }

    /**
     * Nested class to store one catalog row
     */
    public static class Item implements Serializable {
        public String name = "";
        public long id;
        public String freq = "";
        public String price = "";
        public String purchaseDate = "";
    }

    /**
     * An adapter so that list boxes can display a list of filenames from
     * the cloud server.
     */
    public static class CatalogAdapter extends BaseAdapter {

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
                    List<Item> newItems = getCatalog();
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
        public List<Item> getCatalog() {
            /**
             * select all from database, parse json into items, and return list of items
             */
            DbTools db = new DbTools(view1.getContext());
            items = db.getAllItems();

            return items;
        }

        /**
         * The items we display in the list box. Initially this is
         * null until we get items from the server.
         */
        private List<Item> items = new ArrayList<Item>();

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Item getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            if(view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            }

            TextView tv = (TextView)view.findViewById(R.id.textItem);
            tv.setText(items.get(position).name);

            ImageView delete = (ImageView)view.findViewById(R.id.deleteButton);
            ImageView edit = (ImageView)view.findViewById(R.id.editButton);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /**
                     * sql call to delete from db where itemid == getId(position)
                     */
                    DbTools db = new DbTools(v.getContext());
                    db.deleteItem(items.get(position));
                    items.remove(position);
                    notifyDataSetChanged();
                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), AddItemActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("item", items.get(position));
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });

            return view;
        }

        public String getName(int position) {
            return getItem(position).name;
        }
    }
}
