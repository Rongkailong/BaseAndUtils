package com.rkl.common_library.db;
import android.database.sqlite.SQLiteDatabase;
import com.litesuits.orm.BuildConfig;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.SQLiteHelper;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.orhanobut.logger.Logger;
import com.rkl.common_library.base.BaseApplication;
import com.rkl.common_library.constant.ConfigConstant;
import com.rkl.common_library.util.EmptyUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rkl on 2018/1/12.
 * 封装数据库,对外提供基础操作(增删查、映射列表)
 * LiteOrm支持级联查询,对象关系映射为数据库关系
 */

public enum CommonOrm implements SQLiteHelper.OnUpdateListener {
    INSTANCE;
    private LiteOrm mLiteOrm;

    CommonOrm() {
        DataBaseConfig config = new DataBaseConfig(BaseApplication.getmAppContext());
        config.dbName = ConfigConstant.DB_NAME_PATH + File.separator + ConfigConstant.DB_NAME;
        config.dbVersion = 1;
        config.onUpdateListener = this;
        config.debugged = BuildConfig.DEBUG;
        // newCascadeInstance 支持级联操作
        mLiteOrm = LiteOrm.newCascadeInstance(config);
    }

    @Override
    public void onUpdate(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public LiteOrm getmLiteOrm() {
        return mLiteOrm;
    }

    /**
     * save()方法属于保存，若存在对应id的数据,旧的数据将会被新数据覆盖
     * 否则将会新建一列数据
     */
    public void save(Object o) {
        if (o == null) {
            return;
        }

        mLiteOrm.save(o);
    }

    public <T> void save(List<T> collection) {
        if (EmptyUtils.isEmpty(collection)) {
            Logger.e("缓存数据失败！ 请求数据为空");
            return;
        }

        mLiteOrm.save(collection);
        Logger.e("缓存本地数据成功！ ");

    }

    /**
     * insert()方法属于新增，若存在对应id的数据,旧的数据不会被新数据覆盖,日志会抛出SQLiteConstraintException异常
     * 否则将会新建一列数据
     */
    public <T> void insert(Object o) {
        if (o == null) {
            return;
        }
        mLiteOrm.insert(o);
    }

    public <T> void insert(List<T> collection) {
        if (EmptyUtils.isEmpty(collection)) {
            return;
        }
        mLiteOrm.insert(collection);
    }

    /**
     * delete()方法删除操作,通过对象或者对应的model类文件既可删除对应的数据表
     * deleteAll()方法删除整个数据库,然后重新创建---->重置数据库
     */
    public <T> void delete(Object o) {
        if (o == null) {
            return;
        }
        mLiteOrm.delete(o);
    }

    public <T> void delete(Class<T> tClass) {
        if (tClass == null) {
            return;
        }
        mLiteOrm.delete(tClass);
    }

    /**
     * 按条件删除
     *
     * @param tClass
     * @param field   字段名
     * @param strings 值
     * @param <T>
     */
    public <T> void delete(Class<T> tClass, String field, Object[] strings) {
        if (tClass == null) {
            return;
        }
        mLiteOrm.delete(new WhereBuilder(tClass).where(field + " LIKE ?", strings));
    }

    public void deleteAll() {
        //删除数据库
        mLiteOrm.deleteDatabase();
        //数据库重建
        mLiteOrm.openOrCreateDatabase();
    }

    /**
     * 查询行数
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> long queryCount(Class<T> tClass) {
        if (tClass == null) {
            return 0;
        }
        return mLiteOrm.queryCount(tClass);
    }

    /**
     * 查询集合   按降序的字段  去重
     *
     * @param tClass
     * @param desc
     * @param <T>
     * @return
     */
    public <T> List<T> queryAllDesc(Class<T> tClass, String desc) {
        if (tClass == null) {
            return null;
        }
        return mLiteOrm.<T>query(new QueryBuilder(tClass).appendOrderDescBy(desc).distinct(true));
    }

    /**
     * queryById()方法是查询指定id下的对象数据
     *
     * @param id 指定查询的id
     * @param t  指定查询的model实例类（注意：必须是实例化后的对象 例如可传一个new model() ）
     * @return 所查询的对象数据，若未查询成功，返回null
     */
    public <T> T queryById(long id, T t) {
        return (T) (t == null ? null : mLiteOrm.queryById(id, t.getClass()));
    }

    /**
     * 根据QueryBuilder查询
     */
    public <T> List<T> queryByWhere(QueryBuilder<T> queryBuilder) {
        if (mLiteOrm.query(queryBuilder) == null) {
            return new ArrayList<>();
        }
        return mLiteOrm.query(queryBuilder);
    }

    /**
     * 根据关键字在数据表p普通查询
     *
     * @param tClass 数据表对应的model类
     * @param key    关键字段
     * @param strKey 关键字符
     */
    public <T> List<T> queryByKey(Class<T> tClass, String key, String strKey) {
        if (tClass == null) {
            return new ArrayList<>();
        }
        QueryBuilder<T> qb = new QueryBuilder<T>(tClass)
                .where(key + "=?", strKey);
        return queryByWhere(qb);
    }

    /**
     * 根据关键字在数据表模糊查询
     *
     * @param tClass 数据表对应的model类
     * @param key    关键字段
     * @param strKey 关键字符
     */
    public <T> List<T> queryFuzzyByKey(Class<T> tClass, String key, String strKey) {
        if (tClass == null) {
            return new ArrayList<>();
        }
        String str_key = "%" + strKey + "%";
        QueryBuilder<T> qb = new QueryBuilder<T>(tClass)
                .where(key + " LIKE ?", str_key);
        return queryByWhere(qb);
    }

    /**
     * 根据AND关系查询数据表
     * 例如查询Person表的name为张三并且age为18的人员集合
     * * @param tClass 数据表对应的model类
     *
     * @param key1    关键字段1
     * @param strKey1 关键字符1
     * @param key2    关键字段2
     * @param strKey2 关键字符2
     */
    public <T> List<T> queryAndByKey(Class<T> tClass, String key1, String key2, String strKey1, String strKey2) {
        if (tClass == null) {
            return new ArrayList<>();
        }
        QueryBuilder<T> qb = new QueryBuilder<T>(tClass)
                .whereEquals(key1, strKey1)
                .whereAppendAnd()
                .whereEquals(key2, strKey2);
        return queryByWhere(qb);
    }

    /**
     * 根据OR关系查询数据表
     * 例如查询Person表的name为张三或者age为18的人员集合
     * * @param tClass 数据表对应的model类
     *
     * @param key1    关键字段1
     * @param strKey1 关键字符1
     * @param key2    关键字段2
     * @param strKey2 关键字符2
     */
    public <T> List<T> queryORByKey(Class<T> tClass, String key1, String key2, String strKey1, String strKey2) {
        if (tClass == null) {
            return new ArrayList<>();
        }
        QueryBuilder<T> qb = new QueryBuilder<T>(tClass)
                .whereEquals(key1, strKey1)
                .whereAppendOr()
                .whereEquals(key2, strKey2);
        return queryByWhere(qb);
    }

    /**
     * 根据关键字在数据表查询不包含此关键字的数据列表
     *
     * @param tClass 数据表对应的model类
     * @param key    关键字段
     * @param strKey 关键字符
     */
    public <T> List<T> queryNoEqualsByKey(Class<T> tClass, String key, String strKey) {
        if (tClass == null) {
            return new ArrayList<>();
        }
        QueryBuilder<T> qb = new QueryBuilder<T>(tClass)
                .whereNoEquals(key, strKey);
        return queryByWhere(qb);
    }

    /**
     * 查询数据表所有的数据
     */
    public <T> List<T> queryAll(Class<T> tClass) {
        if (tClass == null) {
            return new ArrayList<>();
        }
        return mLiteOrm.query(tClass);
    }

    /**
     * 查看Model内部的映射关系
     *
     * @param ct ct类所对应的数据表作为主表
     * @param cm cm类所对应的数据表作为映射表
     */
    public <T, M> List<M> getMappingList(Class<T> ct, Class<M> cm) {
        if (ct == null || cm == null) {
            return new ArrayList<>();
        }
        List<T> cts = mLiteOrm.query(ct);
        List<M> cms = mLiteOrm.query(cm);
        mLiteOrm.mapping(cts, cms);
        return cms;
    }

    /**
     * 删除整个数据库
     */
    public void deleteDatabase(){
        mLiteOrm.deleteDatabase();
    }
}

