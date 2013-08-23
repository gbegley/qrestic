package net.jinius.qrestic.sql;

import java.util.Formatter;

/**
 *
 */
public class FormattingFragment implements SqlFragment {

    private Object [] args;
    private String format;

    public FormattingFragment(String format) {
        this(format,new Object[0]);
    }

    public FormattingFragment(String format, Object[] args) {
        this.format = format;
        this.args = args;
    }

    @Override
    public String get() {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb);
        formatter.format(format, args);
        return sb.toString();
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

}
