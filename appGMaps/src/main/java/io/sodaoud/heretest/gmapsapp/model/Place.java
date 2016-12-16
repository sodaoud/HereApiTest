package io.sodaoud.heretest.gmapsapp.model;

import android.location.Location;
import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by sofiane on 12/14/16.
 */

public class Place implements SearchSuggestion {

    public Place(String title, Location location) {
        this.title = title;
        position = new double[]{
                location.getLatitude(), location.getLongitude()
        };
    }

    public Place(Parcel in) {
        title = in.readString();
        vicinity = in.readString();
        id = in.readString();
        position = in.createDoubleArray();
    }

    String title;
    String vicinity;
    double[] position;
    String id;

    @Override
    public String getBody() {
        return title;
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(vicinity);
        dest.writeString(id);
        dest.writeDoubleArray(position);
    }

    public String getTitle() {
        return title;
    }

    public String getVicinity() {
        return vicinity;
    }

    public double[] getPosition() {
        return position;
    }

    public String getId() {
        return id;
    }
}
