package com.xlsoft.publicsamples;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import eu.kudan.kudan.ARAPIKey;

public class TitleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        requestPermissions();

        // This key will only work when using the com.xlsoft.publicsamples (this app) application Id.
        ARAPIKey key = ARAPIKey.getInstance();
        key.setAPIKey("fb5BokyKQbGvAtakUwuJqlJS8CDuRycSYR0P57E9GTRUPmgPtFyvUjsX79dHSo5KFKzJTppnBFpDsMbtotW+kD+vOQSylZ89EhW9O4SRubZmzSR8s8nZujfkTbi5DdItkXixWdDlQSmsWfWC9dX2lczQGbns83A81rQHUSm6J7EaxF8uL6yp6TyoBvSVHYBc0/XelYd39MwtP7txWZrQbmXMIDZsdzVkTM7I+HgwehzNkf9zjBVN6iuKopI+/5Q0p/X4JT2FmpzWFA/XY5mEJElk4T56BBAFm9ib7WrXOqAVcYgmHm7WL1K8BLIfa11mJMBMkOjoHQY8K865IKP1rbfqUrIifTS1MUQjVsHbQL6jQReYqqErqcEmYNFKw9CP99q+HEnV4liF0ikwVZaj16HLXPYzstGXmj1bPj574+AGY9aDX3ixSQoqI/Idx/fGMySPQXCFjxps19nMFaVY6lYG/rSiKhIKF+io2sFPFrL5DrRqb6Da9IeWGPCk6NOSW4RWVa+8pxItpCgyeY+doh4AkMTKgw0QOcrsZ/arww1jZqegdDlRpImJ07zUYDMJnsACD1N9wA4mG/VJ6Zpa7xoGFOerAvcBVT5ZjuxbtrjlnxZ4mJB6KRgEHJLbpw4NQZpbG3HcTeUM2+PMKSoXXuP4zXjVKLkK6STRGoAWlVc=");
    }

    public void startMarkerActivity(View view)
    {
        Intent intent = new Intent(this,MarkerActivity.class);
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            permissionsNotSelected();
            requestPermissions();
        }
        else
        {
            startActivity(intent);
        }
    }
    public void startMakerlessFloorActivity(View view)
    {
        Intent intent = new Intent(this,MarkerlessActivity.class);
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            permissionsNotSelected();
            requestPermissions();
        }
        else
        {
            startActivity(intent);
        }
    }

    public void startMarkerlessWallActivity(View view)
    {
        Intent intent = new Intent(this,Markerless_Wall.class);
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            permissionsNotSelected();
            requestPermissions();
        }
        else
        {
            startActivity(intent);
        }
    }

    public void startSimultaneousActivity(View view)
    {
        Intent intent = new Intent(this,SimultaneousDetectionActivity.class);
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            permissionsNotSelected();
            requestPermissions();
        }
        else
        {
            startActivity(intent);
        }
    }

    public void requestPermissions()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},111);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case 111: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                }
                else
                {
                    permissionsNotSelected();
                }
            }
        }
    }

    private void permissionsNotSelected()
    {
        Toast.makeText(this,"You must enable permissions in settings",Toast.LENGTH_LONG).show();

    }
}
