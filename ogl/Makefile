all:
	cd src; make

clean:
	cd src; make clean
	cd test; make clean

astyle:
	astyle `find . -name *.cpp -o -name *.h`
	rm -f `find . -name *.orig | xargs`

test:
	cd test; make
