#include "com_ooieuioo_potestjni_PoJNITtest.h"

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <android/log.h>


#define LOG_TAG "Po_JNI"

#ifdef __cplusplus
extern "C"
{
#endif

/*
 *jstring 2 char*
 */
char* Jstring2CStr(JNIEnv* env, jstring jstr) {
	char* rtn = NULL;
	jclass clsstring = (*env)->FindClass(env, "java/lang/String");
	jstring strencode = (*env)->NewStringUTF(env, "utf-8");
	jmethodID mid = (*env)->GetMethodID(env, clsstring, "getBytes",
			"(Ljava/lang/String;)[B");
	jbyteArray barr = (jbyteArray)(*env)->CallObjectMethod(env, jstr, mid,
			strencode);
	jsize alen = (*env)->GetArrayLength(env, barr);
	jbyte* ba = (*env)->GetByteArrayElements(env, barr, JNI_FALSE);
	if (alen > 0) {
		rtn = (char*) malloc(alen + 1);
		memcpy(rtn, ba, alen);
		rtn[alen] = 0;
	}
	(*env)->ReleaseByteArrayElements(env, barr, ba, 0);
	return rtn;
}

JNIEXPORT jstring JNICALL Java_com_ooieuioo_potestjni_PoJNITtest_HelloWorld(JNIEnv *env, jclass arg)
{
	jstring str = (*env)->NewStringUTF(env, "HelloWorld from JNI !");
	return str;
}

JNIEXPORT jstring JNICALL Java_com_ooieuioo_potestjni_PoJNITtest_TransportStringToC(JNIEnv *env, jclass arg,  jstring str)
{
	char* p = Jstring2CStr(env,str);

	char* Po_str = " from PoJNI!!";

	char* Po_marge = strcat(p,Po_str);

	return (*env)->NewStringUTF(env, Po_marge);
}

JNIEXPORT jint JNICALL Java_com_ooieuioo_potestjni_PoJNITtest_TransportIntToC(JNIEnv *env, jclass arg,  jint Po_Java_number)
{
	int int_from_Java = Po_Java_number;
	int Po_marge_int = Po_Java_number + 1;
	return Po_marge_int;
}

#ifdef __cplusplus
}
#endif
