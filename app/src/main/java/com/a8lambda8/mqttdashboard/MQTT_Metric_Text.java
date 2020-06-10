package com.a8lambda8.mqttdashboard;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import static com.a8lambda8.mqttdashboard.myUtils.TAG;

public class MQTT_Metric_Text extends MQTT_Metric{

    String val;

    MQTT_Metric_Text(MqttClient mqttClient) {
        super(mqttClient);
    }
}
