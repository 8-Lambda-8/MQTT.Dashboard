package com.a8lambda8.mqttdashboard;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import static com.a8lambda8.mqttdashboard.myUtils.LoadStringListFromFile;
import static com.a8lambda8.mqttdashboard.myUtils.SP;
import static com.a8lambda8.mqttdashboard.myUtils.SPEdit;

public class Activity_BrokerSelection extends AppCompatActivity {

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

        List<String> brokerNames = LoadStringListFromFile(getString(R.string.key_brokerNames), getBaseContext());

        /*
        if (brokerNames == null){
            Log.d(TAG, "brokerNames is null");
            brokerNames = new ArrayList<>();
            SaveObjectToFile((ArrayList<String>)brokerNames,brokerNameFiles,getBaseContext());
            brokerNames.add("No Broker");
        }//*/

        RV_BrokerSelection = findViewById(R.id.rv_brokerSelection);
        layoutManager = new LinearLayoutManager(this);
        RV_BrokerSelection.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new Adapter_BrokerSelection(brokerNames);
        RV_BrokerSelection.setAdapter(mAdapter);

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
}
