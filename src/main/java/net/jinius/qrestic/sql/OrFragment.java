package net.jinius.qrestic.sql;

import java.util.Formatter;

/**
 *
 */
public class OrFragment extends FragmentList {
    @Override
    public String get() {
        StringBuilder sb = new StringBuilder();
        Formatter f = new Formatter(sb);
        f.format("(");
        int index = 0;
        for(SqlFragment sf : this) {
            if(index==0) {
                f.format(  "(%s)",sf.get());
            } else {
                f.format(" OR (%s)",sf.get());
            }
            index++;
        }
        f.format(")");
        return sb.toString();
    }
}
