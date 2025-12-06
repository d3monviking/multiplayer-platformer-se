cmake_minimum_required(VERSION 3.10)
project(GameTests)

include(FetchContent)
FetchContent_Declare(
  googletest
  URL https://github.com/google/googletest/archive/refs/tags/v1.14.0.zip
)
set(gtest_force_shared_crt ON CACHE BOOL "" FORCE)
FetchContent_MakeAvailable(googletest)

include_directories(..)

find_package(SFML 2.5 COMPONENTS graphics window system network REQUIRED)
find_package(Boost REQUIRED COMPONENTS system)

set(SOURCE_FILES
    ../level.cpp
    ../player.cpp
    ../tiles.cpp
    TestGlobals.cpp 
)

add_executable(runTests
    PhysicsTest.cpp
    EntityTest.cpp
    MechanicsTest.cpp      
    ${SOURCE_FILES}
)

target_link_libraries(runTests
    gtest_main
    sfml-graphics sfml-window sfml-system sfml-network
    Boost::system
    pthread
)

add_custom_command(TARGET runTests POST_BUILD
    COMMAND ${CMAKE_COMMAND} -E copy_directory
    ${CMAKE_SOURCE_DIR}/../../Sprites  
    ${CMAKE_CURRENT_BINARY_DIR}/../Sprites 
)