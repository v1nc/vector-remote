package de.reckendrees.systems.vectorremote.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.chaquo.python.PyObject;

import de.reckendrees.systems.vectorremote.R;
import de.reckendrees.systems.vectorremote.api.BehaviorTask;
import de.reckendrees.systems.vectorremote.interfaces.MainInterface;

public class UtilitiesFragment extends Fragment {


    private MainInterface robotStore;
    public static UtilitiesFragment newInstance(MainInterface robotStore) {
        UtilitiesFragment fragment = new UtilitiesFragment(robotStore);
        return fragment;
    }
    public UtilitiesFragment(MainInterface robotStore){
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
        View root = inflater.inflate(R.layout.fragment_utilities, container, false);
        final EditText sayEditText= root.findViewById(R.id.editTextSay);
        final Button buttonSay = root.findViewById(R.id.buttonSay);
        final Button driveOn = root.findViewById(R.id.buttonDriveOn);
        final Button driveOff = root.findViewById(R.id.buttonDriveOff);
        buttonSay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String wordsToSay = sayEditText.getText().toString();
                new BehaviorTask(robotStore.getRobot().get("behavior")).execute("say_text",wordsToSay);
            }
        });
        driveOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BehaviorTask(robotStore.getRobot().get("behavior")).execute("drive_on_charger");
            }
        });
        driveOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BehaviorTask(robotStore.getRobot().get("behavior")).execute("drive_off_charger");
            }
        });
        sayEditText.setText("");
        return root;
    }
}