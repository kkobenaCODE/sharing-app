package com.example.sharingapp;

import android.content.Context;

public class AddItemCommand extends Command{
    private ItemList itemList;
    private Item item;
    private Context context;

    public AddItemCommand(ItemList itemList, Item item, Context context) {
        this.itemList = itemList;
        this.item = item;
        this.context = context;
    }
    @Override
    public void execute() {
        itemList.addItem(item);
        setExecuted(itemList.saveItems(context));
    }
}
