package net.jinius.qrestic.sql;

import java.util.List;

/**
 *
 */
public interface SqlFragmentResolver<T> {
    List<SqlFragment> resolve(T t);
}
