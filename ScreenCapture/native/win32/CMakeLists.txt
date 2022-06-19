cmake_minimum_required(VERSION 3.1)

project(ScreenCaptureWin32)

include(FindJNI)

file(GLOB SOURCE_FILES
    "${PROJECT_SOURCE_DIR}/*.h"
    "${PROJECT_SOURCE_DIR}/*.cpp"
    "${PROJECT_SOURCE_DIR}/*.c"
)

include_directories(${JNI_INCLUDE_DIRS})
link_libraries(${JNI_LIBRARIES})
add_library(ScreenCaptureWin32 SHARED ${SOURCE_FILES})