/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_imobile_mt8382tca6416port0_imobileJNI */

#ifndef _Included_com_imobile_mt8382readkeyevent_imobileJNI
#define _Included_com_imobile_mt8382readkeyevent_imobileJNI
#ifdef __cplusplus
extern "C" {
#endif

/**
 * Class: com_imobile_mt8382readkeyevent_imobileJNI_ReadProc
 * Method: ReadProc, Java send String to C, then C return string
 * Signature: ()Ljava/lang/String
 */
JNIEXPORT jstring JNICALL Java_com_imobile_mt8382readkeyevent_imobileJNI_ReadProc(JNIEnv *, jclass, jstring);

/**
 * Class: com_imobile_mt8382readkeyevent_imobileJNI_WriteProc
 * Method: WriteProc, Java send String to C, then C return string, can write parameter to /proc/tca6416
 * Signature: ()Ljava/lang/String
 */
JNIEXPORT jstring JNICALL Java_com_imobile_mt8382readkeyevent_imobileJNI_WriteProc(JNIEnv *, jclass, jstring, jstring);

#ifdef __cplusplus
}
#endif
#endif