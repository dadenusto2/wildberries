package com.example.myapplication;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private CursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cursorAdapter = new ContactsCursorAdapter(this, null, 0);

        ListView list = (ListView) findViewById(R.id.lv_contacts);
        list.setAdapter(cursorAdapter);
        getLoaderManager().initLoader(0, null, this);

        // add new record - dialog box
        FloatingActionButton fabtn_add = (FloatingActionButton) findViewById(R.id.fabtn_add);
        fabtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View getEmpIdView = li.inflate(R.layout.dialog_box, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add new contact");
                builder.setView(getEmpIdView);

                final EditText et_name = (EditText) getEmpIdView.findViewById(R.id.et_name);
                final EditText et_number = (EditText) getEmpIdView.findViewById(R.id.et_number);
                // set dialog message

                builder
                        .setCancelable(false)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                insertContact(et_name.getText().toString(), et_number.getText().toString());
                                restartLoader();

                            }
                        }).create()
                        .show();
            }
        });
    }

    //Insert a new contact
    private void insertContact(String contactName, String contactPhone) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.CONTACT_NAME, contactName);
        values.put(DatabaseHandler.CONTACT_PHONE, contactPhone);
        Uri contactUri = getContentResolver().insert(MyContactsProvider.CONTENT_URI, values);
        Toast.makeText(this, "Created Contact " + contactName, Toast.LENGTH_LONG).show();
    }

    // Delete all contacts
    private void deleteAllContacts() {

        getContentResolver().delete(MyContactsProvider.CONTENT_URI, null, null);
        restartLoader();
        Toast.makeText(this, "All contacts has deleted", Toast.LENGTH_LONG).show();
    }

    // reload all data in listivew
    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, MyContactsProvider.CONTENT_URI, null,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    //set menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle delete action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteAllContacts();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
