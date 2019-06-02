package top.whq6.project;

import lombok.Getter;
import lombok.Setter;
import top.whq6.project.bean.Configuration;
import top.whq6.project.parser.DefaultObjectParser;

public class JSONTool {

  @Getter
  @Setter
  private Configuration configuration;

  private DefaultObjectParser parser;

  public JSONTool(Configuration configuration) {
    this.parser = new DefaultObjectParser(configuration);
    this.configuration = configuration;
  }

  public JSONTool() {
    this(new Configuration());
  }

  public String toJSONString(Object obj) {

    return parser.toJSONString(obj);
  }


}
