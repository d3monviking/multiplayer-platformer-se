# Multiplayer Fiasco

**Multiplayer Fiasco** is a multiplayer 2D platformer game designed for up to four players. The client-side is developed in C++ using the **Simple and Fast Multimedia Library (SFML)** and **Boost.Asio** for networking, while the server-side is implemented in Java, and its native packages.

---

## Features

- Multiplayer support for up to 4 players.
- Smooth and fast gameplay using SFML.
- Simple client-server architecture with Boost.Asio and Java.

---

## Prerequisites

### Server
- **Java Development Kit (JDK)** installed on the server system.

### Client
- **SFML** installed on the client system.
- **C++ Compiler** supporting C++17 or later.

---

## Installation and Setup

### Clone the Repository
```bash 
git clone https://github.com/d3monviking/multiplayer-fiasco.git
```

### Server Setup
1. Navigate to the server directory:
```bash
cd multiplayer-fiasco/server
```
2. Compile the server code:
```bash
javac Server.java
```
3. Run the server:
```bash
java Server
```
### Client Setup
#### Install SFML:
Download SFML from the official [website](https://www.sfml-dev.org/download/sfml/2.6.2/). Follow the installation instructions for your operating system.
#### Configure the client:
1. Navigate to the Client directory:
```bash
cd multiplayer-fiasco/Client
```
2. Open Main.cpp in a text editor. Modify the IP address in line 8 to match your server's IP:
```
udp::endpoint serverEndpoint(boost::asio::ip::make_address("your-server-ip"), 8888);
```
3. To find your server's IP, run the following command on the server system, and copy the IP next to inet and replace "your-server-ip" with the IP address in quotes.
```bash
ifconfig
``` 
4. Build the client:
```bash
make
```

### How to Play
1. Start the server:
```bash
java Server
```
2. Launch the client(s):
  Ensure the server is running before starting the clients. Use the following command to start the game on each client terminal:
```bash
./game
```
Once all players have joined the game, press Enter in the server terminal to start the game.

# Testing

## Prerequisites

Before starting, ensure you have the following installed:

### For Server (Java)

*   Java JDK 17+ (or JDK 21)
*   Maven

### For Client (C++)

*   C++ Compiler (GCC/G++ 11+ or Clang)
*   CMake (Version 3.10+)
*   SFML (Development libraries)
*   Boost (System libraries)
*   Google Test (Optional, the build script can fetch it automatically)

#### Ubuntu/Debian Setup Command:
```bash
sudo apt-get update
sudo apt-get install openjdk-17-jdk maven \
    build-essential cmake libsfml-dev libboost-system-dev \
    libgtest-dev google-mock
```

## Server Tests (with Maven)

The server-side tests use JUnit 5 and are managed by Maven.


**Instructions:**

1.  Navigate to the `server` directory:
    ```bash
    cd multiplayer-fiasco/server
    ```
2.  Run the desired test commands:

    *   **Run Unit Tests Only:**
        This runs all files ending in `Test.java` and skips integration tests.
        ```bash
        mvn test
        ```

    *   **Run Integration Tests Only:**
        This skips unit tests and runs only integration tests (files ending in `IT.java`).
        ```bash
        mvn verify -Dsurefire.skip=true
        ```

    *   **Run All Tests (Unit & Integration):**
        ```bash
        mvn verify
        ```
## Client Tests

The client-side tests use Google Test and are built with CMake.

**Instructions:**

1. First, build the executable files:
    *   **Navigate to the tests directory**
        ```bash
        cd multiplayer-fiasco/Client/tests
        ```

    *   **Create a build directory and navigate into it**
        ```bash
        mkdir -p build && cd build
        ```

    *   **Configure the project with CMake and build it**
        ```bash
        cmake ..
        make
        ```

2.  Now you can run the tests from within the `build` directory.

| Goal                  | Command             | Description                                        |
| --------------------- | ------------------- | -------------------------------------------------- |
| Run Unit Tests        | `./runUnitTests`    | Instant tests. Ideal for running while coding.     |
| Run Integration Tests | `./runIntegrationTests` | Slower tests for physics flow and network code. |