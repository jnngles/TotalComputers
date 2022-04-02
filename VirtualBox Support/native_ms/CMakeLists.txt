cmake_minimum_required(VERSION 3.1)

project(vbox_native)

include(FindJNI)

set(CMAKE_CXX_FLAGS "-Wall -std=c++17 ${CMAKE_CXX_FLAGS}")

set(INCLUDES ${INCLUDES} ${PROJECT_SOURCE_DIR}/include ${JNI_INCLUDE_DIRS})

file(GLOB SOURCE_FILES
    "${PROJECT_SOURCE_DIR}/*.h"
    "${PROJECT_SOURCE_DIR}/*.cpp"
    "${PROJECT_SOURCE_DIR}/*.c"
)

include_directories(${INCLUDES})
link_libraries(${JNI_LIBRARIES})
add_library(vbox_native SHARED ${SOURCE_FILES})
