cmake_minimum_required(VERSION 3.10)
project(GameTests)

include(FetchContent)
FetchContent_Declare(
  googletest
  URL https://github.com/google/googletest/archive/refs/tags/v1.14.0.zip
  DOWNLOAD_EXTRACT_TIMESTAMP TRUE 
)
set(gtest_force_shared_crt ON CACHE BOOL "" FORCE)
FetchContent_MakeAvailable(googletest)

include_directories(..) 

find_package(SFML 2.5 COMPONENTS graphics window system network REQUIRED)
find_package(Boost REQUIRED COMPONENTS system)

set(GAME_SOURCES
    ../level.cpp
    ../player.cpp
    ../tiles.cpp
    TestGlobals.cpp
)

add_executable(runUnitTests
    Unit/PhysicsTest.cpp
    Unit/EntityTest.cpp
    Unit/MechanicsTest.cpp
    ${GAME_SOURCES}
)

target_link_libraries(runUnitTests
    gtest_main
    sfml-graphics sfml-window sfml-system sfml-network
    Boost::system
    pthread
)

add_executable(runIntegrationTests
    Integration/ClientIntegrationTest.cpp
    ${GAME_SOURCES}
)

target_link_libraries(runIntegrationTests
    gtest_main
    sfml-graphics sfml-window sfml-system sfml-network
    Boost::system
    pthread
)
