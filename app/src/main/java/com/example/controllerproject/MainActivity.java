package com.example.controllerproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPhoneCall;
    PhoneCallAdapter phoneCallAdapter;
    private ArrayList<Contacts> contacts;
    private ArrayList<String> allContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(
                this,
                new String[] {Manifest.permission.READ_CONTACTS},
                1
        );

        recyclerViewPhoneCall = findViewById(R.id.recycler_view_phone_call);

        allContacts = new ArrayList<>();
        contacts = getContacts();
        phoneCallAdapter = new PhoneCallAdapter(this,contacts);
        recyclerViewPhoneCall.setHasFixedSize(true);
        recyclerViewPhoneCall.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPhoneCall.setAdapter(phoneCallAdapter);
    }

    public ArrayList<Contacts> getContacts() {
        String displayName,phoneNum,thumbnailUri;
        // Retrieve Contacts from phone
        /*Cursor result = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+ " ASC");*/

        Cursor result = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");


        // Create list of contact objects
        ArrayList<Contacts> contact = new ArrayList<Contacts>();

        // For number of contacts create a contact object with name and number.
        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            displayName = result.getString(result.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)).trim();
            phoneNum = result.getString(result.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).trim();
            thumbnailUri = result.getString(result.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            if(phoneNum.contains(" ")){
                phoneNum = phoneNum.replace(" ","").trim();
            }
            if(phoneNum.contains("-")){
                phoneNum = phoneNum.replace("-","").trim();
            }
            if(phoneNum.contains("+88")){
                phoneNum = phoneNum.replace("+88","").trim();
            }

            if(phoneNum.length() == 11 && !allContacts.contains(phoneNum) ){
                contact.add(new Contacts(displayName,phoneNum,thumbnailUri));
                allContacts.add(phoneNum);
            }
        }

        return contact;
    }


}