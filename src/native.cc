/*
    Plugin for computers in vanilla minecraft!
    Copyright (C) 2022  JNNGL

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

#include <com_jnngl_system_NativeWindowApplication.h>
#include <memory>

#include <j_env.h>
#include <main.h>

void Java_com_jnngl_system_NativeWindowApplication__1Init(JNIEnv* env, jobject instance, jlong uid, jstring path, jobjectArray jargs) {
    tc::jEnv = env;
    tc::app::application = new tc::WindowApplication(instance);

    int length = env->GetArrayLength(jargs);
    char** args = (char**)calloc(length, sizeof(char*));

    for(int i = 0; i < length; i++) {
        args[i] = FromJString((jstring)(env->GetObjectArrayElement(jargs, i)));
    }

    tc::app::OnStart(uid, FromJString(path), length, args);
}

jboolean Java_com_jnngl_system_NativeWindowApplication__1OnClose(JNIEnv*, jobject instance, jlong uid) {
    tc::app::application = new tc::WindowApplication(instance);
    return tc::app::OnClose(uid);
}

void Java_com_jnngl_system_NativeWindowApplication__1Update(JNIEnv*, jobject instance, jlong uid) {
    tc::app::application = new tc::WindowApplication(instance);
    tc::app::Update(uid);
}

void Java_com_jnngl_system_NativeWindowApplication__1Render(JNIEnv*, jobject instance, jlong uid, jobject graphics) {
    tc::app::application = new tc::WindowApplication(instance);
    tc::app::Render(uid, awt::Graphics2D(graphics));
}

void Java_com_jnngl_system_NativeWindowApplication__1ProcessInput(JNIEnv*, jobject instance, jlong uid, jint x, jint y, jboolean type) {
    tc::app::application = new tc::WindowApplication(instance);
    tc::app::ProcessInput(uid, x, y, (tc::InteractType)type);
}