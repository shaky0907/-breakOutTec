cmake_minimum_required(VERSION 3.22)
project(C C)

set(CMAKE_C_STANDARD 99)



add_executable(C Server.c Server.h JsonHandle.h Constantes.h Socket.h SocketServidor.h)
target_link_libraries(C json-c)
