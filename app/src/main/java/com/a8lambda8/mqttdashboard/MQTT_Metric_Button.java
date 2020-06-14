package com.a8lambda8.mqttdashboard;

public class MQTT_Metric_Button extends MQTT_Metric{

    boolean buttonState;

    MQTT_Metric_Button() {

    }

    @Override
    void onClick() {
        super.onClick();
        super.sendMessage(""+(!buttonState?1:0));
    }
}
