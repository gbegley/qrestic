package net.jinius.qrestic.sql;

/**
 *
 */
public class SimpleFragment implements SqlFragment {

    private String sql;

    public SimpleFragment(String sql) {
        this.sql = sql;
    }

    public SimpleFragment(String sql,Object ... args) {
        this.sql = String.format(sql,args);
    }

    @Override
    public String get() {
        return sql;
    }
}
