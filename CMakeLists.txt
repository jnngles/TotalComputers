cmake_minimum_required(VERSION 3.1)

project(total_computers)

set(JAVA_HOME C:/Users/JNNGL/.jdks/temurin-1.8.0_302)
set(JAVA_INCLUDES ${JAVA_HOME}/include ${JAVA_HOME}/include/win32)

set(CMAKE_CXX_FLAGS "-Wall -std=c++17 ${CMAKE_CXX_FLAGS}")

set(INCLUDES ${INCLUDES} ${PROJECT_SOURCE_DIR}/include ${JAVA_INCLUDES})

file(GLOB SOURCE_FILES
    "${PROJECT_SOURCE_DIR}/include/*.h"
    "${PROJECT_SOURCE_DIR}/src/*.cpp"
)

set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} /MT")
set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} /MTd")
set(VCPKG_C_FLAGS_DEBUG "/D_ITERATOR_DEBUG_LEVEL=0")
set(VCPKG_CXX_FLAGS_DEBUG "/D_ITERATOR_DEBUG_LEVEL=0")

include_directories(${INCLUDES})
add_library(total_computers_dll SHARED ${SOURCE_FILES})
add_library(total_computers_lib ${SOURCE_FILES})