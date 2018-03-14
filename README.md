## Overview

This SDK is provided as an Android Library project that can be included in your application using Gradle or as a .aar library.

## Requirements

Requires Android 4.4 (Android API level 19+) and upwards. In the event you attempt to use the Samba SDK on a device with an Android OS lower than 4.4,the Samba instance will be null.

## Let's get started

Please add the following to the build.gradle file from the integrating module:

```
dependencies {
    implementation 'com.squareup.retrofit2:retrofit:2.3.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.3.0'  
    implementation 'com.squareup.okhttp3.okhttp:3.9.0' 
    implementation 'com.squareup.picasso:picasso:2.5.2'
 }
```

## Let's code

In order to connect to our system you will need a publisherId and a secretKey. Please contact your SambaNetworks Account Manager or contact us at sales@sambanetworks.com in order to receive these. 

To properly initialize the Samba SDK, you need to call the init method either in the Application class or in the first activity. 


```
import android.app.Application;
import com.samba.SambaAd;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Samba.init(publisherId, secretKey, userId);
    }
}
```

Note: If you call the ```init``` method with an incorrect **publisherId**, **secretKey** or **userId**, the Samba SDK will initialize, but when calling ```loadAd()```, the **onAdLoadFail()** event will probably be fired with the error message **No content**.

Each time a new publisherId, secretKey or userId is used, the init method must be called in order to ensure the correct setup.

### Samba configuration 

The SDK offers access to the **SambaConfig** class in order to change various SDK settings. This method can be called before or after calling the ```init``` method. The settings will be applied before loading and playing ads.
The values for **publisherId**, **secretKey** and **userId** will be provided by Samba.

```
import com.samba.SambaConfig;
public class MainActivity extends AppCompatActivity {

 @Override
 protected void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   setContentView(R.layout.activity_main);

   SambaConfig.useMobileNetworkForCaching(false);
   SambaConfig.setPrecachingAllowed(false);
   SambaConfig.setSoundDisabled(false);
  }
}
```
The following configurations are available:

|Method	| Default value |                 	Description                                     |
|---------|---------------|------------------------------------------------------------------|
|setAge   |	      null    	 |Sets the age used to retrieve Samba ads. (optional parameter).|
|setGender|	null	|Sets the gender used to retrieve Samba ads.  Available options are: 'M' or 'F'.(optional parameter).|
|setAdOrientation|	null	|Sets the desired orientation when playing the ad. If the value is null or MATCH_VIDEO, the orientation will be determined by the video itself. Available options are: LANDSCAPE, PORTRAIT, AUTO or MATCH_VIDEO.|
|setSoundDisabled |	false | Sets the sound value (sound on or sound off). The default option for sound is enabled.|
|optimizeDownloadOnMobileNetwork|	false |	Sets whether the mobile network should be used for downloading the video ads.|


### Sample ad activity

You are almost ready to play the first ad.
Get the Samba instance by calling method ```getInstance``` in Samba class and passing the activity context.
```
 mSamba=SambaAd.getInstance(FirstActivity.this);
 ```
 
After this you must call the ```loadAd()``` method and after receiving successful load response, call the ```playAd()``` method. You can listen to the result of the load method by registering the **SambaEventListener** on the Samba instance and listening for **AdLoadedSuccess** and **AdLoadedFailed** events.

Once **AdLoadedSuccess** has been returned, you can call the ```playAd()``` method. Additional methods for returning loading status are **isAdLoaded** and **isAdLoading** which are called using the Samba instance.

```
import com.samba.Samba;

public class SampleAdActivity extends AppCompatActivity {
private Samba mSamba;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   setContentView(R.layout.activity_main);
  
   mSamba=SambaAd.getInstance(FirstActivity.this);
   mSamba.setSambaAdListener(createSambaListener());
   mSamba.loadAd();
 }

private SambaListener createSambaListener() {
    return new SambaListener() {
        @Override
        public void onAdLoadSuccess() {
             //Unnecesary check method. Used as an example here.
             if(mSamba.isAdLoaded())
                {
                mSamba.showAd();
                }
        }

        @Override
        public void onAdLoadFail(@NonNull String error) {
          
        }
    };
}}
```
If you would like to check the status of the ad the following methods are available.

samba.isAdLoading()

samba.isAdReady()

samba.isAdShowing()

### OnDestroy
In order to clean-up the ad's resources, Samba's ```destroyAd()``` method must be called when the activity is destroyed:

```
@Override
protected void onDestroy() {
    samba.destroyAd();
    super.onDestroy();
}
```

### AdListener events

Above is an example of creating a **SambaEventListener** object and registering it to the SambaAd object. The following events are triggered:

|**Event**|**Mandatory implementation**|**Description**|
|---------|----------------------------|---------------|
|onAdLoadSuccess|	true	|Triggered when an ad is loaded successfully|
|onAdLoadFail(String error)|	true	|Triggered when an ad is not loaded. The cause error message is returned.|
|onAdStarted |false|	Triggered when an ad starts playing.|
|onAdShowFailed(String error)|	false	|Triggered when and ad fails to display on screen. The cause error message is returned.|
|onAdDidReachEnd|	false	|Triggered when an ad reaches completion.|
|onAdClicked|	false|	Triggered when the user clicks the video ad screen.
|onLeaveApp|	false	|Triggered when leaving the app. (e.g. if the user opens a web page in the native browser from the SDK or clicks a "mailTo" link)|

### NOTE

Callbacks may be executed in a background thread so in order to interact with the UI you need to move the processing to the main thread. This can be done so:

create a Handler object and pass Looper.getMainLooper as a parameter

using Activity.runOnUiThread(Runnable) 

## You're all set
Now you're ready to show high quality and engaging ads to your users! Welcome aboard!
