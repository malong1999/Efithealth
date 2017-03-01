// IHardwareService.aidl
package com.maxiaobu.healthclub;

// Declare any non-default types here with import statements

interface IHardwareService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
             boolean getFlashlightEnabled();
                void setFlashlightEnabled(boolean on);
}
