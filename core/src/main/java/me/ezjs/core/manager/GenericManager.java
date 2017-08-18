package me.ezjs.core.manager;

import me.ezjs.core.model.DAOFilter;
import me.ezjs.core.model.FlipFilter;
import me.ezjs.core.model.Page;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by zero-mac on 16/7/7.
 */
public interface GenericManager<T, ID extends Serializable> {

    /**
     * 根据主键获得指定对象, 默认只能获得没有被删除(包括逻辑删除,物理删除,真实删除)的对象
     *
     * @param id
     * @return
     */
    T get(ID id);

    T get(DAOFilter filter);

    T get(String field, Object val);

    T getFirst(String field, Object val);

    /**
     * 判断指定主键的实体是否存在
     *
     * @param id
     * @return
     */
    boolean exists(ID id);

    /**
     * 保存对象，若改对象没有主键（id=null）则为新增，若已包含主键则为更新
     *
     * @param object
     * @return
     */
    void save(T object);

    void update(T object);

    /**
     * 批量保存,一个保存失败,全部保存失败.
     *
     * @param list
     */
    void saveAll(List<T> list);

    /**
     * 根据过滤条件批量更新.
     *
     * @param t
     * @param filter
     */
    void updateByFilter(T t, DAOFilter filter);

    /**
     * 根据主键删除指定对象, equal to removeLogic
     *
     * @param id
     */
    void remove(ID id);

    void remove(DAOFilter filter);

    /**
     * set using_type to 0
     *
     * @param id
     */
    void removeLogic(ID id);

    /**
     * 逻辑删除 - set using_type = 0
     *
     * @param daoFilter 过滤条件
     */
    void removeLogic(DAOFilter daoFilter) throws IllegalAccessException, InstantiationException;

    /**
     * set using_type to -1
     *
     * @param id
     */
    void removePhysical(ID id);

    /**
     * delete data from db
     *
     * @param id
     */
    void removeReal(ID id);

    /**
     * 物理删除 -- 根据过滤条件
     *
     * @param daoFilter 过滤条件
     */
    void removeReal(DAOFilter daoFilter);

    /**
     * 查询 = listUsingByCreateTime
     *
     * @param filter
     * @return
     */
    List<T> list(DAOFilter filter);

    /**
     * 默认按照createTime进行排序的查询
     *
     * @param filter
     * @return
     */
    List<T> listUsingByCreateTime(DAOFilter filter);

    /**
     * 无默认排序条件的查询
     *
     * @param filter
     * @return
     */
    List<T> listUsing(DAOFilter filter);

    List<T> listByCustomFilter(DAOFilter filter);

    long count(DAOFilter filter);

    long countUsing(DAOFilter filter);

    long countByCustomFilter(DAOFilter filter);

    /**
     * 分页查询
     *
     * @param filter
     * @return
     */
    List<T> flipList(FlipFilter filter);

    Page flipListInPage(FlipFilter filter);

    Page flipUsingInPage(FlipFilter filter);

    byte[] serialization(Object obj) throws IOException;

    Object deserialization(byte[] chunk) throws IOException, ClassNotFoundException;

}
