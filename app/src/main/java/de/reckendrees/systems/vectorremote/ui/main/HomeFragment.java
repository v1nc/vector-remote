package de.reckendrees.systems.vectorremote.ui.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.chaquo.python.PyObject;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

import de.reckendrees.systems.vectorremote.R;
import de.reckendrees.systems.vectorremote.api.DownloadCertTask;
import de.reckendrees.systems.vectorremote.api.InitConnectionTask;
import de.reckendrees.systems.vectorremote.api.StatsTask;
import de.reckendrees.systems.vectorremote.interfaces.HomeInterface;
import de.reckendrees.systems.vectorremote.interfaces.MainInterface;

public class HomeFragment extends Fragment implements HomeInterface {


    private TextView statusText ;
    private Button connectButton;
    private TextView certStatusText ;
    private Button certButton ;
    private EditText nameEditText;
    private EditText serialEditText;
    private EditText ipEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private ProgressBar vectorBar;
    private ProgressBar cubeBar;
    private View root;


    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private MainInterface robotStore;
    private ProgressDialog dialog;
    private boolean isConnecting;
    public static HomeFragment newInstance(MainInterface robotStore) {
        HomeFragment fragment = new HomeFragment(robotStore);
        return fragment;
    }

    public HomeFragment(MainInterface robotStore){
        super();
        this.robotStore = robotStore;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);
        statusText = root.findViewById(R.id.textViewStatus);
        connectButton = root.findViewById(R.id.buttonConnect);
        vectorBar = root.findViewById(R.id.progressBar);
        cubeBar = root.findViewById(R.id.progressBar2);
        certStatusText = root.findViewById(R.id.textViewCertStatus);
        certButton = root.findViewById(R.id.buttonCert);
        nameEditText = root.findViewById(R.id.editTextName);
        serialEditText = root.findViewById(R.id.editTextSerial);
        ipEditText = root.findViewById(R.id.editTextIP);
        emailEditText = root.findViewById(R.id.editTextEmail);
        passwordEditText = root.findViewById(R.id.editTextPassword);

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        isConnecting = false;

        //init credential inputs
        nameEditText.setText(sharedPref.getString("vector_name", ""));
        serialEditText.setText(sharedPref.getString("vector_serial", ""));
        ipEditText.setText(sharedPref.getString("vector_ip", ""));
        emailEditText.setText(sharedPref.getString("vector_email", ""));

        //autosave credential inputs
        nameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                editor.putString("vector_name", nameEditText.getText().toString());
                editor.commit();
            }
        });
        serialEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                editor.putString("vector_serial", serialEditText.getText().toString());
                editor.commit();
            }
        });
        ipEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                editor.putString("vector_ip", ipEditText.getText().toString());
                editor.commit();
            }
        });
        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                editor.putString("vector_email", emailEditText.getText().toString());
                editor.commit();
            }
        });
        //listen for ENTER on password filed
        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    downloadCert();
                    return true;
                }
                return false;
            }
        });
        //check cert status
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            connect(false);
            }
        });
        //connect to the robot
        certButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadCert();
            }
        });
        connectButton.setEnabled(false);
        if(checkCert() && sharedPref.getBoolean("connected",false) && !checkConnectionStatus()){
            connect(true);
        }
        return root;
    }
    private void connect(boolean silent){
        if(!silent){
            dialog = new ProgressDialog(getActivity());
            dialog.show();
        }
        if(!isConnecting){
            isConnecting = true;
            InitConnectionTask task = new InitConnectionTask(this);
            task.execute(
                    sharedPref.getString("vector_serial", ""),
                    sharedPref.getString("vector_ip", ""),
                    sharedPref.getString("vector_name", "")
            );
        }
    }
    private void downloadCert(){
        dialog = new ProgressDialog(getActivity());
        dialog.show();
        saveCredentials();
        DownloadCertTask task= new DownloadCertTask(this);
        task.execute(
                nameEditText.getText().toString(),
                ipEditText.getText().toString(),
                serialEditText.getText().toString(),
                emailEditText.getText().toString(),
                passwordEditText.getText().toString()
        );
    }
    private boolean checkCert(){
        File file = new File(getContext().getFilesDir()+"/.anki_vector/","sdk_config.ini");
        Log.d("DEBUG",getContext().getFilesDir()+"/.anki_vector/");
        if(file.exists()){
            certStatusText.setText(getString(R.string.home_cert_found));
            certStatusText.setTextColor(getResources().getColor(R.color.colorGreen));
            connectButton.setEnabled(true);
            return true;
        }
        else{
            certStatusText.setText(getString(R.string.home_cert_missing));
            certStatusText.setTextColor(getResources().getColor(R.color.colorRed));
            return false;
        }
    }
    private void saveCredentials(){
        editor.putString("vector_name",nameEditText.getText().toString());
        editor.putString("vector_serial", serialEditText.getText().toString());
        editor.putString("vector_ip",ipEditText.getText().toString());
        editor.putString("vector_email",emailEditText.getText().toString());
        editor.commit();
    }
    public void updateCertStatus(Integer status){
        if (dialog != null &&dialog.isShowing()) {
            dialog.dismiss();
        }
       if(status == 1){
           checkCert();
       }else{
           certStatusText.setTextColor(getResources().getColor(R.color.colorRed));
           certStatusText.setText(getString(R.string.home_cert_missing));
           //add error snackbar?
           Snackbar.make(root, getResources().getString(R.string.home_cert_error), Snackbar.LENGTH_LONG)
                   .setAction("Action", null).show();
       }
    }
    private void updateStatus(){
        new StatsTask(robotStore.getRobot(),this).execute();
    }
    public void updateStats(String vector, String cube){
        switch(vector){
            case "1":
                vectorBar.setProgress(33);
                break;
            case "2":
                vectorBar.setProgress(66);
                break;
            case "3":
                vectorBar.setProgress(100);
                break;

        }

        switch(cube){
            case "1":
                cubeBar.setProgress(50);
                break;
            case "2":
                cubeBar.setProgress(100);
                break;

        }
    }
    private boolean checkConnectionStatus(){
        if(robotStore.isConnected()){
            statusText.setText(getResources().getString(R.string.home_status_c));
            statusText.setTextColor(getResources().getColor(R.color.colorGreen));
            connectButton.setText(getResources().getString(R.string.home_status_disconnect));
            return true;
        }else{
            statusText.setText(getResources().getString(R.string.home_status_d));
            statusText.setTextColor(getResources().getColor(R.color.colorRed));
            connectButton.setText(getResources().getString(R.string.home_status_connect));
            return false;
        }
    }
    public void updateConnectionStatus(Integer status, PyObject connection){
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        isConnecting = false;
        if(status == 1){
            this.robotStore.setRobot(connection);
            editor.putBoolean("connected", true);
            editor.commit();
            updateStatus();
        }else{
            statusText.setText(getResources().getString(R.string.home_status_d));
            statusText.setTextColor(getResources().getColor(R.color.colorRed));
            Snackbar.make(root, getResources().getString(R.string.home_status_error), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        checkConnectionStatus();


    }

}