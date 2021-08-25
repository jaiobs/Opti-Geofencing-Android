# OptiGeofencingAndroid
The optiGeofencing Android library enables your application to receive user location updates and geofence events with minimal effort in real time. Also we can use all the map util API's here to utilize all the map functionalies.

The library provides a high-level interface to perform different
**categories** of map operations.

## Platform Support

The optiGeofencingAndroid Framework supports Android API level 16 (Android 4.1) and above.

## Using optiGeofencingAndroid from Your App
### Specifying Gradle Dependencies

To begin, copy optiGeofencing module to your `app` and add in `build.gradle`
dependencies section:
```groovy
implementation project(path: ':optiGeofencing')

settings.gradle
include ':optiGeofencing'

change google playservice json settings in app/google-services.json
```
### Java 8 Requirement

Amplify Android _requires_ Java 8 features. Please add a `compileOptions`
block inside your app's `build.gradle`, as below:

```gradle
android {
    compileOptions {
        coreLibraryDesugaringEnabled true
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
```
Features:

- supports Android-Q
- receive updates on background
- geofence updates (dwell, enter, exit)
- location updates
- configurable update intervals
- Find distance between source and destinations
- Show single or multiple markers
- Custom design markers
- Custom info window
- Draw polyline between two location points


### Requirmenets

1. Location permissions in [*AndroidManifest.xml*](app/src/main/AndroidManifest.xml#L8-L9)

	    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
   	 	<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
   	 	
2. Google maps api key

		<string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">YOUR_KEY</string>
	
### How to use

### Geofence

1. Create [Receiver](app/src/main/java/com/sprotte/geolocator/demo/kotlin/GeofenceIntentService.kt)

```kotlin
class GeofenceIntentService : GeofenceIntentService() {
	
    override fun onGeofence(geofence: Geofence) {
    	Log.v(GeoFenceIntentService::class.java.simpleName, "onGeofence $geofence")	    
    }
}
```		
2. Add receiver to your [manifest](app/src/main/AndroidManifest.xml#L45-L47)

	 	<service
            android:name=".kotlin.GeoFenceIntentService"
            android:permission="android.permission.BIND_JOB_SERVICE" />

3. [Start geofence tracking](app/src/main/java/com/sprotte/geolocator/demo/kotlin/MainActivity.kt#L33-L46)

```kotlin
val geofence = Geofence(
    id = UUID.randomUUID().toString(),
    latitude = 51.0899232,
    longitude = 5.968358,
    radius = 30.0,
    title = "Germany",
    message = "Entered Germany",
    transitionType = GEOFENCE_TRANSITION_ENTER
)
    
Geofencer(this).addGeofence(geofence, GeoFenceIntentService::class.java) { /* successfully added geofence */ }
```
### Location Tracker

1. Create [Receiver](app/src/main/java/com/sprotte/geolocator/demo/kotlin/LocationTrackerService.kt)

```kotlin
class LocationTrackerService : LocationTrackerUpdateIntentService() {

	override fun onLocationResult(locationResult: LocationResult) {  
		Log.v(GeoFenceIntentService::class.java.simpleName, "onLocationResult $location")
  }
}
```
2. Add receiver to [manifest](app/src/main/AndroidManifest.xml#L49-L51)

		<service
            android:name=".kotlin.LocationTrackerService"
            android:permission="android.permission.BIND_JOB_SERVICE" />

3. [Start tracking](app/src/main/java/com/sprotte/geolocator/demo/kotlin/MainActivity.kt#L48-L51)

```kotlin
LocationTracker.requestLocationUpdates(this, LocationTrackerService::class.java)
```

4. Stop tracking

```kotlin
LocationTracker.removeLocationUpdates(requireContext())
```

## License

This library is licensed under the [Apache 2.0 License](./LICENSE).

## Report a Bug

We appreciate your feedback -- comments, questions, and bug reports. Please
[submit a GitHub issue](https://github.com/jaiobs/Opti-Geofencing-Android/issues),
and we'll get back to you.
