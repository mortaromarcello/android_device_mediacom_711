#
# Copyright (C) 2012 The Android Open-Source Project
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

USE_CAMERA_STUB := true
CAMERA_USES_SURFACEFLINGER_CLIENT_STUB := true
HAVE_HTC_AUDIO_DRIVER := true
BOARD_USES_GENERIC_AUDIO := true
BOARD_USES_GPS_TYPE := simulator
BOARD_USES_HDMI := true


TARGET_USE_CUSTOM_LUN_FILE_PATH = "/sys/class/android_usb/android0/f_mass_storage/lun%d/file"
TARGET_USE_CUSTOM_SECOND_LUN_NUM := 1
BOARD_VOLD_MAX_PARTITIONS := 20


#EGL stuff

BOARD_NO_ALLOW_DEQUEUE_CURRENT_BUFFER := true

TARGET_SCREEN_WIDTH := 800
TARGET_SCREEN_EIGHT := 480

BOARD_UMS_LUNFILE := "/sys/class/android_usb/android0/f_mass_storage/lun/file"
BOARD_UMS_2ND_LUNFILE := "/sys/class/android_usb/android0/f_mass_storage/lun1/file"

# use our own su for root
BOARD_USES_ROOT_SU := true

#Recovery Stuff
BOARD_CUSTOM_RECOVERY_KEYMAPPING := ../../device/mediacom/711/recovery_keys.c
BOARD_CUSTOM_RECOVERY_EVENTS := true

BOARD_HAS_NO_SELECT_BUTTON := true
TARGET_HARDWARE_INCLUDE := $(ANDROID_BUILD_TOP)/device/mediacom/711/include
#TARGET_RECOVERY_PRE_COMMAND := "echo -n boot-recovery | busybox dd of=/dev/block/nandf count=1 conv=sync; sync"
TARGET_RECOVERY_FSTAB:=device/mediacom/711/recovery.fstab
TARGET_RECOVERY_INITRC := device/mediacom/711/ramdisk/recovery_init.rc
TARGET_RECOVERY_PIXEL_FORMAT := BGRA_8888




# Extra : to build external modules sources
#TARGET_KERNEL_SOURCE := $(ANDROID_BUILD_TOP)/kernel/
#TARGET_KERNEL_CONFIG := 711i_defconfig
#TARGET_KERNEL_MODULES_EXT := $(ANDROID_BUILD_TOP)/device/mediacom/711/prebuilt/lib/modules


# Beware: set only prebuilt OR source+config
TARGET_PREBUILT_KERNEL := $(ANDROID_BUILD_TOP)/device/mediacom/711/kernel
SW_BOARD_USES_GSENSOR_TYPE := mxc622x
SW_BOARD_GSENSOR_DIRECT_X := true
SW_BOARD_GSENSOR_DIRECT_Y := true
SW_BOARD_GSENSOR_DIRECT_Z := false
SW_BOARD_GSENSOR_XY_REVERT := true
