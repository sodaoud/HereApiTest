package io.sodaoud.heretest.app.util;

import android.location.Location;

import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import io.sodaoud.heretest.app.model.Place;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by sofiane on 12/18/16.
 */

@RunWith(MockitoJUnitRunner.class)
public class UtilTest {


    @Test
    public void test() {
        assertThat(Util.getWaypointFromPlace(new Place("ANY", 1d, 2d)), is(equalTo("geo!1.0,2.0")));

        Location location = mock(Location.class);
        when(location.getLatitude()).thenReturn(2d);
        when(location.getLongitude()).thenReturn(3d);
        assertThat(Util.getPositionFormat(location), is(equalTo("geo:2.0,3.0")));

        GeoCoordinate bottomRight = new GeoCoordinate(4d,5d);
        GeoCoordinate topLeft = new GeoCoordinate(6d,7d);
        GeoBoundingBox box = new GeoBoundingBox(topLeft,bottomRight);

        assertThat(Util.getStringBoundingBox(box), is(equalTo("5.0,4.0,7.0,6.0")));
    }
}
