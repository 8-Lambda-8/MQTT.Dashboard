package com.a8lambda8.mqttdashboard;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;

import static com.a8lambda8.mqttdashboard.myUtils.TAG;

public class MQTT_Metric {

    MqttClient mqttClient;

    String Name;
    String Topic;
    boolean publishing;
    String TopicPub;
    boolean retained;

    int Qos;

    MQTT_Metric() {

    }

    void sendMessage(String payload){
        MqttMessage msg = new MqttMessage();
        msg.setPayload(payload.getBytes(StandardCharsets.UTF_8));
        msg.setRetained(retained);
        msg.setQos(Qos);

        try {
            mqttClient.publish(TopicPub,msg);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    void onClick(){

    }

    void sub(){
        try {
            mqttClient.subscribe(Topic, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Log.d(TAG, "messageArrived: "+topic+"  "+message);
                    updateMetric(message);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void updateMetric(MqttMessage message) {
    }

}
