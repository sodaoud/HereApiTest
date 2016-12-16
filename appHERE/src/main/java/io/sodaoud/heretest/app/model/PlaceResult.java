package io.sodaoud.heretest.app.model;

import android.os.Parcel;

/**
 * Created by sofiane on 12/12/16.
 */

public class PlaceResult extends Place {

    long distance;
    String icon;

    public PlaceResult(Parcel in) {
        super(in);
    }


    public long getDistance() {
        return distance;
    }


    public String getIcon() {
        return icon;
    }

}
