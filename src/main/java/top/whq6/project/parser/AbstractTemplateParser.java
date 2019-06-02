package top.whq6.project.parser;

import lombok.Getter;
import top.whq6.project.bean.Configuration;
import top.whq6.project.transition.ObjectTemplate;

public abstract class AbstractTemplateParser implements Parser {

  @Getter
  private Configuration configuration;

  public AbstractTemplateParser() {
  }

  public AbstractTemplateParser(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

  public abstract ObjectTemplate getTemplate();

  public abstract boolean parseClassAndCreateTemplate(Object obj);
}
