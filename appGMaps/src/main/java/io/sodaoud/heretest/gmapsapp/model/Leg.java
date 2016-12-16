package io.sodaoud.heretest.gmapsapp.model;

import java.io.Serializable;

/**
 * Created by sofiane on 12/13/16.
 */

public class Leg implements Serializable {
    class Maneuver implements Serializable {
        Position position;
        String instruction;
        int travelTime;
        int length;
        String id;
    }

    Waypoint start;
    Waypoint end;
    int length;
    int travelTime;
    Maneuver[] maneuver;


}