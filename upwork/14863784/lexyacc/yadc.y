%{
#include <stdio.h>
#include <string.h>
#include <errno.h>

extern int yylex();
extern int yyparse();
extern int yywrap();
extern FILE* yyin;

#define MYINT 1
#define MYSTR 2
#define MYFLOAT 3
#define MYBOOL 4

typedef struct 
{
    char* name;
    int type;
    char* value;
} item_t;

// functions for fields
static item_t* fields = 0;
static int fieldCnt = 0;

// functions for buttons
static item_t* buttons = 0;
static int buttonCnt = 0;

static void set_list(char* name);

// current fields/buttons array for parser 
static item_t* list = 0;
static int listCnt = 0;

static char* title = 0;
static int set_data_type(char* name, char* value);
static int get_type_by_str(char*);


void yyerror(char* msg);

void generate_java_code(const char* path, const char* name, char* dbConf);

%}

%token NAME VALUE LB RB SIM COMMA ASSIGN

%union {
    char *str;
}

%token <str> NAME
%token <str> VALUE

%%

config: title list list kvs
    ;
title: NAME ASSIGN VALUE SIM { title = $3; }

list: NAME ASSIGN LB 
    values RB SIM 
        {
            set_list($1);
            list = 0;
            listCnt = 0;
        }
    ;


values:
    values COMMA value
    | value
    ;

value:
    VALUE { 
        if (list == 0)
        {
            list = (item_t*)calloc(1024, sizeof(item_t));
        }
        list[listCnt].name = $1;
        list[listCnt].type = -1;
        list[listCnt].value = 0;
        listCnt++;
    }
    ;

kvs:
    kvs kv
    | kv
    ;

kv: NAME ASSIGN VALUE SIM { 
    if (set_data_type($1, $3) != 1)
    {
    }
}

%%

void yyerror(char* msg)
{
    fprintf(stderr, "%s\n", msg);
}

int main(int argc, char** argv)
{
    int rc;
    if (argc != 5)
    {
        fprintf(stderr, "Usage: %s input_file output_file class_name dbconf\n", argv[0]);
        return 1;
    }

    yyin = fopen(argv[1], "r+");
    if (0 == yyin)
    {
        fprintf(stderr, "Failed to open input file <%s>", argv[1]);
        return 1;
    }

    rc = yyparse(); 
    switch(rc)
    {
        case 0:
            {
            FILE* dbConf = fopen(argv[4], "r+");
            char conUrl[1024] = {0};
            int len = 0;
            if (0 == dbConf)
            {
                fprintf(stderr, "Failed to open database configuration file <%s>\n", argv[4]);
                return 1;
            }

            fgets(conUrl, 1024, dbConf);
            // ignore \n
            len = strlen(conUrl);
            if (len >= 1) conUrl[len - 1] = 0;

            fclose(dbConf);

            generate_java_code(argv[2], argv[3], conUrl);
            }
            break;
        case 1:
            fprintf(stderr, "Failed to parse the input, syntax error!");
            break;
        case 2:
            fprintf(stderr, "Failed to parse the input, not enough memory!");
            break;
        default:
            fprintf(stderr, "Failed to parse the input, unknown error!");
            break;
    }

    fclose(yyin);

    return rc;
}

int set_data_type(char* name, char* value)
{
    int i ;
    // find the name in fields
    for (i = 0; i < fieldCnt; i++)
    {
        if (strcmp(name, fields[i].name) == 0)
        {
            int type = get_type_by_str(value);
            if (type == -1)
            {
                char buffer[1024] = {0};
                sprintf(buffer, "The type(%s) of <%s> is invalid.", value, name);
                yyerror(buffer);
                return 0;
            }
            fields[i].type = type;
            return 1;
        }
    }
    // find the name in buttons
    for (i = 0; i < buttonCnt; i++)
    {
        if (strcmp(name, buttons[i].name) == 0)
        {
            buttons[i].value = value;
            return 1;
        }
    }

    {
        char buffer[1024] = {0};
        sprintf(buffer, "Can not find <%s> in both fields and buttons.", name);
        yyerror(buffer);
        return 0;
    }
}

int get_type_by_str(char* val)
{

    if (strcmp(val, "integer") == 0)
    {
        return MYINT;
    } else if (strcmp(val, "string") == 0)
    {
        return MYSTR;
    } else if (strcmp(val, "float") == 0)
    {
        return MYFLOAT;
    }
    else
    {
        return -1;
    }
}

void generate_java_code(const char* path, const char* name, char* connStr)
{
    int i = 0;
    FILE* code = fopen(path, "w+");

    if (0 == code)
    {
        fprintf(stderr, "Failed to generate Java code at <%s>, errno <%d>.\n", path, errno);
        return;
    };

    // Import statements
    fprintf(code,"import java.awt.*;\n");
    fprintf(code,"import javax.swing.*;\n");

    // listeners definition

    fprintf(code, "import java.awt.event.ActionEvent;\n");
    fprintf(code, "import java.awt.event.ActionListener;\n");
    fprintf(code, "import java.io.IOException;\n");
    fprintf(code, "import java.sql.*;\n\n");

    fprintf(code, "class ADDAction implements ActionListener {\n"
                  "    %sFieldEdit %s;\n"
                  "    public ADDAction(%sFieldEdit %s) {\n"
                  "        this.%s = %s;\n"
                  "    }\n"
                  "    public void actionPerformed(ActionEvent e) {\n", name, name, name, name, name, name);
    fprintf(code, "       try {\n");
    fprintf(code, "           String sql = \"INSERT INTO %s (", name);
    for (i = 0; i < fieldCnt; i++)
    {
        if (i != 0) fprintf(code, ",");
        fprintf(code, fields[i].name);
    }

    fprintf(code, "");
    fprintf(code, ") VALUE (");
    for (i = 0; i < fieldCnt; i++)
    {
        fprintf(code, i? ",?":"?");
    }
    fprintf(code, ")\";\n");

    fprintf(code, "                Connection con = DriverManager.getConnection(\"%s\");\n"
                  "                PreparedStatement stmt = con.prepareStatement(sql,"
                                                " Statement.RETURN_GENERATED_KEYS);\n", connStr);

    for (i = 0; i < fieldCnt; i++)
    {
        char* field = fields[i].name;
        switch(fields[i].type)
        {
            case MYINT:
                fprintf(code, "                stmt.setInt(%d, Integer.parseInt(%s.get%s()));\n", i + 1, name, field);
                break;
            case MYFLOAT:
                fprintf(code, "                stmt.setDouble(%d, Double.parseDouble(%s.get%s()));\n", i + 1, name, field);
                break;
            case MYSTR:
                fprintf(code, "                stmt.setString(%d, %s.get%s());\n", i + 1, name, field);
            default:
                break;
        }
    }

    fprintf(code, "                stmt.executeUpdate(sql);\n"
                  "                ResultSet rs = stmt.getGeneratedKeys();\n"
                  "                if (rs.next()) { \n"
                  "                    %s.__setId(rs.getLong(1));\n"
                  "                }\n"
                  "                %s.appendToStatusArea(\"Insert value into database as \" + %s.__getId());\n"
                  "           } catch (Exception e1) {\n"
                  "                %s.appendToStatusArea(e1.getMessage());\n"
                  "           }\n"
                  "     }\n"
                  "}\n", name, name, name, name);

    fprintf(code, "class DELETEAction implements ActionListener {\n"
                  "    %sFieldEdit %s;\n"
                  "    public DELETEAction(%sFieldEdit %s) {\n"
                  "        this.%s = %s;\n"
                  "    }\n"
                  "    public void actionPerformed(ActionEvent e) {\n"
                  "        try {\n", name, name, name, name, name, name);

    fprintf(code, "            String sql = \"DELETE FROM %s ", name);
    for (i = 0; i < fieldCnt; i++)
    {
        char* field = fields[i].name;
        if (i != 0) fprintf(code, " AND ");
        else fprintf(code, " WHERE ");
        fprintf(code, "%s = \" + %s.getDC%s() + \"", field, name, field);
    }
    fprintf(code, "\";\n");

    fprintf(code, "            Connection con = DriverManager.getConnection(\"%s\");\n"
                  "            Statement stmt = con.createStatement();\n"
                  "            stmt.executeUpdate(sql);\n"
                  "        } catch (Exception e1) { \n"
                  "            %s.appendToStatusArea(e1.getMessage());\n"
                  "        } \n"
                  "    }\n"
                  "}\n", connStr, name);

    fprintf(code, "class UPDATEAction implements ActionListener {\n"
                  "    %sFieldEdit %s;\n"
                  "    public UPDATEAction (%sFieldEdit %s) {\n"
                  "        this.%s = %s;\n"
                  "    }\n"
                  "    public void actionPerformed(ActionEvent e) {\n", name, name, name, name, name, name);

    fprintf(code, "        String sql = \"UPDATE %s SET ", name);
    for (i = 0; i < fieldCnt; i++)
    {
        char* field = fields[i].name;
        if (i != 0) fprintf(code, ", ");
        fprintf(code, "%s = \\\"\" + %s.get%s() + \"\\\"", field, name, field);
    }
    fprintf(code, "\" + \" WHERE id = \" + %s.__getId() ;\n", name);

    fprintf(code, "        try {\n"
                  "            Connection con = DriverManager.getConnection(\"%s\");\n"
                  "            Statement stmt = con.createStatement();\n"
                  "            stmt.executeUpdate(sql);\n"
                  "        } catch (Exception e1) { \n"
                  "            %s.appendToStatusArea(e1.getMessage());\n"
                  "        }\n"
                  "     }\n"
                  "}\n", connStr, name);

    fprintf(code, "class QUERYAction implements  ActionListener {\n"
                  "    %sFieldEdit %s;\n"
                  "    public QUERYAction (%sFieldEdit %s) {\n"
                  "        this.%s = %s;\n"
                  "    }\n"
                  "    public void actionPerformed(ActionEvent e) {\n", name, name, name, name, name, name);

    fprintf(code, "        try { \n");
    fprintf(code, "        String sql = \"SELECT * FROM %s ", name);

    for(i = 0; i < fieldCnt; i++)
    {
        char* field = fields[i].name;
        if (i == 0) fprintf(code, " WHERE ");
        else fprintf(code, " AND ");
        fprintf(code, " %s = \" + %s.getDC%s() + \"", field, name, field);
    }

    fprintf(code, "\";\n");

    fprintf(code, "            Connection con = DriverManager.getConnection(\"%s\");\n"
                  "            Statement stmt = con.createStatement();\n"
                  "            ResultSet rs = stmt.executeQuery(sql);\n"
                  "            while(rs.next()) {\n", connStr);

    for(i = 0; i < fieldCnt; i++)
    {
        char* field = fields[i].name;
        fprintf(code, "                %s.appendToStatusArea(\"%s = \" + rs.getString(\"%s\"));\n", name, field, field);
    }

    fprintf(code, "            }\n"
                  "        } catch (Exception e1) {\n"
                  "            %s.appendToStatusArea(e1.getMessage());\n"
                  "        }\n"
                  "    }\n"
                  "}\n", name);


    // Interface definition

    fprintf(code, "interface %sFieldEdit\n", name);
    fprintf(code,"{\n");

    fprintf(code,"\tpublic void appendToStatusArea(String msg);\n");
    fprintf(code,"\tpublic void __setId(long id);\n");
    fprintf(code,"\tpublic long __getId();\n");

    for (i = 0; i < fieldCnt; i++)
    {
        char* field = fields[i].name;

        fprintf(code,"\tpublic void set%s(String %s);\n", field, field);
        fprintf(code,"\tpublic void setDC%s(String %s);\n", field, field);
        fprintf(code,"\tpublic String get%s();\n", field);
        fprintf(code,"\tpublic String getDC%s() throws IllegalFieldValueException;\n", field);
    }

    fprintf(code,"}\n");

    // Class definition
    fprintf(code,"public class %s extends JFrame implements %sFieldEdit\n", name, name);
    fprintf(code,"{\n");

    fprintf(code, "private long __id;\n");
    fprintf(code, "public void __setId(long id) { this.__id = id; }\n");
    fprintf(code, "public long __getId() { return this.__id; }\n");

    // Instance variables
    fprintf(code,"\t// The status area\n");
    fprintf(code,"\tJTextArea statusArea;\n");
    fprintf(code,"\t// Fields\n");
    for (i = 0; i < fieldCnt; i++)
    {
        fprintf(code,"\tprivate JLabel %s_label;\n", fields[i].name);
        fprintf(code,"\tprivate JTextField %s_field;\n", fields[i].name);
    }

    fprintf(code,"\n");
    fprintf(code,"\t// Buttons\n");
    for (i = 0; i < buttonCnt; i++)
    {
        fprintf(code,"\tprivate JButton %s_button;\n", buttons[i].name);
    }

    fprintf(code,"\n");
    // The constructor
    fprintf(code,"\t// Constructor\n");
    fprintf(code,"\tpublic %s()\n", name);
    fprintf(code,"\t{\n");
    fprintf(code,"\t\tsuper(\"%s\");\n", title);
    fprintf(code,"\t\tJPanel fieldsPanel = new JPanel(new BorderLayout());\n");
    fprintf(code,"\t\tJPanel buttonsPanel = new JPanel();\n");
    fprintf(code,"\t\tJPanel upperPanel = new JPanel(new BorderLayout());\n");
    fprintf(code,"\t\tJPanel statusPanel = new JPanel(new BorderLayout());\n");
    fprintf(code,"\t\tupperPanel.add(fieldsPanel, BorderLayout.NORTH);\n");
    fprintf(code,"\t\tupperPanel.add(buttonsPanel, BorderLayout.CENTER);\n");
    fprintf(code,"\t\tgetContentPane().add(upperPanel, BorderLayout.NORTH);\n");
    fprintf(code,"\t\tgetContentPane().add(statusPanel, BorderLayout.CENTER);\n");
    fprintf(code,"\t\tJPanel labelPanel = new JPanel(new GridLayout(%d, 1));\n", fieldCnt);
    fprintf(code,"\t\tJPanel textFieldPanel = new JPanel(new GridLayout(%d, 1));\n", fieldCnt);
    fprintf(code,"\t\tfieldsPanel.add(labelPanel, BorderLayout.WEST);\n");
    fprintf(code,"\t\tfieldsPanel.add(textFieldPanel, BorderLayout.CENTER);\n");
    for (i = 0; i < fieldCnt; i++)
    {
        char* field = fields[i].name;
        fprintf(code,"\t\t%s_label = new JLabel(\"%s\", JLabel.RIGHT);\n", field, field);
        fprintf(code,"\t\t%s_field = new JTextField(20);\n", field);
        fprintf(code,"\t\t%s_label.setLabelFor(%s_field);\n", field, field);
        fprintf(code,"\t\tlabelPanel.add(%s_label);\n", field);
        fprintf(code,"\t\ttextFieldPanel.add(%s_field);\n", field);
        fprintf(code,"\t\t");
    }

    for (i = 0; i < buttonCnt; i++)
    {
        char* button = buttons[i].name;
        char* listener = buttons[i].value;
        fprintf(code,"\t\t%s_button = new JButton(\"%s\");\n", button, button);
        if (listener)
        {
            fprintf(code,"\t\t%s_button.addActionListener(new %s(this));\n", button, listener);
        }
        else
        {
            if (strcmp("ADD", button) == 0 || strcmp("DELETE", button) == 0 
                    || strcmp("UPDATE", button) == 0 || strcmp("QUERY", button) == 0)
                fprintf(code,"\t\t%s_button.addActionListener(new %sAction(this));\n", button, button);
        }
        fprintf(code,"\t\tbuttonsPanel.add(%s_button);\n", button);
        fprintf(code,"\t\t\n");
    }
    fprintf(code,"\t\tstatusPanel.add(new JLabel(\"Status\", JLabel.CENTER), BorderLayout.NORTH);\n");
    fprintf(code,"\t\tstatusArea = new JTextArea();\n");
    fprintf(code,"\t\tstatusArea.setLineWrap(true);\n");
    fprintf(code,"\t\tstatusArea.setEditable(false);\n");
    fprintf(code,"\t\tJScrollPane statusScroller = new JScrollPane(statusArea);\n");
    fprintf(code,"\t\tstatusPanel.add(statusScroller, BorderLayout.CENTER);\n");
    fprintf(code,"\t\tsetDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);\n");
    fprintf(code,"\t\tsetSize(600, 400);\n");
    fprintf(code,"\t\tsetVisible(true);\n");
    fprintf(code,"\t}\n");
    // Bean set/get methods
    for (i = 0; i < fieldCnt; i++)
    {
        char* field = fields[i].name;

        fprintf(code,"\tpublic void set%s(String %s)\n", field, field);
        fprintf(code,"\t{\n");
        fprintf(code,"\t\t%s_field.setText(%s);\n", field, field);
        fprintf(code,"\t}\n");

        fprintf(code,"\tpublic void setDC%s(String %s)\n", field, field);
        fprintf(code,"\t{\n");
        fprintf(code,"\t\t%s_field.setText(%s);\n", field, field);
        fprintf(code,"\t}\n");


        fprintf(code,"\tpublic String get%s()\n", field);
        fprintf(code,"\t{\n");
        fprintf(code,"\t\treturn %s_field.getText();\n", field);
        fprintf(code,"\t}\n");

        fprintf(code,"\tpublic String getDC%s() throws IllegalFieldValueException\n", field);
        fprintf(code,"\t{\n");

        switch(fields[i].type)
        {
            case MYINT:
                fprintf(code,"\t\t try { Integer.parseInt(%s_field.getText()); }", field);
                fprintf(code,"\t\t\tcatch(Exception e) { "
                        "throw new IllegalFieldValueException( %s_field.getText() + \" is not a integer\"); "
                        "};", field);
                break;
            case MYFLOAT:
                fprintf(code,"\t\t try { Float.parseFloat(%s_field.getText()); }", field);
                fprintf(code,"\t\t\tcatch(Exception e) { "
                        "throw new IllegalFieldValueException( %s_field.getText() + \" is not a float\" ); "
                        "};", field);
                break;

            case MYSTR:
            default:
                break;
        }

        fprintf(code,"\t\treturn %s_field.getText();\n", field);

        fprintf(code,"\t}\n");

        fprintf(code,"\n");
    }
    // Append to status area
    fprintf(code,"\tpublic void appendToStatusArea(String message)\n");
    fprintf(code,"\t{\n");
    fprintf(code,"\t\tstatusArea.append(message + \"\\n\");\n");
    fprintf(code,"\t}\n");
    fprintf(code,"\t");
    //  main()
    fprintf(code,"\t// Main method.\n");
    fprintf(code,"\tpublic static void main(String[] args)\n");
    fprintf(code,"\t{\n");
    fprintf(code,"\t\tnew %s();\n", name);
    fprintf(code,"\t}\n");
    fprintf(code,"\t");
    fprintf(code,"}\n");

    fclose(code);
}

void set_list(char* name)
{
    if (strcmp(name, "fields") == 0)
    {
        fields = list;
        fieldCnt = listCnt;
        list = 0;
        listCnt = 0;
    }
    else
    {
        buttons = list;
        buttonCnt = listCnt;
        list = 0;
        listCnt = 0;
    }
} 

