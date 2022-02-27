#ifndef TC_TOTAL_COMPUTERS_H
#define TC_TOTAL_COMPUTERS_H

#include "awt.h"
#include "object.h"
#include "utils.h"

namespace tc {

enum InteractType {
    LEFT_CLICK = 0,
    RIGHT_CLICK = 1
};

class Application : public NativeObjectInstance {
public:
    Application(jobject instance);
    ~Application();

public:
    awt::image::BufferedImage GetIcon();
    const char* GetName();

public:
    void SetIcon(awt::image::BufferedImage icon);
    void SetName(const char* name);

};

class WindowApplication : public Application {
public:
    WindowApplication(jobject instance);
    ~WindowApplication();

public:
    struct ResizeEvent {
        void(*OnResize)(int width, int height);
        void(*OnMaximize)(int width, int height);
        void(*OnUnmaximize)(int width, int height);
    };

    struct MoveEvent {
        void(*OnMove)(int x, int y);
    };

    struct MinimizeEvent {
        void(*OnMinimize)();
        void(*OnUnminimize)();
    };

public:
    void Move(int x, int y);
    void Resize(int width, int height);
    void Maximize(int titleBarHeight);
    void Unmaximize();
    void Minimize();
    void Unminimize();
    bool IsMinimized();

    void RenderCanvas();

    awt::image::BufferedImage GetCanvas();
    int GetX();
    void SetX(int x);
    int GetY();
    void SetY(int y);
    int GetWidth();
    void SetWidth(int width);
    int GetHeight();
    void SetHeight(int height);
    
    bool IsMaximized();

    void AddResizeEvent(ResizeEvent event);
    void RemoveResizeEvent(ResizeEvent event);
    void AddMoveEvent(MoveEvent event);
    void RemoveMoveEvent(MoveEvent event);
    void AddMinimizeEvent(MinimizeEvent event);
    void RemoveMinimizeEvent(MinimizeEvent event);

    int GetMinWidth();
    void SetMinWidth(int minWidth);
    int GetMinHeight();
    void SetMinHeight(int minHeight);

    int GetMaxWidth();
    void SetMaxWidth(int maxWidth);
    int GetMaxHeigth();
    void SetMaxHeight(int maxHeight);

    bool IsResizable();
    void SetResizable(bool resizable);

public:
    jobject instance;

};

}

#endif