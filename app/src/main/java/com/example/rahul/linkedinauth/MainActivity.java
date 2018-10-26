package com.example.rahul.linkedinauth;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    Button btn_linkedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn_linkedIn = findViewById(R.id.btn_linkedIn);
        btn_linkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LISessionManager.getInstance(getApplicationContext()).init(MainActivity.this, buildScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {
                        Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                        // Authentication was successful.  You can now do
                        // other calls with the SDK.
                    }

                    @Override
                    public void onAuthError(LIAuthError error) {
                        Toast.makeText(MainActivity.this, "" + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("llll", error.toString());
                        // Handle authentication errors
                    }
                }, true);


            }
        });


        generateHashkey();

    }


    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Add this line to your existing onActivityResult() method
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }


    public void generateHashkey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.rahul.linkedinauth",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                Log.e("jjjjj", info.packageName);
                Log.e("jjjjj", Base64.encodeToString(md.digest(),
                        Base64.NO_WRAP));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("", e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            Log.d("", e.getMessage(), e);
        }
    }
}
