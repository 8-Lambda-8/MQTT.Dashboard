package com.a8lambda8.mqttdashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;

import static com.a8lambda8.mqttdashboard.myUtils.LoadObjectFromFile;
import static com.a8lambda8.mqttdashboard.myUtils.TAG;

public class Activity_Dashboard extends AppCompatActivity implements MqttCallback {

    MqttAndroidClient mqttClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
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
}