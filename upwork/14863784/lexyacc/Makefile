all: yadc.l yadc.y
	lex yadc.l
	yacc -d yadc.y
	g++ -o yadc lex.yy.c y.tab.c -w

clean:
	rm -f *.c *.h *.o yadc *.class
