Title: Readme of Assignment One — Simple Machine Language (SML)

Description:
==============================================
Write an interpreter for a simple machine language — SML. The general form of a machine language instruction is
    label instruction register-list

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
$ java -cp ./sml.jar SML ../resources/Test.txt

Version:
==============================================
JDK 1.6