package com.a8lambda8.mqttdashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class myUtils {
    static SharedPreferences SP;
    static SharedPreferences.Editor SPEdit;

    static String TAG = "xxx";

    static List<String> brokerNames;

    //brokerNames
    static boolean SaveObjectToFile(Object obj, String filename, Context context){
        boolean worked = true;
        try {
            FileOutputStream fos = context.openFileOutput(filename,Context.MODE_PRIVATE);//new FileOutputStream(file);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(obj);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "SaveObjectToFile exeption: "+e.getMessage());
            worked = false;
        }
        return worked;
    }

    static Object LoadObjectFromFile(String filename,Context context){
        Object object = null;
        Log.d(TAG, "LoadObjectFromFile filename: "+filename);
        try {
            FileInputStream fis = context.openFileInput(filename);//new FileInputStream(file);
            ObjectInputStream is = new ObjectInputStream(fis);
            object = is.readObject();
            is.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "LoadObjectFromFile exeption: "+e.getMessage());
        }
        return object;
    }

    static List<String> LoadStringListFromFile(String filename,Context context){
        ArrayList<String> lst = (ArrayList<String>) LoadObjectFromFile(filename,context);
        Log.d(TAG, "LoadStringListFromFile: "+lst.toString());
        return lst;
    }

    static boolean DeleteFile(String filename, Context context){
        return  context.deleteFile(filename);
    }

}
