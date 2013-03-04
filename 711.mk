#
# Copyright (C) 2011 The Android Open-Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

PRODUCT_CHARACTERISTICS := tablet

PRODUCT_TAGS += dalvik.gc.type-precise

DEVICE_PACKAGE_OVERLAYS := device/mediacom/711/overlay

## (3)  Finally, the least specific parts, i.e. the non-GSM-specific aspects
PRODUCT_PROPERTY_OVERRIDES += \
	ro.com.google.locationfeatures=1 \
	ro.media.dec.jpeg.memcap=20000000 \
	dalvik.vm.lockprof.threshold=500 \
	ro.kernel.android.checkjni=0 \
	dalvik.vm.checkjni=false \
	dalvik.vm.dexopt-data-only=1 \
	ro.vold.umsdirtyratio=20 \
	net.dns1=8.8.8.8 \
	net.dns2=8.8.4.4 

# Permissions
PRODUCT_COPY_FILES += \
	frameworks/native/data/etc/tablet_core_hardware.xml:system/etc/permissions/tablet_core_hardware.xml \
	frameworks/native/data/etc/android.hardware.camera.flash-autofocus.xml:system/etc/permissions/android.hardware.camera.flash-autofocus.xml \
	frameworks/native/data/etc/android.hardware.camera.front.xml:system/etc/permissions/android.hardware.camera.front.xml \
	frameworks/native/data/etc/android.hardware.camera.xml:system/etc/permissions/android.hardware.camera.xml \
	frameworks/native/data/etc/android.hardware.telephony.gsm.xml:system/etc/permissions/android.hardware.telephony.gsm.xml \
	frameworks/native/data/etc/android.hardware.location.gps.xml:system/etc/permissions/android.hardware.location.gps.xml \
	frameworks/native/data/etc/android.hardware.location.xml:system/etc/permissions/android.hardware.location.xml \
	frameworks/native/data/etc/android.hardware.wifi.xml:system/etc/permissions/android.hardware.wifi.xml \
	frameworks/native/data/etc/android.hardware.sensor.accelerometer.xml:system/etc/permissions/android.hardware.sensor.accelerometer.xml \
	frameworks/native/data/etc/android.hardware.sensor.proximity.xml:system/etc/permissions/android.hardware.sensor.proximity.xml \
	frameworks/native/data/etc/android.hardware.touchscreen.multitouch.jazzhand.xml:system/etc/permissions/android.hardware.touchscreen.multitouch.jazzhand.xml \
	frameworks/native/data/etc/android.hardware.usb.host.xml:system/etc/permissions/android.hardware.usb.host.xml \
	frameworks/native/data/etc/android.hardware.usb.accessory.xml:system/etc/permissions/android.hardware.usb.accessory.xml \
	frameworks/native/data/etc/android.software.sip.voip.xml:system/etc/permissions/android.software.sip.voip.xml \
	packages/wallpapers/LivePicker/android.software.live_wallpaper.xml:/system/etc/permissions/android.software.live_wallpaper.xml \

# Other stuff
PRODUCT_PACKAGES += librs_jni rild_sun4i power.sun4i

# EGL stuff
PRODUCT_PACKAGES += gralloc.exDroid hwcomposer.exDroid display.exDroid

# Sensors
#PRODUCT_PACKAGES += memsicd sensors.exDroid libaccelcal
# Camera
#PRODUCT_PACKAGES += camera.exDroid
#
PRODUCT_PACKAGES += lights.exDroid

#PRODUCT_PACKAGES += gps.exDroid

# CM apps
PRODUCT_PACKAGES += com.android.future.usb.accessory

# EXT4 Support
PRODUCT_PACKAGES += make_ext4fs e2fsck

# Audio stuff
PRODUCT_PACKAGES += audio.a2dp.default libaudioutils libtinyalsa audio_policy.sun4i audio.primary.exDroid audio.usb.default

# CedarX libraries
PRODUCT_PACKAGES += libCedarX
#PRODUCT_PACKAGES += libCedarA libCedarX libcedarv libcedarxbase libcedarxosal libswdrm libcedarxsftdemux
#PRODUCT_PACKAGES += libcedarv_adapter libve libfacedetection libaw_audio libaw_audioa libcedarv_base
#PRODUCT_PACKAGES += libstagefright_soft_cedar_h264dec librtmp
#PRODUCT_PACKAGES += libthirdpartstream libcedarxsftstream

# Apps
#PRODUCT_PACKAGES += FDroid
#PRODUCT_PACKAGES += GhostCommander


# copy all others kernel modules under the "modules" directory to system/lib/modules
PRODUCT_COPY_FILES += $(shell test -d device/mediacom/711/prebuilt/lib/modules && \
	find device/mediacom/711/prebuilt/lib/modules -name '*.ko' \
	-printf '%p:system/lib/modules/%f ')

$(call inherit-product, build/target/product/full_base.mk)

# Should be after the full_base include, which loads languages_full
PRODUCT_AAPT_CONFIG := large xlarge mdpi
PRODUCT_AAPT_PREF_CONFIG := mdpi

PRODUCT_NAME := full_711
PRODUCT_DEVICE := 711
