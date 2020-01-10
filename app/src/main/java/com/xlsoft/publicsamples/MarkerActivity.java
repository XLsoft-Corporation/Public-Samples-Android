package com.xlsoft.publicsamples;

import android.os.Bundle;
import android.util.Log;

import eu.kudan.kudan.ARAPIKey;
import eu.kudan.kudan.ARActivity;
import eu.kudan.kudan.ARImageNode;
import eu.kudan.kudan.ARImageTrackable;
import eu.kudan.kudan.ARImageTrackableListener;
import eu.kudan.kudan.ARImageTracker;

public class MarkerActivity extends ARActivity implements ARImageTrackableListener {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Add the API Key, which can be found at https://wiki.kudan.eu/Development_License_Keys
        //This key will only work when using the eu.kudan.ar application Id
        ARAPIKey key = ARAPIKey.getInstance();
        key.setAPIKey("fb5BokyKQbGvAtakUwuJqlJS8CDuRycSYR0P57E9GTRUPmgPtFyvUjsX79dHSo5KFKzJTppnBFpDsMbtotW+kD+vOQSylZ89EhW9O4SRubZmzSR8s8nZujfkTbi5DdItkXixWdDlQSmsWfWC9dX2lczQGbns83A81rQHUSm6J7EaxF8uL6yp6TyoBvSVHYBc0/XelYd39MwtP7txWZrQbmXMIDZsdzVkTM7I+HgwehzNkf9zjBVN6iuKopI+/5Q0p/X4JT2FmpzWFA/XY5mEJElk4T56BBAFm9ib7WrXOqAVcYgmHm7WL1K8BLIfa11mJMBMkOjoHQY8K865IKP1rbfqUrIifTS1MUQjVsHbQL6jQReYqqErqcEmYNFKw9CP99q+HEnV4liF0ikwVZaj16HLXPYzstGXmj1bPj574+AGY9aDX3ixSQoqI/Idx/fGMySPQXCFjxps19nMFaVY6lYG/rSiKhIKF+io2sFPFrL5DrRqb6Da9IeWGPCk6NOSW4RWVa+8pxItpCgyeY+doh4AkMTKgw0QOcrsZ/arww1jZqegdDlRpImJ07zUYDMJnsACD1N9wA4mG/VJ6Zpa7xoGFOerAvcBVT5ZjuxbtrjlnxZ4mJB6KRgEHJLbpw4NQZpbG3HcTeUM2+PMKSoXXuP4zXjVKLkK6STRGoAWlVc=");

    }
    
    // This method should be overridden and all of the AR content setup placed within it
    // This method is called only at the point at which the AR Content is ready to be set up
    @Override
    public void setup() {
        super.setup();

        // Create our trackable with an image
        ARImageTrackable trackable = createTrackable("Marker","lego.jpg");

        // Get the trackable Manager singleton
        ARImageTracker trackableManager = ARImageTracker.getInstance();
        trackableManager.initialise();

        //Add image trackable to the image tracker manager
        trackableManager.addTrackable(trackable);

        // Create an image node using an image of the kudan cow
        ARImageNode cow = new ARImageNode("target.png");

        // Add the image node as a child of the trackable's world
        trackable.getWorld().addChild(cow);

        // Add listener methods that are defined in the ARImageTrackableListener interface
        trackable.addListener(this);
    }

    private ARImageTrackable createTrackable(String trackableName, String assetName)
    {
        // Create a new trackable instance with a name
        ARImageTrackable trackable = new ARImageTrackable(trackableName);

        // Load the  image for this marker
        trackable.loadFromAsset(assetName);

        return trackable;
    }

    // ARImageTrackableListener interface functions, these are called in response to various tracking events
    @Override
    public void didDetect(ARImageTrackable arImageTrackable) {
        Log.d("Marker","Did Detect");
    }

    @Override
    public void didLose(ARImageTrackable arImageTrackable) {
        Log.d("Marker","Did Lose");

    }

    @Override
    public void didTrack(ARImageTrackable arImageTrackable) {
        Log.d("Marker","Did Track");
    }
}
