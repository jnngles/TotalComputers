#ifndef _TCMP_MAIN_H_
#define _TCMP_MAIN_H_

#include "total_computers.h"

namespace tc {

namespace app {

void OnStart(long long uid, char* path, int argc, char* argv[]);
bool OnClose(long long uid);
void Update(long long uid);
void Render(long long uid, awt::Graphics2D g);
void ProcessInput(long long uid, int x, int y, tc::InteractType type);

extern WindowApplication* application;

}

}

#define TCMP_DEF_WND tc::WindowApplication* tc::app::application;

#endif