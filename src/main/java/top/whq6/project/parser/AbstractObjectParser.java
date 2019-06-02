package top.whq6.project.parser;

import lombok.Getter;
import top.whq6.project.bean.Configuration;

public abstract class AbstractObjectParser implements ObjectParser {

  @Getter
  private Configuration configuration;

  public AbstractObjectParser() {
  }

  public AbstractObjectParser(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
}
