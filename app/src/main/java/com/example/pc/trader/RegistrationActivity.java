package com.example.pc.trader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pc.trader.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class RegistrationActivity extends AppCompatActivity {

    private EditText txtEmailAddress;
    private EditText txtPassword;
    private EditText txtNome;
    private FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        txtEmailAddress = findViewById(R.id.txtEmailRegistration);
        txtPassword = findViewById(R.id.txtPasswordRegistration);
        txtNome = findViewById(R.id.txtNameRegistration);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void btnRegistrationUser_Click(View v){
        final ProgressDialog progressDialog = ProgressDialog.show(RegistrationActivity.this, "Por favor, aguarde...", "Processando...", true);
        (firebaseAuth.createUserWithEmailAndPassword(txtEmailAddress.getText().toString(),txtPassword.getText().toString())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if(task.isSuccessful()){
                    Toast.makeText(RegistrationActivity.this,"Registrado com sucesso!",Toast.LENGTH_LONG).show();
                    inicializarFirebase();
                    Usuario usuario = new Usuario();
                    usuario.setUid(UUID.randomUUID().toString());
                    usuario.setNome(txtNome.getText().toString());
                    usuario.setEmail(txtEmailAddress.getText().toString());
                    databaseReference.child("Usuario").child(usuario.getUid()).setValue(usuario);
                    Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(i);
                }else{
                    Log.e("Error", task.getException().toString());
                    Toast.makeText(RegistrationActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();

                }
            }
        });


    }

    private void inicializarFirebase(){

        FirebaseApp.initializeApp(RegistrationActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

}
