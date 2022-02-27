SRC = $(wildcard src/*.cpp)
OBJ = $(patsubst src/%.cpp,out/%.o,$(SRC))
JDK_HOME = C:\Users\JNNGL\.jdks\temurin-1.8.0_302\include
OUTPUT = native.dll
COMPILE_ARGS = -std=c++17

all: $(OBJ) link

out/%.o: src/%.cpp
	g++ $(COMPILE_ARGS) -Iinclude -I$(JDK_HOME) -I$(JDK_HOME)/win32 -c $< -o $@

link:
	g++ -m64 -shared $(OBJ) -o $(OUTPUT)
	ar rcs total_computers.lib $(OBJ)
