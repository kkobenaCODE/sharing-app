package com.example.sharingapp;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ContactListController {
    private ContactList contacts;

    public ContactListController(ContactList contacts) {
        this.contacts = contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts.setContacts(contacts);
    }

    public ArrayList<Contact> getContacts() {
        return this.contacts.getContacts();
    }

    public Contact getContact(int index) {
        return contacts.getContacts().get(index);
    }

    public ArrayList<String> getAllUsernames() {
        ArrayList<String> usernames = new ArrayList<>();

        for (Contact contact : this.contacts.getContacts()) {
            String username = contact.getUsername();
            usernames.add(username);
        }
        return usernames;
    }

    public void loadContacts(Context context) {
        this.contacts.loadContacts(context);
    }

    public boolean addContact(Contact contact, Context context) {
        AddContactCommand addContactCommand = new AddContactCommand(contacts, contact, context);
        addContactCommand.execute();
        return addContactCommand.isExecuted();
    }

    public boolean deleteContact(Contact contact, Context context) {
        DeleteContactCommand deleteContactCommand = new DeleteContactCommand(contacts, contact, context);
        deleteContactCommand.execute();
        return deleteContactCommand.isExecuted();
    }

    public boolean editContact(Contact contact, Contact updatedContact, Context context) {
        EditContactCommand editContactCommand = new EditContactCommand(contacts, contact, updatedContact, context);
        editContactCommand.execute();
        return editContactCommand.isExecuted();
    }

    public int getSize() {
        return contacts.getContacts().size();
    }

    public int getIndex(Contact contact) {
        return contacts.getContacts().indexOf(contact);
    }

    public boolean hasContact(Contact contact) {
        return contacts.hasContact(contact);
    }

    public Contact getContactByUsername(String username) {
        for (Contact contact : this.contacts.getContacts()) {
            String contactUsername = contact.getUsername();

            if (contactUsername.equals(username)) {
                return contact;
            }
        }

        return null;
    }


    public boolean isUsernameAvailable(String username) {
        Contact contact = this.getContactByUsername(username);
        if (contact == null) {
            return true;
        }
        return false;
    }

    public void addObserver(Observer observer) {
        this.contacts.addObserver(observer);
    }

    public void removeObserver(Observer observer) {
        this.contacts.removeObserver(observer);
    }


}
