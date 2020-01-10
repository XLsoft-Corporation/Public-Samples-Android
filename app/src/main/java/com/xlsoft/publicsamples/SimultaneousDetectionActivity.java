package com.xlsoft.publicsamples;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import eu.kudan.kudan.ARAPIKey;
import eu.kudan.kudan.ARActivity;
import eu.kudan.kudan.ARImageNode;
import eu.kudan.kudan.ARImageTrackable;
import eu.kudan.kudan.ARImageTracker;

/*

 This class is a demonstration of the simultaneous tracking feature of the iOS SDK.
 The framework has no hard limit on the number of markers that can be simultaneously detected and tracked.
  However, we have the option to limit the number of simultaneous detections which can improve performance and battery life.

 In this demo we split our standard lego marker into four pieces and add images of numbers as children of the pieces.
 We then add a stepper so that we can experiment with adjusting the maximum simultaneous tracking property.

 */

public class SimultaneousDetectionActivity extends ARActivity implements SeekBar.OnSeekBarChangeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simultaneous_detection);

        // Add the API Key, which can be found at https://wiki.kudan.eu/Development_License_Keys
        // This key will only work when using the eu.kudan.ar application Id
        ARAPIKey key = ARAPIKey.getInstance();
        key.setAPIKey("fb5BokyKQbGvAtakUwuJqlJS8CDuRycSYR0P57E9GTRUPmgPtFyvUjsX79dHSo5KFKzJTppnBFpDsMbtotW+kD+vOQSylZ89EhW9O4SRubZmzSR8s8nZujfkTbi5DdItkXixWdDlQSmsWfWC9dX2lczQGbns83A81rQHUSm6J7EaxF8uL6yp6TyoBvSVHYBc0/XelYd39MwtP7txWZrQbmXMIDZsdzVkTM7I+HgwehzNkf9zjBVN6iuKopI+/5Q0p/X4JT2FmpzWFA/XY5mEJElk4T56BBAFm9ib7WrXOqAVcYgmHm7WL1K8BLIfa11mJMBMkOjoHQY8K865IKP1rbfqUrIifTS1MUQjVsHbQL6jQReYqqErqcEmYNFKw9CP99q+HEnV4liF0ikwVZaj16HLXPYzstGXmj1bPj574+AGY9aDX3ixSQoqI/Idx/fGMySPQXCFjxps19nMFaVY6lYG/rSiKhIKF+io2sFPFrL5DrRqb6Da9IeWGPCk6NOSW4RWVa+8pxItpCgyeY+doh4AkMTKgw0QOcrsZ/arww1jZqegdDlRpImJ07zUYDMJnsACD1N9wA4mG/VJ6Zpa7xoGFOerAvcBVT5ZjuxbtrjlnxZ4mJB6KRgEHJLbpw4NQZpbG3HcTeUM2+PMKSoXXuP4zXjVKLkK6STRGoAWlVc=");

        // Add listener for Seekbar callback methods
        addListener();
    }

    @Override
    public void setup() {
        super.setup();

        // An array that contains the names of the images of the pieces of our lego marker
        String[] trackableImageNames = new String[] {"legoOne.jpg", "legoTwo.jpg", "legoThree.jpg", "legoFour.jpg"};

        // An array that contains the names of the images of pictures of numbers
        String[] imageNodeNames = new String[] {"oneImage.png", "twoImage.png", "threeImage.png", "fourImage.png"} ;

        // Create an ArrayList of trackables from an array of image names
        ArrayList<ARImageTrackable> trackables = createTrackables(trackableImageNames);

        // Create an ArrayLost of image nodes from an array of image names
        ArrayList<ARImageNode> imageNodes = createImageNodes(imageNodeNames);

        // Add the image nodes to the corresponding trackable
        addImageNodesToTrackables(imageNodes,trackables);

        // Add the trackables to the image tracker manager
        addTrackablesToManager(trackables);

    }

    private void addListener()
    {
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);

        seekBar.setOnSeekBarChangeListener(this);
    }

    private ArrayList<ARImageTrackable> createTrackables (String[] imageNames){

        // Create an ArrayList of image trackables
        ArrayList<ARImageTrackable> trackables = new ArrayList<>();

        for (int i = 0 ; i < imageNames.length ; i++)
        {
            // Get the image name
            String trackableImageName = imageNames[i];

            // Create a trackable from that image, the first line sets the trackable's name
            ARImageTrackable trackable = new ARImageTrackable(trackableImageName);
            trackable.loadFromAsset(trackableImageName);

            // Add the trackable to the trackables array
            trackables.add(trackable);
        }

        return trackables;
    }

    private  ArrayList<ARImageNode> createImageNodes (String[] imageNames)
    {
        // Create an ArrayList of image nodes
        ArrayList<ARImageNode> imageNodes = new ArrayList<>();

        for (int i = 0; i < imageNames.length; i++)
        {
            // Get the image name
            String imageName = imageNames[i];

            // Create an imageNode from that image and set the name
            ARImageNode imageNode = new ARImageNode(imageName);
            imageNode.setName(imageName);

            // Add the imageNode to the array
            imageNodes.add(imageNode);
        }

        return imageNodes;
    }

    private void addImageNodesToTrackables(ArrayList<ARImageNode> imageNodes, ArrayList<ARImageTrackable> trackables)
    {
        // This method only works if there are the same number of image nodes as there are trackables to add those image nodes to
        if (imageNodes.size() == trackables.size())
        {
            for (int i = 0; i < imageNodes.size(); i++)
            {
                // For each trackable, add the corresponding image node
                ARImageTrackable trackable = trackables.get(i);
                ARImageNode imageNode = imageNodes.get(i);
                trackable.getWorld().addChild(imageNode);
            }
        }
    }

    private void addTrackablesToManager(ArrayList<ARImageTrackable>trackables)
    {
        ARImageTracker tracker = ARImageTracker.getInstance();
        tracker.initialise();

        // Add the trackables in the array to the trackable manager
        for (ARImageTrackable trackable : trackables)
        {
            tracker.addTrackable(trackable);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
        // Adjusts the maximum number of trackables that can be simultaneously tracked

        // Note: If a number of trackables are currently being tracked, setting the maximum to a value below this number will not cause any trackables to be lost.

        // Instead it means that any new trackables will not be detected if the number that is currently being tracked is
        // equal to or above the maximum value


        String valueString = String.valueOf(seekBar.getProgress());

        if (valueString.equalsIgnoreCase("0")){
          valueString = valueString.concat(" (Unlimited) ");
        }
        TextView textView = (TextView)findViewById(R.id.textView);

        // Adjust our label text to reflect the new maximum simultaneous tracking number
        textView.setText(valueString);

        ARImageTracker.getInstance().setMaximumSimultaneousTracking(seekBar.getProgress());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
