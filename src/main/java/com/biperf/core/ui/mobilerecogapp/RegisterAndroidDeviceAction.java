
package com.biperf.core.ui.mobilerecogapp;

import com.biperf.core.mobileapp.recognition.domain.DeviceType;

public class RegisterAndroidDeviceAction extends BaseRegisterDeviceAction
{
  @Override
  protected DeviceType getDeviceType()
  {
    return DeviceType.ANDROID;
  }
}
