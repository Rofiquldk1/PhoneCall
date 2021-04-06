package com.example.controllerproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
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
    private ArrayList<Contacts> contacts = new ArrayList<>();
    private final ArrayList<String> allContacts = new ArrayList<>();
    private EditText editText_search_phn_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS}, 1);

        editText_search_phn_number = findViewById(R.id.edit_txt_search_phn_num);
        recyclerViewPhoneCall = findViewById(R.id.recycler_view_phone_call);

        contacts = getContacts();
        recyclerViewPhoneCall.setLayoutManager(new LinearLayoutManager(this));
        phoneCallAdapter = new PhoneCallAdapter(this,contacts);
        recyclerViewPhoneCall.setAdapter(phoneCallAdapter);

       // Toast.makeText(getApplicationContext(),getContactDisplayNameByNumber("01866332212"),Toast.LENGTH_LONG).show();


        editText_search_phn_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String mSearchString = editText_search_phn_number.getText().toString();
                allContacts.clear();
                if(mSearchString.length() == 0){
                    contacts = getContacts();
                    recyclerViewPhoneCall.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    phoneCallAdapter = new PhoneCallAdapter(getApplicationContext(),contacts);
                    recyclerViewPhoneCall.setAdapter(phoneCallAdapter);
                    phoneCallAdapter.notifyDataSetChanged();
                }
                else {
                    contacts = getContactDisplayUsingPartialSearch(mSearchString);
                    recyclerViewPhoneCall.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    phoneCallAdapter = new PhoneCallAdapter(getApplicationContext(),contacts);
                    recyclerViewPhoneCall.setAdapter(phoneCallAdapter);
                    phoneCallAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    public ArrayList<Contacts> getContactDisplayUsingPartialSearch(String number) {
        String displayName=" ",phoneNum=" ",thumbnailUri=" ";
        Uri uri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI, Uri.encode(number.trim()));
        ContentResolver contentResolver = getContentResolver();
        Cursor result = contentResolver.query(
                uri, null, null, null, null);

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


    public ArrayList<Contacts> getContacts() {
        String displayName,phoneNum,thumbnailUri;
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