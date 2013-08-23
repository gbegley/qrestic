package net.jinius.qrestic.sql;

import java.util.Formatter;

/**
 *
 */
public class Criteria extends FragmentList implements SqlFragment {
    @Override
    public String get() {
        StringBuilder sb = new StringBuilder();
        Formatter f = new Formatter(sb);
        int index = 0;
        for(SqlFragment sf : this) {
            if(index>0) {
                f.format("\n   AND %s",sf.get());
            } else {
                f.format(" %s",sf.get());
            }
            index++;
        }
        return sb.toString();
    }
}
