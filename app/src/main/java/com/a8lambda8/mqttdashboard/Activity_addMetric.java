package com.a8lambda8.mqttdashboard;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;

import static com.a8lambda8.mqttdashboard.myUtils.SaveObjectToFile;
import static com.a8lambda8.mqttdashboard.myUtils.brokerMetrics;

public class Activity_addMetric extends AppCompatActivity {

    EditText ET_Name, ET_Topic, ET_Topic_pub;

    RadioGroup RG_Qos;
    Switch SW_retained;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_metric);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        final int type = i.getIntExtra("metricType",0);
        final String BrokerName = i.getStringExtra("BrokerName");


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                MQTT_Metric metric = new MQTT_Metric();

                switch(type){
                    case 0: //Text
                        metric = new MQTT_Metric_Text();
                        break;

                    case 1: //Button
                        metric = new MQTT_Metric_Button();
                        break;

                    case 2: //Range
                        metric = new MQTT_Metric_Range();
                        break;

                }

                switch (RG_Qos.getCheckedRadioButtonId()){
                    case R.id.qos0:
                        metric.Qos = 0;
                        break;
                    case R.id.qos1:
                        metric.Qos = 1;
                        break;
                    case R.id.qos2:
                        metric.Qos = 2;
                        break;
                }

                metric.Name = ET_Name.getText().toString();
                metric.Topic = ET_Topic.getText().toString();
                metric.TopicPub = ET_Topic.getText().toString();
                if(metric.TopicPub.equals(""))
                    metric.TopicPub = metric.Topic;

                metric.retained = SW_retained.isChecked();

                brokerMetrics.add(metric);

                metric.sub();

                SaveObjectToFile(brokerMetrics,getString(R.string.key_brokerMetrics)+BrokerName,getBaseContext());

                Snackbar.make(view, "Saved metric", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });



        ET_Name = findViewById(R.id.metric_name);
        ET_Topic = findViewById(R.id.topic);
        ET_Topic_pub = findViewById(R.id.topic_publish);
        RG_Qos = findViewById(R.id.qos);
        SW_retained = findViewById(R.id.retained);


    }
}