package com.alexie.demo.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "search_keyword")
public class SearchKeyword extends BaseEntityNew {
    /**
     * 自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关键词类型见枚举 SearchKeywordTypeEnum
     */
    private String type;

    /**
     * 关键词
     */
    private String keyword;

    /**
     * 使用状态类型见枚举 SearchUseStatusEnum
     */
    @Column(name = "use_status")
    private String useStatus;

    /**
     * 直接调用次数
     */
    @Column(name = "direct_call")
    private Long directCall;

    /**
     * 间接调用次数
     */
    @Column(name = "indirect_call")
    private Long indirectCall;

    /**
     * 调用总次数
     */
    @Column(name = "total_call")
    private Long totalCall;

    /**
     * 同义词个数
     */
    @Column(name = "synonym_num")
    private Integer synonymNum;

    /**
     * 同义词 关键词id 以，分割
     */
    private String synonym;

    /**
     * 关键词类型 KEYWORD STOP_WORDS
     */
    private String category;

    /**
     * 创建日期时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新日期时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 逻辑删除
     */
    private Boolean status;

    /**
     * 创建者
     */
    @Column(name = "create_user")
    private Long createUser;

    /**
     * 更新者
     */
    @Column(name = "update_user")
    private Long updateUser;

    /**
     * 获取自增主键
     *
     * @return id - 自增主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置自增主键
     *
     * @param id 自增主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取关键词类型见枚举 SearchKeywordTypeEnum
     *
     * @return type - 关键词类型见枚举 SearchKeywordTypeEnum
     */
    public String getType() {
        return type;
    }

    /**
     * 设置关键词类型见枚举 SearchKeywordTypeEnum
     *
     * @param type 关键词类型见枚举 SearchKeywordTypeEnum
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取关键词
     *
     * @return keyword - 关键词
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * 设置关键词
     *
     * @param keyword 关键词
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * 获取使用状态类型见枚举 SearchUseStatusEnum
     *
     * @return use_status - 使用状态类型见枚举 SearchUseStatusEnum
     */
    public String getUseStatus() {
        return useStatus;
    }

    /**
     * 设置使用状态类型见枚举 SearchUseStatusEnum
     *
     * @param useStatus 使用状态类型见枚举 SearchUseStatusEnum
     */
    public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
    }

    /**
     * 获取直接调用次数
     *
     * @return direct_call - 直接调用次数
     */
    public Long getDirectCall() {
        return directCall;
    }

    /**
     * 设置直接调用次数
     *
     * @param directCall 直接调用次数
     */
    public void setDirectCall(Long directCall) {
        this.directCall = directCall;
    }

    /**
     * 获取间接调用次数
     *
     * @return indirect_call - 间接调用次数
     */
    public Long getIndirectCall() {
        return indirectCall;
    }

    /**
     * 设置间接调用次数
     *
     * @param indirectCall 间接调用次数
     */
    public void setIndirectCall(Long indirectCall) {
        this.indirectCall = indirectCall;
    }

    /**
     * 获取调用总次数
     *
     * @return total_call - 调用总次数
     */
    public Long getTotalCall() {
        return totalCall;
    }

    /**
     * 设置调用总次数
     *
     * @param totalCall 调用总次数
     */
    public void setTotalCall(Long totalCall) {
        this.totalCall = totalCall;
    }

    /**
     * 获取同义词个数
     *
     * @return synonym_num - 同义词个数
     */
    public Integer getSynonymNum() {
        return synonymNum;
    }

    /**
     * 设置同义词个数
     *
     * @param synonymNum 同义词个数
     */
    public void setSynonymNum(Integer synonymNum) {
        this.synonymNum = synonymNum;
    }

    /**
     * 获取同义词 关键词id 以，分割
     *
     * @return synonym - 同义词 关键词id 以，分割
     */
    public String getSynonym() {
        return synonym;
    }

    /**
     * 设置同义词 关键词id 以，分割
     *
     * @param synonym 同义词 关键词id 以，分割
     */
    public void setSynonym(String synonym) {
        this.synonym = synonym;
    }

    /**
     * 获取关键词类型 KEYWORD STOP_WORDS
     *
     * @return category - 关键词类型 KEYWORD STOP_WORDS
     */
    public String getCategory() {
        return category;
    }

    /**
     * 设置关键词类型 KEYWORD STOP_WORDS
     *
     * @param category 关键词类型 KEYWORD STOP_WORDS
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * 获取创建日期时间
     *
     * @return create_time - 创建日期时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建日期时间
     *
     * @param createTime 创建日期时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新日期时间
     *
     * @return update_time - 更新日期时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新日期时间
     *
     * @param updateTime 更新日期时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取逻辑删除
     *
     * @return status - 逻辑删除
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * 设置逻辑删除
     *
     * @param status 逻辑删除
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * 获取创建者
     *
     * @return create_user - 创建者
     */
    public Long getCreateUser() {
        return createUser;
    }

    /**
     * 设置创建者
     *
     * @param createUser 创建者
     */
    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    /**
     * 获取更新者
     *
     * @return update_user - 更新者
     */
    public Long getUpdateUser() {
        return updateUser;
    }

    /**
     * 设置更新者
     *
     * @param updateUser 更新者
     */
    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }
}