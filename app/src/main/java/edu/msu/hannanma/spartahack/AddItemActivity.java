package edu.msu.hannanma.spartahack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddItemActivity extends AppCompatActivity {

    boolean add = true;
    EditActivity.Item item = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            EditActivity.Item item = (EditActivity.Item)bundle.getSerializable("item");

            if (item != null) {
                EditText nameText = (EditText)findViewById(R.id.editName);
                EditText freqText = (EditText)findViewById(R.id.editDays);
                EditText priceText = (EditText)findViewById(R.id.editPrice);
                Button addEdit = (Button)findViewById(R.id.add);

                nameText.setText(item.name);
                freqText.setText(item.freq);
                priceText.setText(item.price);
                this.item = item;
                addEdit.setText("Done");
                add = false;
            }
        }
    }

    public void onAdd(View view) {
        EditText nameBox = (EditText)findViewById(R.id.editName);
        EditText freqBox = (EditText)findViewById(R.id.editDays);
        EditText priceBox = (EditText)findViewById(R.id.editPrice);
        String name = String.valueOf(nameBox.getText());
        String freq = String.valueOf(freqBox.getText());
        String price = String.valueOf(priceBox.getText());
        if (name == null || freq == null || price == null ||
                name.isEmpty() || freq.isEmpty() || price.isEmpty()) {
            return;
        }
        String purchaseDate;
        if (add) {
            purchaseDate = "";
        } else {
            purchaseDate = item.purchaseDate;
        }

        EditActivity.Item item = new EditActivity.Item();
        item.name = name; item.freq = freq; item.price = price; item.purchaseDate = purchaseDate;

        if (add) {
            DbTools db = new DbTools(view.getContext());
            item.id = db.addItem(item);
        } else {
            DbTools db = new DbTools(view.getContext());
            item.id = this.item.id;
            db.updateItem(item);
        }

        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }

    public void onCancel(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }
}
