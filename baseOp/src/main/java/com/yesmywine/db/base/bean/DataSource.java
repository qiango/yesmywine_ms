package com.yesmywine.db.base.bean;


/**
 * Created by WANG, RUIQING on 12/8/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class DataSource {

    private String userName;
    private String password;
    private String host;
    private Integer port;
    private DatabaseType databaseType;
    private String schema;
    private String url;
    private String driver;

    // redis
    private boolean useRedis = false;
    private Integer expireSecond;
    private String redisHost;
    private Integer redisPort = 6379;


    public DataSource() {
    }


    public DataSource(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public DataSource(DatabaseType databaseType, String host, Integer port) {
        this.host = host;
        this.port = port;
        this.databaseType = databaseType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getUrl() {

        switch (databaseType) {
            case mysql:
                url = DefaultData.getUrlHead(DatabaseType.mysql) + host;
                if (null != port) {
                    url += ":" + port;
                }
                if (null != schema) {
                    url += "/" + schema;
                }
                break;
        }

        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriver() {
        this.driver = DefaultData.getDriver(DatabaseType.mysql);
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public boolean isUseRedis() {
        return useRedis;
    }

    public void setUseRedis(boolean useRedis) {
        this.useRedis = useRedis;
    }

    public Integer getExpireSecond() {
        if (null == this.expireSecond) {
            return 3600*24*7;
        } else {
            return expireSecond;
        }
    }

    public void setExpireSecond(Integer expireSecond) {
        this.expireSecond = expireSecond;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public Integer getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(Integer redisPort) {
        this.redisPort = redisPort;
    }
}
