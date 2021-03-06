/**
 * TeamOne
 */

package com.app.etouchcare.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.etouchcare.R;
import com.app.etouchcare.datamodel.Patients;
import com.app.etouchcare.network.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class AddPatient extends AppCompatActivity {
    final String URL = "https://mapd2016.herokuapp.com/";
    String json="";
    String[] gender = {"male","female", "n/a"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final AddPatient addPatient = this;
        Button btnAdd = (Button) this.findViewById(R.id.btnAdd);

        final Spinner spinner = (Spinner) findViewById(R.id.basic_gender_spinnder);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,gender);
        spinner.setAdapter(arrayAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddPatient.this, "Adding", Toast.LENGTH_SHORT).show();
                EditText txtName = (EditText) findViewById(R.id.editTextName);
                EditText txtRoom = (EditText) findViewById(R.id.editTextRoom);
                EditText txtAge = (EditText) findViewById(R.id.editTextAge);
                EditText txtEmail = (EditText) findViewById(R.id.editTextEmail);
                EditText txtPhone = (EditText) findViewById(R.id.editTextPhone);
                EditText txtAddress = (EditText) findViewById(R.id.editTextAddress);
                EditText txtEmgrName = (EditText) findViewById(R.id.editTextEmergencyName);
                EditText txtEmgrPhone = (EditText) findViewById(R.id.editTextEmergencyPhone);
                EditText txtDiagnosis = (EditText) findViewById(R.id.editTextDiagnosis);

                Patients patient = new Patients();

                patient.setName(txtName.getText().toString());
                patient.setRoom(txtRoom.getText().toString());
                patient.setAge(txtAge.getText().toString());
                patient.setAddress(txtAddress.getText().toString());
                patient.setEmail(txtEmail.getText().toString());
                patient.setPhone(txtPhone.getText().toString());
                patient.setEmergencyName(txtEmgrName.getText().toString());
                patient.setEmergencyPhone(txtEmgrPhone.getText().toString());
                patient.setDiagnosis(txtDiagnosis.getText().toString());

                patient.setGender(spinner.getSelectedItem().toString());

                if (patient.getName().isEmpty())
                {
                    Toast.makeText(AddPatient.this, "Please enter Name", Toast.LENGTH_SHORT).show();
                    txtName.requestFocus();
                    return;
                }
                if (patient.getEmail().isEmpty() || !isValidEmail(patient.getEmail()))
                {
                    Toast.makeText(AddPatient.this, "Please enter valid E-Mail", Toast.LENGTH_SHORT).show();
                    txtEmail.requestFocus();
                    return;
                }
                if (patient.getRoom().isEmpty())
                {
                    Toast.makeText(AddPatient.this, "Please enter Room", Toast.LENGTH_SHORT).show();
                    txtRoom.requestFocus();
                    return;
                }

                Gson gson = new Gson();
                json = gson.toJson(patient);

                JsonObjectRequest req = null;
                try {
                    req = new JsonObjectRequest(URL, new JSONObject(json),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        VolleyLog.v("Response:%n %s", response.toString(4));

                                        Intent intent = new Intent(addPatient, MainPatientListActivity.class);
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.e("Error: ", error.getMessage());
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // add the request object to the queue to be executed
                RequestQueue reqq = VolleySingleton.getInstance().getmRequestQueue();
                reqq.add(req);
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void addPatient(Patients patient){
        Gson gson = new Gson();
        json = gson.toJson(patient);
        JsonObjectRequest req = null;
        try {
            req = new JsonObjectRequest(URL, new JSONObject(json),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                VolleyLog.v("Response:%n %s", response.toString(4));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // add the request object to the queue to be executed
        RequestQueue reqq = VolleySingleton.getInstance().getmRequestQueue();
        reqq.add(req);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_patient_list, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
