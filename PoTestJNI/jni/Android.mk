LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := PoTestJNI
LOCAL_SRC_FILES := com_ooieuioo_potestjni_PoJNITtest.c
include $(BUILD_SHARED_LIBRARY)