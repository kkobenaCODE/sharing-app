package com.example.sharingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity implements Observer {
    private ContactList contactList = new ContactList();
    private ContactListController contactListController = new ContactListController(contactList);

    private ContactList activeBorrowersList = new ContactList();
    private ContactListController activeBorrowersListController = new ContactListController(activeBorrowersList);

    private ItemList itemList = new ItemList();
    private ItemListController itemListController = new ItemListController(itemList);

    private ListView myContacts;
    private ArrayAdapter<Contact> adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        context = getApplicationContext();
        contactListController.addObserver(this);
        contactListController.loadContacts(context);
        itemListController.loadItems(context);

        myContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
                Contact contact = adapter.getItem(pos);
                activeBorrowersListController.setContacts(itemListController.getActiveBorrowers());
                if (activeBorrowersListController != null) {
                    if (activeBorrowersListController.hasContact(contact)) {
                        CharSequence text = "Cannot edit or delete active borrower!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast.makeText(context, text, duration).show();
                        return true;
                    }
                }
                contactListController.loadContacts(context);
                int metaPos = contactListController.getIndex(contact);

                Intent intent = new Intent(context, EditContactActivity.class);
                intent.putExtra("posiiton", metaPos);
                startActivity(intent);

                return true;
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        context = getApplicationContext();
        contactListController.loadContacts(context);
    }

    public void addContactActivity(View view) {
        Intent intent = new Intent(this, AddContactActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        contactListController.removeObserver(this);
    }

    public void update(){
        myContacts = (ListView) findViewById(R.id.my_contacts);
        adapter = new ContactAdapter(ContactsActivity.this, contactListController.getContacts());
        myContacts.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}