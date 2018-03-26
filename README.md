# Samba SDK

## Overview

Our unique ad formats are loved by users and earn more money from advertisers, enhancing your app and earning you more at the same time. 

Simply integrate our SDK and get started today.

The SDK is provided as an Android Library project that can be included in your application as an .aar library. The latest aar file, versio **0.0.1**, can be found in folder **samba-sdk**.

In the near future we plan to distribute the SKD as a Gradle dependency too.

## Requirements

The Samba SDK requires Android 4.4 (Android API level 19+) and upwards. In the event you attempt to use the Samba SDK on a device with an Android OS lower than 4.4, the Samba instance will be null.

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

## Let's get to the code

In order to connect to our system you will need a **publisherId** and a **secretKey**. Please contact your SambaNetworks Account Manager or contact us at sales@sambanetworks.com in order to get these. 

To properly initialize the Samba SDK you need to create an instance of the **SambaSetup** object and provide the publisherId, the secretKey and a userId. After this you need to call the ```init()``` method and pass the SambaSetup instance and the activity context.


```
import com.samba.SambaAd;

public class AdActivity extends AppCompatActivity {

  @Override
 protected void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   setContentView(R.layout.activity_ad);

   SambaSetup sambaSetup = new SambaSetup("YourPublisherId", "YourSecretKey", "UserId");
   Samba.init(sambaSetup, AdActivity.this);
  }
}
```

Note: If you create the **SambaSetup** object with an incorrect **publisherId**, **secretKey** or **userId**, the Samba SDK will initialize, but when calling ```loadAd()```, the **onAdLoadFail()** event will probably be fired with the error message **No content**.

In case the userId changes, the init method must be called again, in order to ensure the correct setup.

### Samba configuration 

The SDK offers access to the **SambaConfig** class in order to change various SDK settings. **SambaConfig** methods can be called before or after calling the ```init``` method. The settings will be applied before loading and playing new ads.

```
import com.samba.SambaConfig;
public class AdActivity extends AppCompatActivity {

 @Override
 protected void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   setContentView(R.layout.activity_main);
   
   SambaSetupe sambaSetup = new SambaSetup("YourPublisherId", "YourSecretKey", "UserId");
   Samba.init(sambaSetup, AdActivity.this);

   SambaConfig.useMobileNetworkForCaching(false);
   SambaConfig.setPrecachingAllowed(false);
   SambaConfig.setSoundDisabled(false);
  }
}
```
The following configurations are available:

|Method	| Default value |                 	Description                                     |
|---------|---------------|------------------------------------------------------------------|
| setAge | null | Sets the user age. It is used to retrieve Samba ads (optional parameter). |
| setGender |	null | Sets the user gender. It is used to retrieve Samba ads.  Available options are: 'M' or 'F' (optional parameter). |
| setAdOrientation |	null | Sets the desired orientation when playing an ad. If the value is null or MATCH_VIDEO, the orientation will be determined by the video itself. Available options are: LANDSCAPE, PORTRAIT, AUTO or MATCH_VIDEO. |
| setSoundDisabled |	false | Enables or disables ads sound (sound on or sound off). The default option for sound is enabled. |
| optimizeDownloadOnMobileNetwork | false | Sets whether the sdk should optimize mobile data usage. More exactl, it sets if mobile data should be used for pre-caching ad videos or not. If set to true, ad videos will not be pre-cached on mobile data. |


### Sample ad activity

You are almost ready to play the first ad.
Get the Samba instance by calling method ```getInstance``` on Samba class by passing the activity context.
```
 samba = SambaAd.getInstance(SampleAdActivity.this);
 ```
 
After this you must call the ```loadAd()``` method and after receiving a successful load response, call ```playAd()``` method. You can listen to loadAd method result by registering a **SambaEventListener** on Samba instance and listening for **AdLoadedSuccess** and **AdLoadedFailed** events.

Once **AdLoadedSuccess** has been called, you can call the ```playAd()``` method.

```
import com.samba.Samba;

public class SampleAdActivity extends AppCompatActivity {
private Samba samba;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   setContentView(R.layout.activity_main);
  
   samba = SambaAd.getInstance(SampleAdActivity.this);
   samba.setSambaAdListener(createSambaListener());
   samba.loadAd();
 }

private SambaListener createSambaListener() {
    return new SambaListener() {
        @Override
        public void onAdLoadSuccess() {
             //Unnecesary check method. Used as an example here.
             if (samba.isAdLoaded()) {
                 samba.showAd();
             }
        }

        @Override
        public void onAdLoadFail(@NonNull String error) {
          
        }
    };
}}
```
If you would like to check the status of the ad, the following methods are available:

samba.isAdLoading()

samba.isAdLoaded()

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
| onAdLoadSuccess | true | Triggered when an ad is loaded successfully. |
| onAdLoadFail(String error) | true | Triggered when an ad can't be loaded. The cause is returned in the error message. |
| onAdStarted | false | Triggered when an ad starts playing. |
| onAdShowFailed(String error) | false | Triggered when an ad fails to display on screen. The cause is returned in the error message. |
| onAdDidReachEnd | false | Triggered when an ad reaches completion. |
| onAdClicked | false | Triggered when the user clicks the video ad screen. |
| onLeaveApp | false | Triggered when leaving the app. (e.g. if the user opens a web page in the native browser from the SDK or clicks a "mailTo" link). |

### NOTE

Callbacks may be executed in a background thread so in order to interact with the UI you need to move the processing to the main thread. This can be done so:

create a Handler object and pass Looper.getMainLooper as a parameter

using Activity.runOnUiThread(Runnable) 

## You're all set
Now you're ready to show high quality and engaging ads to your users. Welcome aboard!
