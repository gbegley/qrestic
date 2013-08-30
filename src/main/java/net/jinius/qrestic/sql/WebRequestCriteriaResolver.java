package net.jinius.qrestic.sql;

import com.google.common.collect.Sets;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.web.context.request.WebRequest;

import java.sql.Types;
import java.util.*;
import java.util.regex.Pattern;

/**
 *
 */
public class WebRequestCriteriaResolver implements SqlFragmentResolver<WebRequest>{

    protected JdbcTemplate jdbcTemplate;
    protected String object;
    protected SqlRowSetMetaData sqlRowSetMetaData;
    protected Map<String,String> paramCriteriaMap = new HashMap<String,String>();
    protected LinkedHashMap<String,Integer> columnNameTypeMap = new LinkedHashMap<String,Integer>();

    public WebRequestCriteriaResolver(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected void compile(){
        final String metadataSql = String.format("select * from (%s) where 1=2", getObject());
        sqlRowSetMetaData = jdbcTemplate.queryForRowSet(metadataSql).getMetaData();
        columnNameTypeMap.clear();
        for(int i=1;i<sqlRowSetMetaData.getColumnCount()+1;i++) {
            columnNameTypeMap.put(sqlRowSetMetaData.getColumnName(i).toLowerCase(), sqlRowSetMetaData.getColumnType(i));
        }
    }


    public Criteria resolve(WebRequest webRequest) {
        if(sqlRowSetMetaData==null) compile();
        Criteria criteria = new Criteria();
        Iterator<String> paramsIt = webRequest.getParameterNames();
        while(paramsIt.hasNext()) {
            String param = paramsIt.next();
            String paramKey = param.toLowerCase();
            String [] values = webRequest.getParameterValues(param);
            for(String value : values) {
                OP op = null; // see if param uses any ops..
                for(OP p:OP.values()) if(value.indexOf(p.op)>=0){
                    op = p;
                    break;
                }
                if(op!=null) {
                    switch (op){
                        case OR:
                            String [] orValues = value.split("\\|");
                            OrFragment orFragment = new OrFragment();
                            for(String s:orValues) {
                                orFragment.add(createFragmentForKeyValue(paramKey,s));
                            }
                            criteria.add(orFragment);
                            break;
                        case RANGE:
                            String [] parts = value.split("~");
                            String operandFormat = DATE_TYPES.contains(columnNameTypeMap.get(paramKey)) ? "'%s'": "%s";
                            SqlFragment low = new FormattingFragment("%s > "+operandFormat,new Object[]{paramKey, parts[0]});
                            SqlFragment high = new FormattingFragment("%s < "+operandFormat,new Object[]{paramKey, parts[1]});
                            criteria.add(low);
                            criteria.add(high);
                            break;
                        case NOT:
                            String val = value.substring(1);
                            String format = CHAR_TYPES.contains(paramKey) ? "%s != '%s'" : "%s != '%s'";
                            criteria.add(new FormattingFragment(format,new Object[]{paramKey,val}));
                            break;
                        default:
                            break;
                    }
                } else {
                    criteria.add(createFragmentForKeyValue(paramKey,value));
                }
            }
        }
        return criteria;
    }



    public void resolve(WebRequest webRequest,Selection toSelection,Map<String,String> paramCriteriaMap) {
        LinkedHashMap<String,Integer> columnNameTypeMap = new LinkedHashMap<String,Integer>();
        String metadataSql = toSelection.clone().withCriteria("1=2").get();
        SqlRowSetMetaData sqlRowSetMetaData = jdbcTemplate.queryForRowSet(metadataSql).getMetaData();
        for(int i=1;i<sqlRowSetMetaData.getColumnCount()+1;i++) {
            columnNameTypeMap.put(sqlRowSetMetaData.getColumnName(i).toLowerCase(), sqlRowSetMetaData.getColumnType(i));
        }
        Iterator<String> paramsIt = webRequest.getParameterNames();
        while(paramsIt.hasNext()) {
            String param = paramsIt.next();
            String paramKey = param.toLowerCase();
            String [] values = webRequest.getParameterValues(param);
            for(String value : values) {
                OP op = null; // see if param uses any ops..
                for(OP p:OP.values()) if(value.indexOf(p.op)>=0){
                    op = p;
                    break;
                }
                if(op!=null) {
                    switch (op){
                        case OR:
                            String [] orValues = value.split("\\|");
                            OrFragment orFragment = new OrFragment();
                            for(String s:orValues) {
                                orFragment.add(createFragmentForKeyValue(paramKey,s));
                            }
                            toSelection.withCriteria(orFragment);
                            break;
                        case RANGE:
                            String [] parts = value.split("~");
                            String operandFormat = DATE_TYPES.contains(columnNameTypeMap.get(paramKey)) ? "'%s'": "%s";
                            SqlFragment low = new FormattingFragment("%s > "+operandFormat,new Object[]{paramKey, parts[0]});
                            SqlFragment high = new FormattingFragment("%s < "+operandFormat,new Object[]{paramKey, parts[1]});
                            toSelection.withCriteria(low);
                            toSelection.withCriteria(high);
                            break;
                        case NOT:
                            String val = value.substring(1);
                            String format = CHAR_TYPES.contains(paramKey) ? "%s != '%s'" : "%s != '%s'";
                            toSelection.withCriteria(new FormattingFragment(format,new Object[]{paramKey,val}));
                            break;
                        default:
                            break;
                    }
                } else {
                    SqlFragment sf = createFragmentForKeyValue(paramKey,value,columnNameTypeMap,paramCriteriaMap);
                    toSelection.withCriteria(sf);
                }
            }
        }
    }


    public static final String stringFragmentFormat = "%s='%s'", stdFragmentFormat = "%s=%s";

    protected SqlFragment createFragmentForKeyValue(String key,String value) {
        SqlFragment sf = null;
        if(paramCriteriaMap.containsKey(key)) {
            String criteriaTemplate = paramCriteriaMap.get(key);
            sf = new FormattingFragment(criteriaTemplate,new Object[]{key,value});
        } else if(columnNameTypeMap.containsKey(key)) {
            Integer type = columnNameTypeMap.get(key);
            String format = CHAR_TYPES.contains(type) ? stringFragmentFormat : stdFragmentFormat;
            sf = new FormattingFragment(format,new Object[]{key,value});
        }
        return sf;
    }

    protected SqlFragment createFragmentForKeyValue(String key,String value,Map<String,Integer> columnNameTypeMap, Map<String,String> paramCriteriaMap) {
        SqlFragment sf = null;
        if(paramCriteriaMap!=null && paramCriteriaMap.containsKey(key)) {
            String criteriaTemplate = paramCriteriaMap.get(key);
            sf = new FormattingFragment(criteriaTemplate,new Object[]{key,value});
        } else if(columnNameTypeMap.containsKey(key)) {
            Integer type = columnNameTypeMap.get(key);
            String format = CHAR_TYPES.contains(type) ? stringFragmentFormat : stdFragmentFormat;
            sf = new FormattingFragment(format,new Object[]{key,value});
        }
        return sf;
    }


    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public void addParamCriteriaMapping(String param,String criteriaTemplate){
        paramCriteriaMap.put(param, criteriaTemplate);
    }


    public static Set<Integer> CHAR_TYPES = Sets.newHashSet();
    static {
        CHAR_TYPES.add(Types.VARCHAR);
        CHAR_TYPES.add(Types.NVARCHAR);
        CHAR_TYPES.add(Types.CHAR);
        CHAR_TYPES.add(Types.NCHAR);
        CHAR_TYPES.add(Types.LONGVARCHAR);
        CHAR_TYPES.add(Types.LONGNVARCHAR);
    }

    public static Set<Integer> DATE_TYPES = Sets.newHashSet();
    static {
        DATE_TYPES.add(Types.DATE);
        DATE_TYPES.add(Types.TIMESTAMP);
    }

    final Pattern DATE_PATTERN = Pattern.compile("(\\d\\d)/(\\d\\d)/(\\d\\d)");

}
