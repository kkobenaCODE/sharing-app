package com.example.sharingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Editing a pre-existing item consists of deleting the old item and adding a new item with the old
 * item's id.
 */
public class EditItemActivity extends AppCompatActivity implements Observer {

    private ItemList item_list = new ItemList();
    private ItemListController itemListController = new ItemListController(item_list);
    private Item item;
    private ItemController itemController;

    private Context context;

    private ContactList contactList = new ContactList();
    private ContactListController contact_list_controller = new ContactListController(contactList);

    private Bitmap image;
    private int REQUEST_CODE = 1;
    private ImageView photo;

    private EditText title;
    private EditText maker;
    private EditText description;
    private EditText length;
    private EditText width;
    private EditText height;
    private Spinner borrowerSpinner;
    private TextView  borrower_tv;
    private Switch status;
    private EditText invisible;

    private ArrayAdapter<String> adapter;
    private boolean onCreateUpdate = false;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        title = (EditText) findViewById(R.id.title);
        maker = (EditText) findViewById(R.id.maker);
        description = (EditText) findViewById(R.id.description);
        length = (EditText) findViewById(R.id.length);
        width = (EditText) findViewById(R.id.width);
        height = (EditText) findViewById(R.id.height);
        borrowerSpinner = (Spinner) findViewById(R.id.borrower_spinner);
        borrower_tv = (TextView) findViewById(R.id.borrower_tv);
        photo = (ImageView) findViewById(R.id.image_view);
        status = (Switch) findViewById(R.id.available_switch);
        invisible = (EditText) findViewById(R.id.invisible);

        invisible.setVisibility(View.GONE);

        context = getApplicationContext();

        itemListController.addObserver(this);
        itemListController.loadItems(context);

        onCreateUpdate = true;

        contact_list_controller.addObserver(this);
        contact_list_controller.loadContacts(context);

        onCreateUpdate = false;
    }

    public void addPhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    public void deletePhoto(View view) {
        image = null;
        photo.setImageResource(android.R.drawable.ic_menu_gallery);
    }

    @Override
    protected void onActivityResult(int request_code, int result_code, Intent intent) {
        super.onActivityResult(request_code, result_code, intent);
        if (request_code == REQUEST_CODE && result_code == RESULT_OK) {
            Bundle extras = intent.getExtras();
            image = (Bitmap) extras.get("data");
            photo.setImageBitmap(image);
        }
    }

    public void deleteItem(View view) {
        boolean success = itemListController.deleteItem(item, context);
        if (!success) {
            return;
        }

        // End EditItemActivity
        itemListController.removeObserver(this);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void saveItem(View view) {

        String title_str = title.getText().toString();
        String maker_str = maker.getText().toString();
        String description_str = description.getText().toString();
        String length_str = length.getText().toString();
        String width_str = width.getText().toString();
        String height_str = height.getText().toString();
        Contact contact = null;
        if(!status.isChecked()) {
            String borrower_str = borrowerSpinner.getSelectedItem().toString();
            contact = contactList.getContactByUsername(borrower_str);
        }

        Dimensions dimensions = new Dimensions(length_str, width_str, height_str);

        if (title_str.equals("")) {
            title.setError("Empty field!");
            return;
        }

        if (maker_str.equals("")) {
            maker.setError("Empty field!");
            return;
        }

        if (description_str.equals("")) {
            description.setError("Empty field!");
            return;
        }

        if (length_str.equals("")) {
            length.setError("Empty field!");
            return;
        }

        if (width_str.equals("")) {
            width.setError("Empty field!");
            return;
        }

        if (height_str.equals("")) {
            height.setError("Empty field!");
            return;
        }


       // Reuse the item id
        String id = itemController.getId();
        Item updated_item = new Item(title_str, maker_str, description_str, dimensions, image, id);
        ItemController updatedItemController = new ItemController(updated_item);
        updatedItemController.setDimensions(dimensions);

        boolean checked = status.isChecked();
        if (!checked) {
            updatedItemController.setStatus("Borrowed");
            updatedItemController.setBorrower(contact);
        }

        boolean success = itemListController.editItem(item, updated_item, context);

        if (!success) {
            return;
        }

        itemListController.removeObserver(this);

//         End EditItemActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Checked = Available
     * Unchecked = Borrowed
     */
    public void toggleSwitch(View view){
        if (status.isChecked()) {
            // Means was previously borrowed
            borrowerSpinner.setVisibility(View.GONE);
            borrower_tv.setVisibility(View.GONE);
            itemController.setBorrower(null);
            itemController.setStatus("Available");

        } else {
            if (contactList.getSize() == 0) {
                invisible.setEnabled(false);
                invisible.setVisibility(View.VISIBLE);
                invisible.requestFocus();
                invisible.setError("No contacts available! Must add borrower to contacts.");
                status.setChecked(true);
            } else {
                // Means was previously available
                borrowerSpinner.setVisibility(View.VISIBLE);
                borrower_tv.setVisibility(View.VISIBLE);
            }
        }
    }

    public void update() {
        if (onCreateUpdate){
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                    contact_list_controller.getAllUsernames());
            borrowerSpinner.setAdapter(adapter);


            item = itemListController.getItem(pos);
            itemController = new ItemController(item);


            Contact contact = itemController.getBorrower();
            if (contact != null){
                int contact_pos = contact_list_controller.getIndex(contact);
                borrowerSpinner.setSelection(contact_pos);
            }


            title.setText(itemController.getTitle());
            maker.setText(itemController.getMaker());
            description.setText(itemController.getDescription());


            length.setText(itemController.getLength());
            width.setText(itemController.getWidth());
            height.setText(itemController.getHeight());


            String status_str = itemController.getStatus();
            if (status_str.equals("Borrowed")) {
                status.setChecked(false);
            } else {
                borrower_tv.setVisibility(View.GONE);
                borrowerSpinner.setVisibility(View.GONE);
            }


            image = itemController.getImage();
            if (image != null) {
                photo.setImageBitmap(image);
            } else {
                photo.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        }
    }

}