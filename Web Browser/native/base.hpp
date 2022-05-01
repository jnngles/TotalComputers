#ifndef __BASE_HPP
#define __BASE_HPP

#include <include/cef_app.h>
#include <include/cef_client.h>
#include <include/cef_render_handler.h>

class Handler : public CefRenderHandler, CefDisplayHandler {
public:
	Handler() : rectWidth(0), rectHeight(0), width(0), height(0), data(nullptr) {}
	Handler(int width, int height) : rectWidth(width), rectHeight(height), width(width), height(height) {
		data = (int*)malloc(width * (size_t)height * 4);
	}

	~Handler() {
		free(data);
	}

public:
    virtual void OnTitleChange(CefRefPtr<CefBrowser> browser,
        const CefString& title) override {
        this->title = title;
    }

    virtual void GetViewRect(CefRefPtr<CefBrowser> browser, CefRect& rect) override {
        rect = CefRect(0, 0, rectWidth, rectHeight);
    }

    virtual void OnPaint(CefRefPtr<CefBrowser> browser,
        PaintElementType type,
        const RectList& dirtyRects,
        const void* buffer,
        int width,
        int height) override {

        if (width != this->width || height != this->height) {
            free(data);
            this->width = width;
            this->height = height;
            resized = true;
            data = (int*)malloc((size_t)width * height * (size_t)4);
        }

        if (data && buffer)
            memcpy(data, buffer, (size_t)width * height * (size_t)4);

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
	bool resized = false;
    std::string title;

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

class CEF_noGPU : public CefApp {
public:
    virtual void OnBeforeCommandLineProcessing(
        const CefString& process_type,
        CefRefPtr<CefCommandLine> command_line) override {
        command_line->AppendSwitch("no-sandbox");
        command_line->AppendSwitch("off-screen-rendering-enabled");
        command_line->AppendSwitch("headless");
    }

public:
    IMPLEMENT_REFCOUNTING(CEF_noGPU);

};

#endif