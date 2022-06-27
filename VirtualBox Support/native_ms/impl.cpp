#include <stdio.h>
#include "VirtualBox.h"
#include "com_jnngl_system_VBoxMS.h"
#include <vector>
#include <string>
#include <atlsafe.h>

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
	IMouse* mouse;
	IKeyboard* keyboard;
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
			printf("[impl.cpp](VBoxMS::getMachineNames) -> Failed to get machine(%u) name [rc=0x%x]\n", i, rc);
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

const wchar_t* GetWC(const char* c) {
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
	IMouse* mouse = NULL;
	IKeyboard* keyboard = NULL;
	BSTR sessionType = SysAllocString(L"headless");
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
	console->get_Mouse(&mouse);
	console->get_Keyboard(&keyboard);

	vm_session* s_data = new vm_session();
	s_data->console = console;
	s_data->display = display;
	s_data->guid = guid;
	s_data->machineName = machineName;
	s_data->progress = progress;
	s_data->session = session;
	s_data->sessionType = sessionType;
	s_data->machine = machine;
	s_data->mouse = mouse;
	s_data->keyboard = keyboard;

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
	SAFE_RELEASE(vm->keyboard);
	SAFE_RELEASE(vm->mouse);
	SysFreeString(vm->guid);
	SysFreeString(vm->sessionType);
	SAFE_RELEASE(vm->machine);
	SysFreeString(vm->machineName);

	vb->instance->Release();
	vb->client->Release();

	CoUninitialize();
}

JNIEXPORT void JNICALL Java_com_jnngl_system_VBoxMS_getScreen
(JNIEnv* env, jobject, jobject vbBuffer, jobject vmBuffer, jintArray oWidth, jintArray oHeight, jobject img) {
	if (!env || !vbBuffer || !vmBuffer || !oWidth || !oHeight || !img) return;
	void* raw = env->GetDirectBufferAddress(vmBuffer);
	if (!raw) return;
	vm_session* vm = reinterpret_cast<vm_session*>(raw);
	if (!vm || !vm->display || !vm->console) return;
	MachineState state;
	vm->console->get_State(&state);
	if (state != MachineState_Running) return;

	ULONG width, height, bpp;
	LONG x, y;
	GuestMonitorStatus status;
	 vm->display->GetScreenResolution(0, &width, &height, &bpp, &x, &y, &status);

	 if (width == 0 || height == 0 || bpp == 0) return;

	 jint* wp = env->GetIntArrayElements(oWidth, 0);
	 jint* hp = env->GetIntArrayElements(oHeight, 0);
	 if (wp[0] != width || hp[0] != height) {
			env->ReleaseIntArrayElements(oWidth, wp, 0);
			env->ReleaseIntArrayElements(oHeight, hp, 0);
			env->SetIntArrayRegion(oWidth, 0, 1, new jint[]{ (jint)width });
			env->SetIntArrayRegion(oHeight, 0, 1, new jint[]{ (jint)height });
			return;
	}
	 env->ReleaseIntArrayElements(oWidth, wp, 0);
	 env->ReleaseIntArrayElements(oHeight, hp, 0);

	 SAFEARRAY* pixArr = NULL;
	 vm->display->TakeScreenShotToArray(0, width, height, BitmapFormat_RGBA, &pixArr);
	 if (!pixArr) return;
	 jsize length = (jsize)width * (jsize)height;
	 if (length <= 0) return;

	 std::vector<jint> pixels;
	 for (int y = 0; y < height; y++) {
		for (int x = 0; x < width; x++) {
				union {
				struct {
					unsigned char b,g,r,a;
				};
				jint value;
			} dst;
			LONG idx = (y * width + x)*4;
			if (idx < 0 || !pixArr || idx >= length * 4) continue;
			SafeArrayGetElement(pixArr, &idx, &dst.r);
			idx = (y * width + x) * 4 + 1;
			if (idx < 0 || !pixArr || idx >= length * 4) continue;
			SafeArrayGetElement(pixArr, &idx, &dst.g);
			idx = (y * width + x) * 4 + 2;
			if (idx < 0 || !pixArr || idx >= length * 4) continue;
			SafeArrayGetElement(pixArr, &idx, &dst.b);
			idx = (y * width + x) * 4 + 3;
			if (idx < 0 || !pixArr || idx >= length * 4) continue;
			SafeArrayGetElement(pixArr, &idx, &dst.a);
			pixels.push_back(dst.value);
		}
	}

	 if (pixels.size() == length) {
		 jmethodID setRGB = env->GetMethodID(env->GetObjectClass(img), "setRGB", "(IIII[III)V");
		  jintArray jpix = env->NewIntArray(length);
		  env->SetIntArrayRegion(jpix, 0, length, pixels.data());
		  env->CallVoidMethod(img, setRGB, 0, 0, width, height, jpix, 0, width);
	}
	
	 if (pixArr) {
		 SafeArrayDestroy(pixArr);
		 pixArr = NULL;
	}
}

JNIEXPORT void JNICALL Java_com_jnngl_system_VBoxMS_click
(JNIEnv* env, jobject, jobject vmBuffer, jint x, jint y, jboolean isLeft) {
	vm_session* vm = reinterpret_cast<vm_session*>(env->GetDirectBufferAddress(vmBuffer));

	vm->mouse->PutMouseEventAbsolute(x + 1, y + 1, 0, 0, isLeft ? 0b001 : 0b010);
	vm->mouse->PutMouseEventAbsolute(x + 1, y + 1, 0, 0, 0b000);
}

JNIEXPORT void JNICALL Java_com_jnngl_system_VBoxMS_key
(JNIEnv* env, jobject, jobject vmBuffer, jintArray scancodes) {
	vm_session* vm = reinterpret_cast<vm_session*>(env->GetDirectBufferAddress(vmBuffer));

	int length = env->GetArrayLength(scancodes);

	ULONG stored;
	jint* arr = env->GetIntArrayElements(scancodes, 0);

	{
		CComSafeArray<long> sarr(length);

		for (int i = 0; i < length; i++) {
			sarr[i] = arr[i];
		}


		vm->keyboard->PutScancodes((LPSAFEARRAY)sarr, &stored);
	}

	env->ReleaseIntArrayElements(scancodes, arr, 0);
}