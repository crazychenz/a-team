# Getting Started

## Java
### Get Apache Ant
If you don't already have Apache Ant, you can find a download for 
Apache Ant at <http://ant.apache.org/bindownload.cgi>.

On Mac OSX you should be able to run:

    brew install ant

Note: You may have to try the install a couple times and install XCode extras.

On linux, the usual package management methods should work,
whether its yum, dnf, or apt installations.

On Windows, there is the Apache manual at <http://ant.apache.org/manual/install.html#installing> and an unofficial tutorial at <https://www.mkyong.com/ant/how-to-install-apache-ant-on-windows/>

### Build With Apache Ant
From a console, goto the java directory and run the following:

    ant

If you want to clean the project files, run:

    ant clean

### Run The Code From A Jar
If you want to run the server or client from the jar:

    java -cp dist/clueless.jar ServerRun
    java -cp dist/clueless.jar ClientRun

### Run Code With Arguments
You can use argument to control the network config. The synopsis:

    java -cp dist/clueless.jar ServerRun [--port PORT] [--backlog BACKLOG] [--address IP_ADDRESS]

Example usage assuming you want to listen on 192.168.1.5:6543:

    java -cp dist/clueless.jar ServerRun --port 6543 --address 192.168.1.5

No arguments will default to backlog of 6, port of 2323, and address of 0.0.0.0 (any address).

## Python
### Start Service
From a console, goto the py-src/clueless directory and run the following:

    python.exe service.py
#### Example Output

    D:\a-team\py-src> python service.py
    listening from 127.0.0.1
    Accepting new connection
    invalid direction

### Run Client Tests
From another console, goto the py-src/clueless directory and run the following:

    python.exe client1.py

### Example Output
    D:\a-team\py-src>python client1.py
    connecting to localhost port 8000
    Scarlet is not active in room Invalid
    Mustard is not active in room Invalid
    White is not active in room Invalid
    Green is not active in room Invalid
    Peacock is not active in room Invalid
    Plum is not active in room Invalid
    Character secessfully claimed.
    Failed to claim character
    Game started.
    Move complete.
    Scarlet is not active in room Invalid
    Mustard is not active in room Invalid
    White is not active in room Invalid
    Green is in Hallway (N Invalid S Invalid E Ballroom W Conservatory O Invalid)
    Peacock is not active in room Invalid
    Plum is not active in room Invalid
    mode 0x60 target 0 A 0 W 0 L 0
    Move invalid.
## GML (Game Maker Studio 2)
You'll want to download Game Maker Studio 2 Trial from https://www.yoyogames.com/get

Once you've got it, just open the project and hit the triangle play button. If you click host, it'll put you into a character selection screen and then the game screen.

```mermaid
graph LR
A[Title Screen]
B[Character Selection]
C[Game]

A --> B
B --> C
```
