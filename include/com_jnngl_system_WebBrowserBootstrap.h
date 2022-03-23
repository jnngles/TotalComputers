/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_jnngl_system_WebBrowserBootstrap */

#ifndef _Included_com_jnngl_system_WebBrowserBootstrap
#define _Included_com_jnngl_system_WebBrowserBootstrap
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_jnngl_system_WebBrowserBootstrap
 * Method:    InitCEF
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_jnngl_system_WebBrowserBootstrap_InitCEF
  (JNIEnv *, jclass, jstring);

/*
 * Class:     com_jnngl_system_WebBrowserBootstrap
 * Method:    CreateBrowser
 * Signature: (II)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_com_jnngl_system_WebBrowserBootstrap_CreateBrowser
  (JNIEnv *, jclass, jint, jint);

/*
 * Class:     com_jnngl_system_WebBrowserBootstrap
 * Method:    Update
 * Signature: (Ljava/nio/ByteBuffer;II)V
 */
JNIEXPORT void JNICALL Java_com_jnngl_system_WebBrowserBootstrap_Update
  (JNIEnv *, jclass, jobject, jint, jint);

/*
 * Class:     com_jnngl_system_WebBrowserBootstrap
 * Method:    GetPixels
 * Signature: (Ljava/nio/ByteBuffer;)[I
 */
JNIEXPORT jintArray JNICALL Java_com_jnngl_system_WebBrowserBootstrap_GetPixels
  (JNIEnv *, jclass, jobject);

/*
 * Class:     com_jnngl_system_WebBrowserBootstrap
 * Method:    ProcessTouch
 * Signature: (Ljava/nio/ByteBuffer;IIZ)V
 */
JNIEXPORT void JNICALL Java_com_jnngl_system_WebBrowserBootstrap_ProcessTouch
  (JNIEnv *, jclass, jobject, jint, jint, jboolean);

/*
 * Class:     com_jnngl_system_WebBrowserBootstrap
 * Method:    RunLoop
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_jnngl_system_WebBrowserBootstrap_RunLoop
  (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif
#endif
