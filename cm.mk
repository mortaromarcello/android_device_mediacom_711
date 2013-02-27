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

$(call inherit-product, device/mediacom/711/full_711.mk)
# Inherit some common CM stuff.
$(call inherit-product, vendor/cm/config/common_full_tablet_wifionly.mk)
$(call inherit-product, vendor/cm/config/gsm.mk)
$(call inherit-product, device/mediacom/711/711-blobs.mk)

TARGET_BOOTANIMATION_NAME := $(TARGET_SCREEN_WIDTH)

## Device identifier. This must come after all inclusions
PRODUCT_NAME := cm_711
PRODUCT_BRAND := Allwinner
PRODUCT_DEVICE := 711
PRODUCT_MODEL := MP711I
PRODUCT_MANUFACTURER := Allwinner
PRODUCT_RELEASE_NAME := 711

UTC_DATE := $(shell date +%s)
DATE     := $(shell date +%Y%m%d)

PRODUCT_BUILD_PROP_OVERRIDES += \
    PRODUCT_NAME=${PRODUCT_MODEL}_${PRODUCT_SFX} \
    TARGET_DEVICE=711 \
    BUILD_NUMBER=${DATE} \
    BUILD_VERSION_TAGS=release-keys \
    TARGET_BUILD_TYPE=eng
