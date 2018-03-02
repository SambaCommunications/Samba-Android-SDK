## Overview

This SDK is provided as an Android Library project that can be included in your application using Gradle or as a .aar library.

## Requirements

Requires Android 4.4 (Android api level above 19) and up. In case you try to use Samba SDK on a device with Android OS lower than 4.4, Samba instance will be null.

## Setup

Add the following to build.gradle file from the integrating module:

```
dependencies {
    implementation 'com.squareup.retrofit2:retrofit:2.3.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.3.0'  
    implementation 'com.squareup.okhttp3.okhttp:3.9.0' 
    implementation 'com.squareup.picasso:picasso:2.5.2'
 }
```
# SDK Usage

## Initialize Samba sdk

In order to properly initialize Samba sdk, you have to call init method either in the Application class or in the first activity. 

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

Note: If you call the ```init``` method with an incorrect **publisherId**, **secretKey** or **userId** ,Samba SDK will initialize, but when calling ```loadAd()``` , **onAdLoadFail()** event will probably be fired with error message **No content**.

Every time a new publisher, secret or user id is used, init method must be called in order to retain the new setup.

## Samba config 

The SDK offers access to **SambaConfig** class in order to change various SDK settings. This method can be called before or after calling ```init``` method. The settings will be applied before loading and playing ads.

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
Following configurations are availble:

|Method	| Default value |                 	Description                                     |
|---------|---------------|------------------------------------------------------------------|
|setAge   |	      0     	 |Sets the age used to retrieve Samba content. (optional parameter) |
|setGender|	null	|Sets the gender user to retrieve Samba content. (optional parameter)|
|setAdOrientation|	null	|Sets the desired orientation when playing the ad. In case the value is null or MATCH_VIDEO, the orientation will be the one preferred by the video. |
|setSoundDisabled |	false |	Sets if the ad's video starts with disabled sound. If set to true value can be changed when playing the video by pressing the icon in the above left corner |
|optimizeDownloadOnMobileNetwork|	false |	Sets if mobile network should be used for downloading ad videos.|


## Each activity

Your are very close to play the first Ad. For this get Samba instance by calling method ```getInstance``` in Samba class and passing the activity context.

After this you must call ```loadAd()``` method and after getting succesful load answer, ```playAd``` method. You can listen to the result of load method by registering **SambaEventListener** on Samba instance and listen for **AdLoadedSuccess** and **AdLoadedFailed** events.

Once **AdLoadedSuccess** has been returned you can call ```playAd()``` method. Additional methods for returning loading status are **isAdLoaded** and **isAdLoading** called using Samba instance.

```
import com.samba.Samba;

public class FirstActivity extends AppCompatActivity {
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
If you would like to ckeck the state of the ad the following methods are available.

samba.isAdLoading()

samba.isAdReady()

samba.isAdShowing()

## OnDestroy
In order to cleanup the ad's resources, samba's destroyAd method must be called when the activity is destroied:

```
@Override
protected void onDestroy() {
    samba.destroyAd();
    super.onDestroy();
}
```

## AdListener events

Above is an example of creating a **SambaEventListener** object and registering it to the Samba Ad object. Folowing events are triggered:

|**Event**|**Mandatory implementation**|**Description**|
|---------|----------------------------|---------------|
|onAdLoadSuccess|	true	|Triggered after an ad is loaded successfuly|
|onAdLoadFail(String error)|	true	|Triggered after an ad is not loaded. The cause error message is returned.|
|onAdStarted |false|	Triggered when ad video starts playing.|
|onAdShowFailed(String error)|	false	|Triggered when ad fails to display on screen.The cause error message is returned.|
|onAdDidReachEnd|	false	|Triggered when ad video reaches end.|
|onAdClicked|	false|	Triggered when the user clicks the ad video screen.
|onLeaveApp|	false	|Triggered when the app is about to go to the background initiated by the sdk.e.g. if the user opens a web page in native browser from SDk or click in a "mailTo" link|

## NOTE

Callbacks may be executed on a background thread so in order to interact with the UI you have to move the processing on the main thread. This can be done like this:

create a Handler object and passing Looper.getMainLooper as parameter

using Activity.runOnUiThread(Runnable) 

 
