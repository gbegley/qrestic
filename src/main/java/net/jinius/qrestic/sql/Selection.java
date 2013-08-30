package net.jinius.qrestic.sql;

import java.util.Formatter;

/**
 *
 */
public class Selection implements SqlFragment {

    private SelectList fields;
    private Criteria criteria;

    private SqlFragment object;
    private SqlFragment aggregates;
    private SqlFragment ordering;
    private SqlFragment grouping;

    public Selection clone(){
        Selection selection = new Selection(object.get());
        if(fields!=null && fields.size()>0) selection.fields = fields;
        if(criteria!=null && criteria.size()>0) {
            selection.criteria = new Criteria();
            selection.criteria.add(criteria.get());
        }
        if(aggregates!=null) selection.aggregates = aggregates;
        if(ordering!=null) selection.ordering = ordering;
        if(grouping!=null) selection.grouping = grouping;
        return selection;
    }

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


    public SelectList getFields() {
        return fields;
    }

    public void setFields(SelectList fields) {
        this.fields = fields;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    public void setObject(SqlFragment object) {
        this.object = object;
    }

    public void setObject(String object) {
        this.object = new SimpleFragment(object);
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
        if(fields==null) fields = new SelectList();
        fields.add(sql);
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
        ordering = simpleFragment;//todo make this additive
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
        if(this.criteria==null) this.criteria = new Criteria();
        this.criteria.add(criteria);
        return this;
    }
}
