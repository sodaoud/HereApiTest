package io.sodaoud.heretest.gmapsapp.model;

import java.io.Serializable;

/**
 * Created by sofiane on 12/13/16.
 */
public class Route implements Serializable {

    class Mode implements Serializable {
        String type;
        String[] transportModes;
        String disabled;
    }

    class Summary implements Serializable {
        int distance;
        int trafficTime;
        int baseTime;
        String[] flags;
        String text;
        int travelTime;
        String _type;
    }

    class BoundingBox implements Serializable{
        Position topLeft;
        Position bottomRight;
    }

    Waypoint[] waypoint;
    Mode mode;
    String[] shape;
    String[] label;
    Leg[] leg;
    Summary summary;
    BoundingBox boundingBox;

    public String getText() {
        return summary.text;
    }

    public String[] getShape() {
        return shape;
    }

    public Position getTopLeft() {
        return boundingBox.topLeft;
    }

    public Position getBottomRight() {
        return boundingBox.bottomRight;
    }
}
