package com.yesmywine.base.record.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 12/22/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class PageModel<T> implements Serializable {
    private Integer page; // 当前页
    private Integer totalPages; // 总页数
    private Integer pageSize;// 每页10条数据
    private Long totalRows; // 总数据数
    private List<T> content; // query condition

    private boolean hasPrevPage = true;
    private boolean hasNextPage = true;

    private String url;
    private String conditionJson;
    private String fields;

    private transient List<QueryCondition> conditions = new ArrayList<>();

    private transient Gson gson = new Gson();


    public PageModel() {
    }

    public PageModel(Map<String, Object> params) {
        Object tempJson = params.remove("conditionJson");
        if (null != tempJson) {
            setConditionJson(tempJson.toString());
        } else {
            addCondition(params);
        }
    }

    public PageModel(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public PageModel(int page, int pageSize, String conditionJson) {
        this.page = page;
        this.pageSize = pageSize;
        setConditionJson(conditionJson);
    }


    public void addCondition(ConditionType conditionType, String field, Object value, String clzPath) {
        QueryCondition condition = null;
        if (null == clzPath) {
            condition = new QueryCondition(conditionType, field, value);
        } else {
            condition = new QueryCondition(conditionType, field, value, clzPath);
        }
        conditions.add(condition);
    }

    public void addCondition(Map<String, Object> param) {
        param.forEach((ka, v) -> {
            String[] ks = ka.split("_");
            String k = ks[0];
            String type = "";
            if (ks.length == 2) {
                type = ks[1];
            }
            String clzPath = null;
            if (ks.length == 3) {
                clzPath = ks[2];
            }

            switch (type) {
                case "l":
                    addCondition(ConditionType.like, k, v, clzPath);
                    break;
                case "nl":
                    addCondition(ConditionType.notLike, k, v, clzPath);
                    break;
                case "ne":
                    addCondition(ConditionType.notEqual, k, v, clzPath);
                    break;
                case "gt":
                    addCondition(ConditionType.greaterThan, k, v, clzPath);
                    break;
                case "lt":
                    addCondition(ConditionType.lessThan, k, v, clzPath);
                    break;
                case "in":
                    addCondition(ConditionType.in,k, v, clzPath);
                    break;
                default:
                    addCondition(ConditionType.equal, k, v, clzPath);
                    break;
            }
        });
    }


    public int getKind() {
        if (null != page && conditions.size() > 0) {
            return 1;//有分页有条件
        } else if (null != page && conditions.size() == 0) {
            return 2;//有分页无条件
        } else if (null == page && conditions.size() > 0) {
            return 3;//无分页有条件
        } else {
            return 4;//无分页无条件
        }
    }

    public Specification<T> getWhereClause() {
        if (conditions.size() > 0) {
            conditionJson = gson.toJson(conditions);
            Specification specification = new Specification<T>() {
                @Override
                public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    Predicate predicate = cb.conjunction();
                    for (int i = 0; i < conditions.size(); i++) {
                        predicate.getExpressions().add(conditions.get(i).getExpression(root, cb));
                    }
                    List<Order> orderList = new ArrayList<>();
                    orderList.add(cb.desc(root.get("createTime")));
                    query.orderBy(orderList);
                    return predicate;
                }
            };
            return specification;
        } else {
            Specification specification = new Specification<T>() {
                @Override
                public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    Predicate predicate = cb.conjunction();
                    List<Order> orderList = new ArrayList<>();
                    orderList.add(cb.desc(root.get("createTime")));
                    query.orderBy(orderList);
                    return predicate;
                }
            };
            return specification;
        }
    }

//
//	public List<Selection<?>> getSelectField(Root<T> root){
//		String[] fieldArray = fields.split(",");
//
//		List<Selection<? extends Object>> selectionList = new ArrayList<Selection<? extends Object>>();
//
//		for (int i = 0; i < fieldArray.length ; i++) {
//			Selection<? extends Object> selection = root.get(fieldArray[i]);
//			selectionList.add(selection);
//		}
//
//		return selectionList;
//
//	}

    public Pageable getPageable() {
        if (null == page) {
            return null;
        } else {
            Pageable pageable = new PageRequest(getPage()-1, getPageSize());
            return pageable;
        }
    }

    public Pageable getPageable(Sort sort) {
        Pageable pageable = new PageRequest(getPage(), getPageSize(), sort);
        return pageable;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    private void setBool() {
        if (this.page >= totalPages - 1) {
            hasNextPage = false;
        }
        if (this.page <= 0) {
            hasPrevPage = false;
        }
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
        setBool();
    }

    public Integer getPageSize() {
        if (null == pageSize) {
            return 10;
        } else {
            return pageSize;
        }
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Long totalRows) {
        this.totalRows = totalRows;
        if (null == totalPages) {
            long tempTPd = totalRows % getPageSize();
            Integer tempTp = Integer.valueOf((totalRows / getPageSize()) + "");
            if (tempTPd == 0) {
                setTotalPages(tempTp);
            } else {
                setTotalPages(tempTp + 1);
            }
        }
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public boolean isHasPrevPage() {
        return hasPrevPage;
    }

    public void setHasPrevPage(boolean hasPrevPage) {
        this.hasPrevPage = hasPrevPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public String getConditionJson() {
        return conditionJson;
    }

    public void setConditionJson(String conditionJson) {
        this.conditionJson = conditionJson;
        Type listType = new TypeToken<List<T>>() {
        }.getType();
        this.conditions = gson.fromJson(this.conditionJson, listType);
    }

    public List<QueryCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<QueryCondition> conditions) {
        this.conditions = conditions;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }
}
