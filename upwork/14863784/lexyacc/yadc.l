%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "y.tab.h"

%}
chars [A-Za-z0-9_]
assign "="
LB "{"
RB "}"
sim ";"
comma ","
name {chars}+
value \"[A-Za-z0-9 \t_]+\"
comments #.*$
%%
{name} { yylval.str = strdup(yytext); return NAME; }
{value} {
    int len = strlen(yytext);
    yylval.str = (char*) calloc(len, 1);
    memcpy(yylval.str, yytext + 1, len - 2);
    return VALUE;
}
{assign} { return ASSIGN;}
{sim} { return SIM;}
{comma} { return COMMA;}
{LB} { return LB;}
{RB} { return RB;}
{comments} ;
%%


int yywrap()
{
return 1;
}
