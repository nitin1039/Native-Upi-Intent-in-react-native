package com.javacodeinreactnative;

import android.content.Intent;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint;
import com.facebook.react.defaults.DefaultReactActivityDelegate;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class MainActivity extends ReactActivity {

  static final int UPI_PAYMENT_REQUEST_CODE = 1;

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
    return "JavaCodeInReactNative";
  }

  /**
   * Returns the instance of the {@link ReactActivityDelegate}. Here we use a util class {@link
   * DefaultReactActivityDelegate} which allows you to easily enable Fabric and Concurrent React
   * (aka React 18) with two boolean flags.
   */
  @Override
  protected ReactActivityDelegate createReactActivityDelegate() {
    return new DefaultReactActivityDelegate(
        this,
        getMainComponentName(),
        // If you opted-in for the New Architecture, we enable the Fabric Renderer.
        DefaultNewArchitectureEntryPoint.getFabricEnabled());
  }


  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == UPI_PAYMENT_REQUEST_CODE) {
      WritableMap params = Arguments.createMap();


      if (data == null) {
        // Payment was canceled
        params.putString("status", "cancelled");
        params.putString("message", "Payment was cancelled by the user");
      } else {
        String response = data.getStringExtra("response");
        // Check the response to determine payment status
        if (response != null && response.toLowerCase().contains("success")) {
          params.putString("status", "success");
        } else {
          params.putString("status", "failure");
          params.putString("message", "Payment failed");
        }
      }


      getReactInstanceManager().getCurrentReactContext()
              .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
              .emit("onUpiPaymentComplete", params);
    }
  }
}
