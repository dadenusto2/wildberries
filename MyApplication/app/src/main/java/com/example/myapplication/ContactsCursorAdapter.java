package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Eyehunt Team on 03/07/18.
 */
public class ContactsCursorAdapter extends CursorAdapter {
    public ContactsCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        return LayoutInflater.from(context).inflate(
                R.layout.row_contact,viewGroup,false );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        @SuppressLint("Range") String contactName = cursor.getString(
                cursor.getColumnIndex(DatabaseHandler.CONTACT_NAME));
        @SuppressLint("Range") String contactPhone = cursor.getString(
                cursor.getColumnIndex(DatabaseHandler.CONTACT_PHONE));
        TextView nameTextView = (TextView) view.findViewById(R.id.tv_name);
        TextView phoneTextView = (TextView) view.findViewById(R.id.tv_phone);
        nameTextView.setText(contactName);
        phoneTextView.setText(contactPhone);

    }
}

