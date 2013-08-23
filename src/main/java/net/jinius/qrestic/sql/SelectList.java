package net.jinius.qrestic.sql;

import java.util.Formatter;

/**
 *
 */
public class SelectList extends FragmentList {

    public SelectList() {
    }

    public SelectList(String ... fields){
        for(String s:fields) {
            this.add(s);
        }
    }

    @Override
    public String get() {
        StringBuilder sb = new StringBuilder();
        Formatter f = new Formatter(sb);
        int index = 0;
        for(SqlFragment sf : this) {
            if(index>0) {
                f.format("\n\t, %s",sf.get());
            } else {
                f.format("\n\t  %s",sf.get());
            }
            index++;
        }
        return sb.toString();
    }
}
