package hackatum.de.checkcrash;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Geolocation {

    public static LatLng actualLocation;

    /**
     * Get address and street name by LatLng
     *
     * @param c
     * @param location
     * @return [addres field1, addres field2,addres field3]
     */
    public static String[] getLocationName(Context c, LatLng location) {
        Geocoder geocoder = new Geocoder(c, Locale.GERMAN);
        try {
            List<Address> locations = geocoder.getFromLocation(location.latitude, location.longitude, 1);
            Address address = locations.get(0);
            System.out.println(address.getAddressLine(3));
            return new String[]{address.getAddressLine(0), address.getAddressLine(1), address.getAddressLine(2)};
        } catch (IOException e) {
            e.printStackTrace();
            return new String[]{"", "", ""};
        }
    }

    /**
     * request the location
     *
     * @param c
     * @param api
     */
    public static void requestGeolocation(Context c, GoogleApiClient api) {
        LocationManager locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);

        //Stop if the permission to access location are not granted
        if (ActivityCompat.checkSelfPermission(c, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(c, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //Get location and store in `actualLocation`
        Location location = LocationServices.FusedLocationApi.getLastLocation(api);

        if (location == null) {
            return;
        }
        actualLocation = new LatLng(location.getLatitude(), location.getLongitude());
    }


}
