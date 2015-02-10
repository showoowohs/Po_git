#include "com_ooieueioo_pojnireadwriteproc_PoJNIT.h"

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

JNIEXPORT jstring JNICALL Java_com_ooieueioo_pojnireadwriteproc_PoJNIT_ReadProc(JNIEnv *env, jclass arg,  jstring str)
{
	LOGI("[Po add] TransportStringToC() oooo");
	char* p = Jstring2CStr(env,str);


#define CHUNK 1024 /* read 1024 bytes at a time */
	char buf[CHUNK];
	FILE *file;
	size_t nread;

	file = fopen("/proc/Po_value", "r");
	if (file) {
		while ((nread = fread(buf, 1, sizeof buf, file)) > 0)
			fwrite(buf, 1, nread, stdout);

		if (ferror(file)) {
			/* deal with error */
		}
		fclose(file);
	}

	LOGI("[Po add] TransportStringToC() %s", buf);
	char *test = strtok(buf, "\n");
//	while (test != NULL) {
//		LOGI("[Po add] TransportStringToC()  test=%s", test);

//	}


	if(strcmp(test, "1") == 0){
		p = test;
	}else if(strcmp(test, "0") == 0){
		p = test;
	}else{
		p = "xx";
	}

	//LOGI("[Po add] TransportStringToC() %s", p);
	//Po log start
	//LOGI("[Po add] TransportStringToC() %s", buf);
	//Po log end

	return (*env)->NewStringUTF(env, p);
}

#ifdef __cplusplus
}
#endif
