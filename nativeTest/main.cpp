#include <main.h>

#include <iostream>

TCMP_DEF_WND

int x, y;

void tc::app::OnStart(char* path, int argc, char* argv[]) {
    application->SetName("Native Test");
    application->SetX(100);
    application->SetY(100);
    application->SetWidth(200);
    application->SetHeight(200);
    std::cout << "Hello world from native application located at " << path << "! [" << application->GetName() << ']' << std::endl;
}

bool tc::app::OnClose() {
    return true;
}

void tc::app::Update() {

}

void tc::app::Render(awt::Graphics2D g) {
    g.SetColor(awt::Color::Black());
    g.FillRect(0, 0, application->GetWidth(), application->GetHeight());
    g.SetColor(awt::Color::Red());
    g.FillRect(0, 0, x, y);
}

void tc::app::ProcessInput(int _x, int _y, tc::InteractType type) {
    x = _x;
    y = _y;
    application->RenderCanvas();
}