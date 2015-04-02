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

JNIEXPORT jstring JNICALL Java_com_imobile_iq8readserialnumber_iMobileReadSerialNumber_ReadSN(
		JNIEnv *env, jclass arg) {


	// ls -al | grep '^d'
	FILE *pp;
	pp = popen("/system/IQ8_Read_SN.sh", "r");
	if (pp != NULL) {
		while (1) {
			char *line;
			char buf[1000];
			line = fgets(buf, sizeof buf, pp);
			if (line == NULL)
				break;
			if (line[0] == 'd')
				LOGI("[Po add]%s", line); // line includes '\n'
		}
		pclose(pp);
	}



	 //system("/system/IQ8_Read_SN.sh");




	LOGI("[Po add] ReadSN() 111");
	jstring str = (*env)->NewStringUTF(env, "ReadSN from JNI !");

	//Po log start
	LOGI("[Po add] ReadSN() 222");
	return str;
}

#ifdef __cplusplus
}
#endif
