package net.jinius.qrestic.sql;

/**
*
*/
public enum OP {
    OR('|'),
    RANGE('~'),
    NOT('!')
    ;

    char op;
    OP(char c) {
        this.op = c;
    }
    public String toString(){
        return new String(new char[]{this.op});
    }
}
