package common.model;



import java.util.List;
import java.util.logging.Filter;

/**
 * .*.@author.zhangwensen
 * .*.@date.2019/12/5
 * .
 */

public class QueryParams {

    private String id;
    private Integer page = 1;                    //当前页数
    private Integer rows = 20;                    //每页显示记录数
    private Integer total = -1;                    //总记录数
    private Integer totalPage = -1;                //总页数
    private Integer startRow = 0;                //当前页在数据库中的起始行
    private Integer endRow = 0;                //当前页在数据库中的截止行
    private Integer skip = 0;                //忽略前面记录数
    private String searchText;                    //查询字段值
    private String convertSearchText;                    //查询字段值 简繁体转换
    private String searchType;                    //查询字段
    private String type;                         //类型
    private boolean smartFlag = false;             //是否智能搜索
    private String orderBy;                        //排序顺序
    private String orderText;                    //排序字段
    private List<Filter> filters;                //查询条件
    private String starts;
    private String ends;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getStartRow() {
        return startRow;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }

    public Integer getEndRow() {
        return endRow;
    }

    public void setEndRow(Integer endRow) {
        this.endRow = endRow;
    }

    public Integer getSkip() {
        return skip;
    }

    public void setSkip(Integer skip) {
        this.skip = skip;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getConvertSearchText() {
        return convertSearchText;
    }

    public void setConvertSearchText(String convertSearchText) {
        this.convertSearchText = convertSearchText;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSmartFlag() {
        return smartFlag;
    }

    public void setSmartFlag(boolean smartFlag) {
        this.smartFlag = smartFlag;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderText() {
        return orderText;
    }

    public void setOrderText(String orderText) {
        this.orderText = orderText;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public String getStarts() {
        return starts;
    }

    public void setStarts(String starts) {
        this.starts = starts;
    }

    public String getEnds() {
        return ends;
    }

    public void setEnds(String ends) {
        this.ends = ends;
    }
}
