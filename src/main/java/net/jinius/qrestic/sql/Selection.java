package net.jinius.qrestic.sql;

import java.util.Formatter;

/**
 *
 */
public class Selection implements SqlFragment {

    private SqlFragment fields;
    private SqlFragment object;
    private SqlFragment criteria;
    private SqlFragment aggregates;
    private SqlFragment ordering;
    private SqlFragment grouping;

    public Selection(String object) {
        this.object = new SimpleFragment(object);
    }

    @Override
    public String get() {
        StringBuilder sb = new StringBuilder();
        Formatter f = new Formatter(sb);
        f.format("SELECT ");

        String sFields = null;
        if(fields==null) {
            f.format("*");
        } else {
            f.format("%s",fields.get());
            if(aggregates!=null) {
                f.format(",\n       %s",aggregates.get());
            }
        }

        f.format("\n  FROM %s", object.get());
        if(criteria!=null) {
            String sCriteria = criteria.get();
            if(sCriteria!=null&&sCriteria.length()>0){
                f.format("\n WHERE %s", sCriteria);
            }
        }

        if(aggregates!=null && fields!=null) {
            String gb = fields.get();
            f.format("\n GROUP BY %s", gb);
            if(grouping!=null) {
            f.format("\n    , %s",grouping.get());
            }
        } else if (grouping!=null){
            String gb = grouping.get();
            f.format("\n GROUP BY %s", gb);
        }

        if(ordering!=null) {
            String g = ordering.get();
            if(g!=null&&g.length()>0){
                f.format("\n ORDER BY %s", g);
            }
        }

        return sb.toString();
    }

    public SqlFragment getCriteria() {
        return criteria;
    }

    public void setCriteria(SqlFragment criteria) {
        this.criteria = criteria;
    }

    public SqlFragment getFields() {
        return fields;
    }

    public void setFields(SqlFragment fields) {
        this.fields = fields;
    }

    public void setFields(String fields) {
        this.fields = new SimpleFragment(fields);
    }

    public SqlFragment getObject() {
        return object;
    }

    public void setObject(SqlFragment object) {
        this.object = object;
    }

    public void setObject(String object) {
        this.object = new SimpleFragment(object);
    }

    public void setCriteria(String criteria1) {
        this.criteria = new SimpleFragment(criteria1);
    }

    public SqlFragment getAggregates() {
        return aggregates;
    }

    public void setAggregates(SqlFragment aggregates) {
        this.aggregates = aggregates;
    }

    public SqlFragment getOrdering() {
        return ordering;
    }

    public void setOrdering(SqlFragment ordering) {
        this.ordering = ordering;
    }

    public SqlFragment getGrouping() {
        return grouping;
    }

    public void setGrouping(SqlFragment grouping) {
        this.grouping = grouping;
    }

    public Selection withGrouping(SqlFragment grouping) {
        setGrouping(grouping);
        return this;
    }

    public Selection withFields(String fields) {
        return withFields(new SimpleFragment(fields));
    }

    public Selection withFields(SqlFragment sql) {
        setFields(sql);
        return this;
    }

    public Selection withAggregates(String aggregates) {
        setAggregates(new SimpleFragment(aggregates));
        return this;
    }

    public Selection withAggregates(SqlFragment sql) {
        setAggregates(sql);
        return this;
    }

    public Selection withOrdering(SimpleFragment simpleFragment) {
        setAggregates(simpleFragment);
        return this;
    }



    public Selection withObject(String object) {
        return withObject(new SimpleFragment(object));
    }

    private Selection withObject(SimpleFragment object) {
        setObject(object);
        return this;
    }

    public Selection withCriteria(String criteria) {
        return withCriteria(new SimpleFragment(criteria));
    }

    public Selection withCriteria(SqlFragment criteria) {
        setCriteria(criteria);
        return this;
    }
}
