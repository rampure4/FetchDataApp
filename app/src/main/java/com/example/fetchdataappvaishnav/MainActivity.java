package com.example.fetchdataappvaishnav;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // helps with how the screen is set up which is done inside of the activity_main.xml
        setContentView(R.layout.activity_main);

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch data from the server
        new FetchDataTask().execute("https://fetch-hiring.s3.amazonaws.com/hiring.json");
    }

    // AsyncTask to fetch data in the background
    private class FetchDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String jsonString) {
            if (jsonString.isEmpty()) {
                Toast.makeText(MainActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Parse the JSON data
            itemList = parseJsonData(jsonString);
            List<Item> recyclerViewItems = processData(itemList);

            itemAdapter = new ItemAdapter(recyclerViewItems);
            recyclerView.setAdapter(itemAdapter);
        }

        // Function to parse the JSON response and return a list of items
        private List<Item> parseJsonData(String jsonString) {
            List<Item> items = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    int listId = jsonObject.getInt("listId");
                    String name = jsonObject.optString("name");

                    // Filter out items where the name is null or empty
                    if (name != null && !name.trim().isEmpty()) {
                        Item item = new Item(id, listId, name);
                        items.add(item);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return items;
        }

        private List<Item> processData(List<Item> itemList) {
            // Remove items with null or blank names directly from the itemList
            itemList.removeIf(item -> item.getName() == null || item.getName().trim().isEmpty());

            // Sort the items first by listId and then by name
            Collections.sort(itemList, new Comparator<Item>() {
                @Override
                public int compare(Item o1, Item o2) {
                    int listIdCompare = Integer.compare(o1.getListId(), o2.getListId());
                    if (listIdCompare != 0) {
                        return listIdCompare;  // Sort by listId first
                    } else {
                        return o1.getName().compareTo(o2.getName());  // Then by name
                    }
                }
            });

            // Group the items by listId and add headers
            List<Item> recyclerViewItems = new ArrayList<>();
            int currentListId = -1;
            for (Item item : itemList) {
                // Add a header if we encounter a new listId
                if (item.getListId() != currentListId) {
                    currentListId = item.getListId();
                    recyclerViewItems.add(new Item("List ID: " + currentListId));  // Add a valid header title
                }
                // Add the actual item to the list
                recyclerViewItems.add(item);
            }

            return recyclerViewItems;
        }

    }}