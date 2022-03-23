#ifndef __BASE_HPP_
#define __BASE_HPP_

#include <include/cef_app.h>
#include <include/cef_client.h>
#include <include/cef_render_handler.h>

#include <iostream>

class Handler : public CefRenderHandler {
public:
    Handler() : rectWidth(0), rectHeight(0), width(0), height(0), data(nullptr) {}
    Handler(int width, int height) : rectWidth(width), rectHeight(height), width(width), height(height) {
        data = (int*)malloc(width*(size_t)height*4);
    }
    ~Handler() {
        free(data);
    }

public:
    virtual void GetViewRect(CefRefPtr<CefBrowser> browser, CefRect& rect) override {
        rect = CefRect(0, 0, rectWidth, rectHeight);
    }

    virtual void OnPaint(CefRefPtr<CefBrowser> browser,
                         PaintElementType type,
                         const RectList& dirtyRects,
                         const void* buffer,
                         int width,
                         int height) override {
        if (queue) return;

        if (width != this->width || height != this->height) {
            free(data);
            this->width = width;
            this->height = height;
            resized = true;
            data = (int*)malloc((size_t)width*height*(size_t)4);
        }

        if(data && buffer)
            memcpy(data, buffer, (size_t)width*height*(size_t)4);
        
        queue = 1;
    }

public:
    void resize(int width, int height) {
        rectWidth = width;
        rectHeight = height;
    }

private:
    int rectWidth, rectHeight;

public:
    int width, height;
    int* data;
    bool queue = 0;
    bool resized = false;

public:
    IMPLEMENT_REFCOUNTING(Handler);

};

class BrowserClient : public CefClient {
public:
    BrowserClient(Handler* handler)
        : m_handler(handler)
    {}

    CefRefPtr<CefRenderHandler> GetRenderHandler() override {
        return m_handler;
    }

public:
    CefRefPtr<CefRenderHandler> m_handler;

public:
    IMPLEMENT_REFCOUNTING(BrowserClient);

};

#endif