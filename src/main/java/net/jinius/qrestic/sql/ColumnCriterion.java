package net.jinius.qrestic.sql;

/**
 *
 */
public class ColumnCriterion implements SqlFragment{

    public String key;
    public String op;
    public String value;

    public static ColumnCriterion eq(String k,String value){
        return build(k,"=","'"+value+"'");
    }

    public static ColumnCriterion eq(String k,Number n){
        return build(k,"=",n.toString());
    }

    public static ColumnCriterion build(String k,String op, String value){
        ColumnCriterion cc = new ColumnCriterion();
        cc.key = k;
        cc.op = op;
        cc.value = value;
        return cc;
    }



    @Override
    public String get() {
        return key + op + value;
    }
}
