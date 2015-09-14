#include "com_ooieueioo_potestjni_PoJNITtest.h"

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

JNIEXPORT jstring JNICALL Java_com_ooieueioo_potestjni_PoJNITtest_HelloWorld(JNIEnv *env, jclass arg)
{
	jstring str = (*env)->NewStringUTF(env, "HelloWorld from JNI !");

	//Po log start
	LOGI("[Po add] HelloWorld()");
	char newline[256];
	//FILE *fd = popen("/system/bin/sh /system/ethtool/ee9wj eth0", "r");
	FILE *fd = popen("cp /mnt/sdcard/123 /system/456", "r");
	while((fgets(newline, 256, fd)) != NULL) {
	//printf("We've got a newline %s", newline);
		LOGI("[Po add] %s", newline);
	}
	pclose(fd);
	return str;
}

JNIEXPORT jstring JNICALL Java_com_ooieueioo_potestjni_PoJNITtest_TransportStringToC(JNIEnv *env, jclass arg,  jstring str)
{
	char* p = Jstring2CStr(env,str);
	char* Po_str = " from PoJNI!!";
	char* Po_marge = strcat(p,Po_str);

	//Po log start
	//LOGI("[Po add] TransportStringToC() %s", Po_marge);
	//Po log end

	return (*env)->NewStringUTF(env, Po_marge);
}

JNIEXPORT jint JNICALL Java_com_ooieueioo_potestjni_PoJNITtest_TransportIntToC(JNIEnv *env, jclass arg,  jint Po_Java_number)
{
	int int_from_Java = Po_Java_number;
	int Po_marge_int = Po_Java_number + 1;

	//Po log start
	//LOGI("[Po add] TransportIntToC() %d", Po_marge_int);
	//Po log end
	return Po_marge_int;
}

JNIEXPORT jintArray JNICALL Java_com_ooieueioo_potestjni_PoJNITtest_TransportIntArrayToC(JNIEnv *env, jclass arg,  jintArray Po_Java_IntArray)
{
	// 1.Get Po_Java_IntArray size
	int len = (*env)->GetArrayLength(env, Po_Java_IntArray);

	// if Po_Java_IntArray size = 0 break
	if(len==0){
		return Po_Java_IntArray;
	}

	// get Po_Java_IntArray address
	jint* p = (*env)-> GetIntArrayElements(env, Po_Java_IntArray, 0);
	int i=0;
	for(i = 0; i < len; i++){
		*(p+i) += 5; //取出的每個元素加五
	}

	//Po log start
	//LOGI("[Po add] TransportIntArrayToC()");
	//Po log end

	return Po_Java_IntArray;
}

JNIEXPORT jboolean JNICALL Java_com_ooieueioo_potestjni_PoJNITtest_TransportBooleanToC(JNIEnv *env, jclass arg, jboolean Po_Java_boolean)
{
	bool boolean_from_Java = Po_Java_boolean;
	bool Po_JNI_boolean = !boolean_from_Java;

	//Po log start
	//LOGI("[Po add] TransportBooleanToC()");
	//Po log end

	return Po_JNI_boolean;
}

JNIEXPORT jstring JNICALL Java_com_ooieueioo_potestjni_PoJNITtest_ReadProc(JNIEnv *env, jclass arg,  jstring str)
{
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


/***** Po area
 *
*/
JNIEXPORT jstring JNICALL Java_com_ooieueioo_potestjni_PoJNITtest_ReadMacAddress(JNIEnv *env, jclass arg)
{

	char Po_str[1024];
	LOGI("[Po add] ReadMacAddress()");

	//** write**/
	//** echo ShowMac > /proc/eth0 **/
	/*
	FILE *ff = fopen("/proc/eth0", "w");
	if (ff == NULL)
	{
		LOGI("[Po add] WriteProc() error!");
	}

	/// print some text
	// echo S > /proc/eth0
	// 會造成 OS 當機 start
	const char *text = "ShowMac";
	fprintf(ff, "%s\n", text);
	// 會造成 OS 當機 End
	LOGI("[Po add] WriteProc() success!");
	fclose(ff);
	*/

	/** read **/
	/** cat /proc/eth0 **/
#define CHUNK 1024 /* read 1024 bytes at a time */

	char buf[CHUNK];
	FILE *file;
	size_t nread;

	file = fopen("/proc/eth0", "r");
	if (file) {
		while ((nread = fread(buf, 1, sizeof buf, file)) > 0)
			fwrite(buf, 1, nread, stdout);

		if (ferror(file)) {
			// deal with error
		}
		fclose(file);
	}else{
		strcpy(Po_str, "Not Read Mac");
		return (*env)->NewStringUTF(env, Po_str);;
	}

	LOGI("[Po add] ReadProc() %s", buf);

	/** read end **/

	// check format
	if(strstr(buf, "not") || strstr(buf, "thing")){
		LOGI("[Po add] return()");
		strcpy(Po_str, "Not Read Mac");
		return (*env)->NewStringUTF(env, Po_str);;
	}

	/** strsep start **/
	char* const delim = " ";
	char *token, *cur = buf;

	// 00 60 6e 10 00 f1 51 55 46 0a.....
	// get 5 number (result -> 00 60 6e 10 00 f1)
	int number = 0;
	while (token = strsep(&cur, delim)) {
		if(number++ < 6){
			LOGI("[Po add] %s\n", token);
			strcat(Po_str, token);
		}
	}
	/** strsep End **/


	LOGI("[Po add] Po_str = %s", Po_str);



	return (*env)->NewStringUTF(env, Po_str);;
}

#ifdef __cplusplus
}
#endif
