Information regarding the program:

Assumptions:

- It is assumed that the user clearly knows how the format of the .config file should look like. 
- The user has to input appropriate strings and integers.

Limitations:

-Some exception handling is needed to perform for integers/strings/etc. 

Source Files:

Dialog.java: This file contains code to the GUI where the user can interact with. The user may able to interact with menus however they like and input data into the text editor. Basically, where I have implemented ActionListener for a callback mechanism which then implements the interface ActionListener. 

DialogcDocument.java: This file is used to set the style of the text, document, etc.

DummyListener.java: Testing the buttons once generated.

GenerateException.java: Basically, generates an exception.

Generator.java: Once the user compiles and the compiles plus runs the .config text file this will come to play and create the required buttons, field, and titles in another window.

FileBrowseDialog.java: This file lets the user browse any files they would like to see inside the text editor but mainly any .config extension file.

NativeManager.java: Dialogc will call the Generator file and the  link to NativeManager file. Also, libJNIpm.so is loaded by the Java at runtime due to this file. It knows the prefix with ‘lib’ and postfix with ‘.so’(extension for Unix/Linux). Create and Destroy are being free in this file for the JNI component. 

HashTable.c: This file stores all data into a hash table.

HashTable.h: Contains all the functions for the .c file to operate. 


