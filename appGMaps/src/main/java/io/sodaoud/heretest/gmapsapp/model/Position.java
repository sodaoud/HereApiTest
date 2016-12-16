package io.sodaoud.heretest.gmapsapp.model;

import java.io.Serializable;

/**
 * Created by sofiane on 12/13/16.
 */

public class Position implements Serializable {
    double latitude;
    double longitude;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
