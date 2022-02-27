#ifndef _TCMP_MAIN_H_
#define _TCMP_MAIN_H_

#include "total_computers.h"

namespace tc {

namespace app {

void OnStart(char* path, int argc, char* argv[]);
bool OnClose();
void Update();
void Render(awt::Graphics2D g);
void ProcessInput(int x, int y, tc::InteractType type);

extern WindowApplication* application;

}

}

#define TCMP_DEF_WND tc::WindowApplication* tc::app::application;

#endif