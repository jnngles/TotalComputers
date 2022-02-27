
#include "utils.h"

#include "j_env.h"

char* FromJString(jstring jstr) {
    const char* str = tc::jEnv->GetStringUTFChars(jstr, 0);
    char* copy = (char*)calloc(tc::jEnv->GetStringLength(jstr), sizeof(char));
    strcpy(copy, str);
    tc::jEnv->ReleaseStringUTFChars(jstr, str);
    return copy;
}

int* FromJIntArray(int* size, jintArray jarr) {
    size[0] = tc::jEnv->GetArrayLength(jarr);
    return (int*)tc::jEnv->GetIntArrayElements(jarr, 0);
}

float* FromJFloatArray(int* size, jfloatArray jarr) {
    size[0] = tc::jEnv->GetArrayLength(jarr);
    return tc::jEnv->GetFloatArrayElements(jarr, 0);
}

double* FromJDoubleArray(int* size, jdoubleArray jarr) {
    size[0] = tc::jEnv->GetArrayLength(jarr);
    return tc::jEnv->GetDoubleArrayElements(jarr, 0);
}

char* FromJCharArray(int* size, jcharArray jarr) {
    size[0] = tc::jEnv->GetArrayLength(jarr);
    return (char*)tc::jEnv->GetCharArrayElements(jarr, 0);
}

signed char* FromJByteArray(int* size, jbyteArray jarr) {
    size[0] = tc::jEnv->GetArrayLength(jarr);
    return tc::jEnv->GetByteArrayElements(jarr, 0);
}

short* FromJShortArray(int* size, jshortArray jarr) {
    size[0] = tc::jEnv->GetArrayLength(jarr);
    return tc::jEnv->GetShortArrayElements(jarr, 0);
}

long long* FromJLongArray(int* size, jlongArray jarr) {
    size[0] = tc::jEnv->GetArrayLength(jarr);
    return tc::jEnv->GetLongArrayElements(jarr, 0);
}

bool* FromJBooleanArray(int* size, jbooleanArray jarr) {
    size[0] = tc::jEnv->GetArrayLength(jarr);
    return (bool*)tc::jEnv->GetBooleanArrayElements(jarr, 0);
}

char** FromJStringArray(int* size, jobjectArray jarr) {
    size[0] = tc::jEnv->GetArrayLength(jarr);
    char** arr = (char**)calloc(size[0], sizeof(char*));
    for(int i = 0; i < size[0]; i++) {
        jstring str = (jstring) tc::jEnv->GetObjectArrayElement(jarr, i);
        const char* cStr = tc::jEnv->GetStringUTFChars(str, 0);
        strcpy(arr[i], cStr);
        tc::jEnv->ReleaseStringUTFChars(str, cStr);
    }
    return arr;
}

jstring ToJString(const char* str) {
    return tc::jEnv->NewStringUTF(str);
}

jintArray ToJIntArray(int* arr, int arrc) {
    jintArray jarr = tc::jEnv->NewIntArray(arrc);
    tc::jEnv->SetIntArrayRegion(jarr, 0, arrc, (jint*)arr);
    return jarr;
}

jfloatArray ToJFloatArray(float* arr, int arrc) {
    jfloatArray jarr = tc::jEnv->NewFloatArray(arrc);
    tc::jEnv->SetFloatArrayRegion(jarr, 0, arrc, arr);
    return jarr;
}

jdoubleArray ToJDoubleArray(double* arr, int arrc) {
    jdoubleArray jarr = tc::jEnv->NewDoubleArray(arrc);
    tc::jEnv->SetDoubleArrayRegion(jarr, 0, arrc, arr);
    return jarr;
}

jcharArray ToJCharArray(const char* arr, int arrc) {
    jcharArray jarr = tc::jEnv->NewCharArray(arrc);
    tc::jEnv->SetCharArrayRegion(jarr, 0, arrc, (jchar*)arr);
    return jarr;
}

jbyteArray ToJByteArray(signed char* arr, int arrc) {
    jbyteArray jarr = tc::jEnv->NewByteArray(arrc);
    tc::jEnv->SetByteArrayRegion(jarr, 0, arrc, arr);
    return jarr;
}

jshortArray ToJShortArray(short* arr, int arrc) {
    jshortArray jarr = tc::jEnv->NewShortArray(arrc);
    tc::jEnv->SetShortArrayRegion(jarr, 0, arrc, arr);
    return jarr;
}

jlongArray ToJLongArray(long long* arr, int arrc) {
    jlongArray jarr = tc::jEnv->NewLongArray(arrc);
    tc::jEnv->SetLongArrayRegion(jarr, 0, arrc, arr);
    return jarr;
}

jbooleanArray ToJBooleanArray(bool* arr, int arrc) {
    jbooleanArray jarr = tc::jEnv->NewBooleanArray(arrc);
    tc::jEnv->SetBooleanArrayRegion(jarr, 0, arrc, (jboolean*)arr);
    return jarr;
}

jobjectArray ToJStringArray(const char** arr, int arrc) {
    jobjectArray jarr = tc::jEnv->NewObjectArray(arrc, tc::jEnv->FindClass("java/lang/String"), 0);
    for(int i = 0; i < arrc; i++) {
        tc::jEnv->SetObjectArrayElement(jarr, i, tc::jEnv->NewStringUTF(arr[i]));
    }
    return jarr;
}