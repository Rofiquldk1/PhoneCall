package com.example.controllerproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PhoneCallAdapter extends RecyclerView.Adapter<PhoneCallAdapter.PhoneCallViewHolder>  {
    Context mContext;
    ArrayList<Contacts> contacts;

    public PhoneCallAdapter(Context mContext, ArrayList<Contacts> contacts) {
        this.mContext = mContext;
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public PhoneCallAdapter.PhoneCallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_contacts, parent, false);
        return new PhoneCallAdapter.PhoneCallViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneCallAdapter.PhoneCallViewHolder holder, int position) {
        Contacts contact = contacts.get(position);
        holder.text_name.setText(contact.getName());
        holder.text_phoneNumber.setText(contact.getPhoneNumber());

        if(contact.getImage() == null){
            holder.circleImageViewProfile.setVisibility(View.GONE);
            holder.mIdentifierCard.setVisibility(View.VISIBLE);
            String SingleText = contact.getName().substring(0,1);
            holder.text_identifier.setText(SingleText);
        } else {
            Glide.with(mContext)
                    .load(contact.getImage())
                    .into(holder.circleImageViewProfile);
            holder.mIdentifierCard.setVisibility(View.GONE);
            holder.circleImageViewProfile.setVisibility(View.VISIBLE);
        }

        holder.btn_phone_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = contacts.get(position).getPhoneNumber();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phone));
                mContext.startActivity(callIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class PhoneCallViewHolder extends RecyclerView.ViewHolder {
        Button btn_phone_call;
        TextView text_name,text_phoneNumber,text_identifier;
        public LinearLayout linearLayoutContact;
        public CircleImageView circleImageViewProfile;
        CardView mIdentifierCard ;
        public PhoneCallViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_phone_call = itemView.findViewById(R.id.btn_call);
            text_identifier = itemView.findViewById(R.id.text_identifier);
            text_name = itemView.findViewById(R.id.display_contactName);
            text_phoneNumber = itemView.findViewById(R.id.display_contactPhoneNumber);
            linearLayoutContact = itemView.findViewById(R.id.linear_layout_contact);
            circleImageViewProfile = itemView.findViewById(R.id.circle_image_profile);
            mIdentifierCard = itemView.findViewById(R.id.card_identifier);
        }
    }
}
