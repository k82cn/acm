#!/bin/sh

TEMPDIR="/tmp/dialogc_installer"
INSTALLDIR=""
BUILDONLY=0
INSTALLONLY=0

modify_bashrc() (
<<<<<<< HEAD
    BASHRC="$HOME/.bashrc"
    echo "export PATH=$PATH:$INSTALLDIR/bin" >> $BASHRC
    echo "export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$INSTALLDIR/lib" >> $BASHRC
)

check_parameters() {
    if [ "$1" != "" ]; then
        if [ "$1" = "--build" ]; then
            BUILDONLY=1
        elif [ "$1" = "--install" ]; then
            INSTALLONLY=1
        else
            echo "Invalid parameter: '$1', valid parameters are: '--build', '--install"
            exit 1
        fi
    fi
=======
	BASHRC="$HOME/.bashrc"
	echo "export PATH=$PATH:$INSTALLDIR/bin" >> $BASHRC
	echo "export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$INSTALLDIR/lib" >> $BASHRC
)

check_parameters() {
	if [ "$1" != "" ]; then
		if [ "$1" = "--build" ]; then
			BUILDONLY=1
		elif [ "$1" = "--install" ]; then
			INSTALLONLY=1
		else
			echo "Invalid parameter: '$1', valid parameters are: '--build', '--install"
			echo
			exit 1
		fi
	fi
>>>>>>> 3575a1c6564c064f2fc8054bd32b798f09da6574
}

# Extract source files and build
do_build() {
<<<<<<< HEAD
    mkdir -p $TEMPDIR > /dev/null 2>&1
    if [ $? -ne 0 ]; then
        echo "Can not create temporary directory"
        echo "Exiting..."
        echo
        return 1
    fi

    echo "Extracting source files into $TEMPDIR... "
    BEGIN=`cat $0 | awk '/^__END_OF_INSTALL__/ {print NR + 1}'`
    echo

    tail -n +$BEGIN $0 | tar xz -C $TEMPDIR

    echo "Building... "
    cd $TEMPDIR
    make clean > /dev/null 2>&1
    make > "$TEMPDIR/build.log" 2>&1
    RETVAL=$?

    if [ $RETVAL -ne 0 ]; then
        echo "fail"
        echo "Build was failed, check $TEMPDIR/build.log for errors"
        echo "Exiting..."
        echo
        return 1
    fi

    echo "done"
    return 0
=======
	mkdir -p $TEMPDIR > /dev/null 2>&1
	if [ $? -ne 0 ]; then
		echo "Can not create temporary directory"
		echo "Exiting..."
		echo
		return 1
	fi

	echo -n "Extracting source files into $TEMPDIR... "
	BEGIN=`cat $0 | awk '/^__END_OF_INSTALL__/ {print NR + 1}'`
	echo

	tail -n +$BEGIN $0 | tar xz -C $TEMPDIR

	echo -n "Building... "
	cd $TEMPDIR
	make clean > /dev/null 2>&1
	make > "$TEMPDIR/build.log" 2>&1
	RETVAL=$?

	if [ $RETVAL -ne 0 ]; then
		echo "fail"
		echo "Build was failed, check $TEMPDIR/build.log for errors"
		echo "Exiting..."
		echo
		return 1
	fi

	echo "done"
	return 0
>>>>>>> 3575a1c6564c064f2fc8054bd32b798f09da6574
}

# Installation
do_install() {
<<<<<<< HEAD
    if [ ! -d "$TEMPDIR" ]; then
        echo "Previous build is not available"
        echo "Exiting.."
        echo
        exit 0
    fi

    echo "Enter installation directory (i.e $HOME/dialogc): "
    read INSTALLDIR

    if [ "$INSTALLDIR" = "" ]; then
        INSTALLDIR=$HOME/dialogc
    fi

    if [ ! -d "$INSTALLDIR" ]; then
        echo "Directory '$INSTALLDIR' is not exist, do you want to create it? (y/n): "
        read INPUT
        if [ "$INPUT" != "y" ]; then
            echo "Exiting..."
            echo
            exit 1
        fi
        mkdir -p $INSTALLDIR > /dev/null 2>&1
        if [ $? -ne 0 ]; then
            echo "Can not create directory, check your permission, etc."
            echo "Exiting..."
            echo
            exit 1
        fi
    fi

    if [ ! -w $INSTALLDIR ]; then
        echo "No write permission to directory '$INSTALLDIR'"
        echo "Please change it's permission and run the installer again"
        echo "Exiting..."
        echo
        exit 0
    fi

    echo "Copying files... "
    mkdir -p "$INSTALLDIR/lib" > /dev/null 2>&1 && \
    mkdir -p "$INSTALLDIR/conf" > /dev/null 2>&1 && \
    mv $TEMPDIR/bin/*.so $INSTALLDIR/lib > /dev/null 2>&1 && \
    cp -r $TEMPDIR/bin $INSTALLDIR > /dev/null 2>&1 && \
    cp -r $TEMPDIR/icons $INSTALLDIR > /dev/null 2>&1 && \
    cp $TEMPDIR/db.conf $INSTALLDIR/conf/ > /dev/null 2>&1 && \
    cp $TEMPDIR/About.txt $TEMPDIR/README.txt $INSTALLDIR > /dev/null 2>&1

    echo "#!/bin/sh" > $INSTALLDIR/bin/dialogc
    echo "cd $INSTALLDIR" >> $INSTALLDIR/bin/dialogc
    echo "java -cp .:./bin -Djava.library.path=$INSTALLDIR/bin -Ddialogc.home=$INSTALLDIR Dialogc" >> $INSTALLDIR/bin/dialogc
    chmod +x $INSTALLDIR/bin/dialogc

    if [ $? -ne 0 ]; then
        echo "fail"
        echo "Copy was failed"
        echo "Exiting..."
        echo
        exit 1
    fi

    echo "done"

    echo "Adding entries into .bashrc... "
    modify_bashrc
    echo done

    echo "Cleaning up... "
    rm -rf $TEMPDIR
    echo "done"

    echo
    echo "Dialogc has been installed in '$INSTALLDIR', configure '$INSTALLDIR/conf/db.conf' for database connection."
    echo
=======
	if [ ! -d "$TEMPDIR" ]; then
		echo "Previous build is not available"
		echo "Exiting.."
		echo
		exit 0
	fi

	echo -n "Enter installation directory (i.e $HOME/dialogc): "
	read INSTALLDIR

	if [ "$INSTALLDIR" = "" ]; then
		echo "Installation directory must be set"
		echo "Exiting..."
		echo
		exit 1
	fi

	if [ ! -d "$INSTALLDIR" ]; then
		echo -n "Directory '$INSTALLDIR' is not exist, do you want to create it? (y/n): "
		read INPUT
		if [ "$INPUT" != "y" ]; then
			echo "Exiting..."
			echo
			exit 1
		fi
		mkdir -p $INSTALLDIR > /dev/null 2>&1
		if [ $? -ne 0 ]; then
			echo "Can not create directory, check your permission, etc."
			echo "Exiting..."
			echo
			exit 1
		fi
	fi

	if [ ! -w $INSTALLDIR ]; then
		echo "No write permission to directory '$INSTALLDIR'"
		echo "Please change it's permission and run the installer again"
		echo "Exiting..."
		echo
		exit 0
	fi

	echo -n "Copying files... "
	mkdir -p "$INSTALLDIR/lib" > /dev/null 2>&1 && \
	mv $TEMPDIR/bin/*.so $INSTALLDIR/lib > /dev/null 2>&1 && \
	cp -r $TEMPDIR/bin $INSTALLDIR > /dev/null 2>&1 && \
	cp -r $TEMPDIR/icons $INSTALLDIR > /dev/null 2>&1 && \
	cp $TEMPDIR/About.txt $TEMPDIR/README.txt $INSTALLDIR > /dev/null 2>&1

	if [ $? -ne 0 ]; then
		echo "fail"
		echo "Copy was failed"
		echo "Exiting..."
		echo
		exit 1
	fi

	echo "done"

	echo -n "Adding entries into .bashrc... "
	modify_bashrc
	echo done

	echo -n "Cleaning up... "
	rm -rf $TEMPDIR
	echo "done"

	echo
	echo "Dialogc has been installed in '$INSTALLDIR'"
	echo
>>>>>>> 3575a1c6564c064f2fc8054bd32b798f09da6574
}

check_parameters $1

echo "-----------------------------"
echo "      Dialogc Installer      "
echo "-----------------------------"
echo

# Install confirmation
if [ $BUILDONLY -eq 1 ]; then
<<<<<<< HEAD
    echo "This installer will build Dialogc"
elif [ $INSTALLONLY -eq 1 ]; then
    echo "This installer will install Dialogc"
else
    echo "This installer will build and install Dialogc"
fi

echo "Do you want to continue? (y/n): "
read INPUT
if [ "$INPUT" != "y" ]; then
    echo "Exiting..."
    echo
    exit 1
=======
	echo "This installer will build Dialogc"
elif [ $INSTALLONLY -eq 1 ]; then
	echo "This installer will install Dialogc"
else
	echo "This installer will build and install Dialogc"
fi

echo -n "Do you want to continue? (y/n): "
read INPUT
if [ "$INPUT" != "y" ]; then
	echo "Exiting..."
	echo
	exit 1
>>>>>>> 3575a1c6564c064f2fc8054bd32b798f09da6574
fi

echo

if [ $INSTALLONLY -ne 1 ]; then
<<<<<<< HEAD
    do_build
    [ $? -ne 0 ] && exit 1
fi

if [ $BUILDONLY -eq 1 ]; then
    echo
    exit 0
=======
	do_build
	[ $? -ne 0 ] && exit 1
fi

if [ $BUILDONLY -eq 1 ]; then
	echo
	exit 0
>>>>>>> 3575a1c6564c064f2fc8054bd32b798f09da6574
fi

do_install

exit 0

__END_OF_INSTALL__
