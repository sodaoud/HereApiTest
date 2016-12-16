package io.sodaoud.heretest.app.model;

import java.io.Serializable;

/**
 * Created by sofiane on 12/13/16.
 */

public class Waypoint implements Serializable {
    String linkId;
    Position mappedPosition;
    Position originalPosition;
    String type;
    double spot;
    String sideOfStreet;
    String mappedRoadName;
    String label;
    int shapeIndex;
}
