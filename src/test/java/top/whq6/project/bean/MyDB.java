package top.whq6.project.bean;

public class MyDB {

  private MySQL mysql;

  private Mongo mongo;

  private Redis redis;


  @Override
  public String toString() {
    return "MyDB{" +
        "mysql=" + mysql +
        ", mongo=" + mongo +
        ", redis=" + redis +
        '}';
  }

  public MySQL getMysql() {
    return mysql;
  }

  public void setMysql(MySQL mysql) {
    this.mysql = mysql;
  }

  public Mongo getMongo() {
    return mongo;
  }

  public void setMongo(Mongo mongo) {
    this.mongo = mongo;
  }

  public Redis getRedis() {
    return redis;
  }

  public void setRedis(Redis redis) {
    this.redis = redis;
  }
}
