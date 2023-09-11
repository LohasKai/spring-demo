package com.mhz.demo.utils;

import com.alibaba.druid.pool.DruidPooledConnection;

import java.sql.Connection;
import java.sql.SQLException;


public class ConnectionUtils {

    private static ConnectionUtils connectionUtils = new ConnectionUtils();

    public ConnectionUtils() {
    }

    public static ConnectionUtils getInstance(){
        return connectionUtils;
    }


    private ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();

    /**
     * 判断当前线程是否已经获取连接，如果没有连接需要从线程池新建连接
     * @return
     */
   public  Connection getCurrentThreadConn() throws SQLException {
       Connection connection = connectionThreadLocal.get();
       if (null == connection){
           DruidPooledConnection con = DruidUtils.getInstance().getConnection();
           connectionThreadLocal.set(con);
       }
       return connection;
   }

}
