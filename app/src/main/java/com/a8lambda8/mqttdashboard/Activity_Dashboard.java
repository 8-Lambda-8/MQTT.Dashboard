package com.a8lambda8.mqttdashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.a8lambda8.mqttdashboard.myUtils.LoadObjectFromFile;
import static com.a8lambda8.mqttdashboard.myUtils.TAG;
import static com.a8lambda8.mqttdashboard.myUtils.mqttClient;

public class Activity_Dashboard extends AppCompatActivity implements MqttCallback, Adapter_Dashboard.OnBrokerSelectListener {

    RecyclerView RV_Dashboard;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    MqttMessage msg = new MqttMessage();
                    msg.setQos(0);
                    msg.setRetained(true);
                    msg.setPayload("1".getBytes(StandardCharsets.UTF_8));
                    mqttClient.publish("/LightSwitch/0/0",msg);

                } catch (MqttException e) {
                    e.printStackTrace();
                }

                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)                        .setAction("Action", null).show();
            }
        });

        RV_Dashboard = findViewById(R.id.rv_dashboard);
        layoutManager = new GridLayoutManager(this,3);
        RV_Dashboard.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        //mAdapter = new Adapter_Dashboard(brokerNames, this);
        //RV_Dashboard.setAdapter(mAdapter);
        //registerForContextMenu(RV_Dashboard);





        BrokerConfig brokerConfig = (BrokerConfig) LoadObjectFromFile(getString(R.string.key_brokerConfig)+getIntent().getStringExtra("BrokerName"),getBaseContext());

        String host = "tcp://"+brokerConfig.getAddress()+":"+brokerConfig.getPort();
        Log.d(TAG, "Host: "+host);
        mqttClient = new MqttAndroidClient(getBaseContext(),host,brokerConfig.getClientID());

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setKeepAliveInterval(60);//seconds
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setAutomaticReconnect(true);

        mqttConnectOptions.setUserName(brokerConfig.getUname());
        mqttConnectOptions.setPassword(brokerConfig.getPasswd().toCharArray());

        try {
            mqttClient.connect(mqttConnectOptions,null,new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure "+ exception.toString());
                    Snackbar.make(fab, exception.toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            });

            if (mqttClient.isConnected())
                mqttClient.subscribe("/#",0);
        } catch (MqttException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

        mqttClient.setCallback(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mqttClient.unregisterResources();
        mqttClient.close();

    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.d(TAG, "messageArrived: "+topic+"  "+message);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d(TAG, "deliveryComplete: "+token.toString());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_metric) {


            String[] Types = {"Text","Button","Range"};

            ArrayAdapter adapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_list_item_1,Types);

            new AlertDialog.Builder(this)
                    .setTitle("Choose ype")
                    .setNegativeButton(android.R.string.cancel,null)
                    .setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent = new Intent(getBaseContext(), Activity_addMetric.class);
                            intent.putExtra("MetricType",i);
                            startActivity(intent);

                        }
                    })
                    .show();


            return true;
        }

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBrokerClick(int pos) {

    }
}