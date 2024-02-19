package com.example.sharingapp;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddContactActivity extends AppCompatActivity {

    private ContactList contactList = new ContactList();
    private ContactListController contactListController = new ContactListController(contactList);
    private Context context;
    private EditText username;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);

        context = getApplicationContext();
        contactListController.loadContacts(context);
    }

    public void saveContact(View view) {
        String usernameStr = username.getText().toString();
        String emailStr = email.getText().toString();

        if (usernameStr.equals("")) {
            username.setError("Empty field!");
            return;
        }
        if (emailStr.equals("")) {
            email.setError("Empty field!");
            return;
        }
        if (!emailStr.contains("@")){
            email.setError("Must be an email address!");
            return;
        }
        if (!contactListController.isUsernameAvailable(usernameStr)){
            username.setError("Username already taken!");
            return;
        }
        Contact contact = new Contact(null, usernameStr, emailStr);

        boolean success = contactListController.addContact(contact, context);

        if (!success) {
            return;
        }

        finish();
    }

}

