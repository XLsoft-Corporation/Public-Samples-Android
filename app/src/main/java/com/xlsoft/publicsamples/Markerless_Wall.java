package com.xlsoft.publicsamples;

/*

 In this demo we demonstrate one particular implementation of markerless tracking, which we refer to as "Wall Tracking".

 For this implementation, we add the target node as a child of the camera node associated with the content view port.
 This causes the target node to stay in a fixed position relative to the screen, regardless of the device orientation

 */

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.view.GestureDetectorCompat;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.WindowManager;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import eu.kudan.kudan.ARAPIKey;
import eu.kudan.kudan.ARActivity;
import eu.kudan.kudan.ARArbiTrack;
import eu.kudan.kudan.ARArbiTrackListener;
import eu.kudan.kudan.ARImageNode;
import eu.kudan.kudan.ARNode;
import eu.kudan.kudan.ARRenderer;


public class Markerless_Wall extends ARActivity implements GestureDetector.OnGestureListener, ARArbiTrackListener{

    private GestureDetectorCompat gestureDetect;
    // Keep a reference to the wall target node to allow for rotations when the device rotates
    private ARNode wallTargetNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markerless_wall);
        // Create gesture recogniser to start and stop arbitrack
        gestureDetect = new GestureDetectorCompat(this,this);

        // Add the API Key, which can be found at https://wiki.kudan.eu/Development_License_Keys
        // This key will only work when using the eu.kudan.ar application Id
        ARAPIKey key = ARAPIKey.getInstance();
        key.setAPIKey("cESnkLOsPfTyPAuGnPesSHVOl05R0iTOWAB6Bv2uDWCY35aBdaTuKRKbMmtTyQCrConWpLN3hki+d3rG23o6Zq3/RRtc3cKWHhMvtM1EcTS1FaJbGfHVmJL8kBEboJ/SGBcBJ9mZ0NSpVULZpI6YrYF6FVjUUJbFJILtFpFusL2zT8B/fatr7ZQwUfo0P4bZ/EcObSCdJzEbTakOHXZbwZeu+uoHUB5Ukf0N+f+cVGZAm0UqBQkaiZR4lXUBaou1VS/oePGJVQI6YaHWw9kxLHJPHX+Rnz6RzliB4gEISRSBnX3itVl1vv7LDHcS5E4PZrIjLo5oNYlsv56sobaqJNwtqSIcrvaVI8BGbMiG8uOmOmgkF/xnSCI4RYBVoDQT3wZY7YecMMc/pwvLMW8EEO5nDYEGZy04gKBQzVcILA7gAmjkPZtTiow/TD5nvIy3VyUkUojhGjEo/wC5OPkpYagZoHHmuy01sRymU3vyTf0QGwH7kZkiELlvqcTHT0rjL+NDSyRolLYpuXqhjBEEdz9JJYZ5XcVk3JBIEebJbejeZ7J2/3syyeCyahQ/5z9iwJxXRYgci6nUuprHE4SUGMJeMjnRp6bwlIfkdC3PtWxLN5XbNRO3C2t/zOMXdqJnQ4nMuiDFreNTxFcM0Pah2D60WwfZVNZlSTUudQAGFSw=");
    }

    // This method should be overridden and all of the AR content setup placed within it
    // This method is called only at the point at which the AR Content is ready to be set up
    @Override
    public void setup() {
        super.setup();

        // We choose the orientation of the wall node to depend on the device orientation
        Quaternion wallOrientation = wallOrientationForDeviceOrientation();

        // Create a target node. A target node is a node whose position is used to determine the initial position of arbitrack's world when arbitrack is started
        // The target node in this case is an image of the Kudan Cow
        // Place the target node a distance of 1000 behind the screen
        Vector3f targetPosition = new Vector3f(0,0,-1000);
        Vector3f wallScale = new Vector3f(0.5f,0.5f,0.5f);
        this.wallTargetNode = createImageNode("cowtarget.png",wallOrientation,wallScale,targetPosition);

        // Add our target node as a child of the camera node associated with the content view port
        getARView().getContentViewPort().getCamera().addChild(wallTargetNode);

        // Create an image node to place in arbitrack's world
        ARImageNode trackingImageNode = createImageNode("cowtracking.png",Quaternion.IDENTITY,Vector3f.UNIT_XYZ,Vector3f.ZERO);

        // Set up arbitrack
        setUpArbiTrack(this.wallTargetNode,trackingImageNode);
    }

    private ARImageNode createImageNode(String imageName, Quaternion orientation, Vector3f scale, Vector3f position)
    {
        ARImageNode imageNode = new ARImageNode(imageName);
        imageNode.setOrientation(orientation);
        imageNode.setPosition(position);
        imageNode.setScale(scale);

        return imageNode;
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

        // Add this activity as a listener of arbitrack
        arbiTrack.addListener(this);
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

    // Called by the system when the device configuration changes while your
    // activity is running

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        this.wallTargetNode.setOrientation(wallOrientationForDeviceOrientation());

    }

    //  Method called when arbitrack has started tracking and is receiving gyro updates
    @Override
    public void arbiTrackStarted() {
        ARArbiTrack arbiTrack = ARArbiTrack.getInstance();

        // Rotate the tracking node so that it has the same full orientation as the target node
        // As the target node is a child of the camera world and the tracking node is a child of arbitrack's world, we must first rotate the tracking node by the inverse of arbitrack's world orientation.
        // This is so to the eye it has the same orientation as the target node

        // At this point we can update the orientation of the tracking node as arbitrack will have updated it's orientation
        Quaternion targetOrientation = arbiTrack.getTargetNode().getOrientation();
        ARNode trackingNode = arbiTrack.getWorld().getChildren().get(0);
        trackingNode.setOrientation(arbiTrack.getWorld().getOrientation().inverse().mult(targetOrientation));
    }

    // Returns the correct orientation for the wall target node for various device orientations
    private static Quaternion wallOrientationForDeviceOrientation()
    {
        Context context = ARRenderer.getInstance().getActivity();
        Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int displayRotation = display.getRotation();

        // The angles we will rotate our wall node by
        // The components are {x,y,z} in radians
        float[] angles = {0.0F,0.0F,0.0F};

        Quaternion rotation;

        switch (displayRotation)
        {
            case Surface.ROTATION_0:
                angles[2] = (float)Math.PI/2.0F;
                rotation = new Quaternion(angles);
                return rotation;
            case Surface.ROTATION_90:
                rotation = Quaternion.IDENTITY;
                return rotation;
            case Surface.ROTATION_180:
                angles[2] = -(float)Math.PI/2.0F;
                rotation = new Quaternion(angles);
                return  rotation;
            case Surface.ROTATION_270:
                angles[2] = (float)Math.PI;
                rotation = new Quaternion(angles);
                return rotation;
        }

        return Quaternion.IDENTITY;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetect.onTouchEvent(event);
        return super.onTouchEvent(event);
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
