cmake_minimum_required(VERSION 3.1)

project(tcmp_browser)

include(FindJNI)

set(CMAKE_CXX_FLAGS "-Wall -std=c++17 ${CMAKE_CXX_FLAGS}")

set(INCLUDES ${INCLUDES} ${PROJECT_SOURCE_DIR}/include ${JNI_INCLUDE_DIRS})

set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} /MT")
set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} /MTd")
set(VCPKG_C_FLAGS_DEBUG "/D_ITERATOR_DEBUG_LEVEL=0")
set(VCPKG_CXX_FLAGS_DEBUG "/D_ITERATOR_DEBUG_LEVEL=0")

file(GLOB SOURCE_FILES
    "${PROJECT_SOURCE_DIR}/*.hpp"
    "${PROJECT_SOURCE_DIR}/*.h"
    "${PROJECT_SOURCE_DIR}/*.cpp"
    "${PROJECT_SOURCE_DIR}/*.cc"
)

link_directories(${PROJECT_SOURCE_DIR}/lib)
link_libraries(${JNI_LIBRARIES} libcef libcef_dll_wrapper)
include_directories(${INCLUDES})
add_library(application SHARED ${SOURCE_FILES})