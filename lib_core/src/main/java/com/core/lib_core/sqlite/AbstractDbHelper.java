package com.core.lib_core.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.core.lib_core.CoreApplication;
import com.core.lib_core.sqlite.agent.LogAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库SQLiteOpenHelper基础子类<br>
 * <p>
 * 注意子类数据库需要初始化 db<br>
 *
 * @author lichengfeng<br>
 * 2014年7月15日下午6:43:04
 */
public abstract class AbstractDbHelper extends SQLiteOpenHelper {

    protected SQLiteDatabase db;

    /**
     * @param databaseName
     * @param version
     */
    public AbstractDbHelper(String databaseName, int version) {
        // 该操作不会立即生成db文件
        super(CoreApplication.getContext(), databaseName, null, version);
        // * 当调用 getWritableDatabase的时候 才开始创建数据库(db文件)
        // synchronized (this) {
        // // 打开数据库
        // this.db = getWritableDatabase();
        // }
    }

    /**
     * 返回创建数据表的sql
     *
     * @return
     */
    protected abstract String getCreateSql();

    /**
     * 返回版本升级的sql
     *
     * @param oldVersion
     * @param newVersion
     * @return
     */
    protected abstract String getUpgradeSql(int oldVersion, int newVersion);

    /**
     * 目前不接受param为null的情况
     *
     * @param sql    格式: select * from table where name = ? and sex = ?;
     * @param params 格式: coffee 1
     * @return
     */
    public String parse(String sql, Object... params) {
        if (params.length == 0) {
            return sql;
        }
        StringBuilder sb = new StringBuilder(sql);
        int index = -1;
        for (Object param : params) {
            index = sb.indexOf("?", index);
            if (param instanceof String) {
                sb.replace(index, index + 1, "'" + param + "'");
            } else if (param instanceof Integer || param instanceof Long || param instanceof Double) {
                sb.replace(index, index + 1, String.valueOf(param));
            } else {
                continue;
            }
            index += param.toString().length();
        }
        return sb.toString();
    }

    /**
     * {@link #getWritableDatabase()} Create and/or open a database that will be used for reading and writing. 数据库创建， onCreate执行， 注意该方法只执行一次
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db; // 该代码仅执行一次
        String sqls = getCreateSql();
        for (String sql : sqls.split(";")) {
            try {
                db.execSQL(sql);
            } catch (Exception e) {
                LogAgent.w("DB_onCreate", sql, e);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.db = db;
        String sqls = getUpgradeSql(oldVersion, newVersion);
        for (String sql : sqls.split(";")) {
            try {
                db.execSQL(sql);
            } catch (Exception e) {
                LogAgent.w("DB_onCreate", sql, e);
            }
        }
    }

    // /////// 以下是扩展方法 \\\\\\\\\\\\\\\\\\

    /**
     * 按照执行的beanClass创建数据库 如果数据表存且数据为空则删除，否则抛异常
     */
    public synchronized void reCreateTable(Class<?> beanClass) {
        try {
            // 删除数据库
            String sql = "drop table " + TSqliteUtils.getTableName(beanClass);
            db.execSQL(sql);
            // 重新创建数据库
            sql = TSqliteUtils.generateTableSql(beanClass);
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按照执行的beanClass创建数据库 如果数据表存且数据为空则删除，否则抛异常
     */
    synchronized void createTable(Class<?> beanClass) {
        try {
            String sql = TSqliteUtils.generateTableSql(beanClass);
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public synchronized void dropTable(String tableName) {
        try {
            db.execSQL("drop table " + tableName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增记录
     */
    public synchronized <T> void insert(T bean) {
        String sql = null;
        try {
            sql = TSqliteUtils.getInsertSql(bean);
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized <T> int insert(T bean, boolean returnPk) {
        String sql = null;
        try {
            sql = TSqliteUtils.getInsertSql(bean);
            db.execSQL(sql);
            sql = "select max(_id) from " + TSqliteUtils.getTableName(bean.getClass());
            int pk = queryForColumn(sql, Integer.class);
            return pk;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 删除记录
     *
     * @param beanClass ;
     * @parem id : 如果该参数不为空则 只取一个值
     */
    public synchronized <T> void delete(Class<T> beanClass, Integer... id) {
        String tableName = TSqliteUtils.getTableName(beanClass);
        try {
            if (id.length > 0) {
                db.delete(tableName, "_id=?", new String[]{String.valueOf(id[0])});
            } else {// 删除所有的行
                db.delete(tableName, null, new String[]{});
            }
        } catch (Exception e) {
            e.printStackTrace();
            // db.execSQL("delete from t_address WHERE _id= 226");
        }
    }

    /**
     * 更新记录
     */
    public synchronized <T> void update(T bean) {
        try {
            String sql = TSqliteUtils.getUpdateSql(bean);
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 查询记录
     */
    public <T> T queryForObject(Class<T> beanClass, String[] columns, String condition) {
        Cursor c = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ");
            if (columns != null && columns.length > 0) {
                for (int i = 0; i < columns.length; i++) {
                    sql.append(columns[i]);
                    if (i + 1 < columns.length) {
                        sql.append(",");
                    }
                }
            } else {
                sql.append("*");
            }
            sql.append(" from " + TSqliteUtils.getTableName(beanClass));
            if (condition != null && !"true".equals(condition)) {
                sql.append(" where ").append(condition);
            }

            c = db.rawQuery(sql.toString(), new String[]{});
            List<T> lst = TSqliteUtils.processResultSetToList(c, beanClass);
            if (lst.size() > 0) {
                return lst.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return null;
    }

    /**
     * @param beanClass
     * @param sql
     * @return
     */
    public <T> List<T> queryForList(Class<T> beanClass, String sql) {
        List<T> lst = new ArrayList<T>();
        Cursor c = null;
        try {
            LogAgent.d("sql:query", sql.toString());
            c = db.rawQuery(sql.toString(), new String[]{});
            lst = TSqliteUtils.processResultSetToList(c, beanClass);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }
        return lst;
    }

    /**
     * 返回列表<br>
     * 通用公式 "select * from FlyCrocodile where "+条件+" order by "+排序+" limit " +要显示多少条记录+" offset "+跳过多少条记录 <br>
     *
     * @param orderBy 传入值: column desc [limit 0,10]
     * @return ： 如果无记录，则返回一个size==0的list
     */
    public <T> List<T> queryForList(Class<T> beanClass, String[] columns, String condition, String orderBy) {
        List<T> lst = new ArrayList<T>();
        Cursor c = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ");
            if (columns != null && columns.length > 0) {
                for (int i = 0; i < columns.length; i++) {
                    sql.append(columns[i]);
                    if (i + 1 < columns.length) {
                        sql.append(",");
                    }
                }
            } else {
                sql.append("*");
            }
            sql.append(" from ").append(TSqliteUtils.getTableName(beanClass));
            if (condition != null && !"true".equals(condition)) {
                sql.append(" where ").append(condition);
            }
            if (orderBy != null) {
                sql.append(" order by ").append(orderBy);
            }
            LogAgent.d("sql:query", sql.toString());
            c = db.rawQuery(sql.toString(), new String[]{});
            lst = TSqliteUtils.processResultSetToList(c, beanClass);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }
        return lst;
    }

    /**
     * 单列查询
     *
     * @return : 如果无记录， 则返回 null 而不是 字符串"null"
     */
    @SuppressWarnings("unchecked")
    public <T> T queryForColumn(String sql, Class<T> primtiveType) {
        T colValue = null;
        try {
            String value = null;
            Cursor c = db.rawQuery(sql, new String[]{});
            if (c.moveToNext()) {
                value = c.getString(0);
            }
            c.close();//
            if ("null".equals(value) || value == null) {
                return null;
            } else if (primtiveType == Integer.class || primtiveType == int.class) {
                colValue = (T) Integer.valueOf(value);
            } else if (primtiveType == Long.class || primtiveType == long.class) {
                colValue = (T) Long.valueOf(value);
            } else {
                colValue = (T) value;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return colValue;
    }

    public void execSQL(String sql) {
        try {
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭数据库
     */
    public void close() {
        if (this.db != null) {
            this.db.close();
        }
    }

}
