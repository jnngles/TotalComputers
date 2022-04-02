#include <stdio.h>
#include "VirtualBox.h"
#include "com_jnngl_system_VBoxMS.h"
#include <vector>
#include <string>

#define SAFE_RELEASE(x) \
    if (x) { \
        x->Release(); \
        x = NULL; \
    }

class vb_data {
public:
	IVirtualBoxClient* client;
	IVirtualBox* instance;
};

class vm_session {
public:
	ISession* session;
	IConsole* console;
	IProgress* progress;
	IMachine* machine;
	IDisplay* display;
	BSTR sessionType;
	BSTR guid;
	BSTR machineName;
};

JNIEXPORT jobjectArray JNICALL Java_com_jnngl_system_VBoxMS_getMachineNames
(JNIEnv* env, jobject, jobject dataBuffer) {
	vb_data* data = reinterpret_cast<vb_data*>(env->GetDirectBufferAddress(dataBuffer));

	HRESULT rc;
	SAFEARRAY* machinesArray = NULL;
	std::vector<jstring> names;

	rc = data->instance->get_Machines(&machinesArray);
	if (FAILED(rc)) {
		printf("[impl.cpp](VBoxMS::getMachineNames) -> Failed to access machines [rc=0x%x]\n", rc);
		return nullptr;
	}

	IMachine** machines;
	rc = SafeArrayAccessData(machinesArray, (void**) & machines);
	if (FAILED(rc)) {
		printf("[impl.cpp](VBoxMS::getMachineNames) -> Failed to read safe array [rc=0x%x]\n", rc);
		return nullptr;
	}

	for (ULONG i = 0; i < machinesArray->rgsabound[0].cElements; ++i) {
		BSTR str;

		rc = machines[i]->get_Name(&str);
		if (FAILED(rc)) {
			printf("Failed to get machine(%u) name [rc=0x%x]\n", i, rc);
			return nullptr;
		}

		std::wstring wstr(str);
		char* utf = new char[wstr.length()+1];
		memset(utf, 0, wstr.length() + 1);
		wcstombs(utf, str, wstr.length());
		SysFreeString(str);
		names.push_back(env->NewStringUTF(utf));
	}

	jobjectArray arr = env->NewObjectArray(names.size(), env->FindClass("java/lang/String"), 0);
	for (int i = 0; i < names.size(); i++) {
		env->SetObjectArrayElement(arr, i, names[i]);
	}

	return arr;
}

JNIEXPORT jobject JNICALL Java_com_jnngl_system_VBoxMS_init
(JNIEnv* env, jobject) {
	CoInitialize(NULL);

	IVirtualBoxClient* vbClient;
	HRESULT rc = CoCreateInstance(CLSID_VirtualBoxClient,
		NULL,
		CLSCTX_INPROC_SERVER,
		IID_IVirtualBoxClient,
		(void**)&vbClient);

	if (FAILED(rc)) {
		printf("[impl.cpp](VBoxMS::init) -> Failed to create virtual box client [rc=0x%x]\n", rc);
		return nullptr;
	}

	IVirtualBox* vb;
	rc = vbClient->get_VirtualBox(&vb);
	if (FAILED(rc)) {
		printf("[impl.cpp](VBoxMS::init) -> Failed to access virtual box instance [rc=0x%x]\n", rc);
		return nullptr;
	}

	vb_data* data = new vb_data();
	data->client = vbClient;
	data->instance = vb;

	return env->NewDirectByteBuffer(data, sizeof(vb_data*));
}

const wchar_t* GetWC(const char* c)
{
	const size_t cSize = strlen(c) + 1;
	wchar_t* wc = new wchar_t[cSize];
	mbstowcs(wc, c, cSize);

	return wc;
}

JNIEXPORT jobject JNICALL Java_com_jnngl_system_VBoxMS_launchVM
(JNIEnv* env, jobject, jobject dataBuffer, jstring name) {
	vb_data* data = reinterpret_cast<vb_data*>(env->GetDirectBufferAddress(dataBuffer));

	HRESULT rc;

	IMachine* machine = NULL;
	const char* nameUTF = env->GetStringUTFChars(name, 0);
	const wchar_t* Wname = GetWC(nameUTF);
	BSTR machineName = SysAllocString(Wname);
	delete Wname;
	env->ReleaseStringUTFChars(name, nameUTF);

	rc = data->instance->FindMachine(machineName, &machine);

	if (FAILED(rc)) {
		printf("[impl.cpp](VBoxMS::launchVM) -> Failed to find machine [rc=0x%x]\n", rc);
		return nullptr;
	}

	ISession* session = NULL;
	IConsole* console = NULL;
	IProgress* progress = NULL;
	IDisplay* display = NULL;
	BSTR sessionType = SysAllocString(L"gui");
	BSTR guid;

	rc = machine->get_Id(&guid);
	if (FAILED(rc)) {
		printf("[impl.cpp](VBoxMS::launchVM) -> Failed to recieve machine ID [rc=0x%x]\n", rc);
		return nullptr;
	}

	rc = CoCreateInstance(CLSID_Session,
		NULL,
		CLSCTX_INPROC_SERVER,
		IID_ISession,
		(void**)&session);
	if (FAILED(rc)) {
		printf("[impl.cpp](VBoxMS::launchVM) -> Failed to create new session [rc=0x%x]\n", rc);
		return nullptr;
	}

	rc = machine->LaunchVMProcess(session, sessionType,
		NULL, &progress);
	if (FAILED(rc)) {
		printf("[impl.cpp](VBoxMS::launchVM) -> Failed to open remote session [rc=0x%x]\n", rc);
		return nullptr;
	}

	rc = progress->WaitForCompletion(-1);

	machine->LockMachine(session, LockType::LockType_Shared);

	session->get_Console(&console);
	console->get_Display(&display);

	vm_session* s_data = new vm_session();
	s_data->console = console;
	s_data->display = display;
	s_data->guid = guid;
	s_data->machineName = machineName;
	s_data->progress = progress;
	s_data->session = session;
	s_data->sessionType = sessionType;
	s_data->machine = machine;

	return env->NewDirectByteBuffer(s_data, sizeof(vm_session*));
}

JNIEXPORT void JNICALL Java_com_jnngl_system_VBoxMS_closeVM
(JNIEnv* env, jobject, jobject vbBuffer, jobject vmBuffer) {
	vb_data* vb = reinterpret_cast<vb_data*>(env->GetDirectBufferAddress(vbBuffer));
	vm_session* vm = reinterpret_cast<vm_session*>(env->GetDirectBufferAddress(vmBuffer));

	HRESULT rc;
	rc = vm->console->PowerDown(&vm->progress);
	rc = vm->progress->WaitForCompletion(-1);
	rc = vm->session->UnlockMachine();

	SAFE_RELEASE(vm->console);
	SAFE_RELEASE(vm->progress);
	SAFE_RELEASE(vm->session);
	SysFreeString(vm->guid);
	SysFreeString(vm->sessionType);
	SAFE_RELEASE(vm->machine);
	SysFreeString(vm->machineName);

	vb->instance->Release();
	vb->client->Release();

	CoUninitialize();
}

JNIEXPORT jbyteArray JNICALL Java_com_jnngl_system_VBoxMS_getScreen
(JNIEnv* env, jobject, jobject vbBuffer, jobject vmBuffer, jintArray oWidth, jintArray oHeight) {
	do {
		vm_session* vm = reinterpret_cast<vm_session*>(env->GetDirectBufferAddress(vmBuffer));
		if (!vm || !vm->display || !vm->console) break;
		MachineState state;
		vm->console->get_State(&state);
		if (state != MachineState_Running) break;

		ULONG width, height, bpp;
		LONG x, y;
		GuestMonitorStatus status;
		vm->display->GetScreenResolution(0, &width, &height, &bpp, &x, &y, &status);

		if (width == 0 || height == 0 || bpp == 0) break;

		jbyte* pixels = NULL;
		SAFEARRAY* pixArr;
		vm->display->TakeScreenShotToArray(0, width, height, BitmapFormat_RGBA, &pixArr);
		SafeArrayAccessData(pixArr, (void**)&pixels);

		env->SetIntArrayRegion(oWidth, 0, 1, new jint[]{ (jint)width });
		env->SetIntArrayRegion(oHeight, 0, 1, new jint[]{ (jint)height });

		jsize size = width * height * 4;
		jbyteArray jpixels = env->NewByteArray(size);
		env->SetByteArrayRegion(jpixels, 0, size, pixels);

		return jpixels;
	} while (0);

	return NULL;

}