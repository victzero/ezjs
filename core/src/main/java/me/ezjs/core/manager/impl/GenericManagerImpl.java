package me.ezjs.core.manager.impl;

import me.ezjs.core.manager.GenericManager;
import me.ezjs.core.mapper.GenericMapper;
import me.ezjs.core.model.*;
import org.slf4j.Logger;
import org.springframework.util.Assert;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zero-mac on 16/7/7.
 */
public class GenericManagerImpl<T extends RootObject, ID extends Serializable> implements GenericManager<T, ID> {

    protected GenericMapper<T, ID> mapper;
    protected Class<?> clazz;

    public GenericManagerImpl(GenericMapper<T, ID> genericDao, Class<?> clazz) {
        this.mapper = genericDao;
        this.clazz = clazz;
    }

    @Override
    public T get(ID id) {
        T obj = mapper.getById(clazz, id);
        if (obj != null && obj.getUsingType() != null && UsingType.in_using.getCode() == obj.getUsingType()) {
            return obj;
        }
        return null;
    }

    @Override
    public T get(DAOFilter filter) {
        filter.addSearch(UsingType.in_using.getCode(), Operate.EQUAL, "usingType");
        T t = mapper.get(filter);
        return t;
    }

    @Override
    public T get(String field, Object val) {
        DAOFilter filter = new DAOFilter(clazz);
        filter.addSearch(val, Operate.EQUAL, field);
        filter.addSearch(UsingType.in_using.getCode(), Operate.EQUAL, "usingType");
        return mapper.get(filter);
    }

    @Override
    public T getFirst(String field, Object val) {
        T obj = mapper.getFirst(clazz, field, val.toString());
        if (obj != null && obj.getUsingType() != null && UsingType.in_using.getCode() == obj.getUsingType()) {
            return obj;
        }
        return null;
    }

    @Override
    public boolean exists(ID id) {
        return get(id) != null;
    }

    @Override
    public void save(T object) {
        if (object.getId() == null) {
            mapper.create(object);
        } else {
            mapper.update(object);
        }
    }

    @Override
    public void update(T object) {
        Assert.notNull(object.getId());
        mapper.update(object);
    }

    @Override
    public void saveAll(List<T> list) {
        for (T item :
                list) {
            save(item);
        }
    }

    @Override
    public void updateByFilter(T t, DAOFilter filter) {
        mapper.updateByFilter(t, filter);
    }

    @Override
    public void remove(ID id) {
        removeLogic(id);
    }

    @Override
    public void remove(DAOFilter filter) {
        removeLogic(filter);
    }

    @Override
    public void removeLogic(ID id) {
        T obj = mapper.getById(clazz, id);
        if (obj == null) {
            return;
        }
        obj.setUsingType(UsingType.logic_delete.getCode());
        mapper.update(obj);
    }

    @Override
    public void removeLogic(DAOFilter filter){
        try {
            T obj = (T) clazz.newInstance();
            obj.setUsingType(UsingType.logic_delete.getCode());
            mapper.updateByFilter(obj, filter);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removePhysical(ID id) {
        T obj = mapper.getById(clazz, id);
        if (obj == null) {
            return;
        }
        obj.setUsingType(UsingType.physical_delete.getCode());
        mapper.update(obj);
    }

    @Override
    public void removeReal(ID id) {
        mapper.remove(clazz, id);
    }

    @Override
    public void removeReal(DAOFilter filter) {
        filter.addSearch(UsingType.logic_delete.getCode(), Operate.EQUAL, "using_type");
        mapper.removeRealByFilter(filter);
    }

    @Override
    public List<T> list(DAOFilter filter) {
        return listUsingByCreateTime(filter);
    }

    @Override
    public List<T> listUsingByCreateTime(DAOFilter filter) {
        filter.addSearch(UsingType.in_using.getCode(), Operate.EQUAL, "usingType");
        //mysql 5.7中order_by的排序字段要求必须在select语句中存在(select distinct id 时则不包含createTime)
        // 有两个解决办法: 1. 取消createTime排序条件 , 2. 取消distinct关键字
        filter.addSort("createTime", true);
        return listByCustomFilter(filter);
    }

    @Override
    public List<T> listUsing(DAOFilter filter) {
        filter.addSearch(UsingType.in_using.getCode(), Operate.EQUAL, "usingType");
        return listByCustomFilter(filter);
    }

    @Override
    public List<T> listByCustomFilter(DAOFilter filter) {
        return mapper.list(filter);
    }

    @Override
    public long count(DAOFilter filter) {
        return countUsing(filter);
    }

    @Override
    public long countUsing(DAOFilter filter) {
        filter.addSearch(UsingType.in_using.getCode(), Operate.EQUAL, "usingType");
        return mapper.count(filter);
    }

    @Override
    public long countByCustomFilter(DAOFilter filter) {
        return mapper.count(filter);
    }

    @Override
    public List<T> flipList(FlipFilter filter) {
        return mapper.flip(filter);
    }

    @Override
    public Page flipListInPage(FlipFilter filter) {
        long count = mapper.count(filter.deepClone());
        if (count == 0) {
            return new Page(filter, 0, new ArrayList());
        }

        List<T> list = mapper.flip(filter);
        return new Page(filter, count, list);
    }

    @Override
    public Page flipUsingInPage(FlipFilter filter) {
        filter.addSearch(UsingType.in_using.getCode(), Operate.EQUAL, "usingType");
        filter.addSort("createTime", true);
        FlipFilter filter1 = filter.deepClone();
        long count = mapper.count(filter1);
        if (count == 0) {
            return new Page(filter, 0, new ArrayList());
        }

        List<T> list = mapper.flip(filter);
        return new Page(filter, count, list);
    }

    protected void printException2Log(Exception e, Logger logger) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        e.printStackTrace(new PrintWriter(buf, true));
        String expMessage = buf.toString();
        logger.error(expMessage);
        try {
            buf.close();
        } catch (IOException e1) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] serialization(Object obj) throws IOException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteArray);
        out.writeObject(obj);
        byte[] bytes = byteArray.toByteArray();
        return bytes;
    }

    @Override
    public Object deserialization(byte[] chunk) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArray = new ByteArrayInputStream(chunk);
        ObjectInputStream in = new ObjectInputStream(byteArray);
        Object obj = in.readObject();
        return obj;
    }

    protected boolean hasId(RootObject obj) {
        if (obj.getId() == null) {
            return false;
        }
        return true;
    }
}
