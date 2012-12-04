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
TARGET_BOOTLOADER_BOARD_NAME := crane
TARGET_BOARD_PLATFORM := exDroid

USE_CAMERA_STUB := true
HAVE_HTC_AUDIO_DRIVER := true
BOARD_USES_GENERIC_AUDIO := true
BOARD_USES_GPS_TYPE := simulator
BOARD_USES_HDMI := true

TARGET_NO_BOOTLOADER := true
TARGET_NO_RADIOIMAGE := true

#CPU stuff
TARGET_CPU_ABI := armeabi-v7a
TARGET_CPU_ABI2 := armeabi
TARGET_ARCH_VARIANT := armv7-a-neon
TARGET_GLOBAL_CFLAGS += -mtune=cortex-a8 -mfpu=neon -mfloat-abi=softfp
TARGET_GLOBAL_CPPFLAGS += -mtune=cortex-a8 -mfpu=neon -mfloat-abi=softfp
ARCH_ARM_HAVE_TLS_REGISTER := true
TARGET_CPU_SMP := true

TARGET_CUSTOM_RELEASETOOL := ./device/mediacom/711/releasetools/squisher

BOARD_HAVE_BLUETOOTH := true
BOARD_HAS_VIBRATOR_IMPLEMENTATION := ../../device/mediacom/711/vibrator.c
BOARD_HAS_SDCARD_INTERNAL := true
TARGET_USE_CUSTOM_LUN_FILE_PATH = "/sys/class/android_usb/android0/f_mass_storage/lun%d/file"
TARGET_USE_CUSTOM_SECOND_LUN_NUM := 1
BOARD_VOLD_MAX_PARTITIONS := 20

TARGET_USERIMAGES_USE_EXT4 := true
BOARD_BOOTIMAGE_PARTITION_SIZE := 33554432
BOARD_RECOVERYIMAGE_PARTITION_SIZE := 33554432
#BOARD_SYSTEMIMAGE_PARTITION_SIZE := 268435456
BOARD_SYSTEMIMAGE_PARTITION_SIZE := 301989888
BOARD_USERDATAIMAGE_PARTITION_SIZE := 1073741824
BOARD_FLASH_BLOCK_SIZE := 4096

#EGL stuff
BOARD_EGL_CFG := device/mediacom/711/egl.cfg
USE_OPENGL_RENDERER := true
BOARD_USE_SKIA_LCDTEXT := true
COMMON_GLOBAL_CFLAGS += -DSURFACEFLINGER_FORCE_SCREEN_RELEASE
TARGET_BOOTANIMATION_PRELOAD := true
TARGET_BOOTANIMATION_TEXTURE_CACHE := true
TARGET_BOOTANIMATION_USE_RGB565 := true
TARGET_SCREEN_WIDTH := 800
TARGET_SCREEN_EIGHT := 480

# audio & camera & cedarx
CEDARX_CHIP_VERSION := F23
CEDARX_USE_SWAUDIO := N
CAMERA_USES_SURFACEFLINGER_CLIENT_STUB := true

# use our own su for root
BOARD_USES_ROOT_SU := true

#Recovery Stuff
#TARGET_RECOVERY_UI_LIB := librecovery_ui_generic
#TARGET_RECOVERY_UPDATER_LIBS += librecovery_updater_generic
BOARD_CUSTOM_RECOVERY_KEYMAPPING := ../../device/mediacom/711/recovery_keys.c
BOARD_USE_LEGACY_TOUCHSCREEN := true
BOARD_HAS_NO_SELECT_BUTTON := true
TARGET_HARDWARE_INCLUDE := $(ANDROID_BUILD_TOP)/device/mediacom/711/include
TARGET_RECOVERY_PRE_COMMAND := "echo -n boot-recovery | busybox dd of=/dev/block/nandf count=1 conv=sync; sync"
TARGET_RECOVERY_FSTAB:=device/mediacom/711/recovery.fstab
TARGET_RECOVERY_INITRC := device/mediacom/711/ramdisk/recovery_init.rc

# Wifi stuff
BOARD_WIFI_VENDOR                := realtek
WPA_SUPPLICANT_VERSION           := VER_0_8_X
BOARD_WPA_SUPPLICANT_DRIVER      := WEXT
BOARD_WPA_SUPPLICANT_PRIVATE_LIB := lib_driver_cmd_rtl
BOARD_HOSTAPD_DRIVER             := WEXT
BOARD_HOSTAPD_PRIVATE_LIB        := lib_driver_cmd_rtl
BOARD_WLAN_DEVICE                := rtl8192cu
WIFI_DRIVER_MODULE_NAME          := 8192cu
WIFI_DRIVER_MODULE_PATH          := "/system/lib/modules/8192cu.ko"
TARGET_CUSTOM_WIFI := ../../hardware/realtek/wlan/wifi_realtek.c

COMMON_GLOBAL_CFLAGS += -DICS_CAMERA_BLOB #temporary

ENABLE_WEBGL := true
# Extra : to build external modules sources
#TARGET_KERNEL_SOURCE := $(ANDROID_BUILD_TOP)/kernel/
#TARGET_KERNEL_CONFIG := 711i_defconfig
#TARGET_KERNEL_MODULES_EXT := $(ANDROID_BUILD_TOP)/device/mediacom/711/prebuilt/lib/modules
BOARD_KERNEL_CMDLINE := console=ttyS0,115200 rw init=/init loglevel=8
BOARD_KERNEL_BASE := 0x40000000
BOARD_KERNEL_PAGESIZE := 2048

# Beware: set only prebuilt OR source+config
TARGET_PREBUILT_KERNEL := $(ANDROID_BUILD_TOP)/device/mediacom/711/kernel
SW_BOARD_USES_GSENSOR_TYPE := mxc622x
SW_BOARD_GSENSOR_DIRECT_X := true
SW_BOARD_GSENSOR_DIRECT_Y := true
SW_BOARD_GSENSOR_DIRECT_Z := false
SW_BOARD_GSENSOR_XY_REVERT := true
