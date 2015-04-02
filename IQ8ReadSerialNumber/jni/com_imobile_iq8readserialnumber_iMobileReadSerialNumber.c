#include "com_imobile_iq8readserialnumber_iMobileReadSerialNumber.h"

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <android/log.h>
// file : myboolean.h
#ifndef MYBOOLEAN_H
#define MYBOOLEAN_H

#define false 0
#define true 1
typedef int bool; // or #define bool int

#endif

#define LOG_TAG "Po_JNI"
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, fmt, ##args)

#ifdef __cplusplus
extern "C"
{
#endif

int system(const char *command);

JNIEXPORT jstring JNICALL Java_com_imobile_iq8readserialnumber_iMobileReadSerialNumber_HelloWorld(
		JNIEnv *env, jclass arg) {
	jstring str = (*env)->NewStringUTF(env, "HelloWorld from JNI !");

	//Po log start
	LOGI("[Po add] HelloWorld()");
	return str;
}

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

JNIEXPORT jstring JNICALL Java_com_imobile_iq8readserialnumber_iMobileReadSerialNumber_ReadSN(
		JNIEnv *env, jclass arg, jstring Path) {

	char* origin_path = Jstring2CStr(env, Path);
	char* p = "";

#define CHUNK 1024 // read 1024 bytes at a time
	char buf[CHUNK];
	FILE *file;
	size_t nread;

	file = fopen("/data/Po_prop.txt", "r");
	if (file) {
		while ((nread = fread(buf, 1, sizeof buf, file)) > 0)
			fwrite(buf, 1, nread, stdout);

		if (ferror(file)) {
			// deal with error
		}
		fclose(file);
	}

	LOGI("[Po add] ReadProc() %s", buf);


	LOGI("[Po add] ReadSN() 111");
	jstring str = (*env)->NewStringUTF(env, "ReadSN from JNI !");

	//Po log start
	LOGI("[Po add] ReadSN() 222");
	return str;
}

#ifdef __cplusplus
}
#endif
