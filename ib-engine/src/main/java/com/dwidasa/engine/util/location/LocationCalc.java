package com.dwidasa.engine.util.location;

/**
 * Use this Class to proceed calculation related to GPS coordinates.
 * Such as: distance between two coordinates, extreme coordinates
 * for a certain Point and a given distance range.
 *
 * @author satriyo
 */
public class LocationCalc {

    //-- radius of the earth planet (in meters)
    public static final double EARTH_RADIUS = 6371009;

    /**
     * calculate distance (in meters) between two GPS coordinates using Haversine formula
     *
     * @param latitude1  latitude of 1st GPS coordinate
     * @param longitude1 longitude of 1st GPS coordinate
     * @param latitude2  latitude of 2nd GPS coordinate
     * @param longitude2 longitude of 2nd GPS coordinate
     * @return distance between two GPS coordinates (in meters)
     */
    public static double getDistanceBetweenCoordinates(double latitude1, double longitude1,
                                                       double latitude2, double longitude2) {
        double dLat = Math.toRadians(latitude2 - latitude1);
        double dLng = Math.toRadians(longitude2 - longitude1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    /**
     * calculate latitude difference for a given distance
     *
     * @param distance distance in meters
     * @return latitude degree difference
     */
    public static double getLatitudeDifference(double distance) {
        double latitudeRadians = distance / EARTH_RADIUS;
        return Math.toDegrees(latitudeRadians);
    }

    /**
     * calculate longitude difference for a given distance and coordinate
     *
     * @param latitude  latitude of coordinate
     * @param longitude longitude of coordinate
     * @param distance  distance from coordinate in meters
     * @return longitude degree difference
     */
    public static double getLongitudeDifference(double latitude, double longitude, double distance) {
        double lat1 = Math.toRadians(latitude);
        double longitudeRadius = Math.cos(lat1) * EARTH_RADIUS;
        double diffLong = (distance / longitudeRadius);
        return Math.toDegrees(diffLong);
    }

    public static void main(String args[]) {
        //-- hitung jarak dsi - warung semar
        System.out.println("hasil = " +
                getDistanceBetweenCoordinates(-6.236039660232128, 106.66072368621826,
                        -6.237660791193615, 106.65999412536621));
    }
}
