
#include <com_jnngl_system_NativeWindowApplication.h>
#include <memory>

#include <j_env.h>
#include <main.h>

void Java_com_jnngl_system_NativeWindowApplication__1Init(JNIEnv* env, jobject instance, jstring path, jobjectArray jargs) {
    tc::jEnv = env;
    tc::app::application = new tc::WindowApplication(instance);

    int length = env->GetArrayLength(jargs);
    char** args = (char**)calloc(length, sizeof(char*));

    for(int i = 0; i < length; i++) {
        args[i] = FromJString((jstring)(env->GetObjectArrayElement(jargs, i)));
    }

    tc::app::OnStart(FromJString(path), length, args);
}

jboolean Java_com_jnngl_system_NativeWindowApplication__1OnClose(JNIEnv*, jobject instance) {
    tc::app::application = new tc::WindowApplication(instance);
    return tc::app::OnClose();
}

void Java_com_jnngl_system_NativeWindowApplication__1Update(JNIEnv*, jobject instance) {
    tc::app::application = new tc::WindowApplication(instance);
    tc::app::Update();
}

void Java_com_jnngl_system_NativeWindowApplication__1Render(JNIEnv*, jobject instance, jobject graphics) {
    tc::app::application = new tc::WindowApplication(instance);
    tc::app::Render(awt::Graphics2D(graphics));
}

void Java_com_jnngl_system_NativeWindowApplication__1ProcessInput(JNIEnv*, jobject instance, jint x, jint y, jboolean type) {
    tc::app::application = new tc::WindowApplication(instance);
    tc::app::ProcessInput(x, y, (tc::InteractType)type);
}