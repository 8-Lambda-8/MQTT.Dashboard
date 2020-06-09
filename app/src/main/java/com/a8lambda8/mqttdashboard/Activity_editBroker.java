package com.a8lambda8.mqttdashboard;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import static com.a8lambda8.mqttdashboard.myUtils.SaveObjectToFile;
import static com.a8lambda8.mqttdashboard.myUtils.brokerNames;

public class Activity_editBroker extends AppCompatActivity {
    EditText ET_Name, ET_Address, ET_Port, ET_uname, ET_passwd, ET_clientId;

    String brokerName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_broker);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!(ET_Name.getText().toString().isEmpty()||
                        ET_Address.getText().toString().isEmpty()||
                        ET_Port.getText().toString().isEmpty()||
                        ET_clientId.getText().toString().isEmpty())) {
                    if(!brokerNames.contains(ET_Name.getText().toString())) {


                        if(!brokerName.equals(ET_Name.getText().toString())){
                            brokerNames.remove(brokerName);
                            DeleteFile(getString(R.string.key_brokerConfig)+brokerName,getBaseContext());
                            brokerNames.add(ET_Name.getText().toString());
                        }
                        SaveObjectToFile(brokerNames,getString(R.string.key_brokerNames),getBaseContext());

                        BrokerConfig brokerConfig = new BrokerConfig(
                                ET_Name.getText().toString(),
                                ET_Address.getText().toString(),
                                Integer.parseInt(ET_Port.getText().toString()),
                                ET_uname.getText().toString(),
                                ET_passwd.getText().toString(),
                                ET_clientId.getText().toString()
                        );
                        SaveObjectToFile(brokerConfig,getString(R.string.key_brokerConfig)+ET_Name.getText().toString(),getBaseContext());

                        Snackbar.make(view, "Saved Broker", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }else{
                        Snackbar.make(view, "Broker already exists", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                }else{
                    Snackbar.make(view, "Name is null", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        ET_Name = findViewById(R.id.name);
        ET_Address = findViewById(R.id.address);
        ET_Port = findViewById(R.id.port);
        ET_uname = findViewById(R.id.uname);
        ET_passwd = findViewById(R.id.passwd);
        ET_clientId = findViewById(R.id.clientId);

    }
}