package com.a8lambda8.mqttdashboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import static com.a8lambda8.mqttdashboard.myUtils.DeleteFile;
import static com.a8lambda8.mqttdashboard.myUtils.LoadStringListFromFile;
import static com.a8lambda8.mqttdashboard.myUtils.SP;
import static com.a8lambda8.mqttdashboard.myUtils.SPEdit;
import static com.a8lambda8.mqttdashboard.myUtils.SaveObjectToFile;
import static com.a8lambda8.mqttdashboard.myUtils.brokerNames;
import static com.a8lambda8.mqttdashboard.myUtils.TAG;

public class Activity_BrokerSelection extends AppCompatActivity implements Adapter_BrokerSelection.OnBrokerSelectListener{

    RecyclerView RV_BrokerSelection;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker_selection);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SP = PreferenceManager.getDefaultSharedPreferences(this);
        SPEdit = SP.edit();
        SPEdit.apply();

        brokerNames = LoadStringListFromFile(getString(R.string.key_brokerNames), getBaseContext());

        if (brokerNames == null){
            Log.d(TAG, "brokerNames is null");
            brokerNames = new ArrayList<>();
            SaveObjectToFile((ArrayList<String>)brokerNames,getString(R.string.key_brokerNames),getBaseContext());
        }

        RV_BrokerSelection = findViewById(R.id.rv_brokerSelection);
        layoutManager = new LinearLayoutManager(this);
        RV_BrokerSelection.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new Adapter_BrokerSelection(brokerNames, this);
        RV_BrokerSelection.setAdapter(mAdapter);
        registerForContextMenu(RV_BrokerSelection);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_brokerselection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_broker) {
            Intent i = new Intent(this, Activity_addBroker.class);
            startActivity(i);
            return true;
        }

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        // Inflate Menu from xml resource
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_brokerselection_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        ContextMenuRecyclerView.RecyclerContextMenuInfo info = (ContextMenuRecyclerView.RecyclerContextMenuInfo) item.getMenuInfo();

        Log.d(TAG, "onContextItemSelected: "+item.toString());

        switch (item.getItemId()){
            case R.id.context_edit:

                Intent i = new Intent(getBaseContext(), Activity_editBroker.class);
                i.putExtra("BrokerName",brokerNames.get(info.position));
                startActivity(i);

                break;
            case R.id.context_delete:
                final int pos = info.position;

                new AlertDialog.Builder(this)
                        .setTitle("Delete the Broker?")
                        .setMessage("Delete "+brokerNames.get(info.position)+"?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteFile(getString(R.string.key_brokerConfig)+brokerNames.get(pos),getBaseContext());
                                brokerNames.remove(pos);
                                SaveObjectToFile(brokerNames,getString(R.string.key_brokerNames),getBaseContext());
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel,null)
                        .setIcon(android.R.drawable.ic_menu_delete)
                        .show();


                break;
        }


        return false;
    }

    @Override
    public void onBrokerClick(int pos) {

        Intent i = new Intent(getBaseContext(), Activity_Dashboard.class);
        i.putExtra("BrokerName",brokerNames.get(pos));

        startActivity(i);
    }
}
