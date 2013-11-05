package com.framework.leopardus.utils.wrappers;

import java.util.HashMap;
import java.util.Map;

import android.location.Location;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragmentWrapper {

	public static final int NONE = 0;
	public static final int NORMAL = 1;
	public static final int HYBRID = 2;
	public static final int SATELITE = 3;
	public static final int TERRAIN = 4;

	private GoogleMap mMap;
	Map<String, LatLng> locations = new HashMap<String, LatLng>();

	public MapFragmentWrapper(FragmentActivity activity, int mapId) {
		if (mMap == null) {
			mMap = ((SupportMapFragment) activity.getSupportFragmentManager()
					.findFragmentById(mapId)).getMap();
			if (mMap != null) {
				mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			}
		}
	}

	public GoogleMap getMap() {
		return mMap;
	}

	public void setMapType(int type) {
		if (mMap != null) {
			if (type == NONE) {
				mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
			} else if (type == NORMAL) {
				mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			} else if (type == HYBRID) {
				mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			} else if (type == SATELITE) {
				mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			} else if (type == TERRAIN) {
				mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			}
		}
	}

	public Marker createMarker(String title, Location location) {
		return createMarker(title, location, null, -1);
	}

	public Marker createMarker(String title, Location location, String snippet) {
		return createMarker(title, location, snippet, -1);
	}

	public Marker createMarker(String title, Location location, String snippet,
			int icon) {
		MarkerOptions options = new MarkerOptions();
		options.title(title);
		makeLocation(title, location.getLatitude(), location.getLongitude());
		LatLng loc = locations.get(title);
		options.position(loc);
		if (snippet != null)
			options.snippet(snippet);
		if (icon != -1)
			options.icon(BitmapDescriptorFactory.fromResource(icon));
		Marker m = mMap.addMarker(options);
		return m;
	}

	public Marker createMarker(String title, double lat, double lng) {
		return createMarker(title, lat, lng, null, -1);
	}

	public Marker createMarker(String title, double lat, double lng,
			String snippet) {
		return createMarker(title, lat, lng, snippet, -1);
	}

	public Marker createMarker(String title, double lat, double lng,
			String snippet, int icon) {
		MarkerOptions options = new MarkerOptions();
		options.title(title);
		makeLocation(title, lat, lng);
		LatLng loc = locations.get(title);
		options.position(loc);
		if (snippet != null)
			options.snippet(snippet);
		if (icon != -1)
			options.icon(BitmapDescriptorFactory.fromResource(icon));
		Marker m = mMap.addMarker(options);
		return m;
	}

	public void makeLocation(String name, double lat, double lng) {
		LatLng Location = new LatLng(lat, lng);
		locations.put(name, Location);
	}

	public void MoveCamera(String name, int zoom) {
		if (locations.containsKey(name)) {
			MoveCamera(locations.get(name), zoom);
		}
	}

	public void MoveCamera(double lat, double lng, int zoom) {
		LatLng Location = new LatLng(lat, lng);
		MoveCamera(Location, zoom);
	}

	public void MoveCamera(LatLng Location, int zoom) {
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Location, zoom));
	}

}
