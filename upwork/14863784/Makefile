# Targets
#  TODO: Make the build compile a static libary
#  when testing no longer required for A1.
<<<<<<< HEAD
JAVA_HEADER=-I/usr/lib/jvm/java-6-openjdk-i386/include -I/usr/lib/jvm/java-6-openjdk-i386/include/linux 
#JAVA_HEADER=-I/System/Library/Frameworks/JavaVM.framework/Versions/A/Headers/
=======
#JAVA_HEADER=-I/usr/lib/jvm/java-6-openjdk-i386/include -I/usr/lib/jvm/java-6-openjdk-i386/include/linux 
#JAVA_HEADER=-I/System/Library/Frameworks/JavaVM.framework/Versions/A/Headers/
JAVA_HEADER=-I"C:/Program Files/Java/jdk1.7.0_65/include"
>>>>>>> 3575a1c6564c064f2fc8054bd32b798f09da6574

SO_FLAG=-Wl,-soname,libNativeManager.so
#SO_FLAG= 

all: init libraries lexyacc Dialogc

init:
	@mkdir -p bin

clean:
	@rm -rf bin */*.orig */*.class
	cd lexyacc; make clean

# Java code
Dialogc: src/Dialogc.java src/NativeManager.java src/Generator.java src/DialogcDocument.java src/gui/FileBrowseDialog.java src/Main.java
	javac src/Dialogc.java -d bin -sourcepath src
	javac src/Main.java -d bin -sourcepath src
	cp README.txt bin
	cp About.txt bin
	cp -R icons bin
	cd lexyacc; make
	mv lexyacc/yadc bin/

# Libraries
libraries: native/NativeManager.c native/ParameterManager.c native/HashTable.c
	gcc native/NativeManager.c native/ParameterManager.c native/HashTable.c -g -fPIC -shared ${SO_FLAG} -o bin/libNativeManager.so ${JAVA_HEADER}  -w
	cp bin/libNativeManager.so bin/libNativeManager.jnilib

<<<<<<< HEAD
dialogc:
	tar cvf dialogc.tar src/ native/ lexyacc/ Makefile icons/ README.txt About.txt db.conf
	gzip -f dialogc.tar
	cp install_template.sh dialogc.sh
	cat dialogc.tar.gz >> dialogc.sh
	rm -f dialogc.tar dialogc.tar.gz
=======
brel_tar:
	tar cvf dialogc.tar src/ native/ icons/ lexyacc/ README.txt About.txt db.conf
	gzip dialog.tar
	cp install_template.sh dialogc.sh
	cat dialogc.tar.gz >> dialogc.sh
	rm -f dialog.tar.gz
	chmod +x dialogc.sh
>>>>>>> 3575a1c6564c064f2fc8054bd32b798f09da6574
