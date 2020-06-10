package com.a8lambda8.mqttdashboard;

import android.content.Intent;
import android.telecom.StatusHints;

import org.eclipse.paho.client.mqttv3.MqttClient;

public class MQTT_Metric_Button extends MQTT_Metric{

    boolean buttonState;

    MQTT_Metric_Button(MqttClient mqttClient) {
        super(mqttClient);
    }

    @Override
    void onClick() {
        super.onClick();
        super.sendMessage(""+(!buttonState?1:0));
    }
}
