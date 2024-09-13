package com.example.fetchdataappvaishnav;

public class Item {
    private int id;
    private int listId;
    private String name;
    private boolean isHeader;  // New flag to indicate if this is a header

    // Constructor for regular items
    public Item(int id, int listId, String name) {
        this.id = id;
        this.listId = listId;
        this.name = name;
        this.isHeader = false;  // Regular item
    }

    // Constructor for header items (used when adding headers)
    public Item(String headerTitle) {
        this.name = headerTitle;  // Set the name as the header title
        this.isHeader = true;     // This is a header
    }

    // Getter to check if it's a header
    public boolean isHeader() {
        return isHeader;
    }

    public int getListId() {
        return listId;
    }

    public String getName() {
        return name;
    }
}
