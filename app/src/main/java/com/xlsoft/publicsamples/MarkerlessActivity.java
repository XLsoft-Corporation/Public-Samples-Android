package com.xlsoft.publicsamples;

/*

 In this demo we demonstrate one particular implementation of markerless tracking, which we refer to as "Floor Tracking".
 The floor aspect refers to the fact that we add the target node as a child of the gyroplacemanager's world.

 This world is placed on a plane representing the floor and so can be used for tracking AR content that remains fixed relative to a point on the floor

 */

import android.support.v4.view.GestureDetectorCompat;
import android.os.Bundle;

import eu.kudan.kudan.ARAPIKey;
import eu.kudan.kudan.ARActivity;
import eu.kudan.kudan.ARArbiTrack;
import eu.kudan.kudan.ARGyroPlaceManager;
import eu.kudan.kudan.ARImageNode;
import eu.kudan.kudan.ARNode;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;


public class MarkerlessActivity extends ARActivity implements
        GestureDetector.OnGestureListener{

    private GestureDetectorCompat gestureDetect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Create gesture recogniser to start and stop arbitrack
        gestureDetect = new GestureDetectorCompat(this,this);

        // Add the API Key, which can be found at https://wiki.kudan.eu/Development_License_Keys
        // This key will only work when using the eu.kudan.ar application Id
        ARAPIKey key = ARAPIKey.getInstance();
        key.setAPIKey("fb5BokyKQbGvAtakUwuJqlJS8CDuRycSYR0P57E9GTRUPmgPtFyvUjsX79dHSo5KFKzJTppnBFpDsMbtotW+kD+vOQSylZ89EhW9O4SRubZmzSR8s8nZujfkTbi5DdItkXixWdDlQSmsWfWC9dX2lczQGbns83A81rQHUSm6J7EaxF8uL6yp6TyoBvSVHYBc0/XelYd39MwtP7txWZrQbmXMIDZsdzVkTM7I+HgwehzNkf9zjBVN6iuKopI+/5Q0p/X4JT2FmpzWFA/XY5mEJElk4T56BBAFm9ib7WrXOqAVcYgmHm7WL1K8BLIfa11mJMBMkOjoHQY8K865IKP1rbfqUrIifTS1MUQjVsHbQL6jQReYqqErqcEmYNFKw9CP99q+HEnV4liF0ikwVZaj16HLXPYzstGXmj1bPj574+AGY9aDX3ixSQoqI/Idx/fGMySPQXCFjxps19nMFaVY6lYG/rSiKhIKF+io2sFPFrL5DrRqb6Da9IeWGPCk6NOSW4RWVa+8pxItpCgyeY+doh4AkMTKgw0QOcrsZ/arww1jZqegdDlRpImJ07zUYDMJnsACD1N9wA4mG/VJ6Zpa7xoGFOerAvcBVT5ZjuxbtrjlnxZ4mJB6KRgEHJLbpw4NQZpbG3HcTeUM2+PMKSoXXuP4zXjVKLkK6STRGoAWlVc=");

        setContentView(R.layout.activity_markerless);
    }

    // This method should be overridden and all of the AR content setup placed within it
    // This method is called only at the point at which the AR Content is ready to be set up
    @Override
    public void setup() {
        super.setup();
        
        // We choose the orientation of our floor node so that the target node lies flat on the floor. We rotate the node by -90 degrees about the x axis
        float[] angles = {-(float)Math.PI/2.0f,0.0f, 0.0f};
        Quaternion floorOrientation = new Quaternion(angles);

        // Create a target node. A target node is a node whose position is used to determine the initial position of arbitrack's world when arbitrack is started
        // The target node in this case is an image node of the Kudan Cow
        Vector3f floorScale = new Vector3f(0.5f,0.5f,0.5f);
        ARImageNode floorTarget = createImageNode("cowtarget.png",floorOrientation,floorScale);

        // Add our target node to the gyroplacemanager's world
        // The position of the target node is used to determine the initial position of arbitrack's world
        addNodeToGyroPlaceManager(floorTarget);

        // Create an image node to place in arbitrack's world
        // We can choose the tracking node to have the same orientation as the target node
        Vector3f trackingScale = new Vector3f(1.0f,1.0f,1.0f);
        ARImageNode trackingImageNode = createImageNode("cowtracking.png",floorOrientation,trackingScale);

        // Set up arbitrack
        setUpArbiTrack(floorTarget, trackingImageNode);
    }

    private ARImageNode createImageNode(String imageName, Quaternion orientation, Vector3f scale)
    {
        ARImageNode imageNode = new ARImageNode(imageName);
        imageNode.setOrientation(orientation);
        imageNode.setScale(scale);

        return imageNode;
    }

    private void addNodeToGyroPlaceManager(ARNode node)
    {
        // The gyroplacemanager positions it's world on a plane that represents the floor.
        // You can adjust the floor depth (The distance between the device and the floor) using ARGyroPlaceManager's floor depth variable.
        // The default floor depth is -150
        ARGyroPlaceManager gyroPlaceManager = ARGyroPlaceManager.getInstance();
        gyroPlaceManager.initialise();
        gyroPlaceManager.getWorld().addChild(node);
    }

    private void setUpArbiTrack(ARNode targetNode, ARNode childNode)
    {
        // Get the arbitrack manager and initialise it
        ARArbiTrack arbiTrack = ARArbiTrack.getInstance();
        arbiTrack.initialise();

        // Set it's target node
        arbiTrack.setTargetNode(targetNode);

        // Add the tracking image node to the arbitrack world
        arbiTrack.getWorld().addChild(childNode);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetect.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        ARArbiTrack arbiTrack = ARArbiTrack.getInstance();

        // If arbitrack is tracking, stop the tracking so that it's world is no longer rendered, and make it's target nodes visible
        if (arbiTrack.getIsTracking())
        {
            arbiTrack.stop();
            arbiTrack.getTargetNode().setVisible(true);
        }
        // If it's not tracking, start the tracking and hide the target node

        // When arbitrack has started, it will take the initial position of it's target node and it's world will be rendered to the screen.
        // After this, it's pose will be continually updated to give the appearance of it remaining in the same place
        else
        {
            arbiTrack.start();
            arbiTrack.getTargetNode().setVisible(false);
        }

        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
