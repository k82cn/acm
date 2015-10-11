include ../make.misc

OBJS=log.o \
     fs.o \
     tcp.o \
     buffer.o \
     mem.o \
     md5.o \
     hash.o \
     thread.o \
     utils.o \
     bitmap.o \
     props.o

all: libogl.a

CC=g++
INCLUDE=../includes
CFLAGS=${OGL_CFLAGS}

libogl.a: ${OBJS}
	ar cr libogl.a ${OBJS}
	ranlib libogl.a

${OBJS}: %.o:%.cpp
	${CC} -c ${CFLAGS} -I${INCLUDE} $< -o $@

clean:
	rm -f *.o *.a *.orig *~
