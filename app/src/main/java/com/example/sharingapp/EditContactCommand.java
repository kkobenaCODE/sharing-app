package com.example.sharingapp;

import android.content.Context;

public class EditContactCommand extends  Command{
    private ContactList contactList;
    private Contact contact;
    private Contact updatedContact;
    private Context context;

    public EditContactCommand(ContactList contactList, Contact contact, Contact updatedContact, Context context) {
        this.contactList = contactList;
        this.contact = contact;
        this.updatedContact = updatedContact;
        this.context = context;
    }

    @Override
    public void execute() {
        contactList.deleteContact(contact);
        contactList.addContact(updatedContact);
        contactList.saveContacts(context);
    }
}
