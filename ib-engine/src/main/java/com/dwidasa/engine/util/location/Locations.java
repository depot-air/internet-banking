package com.dwidasa.engine.util.location;

import com.dwidasa.engine.model.Location;

import java.util.*;

/**
 * Use this Class for sorting and get distances for set of {@link com.dwidasa.engine.model.Location}
 * from a certain given GPS coordinate
 * <p/>
 * To use this class:<br/>
 * 1. Call the constructor, it will initiate, calculate, and sort the locations and distances<br/>
 * 2. Call {@link Locations.getSortedLocation} to get sorted locations by distance from coordinate<br/>
 * 3. For every {@link com.dwidasa.engine.model.Location}, call {@link Locations.getDistance(Location)}
 * to get the distance of {@link Location} to the given coordinate
 *
 * @author satriyo
 */
public class Locations {
    private double latitude;
    private double longitude;
    private HashMap<Location, Double> distances;
    private List<Location> sortedLocations;

    /**
     * constructor Class to initiate and calculate given GPS coordinate
     * position with set of {@link com.dwidasa.engine.model.Location}
     *
     * @param latitude  latitude degree of GPS coordinate
     * @param longitude longitude degree of GPS coordinate
     * @param locations set of {@link com.dwidasa.engine.model.Location}
     *                  to be sorted and to be calculated
     */
    public Locations(double latitude, double longitude, List<Location> locations) {
        this.latitude = latitude;
        this.longitude = longitude;

        this.calculateDistance(locations);
    }

    /**
     * calculate distances for set of {@link com.dwidasa.engine.model.Location}
     * to a given GPS coordinate
     *
     * @param locations set of {@link com.dwidasa.engine.model.Location} to be calculated
     */
    private void calculateDistance(List<Location> locations) {
        distances = new HashMap<Location, Double>();
        @SuppressWarnings("unchecked")
        SortedSet<LocationAndDistance> set = new TreeSet<LocationAndDistance>(new DistanceComparator());

        for (Location location : locations) {

            double distance = LocationCalc.getDistanceBetweenCoordinates(this.latitude, this.longitude,
                    location.getLatitude(), location.getLongitude());

            distances.put(location, distance);
            set.add(new LocationAndDistance(location, distance));
        }

        sortedLocations = new ArrayList<Location>();
        for (LocationAndDistance location : set) {
            sortedLocations.add(location.location);
        }
    }

    /**
     * get sorted set of {@link com.dwidasa.engine.model.Location}.
     * List sorted by distance to the given coordinate
     *
     * @return sorted set of {@link com.dwidasa.engine.model.Location}
     */
    public List<Location> getSortedLocation() {
        return this.sortedLocations;
    }

    /**
     * get distance of a {@link com.dwidasa.engine.model.Location} from given coordinate
     *
     * @param location certain location that has been supplied during Class construction
     * @return distance to the given coordinate (in meters)
     */
    public double getDistance(Location location) {
        return this.distances.get(location);
    }

    /**
     * Class to represent Location and Distance to certain coordinate
     *
     * @author ryoputranto
     */
    private class LocationAndDistance {
        Location location;
        double distance;

        LocationAndDistance(Location location, double distance) {
            this.location = location;
            this.distance = distance;
        }
    }

    /**
     * Comparator Class to compare distance between to {@link LocationAndDistance}
     *
     * @author ryoputranto
     */
    @SuppressWarnings("rawtypes")
    private class DistanceComparator implements Comparator {
        public int compare(Object arg0, Object arg1) {
            return this.compare((LocationAndDistance) arg0, (LocationAndDistance) arg1);
        }

        private int compare(LocationAndDistance o1, LocationAndDistance o2) {
            return Double.compare(o1.distance, o2.distance);
        }
    }
}
