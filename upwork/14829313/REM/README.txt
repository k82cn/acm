Title: Readme of coursework assignment 5 — TCP and UDP

Description:
==============================================
The first client to connect to the server sends audio the audio. Clients that connect to the server after this will receive the audio and play it back.

File Struct:
==============================================
README.txt      The readme of this project
build.xml       The Ant build script
resources       The example of audio file
src             The source code of this project

How to build:
==============================================
Run "ant" at based directory, for example:

$ ant

After the job finished, there're three new direcories:
bin              The classes file
docs             Java document of public class
dis              The jar file of this project

How to run:
==============================================
Go to the directory of jar file:
$ cd dist

Start service:
$ java -cp ./cw5.jar coursework5.server.ClientManager

Start first client:
$ java -cp ./cw5.jar coursework5.client.Client localhost 28080 ../resources/1.wav 

Start other clients:
$ java -cp ./cw5.jar coursework5.client.Client localhost 28080 

Version:
==============================================
JDK 1.8