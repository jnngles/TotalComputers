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

#include "j_env.h"

#include "total_computers.h"
using namespace tc;

NativeObject clsApplication;
NativeObject clsWindowApplication;

Application::Application(jobject instance)
    : NativeObjectInstance(instance)
{
    tcInitNativeObject(clsApplication, "com/jnngl/totalcomputers/system/desktop/Application", {
        { "getIcon", "()Ljava/awt/image/BufferedImage;", false },
        { "getName", "()Ljava/lang/String;", false },
        { "setIcon", "(Ljava/awt/image/BufferedImage;)V", false },
        { "setName", "(Ljava/lang/String;)V", false },
    }, {});
}

Application::~Application() {}

awt::image::BufferedImage Application::GetIcon() {
    return awt::image::BufferedImage(jEnv->CallObjectMethod(METHOD(clsApplication, 0)));
}

const char* Application::GetName() {
    return FromJString((jstring)jEnv->CallObjectMethod(METHOD(clsApplication, 1)));
}

void Application::SetIcon(awt::image::BufferedImage icon) {
    jEnv->CallVoidMethod(METHOD(clsApplication, 2), icon.instance);
}

void Application::SetName(const char* name) {
    jEnv->CallVoidMethod(METHOD(clsApplication, 3), jEnv->NewStringUTF(name));
}




WindowApplication::WindowApplication(jobject instance)
    : instance(instance), Application(instance)
{
    tcInitNativeObject(clsWindowApplication, "com/jnngl/system/NativeWindowApplication", {
        { "move", "(II)V", false },
        { "resize", "(II)V", false },
        { "maximize", "(I)V", false },
        { "unmaximize", "()V", false },
        { "minimize", "()V", false },
        { "unminimize", "()V", false },
        { "isMinimized", "()Z", false },
        { "renderCanvas", "()V", false },
        { "getCanvas", "()Ljava/awt/image/BufferedImage;", false },
        { "getX", "()I", false },
        { "setX", "(I)V", false },
        { "getY", "()I", false },
        { "setY", "(I)V", false },
        { "getWidth", "()I", false },
        { "setWidth", "(I)V", false },
        { "getHeight", "()I", false },
        { "setHeight", "(I)V", false },
        { "isMaximized", "()Z", false },
        { "getMinWidth", "()I", false },
        { "setMinWidth", "(I)V", false },
        { "getMinHeight", "()I", false },
        { "setMinHeight", "(I)V", false },
        { "getMaxWidth", "()I", false },
        { "setMaxWidth", "(I)V", false },
        { "getMaxHeight", "()I", false },
        { "setMaxHeight", "(I)V", false },
        { "isResizable", "()Z", false },
        { "setResizable", "(Z)V", false },
    }, {});
}

void WindowApplication::Move(int x, int y) {
    jEnv->CallVoidMethod(METHOD(clsWindowApplication, 0), (jint)x, (jint)y);
}

void WindowApplication::Resize(int width, int height) {
    jEnv->CallVoidMethod(METHOD(clsWindowApplication, 1), (jint)width, (jint)height);
}

void WindowApplication::Maximize(int titleBarHeight) {
    jEnv->CallVoidMethod(METHOD(clsWindowApplication, 2), (jint)titleBarHeight);
}

void WindowApplication::Unmaximize() {
    jEnv->CallVoidMethod(METHOD(clsWindowApplication, 3));
}

void WindowApplication::Minimize() {
    jEnv->CallVoidMethod(METHOD(clsWindowApplication, 4));
}

void WindowApplication::Unminimize() {
    jEnv->CallVoidMethod(METHOD(clsWindowApplication, 5));
}

bool WindowApplication::IsMinimized() {
    return jEnv->CallBooleanMethod(METHOD(clsWindowApplication, 6));
}

void WindowApplication::RenderCanvas() {
    jEnv->CallVoidMethod(METHOD(clsWindowApplication, 7));
}

awt::image::BufferedImage WindowApplication::GetCanvas() {
    return awt::image::BufferedImage(jEnv->CallObjectMethod(METHOD(clsWindowApplication, 8)));
}

int WindowApplication::GetX() {
    return jEnv->CallIntMethod(METHOD(clsWindowApplication, 9));
}

void WindowApplication::SetX(int x) {
    jEnv->CallVoidMethod(METHOD(clsWindowApplication, 10), (jint)x);
}

int WindowApplication::GetY() {
    return jEnv->CallIntMethod(METHOD(clsWindowApplication, 11));
}

void WindowApplication::SetY(int y) {
    jEnv->CallVoidMethod(METHOD(clsWindowApplication, 12), (jint)y);
}

int WindowApplication::GetWidth() {
    return jEnv->CallIntMethod(METHOD(clsWindowApplication, 13));
}

void WindowApplication::SetWidth(int width) {
    jEnv->CallVoidMethod(METHOD(clsWindowApplication, 14), (jint)width);
}

int WindowApplication::GetHeight() {
    return jEnv->CallIntMethod(METHOD(clsWindowApplication, 15));
}

void WindowApplication::SetHeight(int height) {
    jEnv->CallVoidMethod(METHOD(clsWindowApplication, 16), (jint)height);
}

bool WindowApplication::IsMaximized() {
    return jEnv->CallBooleanMethod(METHOD(clsWindowApplication, 17));
}

int WindowApplication::GetMinWidth() {
    return jEnv->CallIntMethod(METHOD(clsWindowApplication, 18));
}

void WindowApplication::SetMinWidth(int minWidth) {
    jEnv->CallVoidMethod(METHOD(clsWindowApplication, 19), (jint)minWidth);
}

int WindowApplication::GetMinHeight() {
    return jEnv->CallIntMethod(METHOD(clsWindowApplication, 20));
}

void WindowApplication::SetMinHeight(int minHeight) {
    jEnv->CallVoidMethod(METHOD(clsWindowApplication, 21), (jint)minHeight);
}

int WindowApplication::GetMaxWidth() {
    return jEnv->CallIntMethod(METHOD(clsWindowApplication, 22));
}

void WindowApplication::SetMaxWidth(int maxWidth) {
    jEnv->CallVoidMethod(METHOD(clsWindowApplication, 23), (jint)maxWidth);
}

int WindowApplication::GetMaxHeigth() {
    return jEnv->CallIntMethod(METHOD(clsWindowApplication, 24));
}

void WindowApplication::SetMaxHeight(int maxHeight) {
    jEnv->CallVoidMethod(METHOD(clsWindowApplication, 25), (jint)maxHeight);
}

bool WindowApplication::IsResizable() {
    return jEnv->CallBooleanMethod(METHOD(clsWindowApplication, 26));
}

void WindowApplication::SetResizable(bool resizable) {
    jEnv->CallVoidMethod(METHOD(clsWindowApplication, 27), (jboolean)resizable);
}