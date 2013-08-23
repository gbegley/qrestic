package net.jinius.qrestic.sql;

import java.util.ArrayList;

/**
 *
 */
public abstract class FragmentList
       extends ArrayList<SqlFragment>
       implements SqlFragment {

    public void add(String s) {
        this.add(new SimpleFragment(s));
    }
}
