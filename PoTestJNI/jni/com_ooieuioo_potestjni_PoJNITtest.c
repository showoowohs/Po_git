#include "com_ooieuioo_potestjni_PoJNITtest.h"

#include <stdlib.h>
#include <stdio.h>
#ifdef __cplusplus
extern "C"
{
#endif

JNIEXPORT jstring JNICALL Java_com_ooieuioo_potestjni_PoJNITtest_HelloWorld(JNIEnv *env, jclass arg)
{
	jstring str = (*env)->NewStringUTF(env, "HelloWorld from JNI !");
	return str;
}

#ifdef __cplusplus
}
#endif
