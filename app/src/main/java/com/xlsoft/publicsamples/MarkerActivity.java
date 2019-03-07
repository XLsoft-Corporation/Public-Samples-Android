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
        key.setAPIKey("cESnkLOsPfTyPAuGnPesSHVOl05R0iTOWAB6Bv2uDWCY35aBdaTuKRKbMmtTyQCrConWpLN3hki+d3rG23o6Zq3/RRtc3cKWHhMvtM1EcTS1FaJbGfHVmJL8kBEboJ/SGBcBJ9mZ0NSpVULZpI6YrYF6FVjUUJbFJILtFpFusL2zT8B/fatr7ZQwUfo0P4bZ/EcObSCdJzEbTakOHXZbwZeu+uoHUB5Ukf0N+f+cVGZAm0UqBQkaiZR4lXUBaou1VS/oePGJVQI6YaHWw9kxLHJPHX+Rnz6RzliB4gEISRSBnX3itVl1vv7LDHcS5E4PZrIjLo5oNYlsv56sobaqJNwtqSIcrvaVI8BGbMiG8uOmOmgkF/xnSCI4RYBVoDQT3wZY7YecMMc/pwvLMW8EEO5nDYEGZy04gKBQzVcILA7gAmjkPZtTiow/TD5nvIy3VyUkUojhGjEo/wC5OPkpYagZoHHmuy01sRymU3vyTf0QGwH7kZkiELlvqcTHT0rjL+NDSyRolLYpuXqhjBEEdz9JJYZ5XcVk3JBIEebJbejeZ7J2/3syyeCyahQ/5z9iwJxXRYgci6nUuprHE4SUGMJeMjnRp6bwlIfkdC3PtWxLN5XbNRO3C2t/zOMXdqJnQ4nMuiDFreNTxFcM0Pah2D60WwfZVNZlSTUudQAGFSw=");

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
