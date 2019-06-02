package top.whq6.project.parser;

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import top.whq6.project.bean.Configuration;
import top.whq6.project.bean.TypeHandlerEnum;
import top.whq6.project.handler.ConvertTypeForJSONHandler;
import top.whq6.project.transition.ObjectTemplate;

@Slf4j
public class DefaultObjectParser extends AbstractObjectParser {

  private static final String EMPTY_JSON = "{}";

  private TemplateParser templateParser;

  public DefaultObjectParser() {
    this(null);
  }

  public DefaultObjectParser(Configuration configuration) {
    super(configuration);
    templateParser = new TemplateParser(configuration);
  }

  @Override
  public String toJSONString(Object obj) {

    if (templateParser.parseClassAndCreateTemplate(obj)) {
      return EMPTY_JSON;
    }

    ObjectTemplate template = templateParser.getTemplate();

    ArrayList<String> baseFieldList = analyzeObject2BaseList(obj, template);
    // analyzeObject2DateList(obj, template);
    // analyzeObject2EnumList(obj, template);
    // analyzeObject2ComplexList(obj, template);
    // analyzeObject2ArrayTypeList(obj, template);

    return "";
  }


  private ArrayList<String> analyzeObject2BaseList(Object obj, ObjectTemplate template) {

    Configuration configuration = this.getConfiguration();
    if (configuration == null) {
      log.error("Configuration field is not init");
      throw new NullPointerException();
    }

    ConvertTypeForJSONHandler baseHandler =
        this.getConfiguration().getHandlerByName(TypeHandlerEnum.BASE);
    ConvertTypeForJSONHandler booleanHandler =
        this.getConfiguration().getHandlerByName(TypeHandlerEnum.BOOLEAN);

    Set<String> baseTypeValues = template.getBaseTypeValues();
    Set<String> valueTypeNames = template.getValueTypeNames();
    ImmutableMap<String, Class<?>> allFieldCls = template.getAllFieldCls();

    baseTypeValues.addAll(valueTypeNames);

    Set<String> booleanType = baseTypeValues.stream()
        .filter(v -> allFieldCls.get(v) == boolean.class || allFieldCls.get(v) == Boolean.class)
        .collect(Collectors.toSet());

    baseTypeValues.removeAll(booleanType);

    if (log.isDebugEnabled()) {
      log.debug("Base type: [{}]", baseTypeValues);
      log.debug("Boolean type: [{}]", booleanType);
    }

    ArrayList<String> list = streamFilter(obj, template, baseHandler, baseTypeValues);
    ArrayList<String> booleanList = streamFilter(obj, template, booleanHandler, booleanType);
    list.addAll(booleanList);
    return list;
  }

  private ArrayList<String> streamFilter(
      Object obj,
      ObjectTemplate template,
      ConvertTypeForJSONHandler handler,
      Set<String> typeValues) {

    ArrayList<String> list = new ArrayList<>(typeValues.size());

    typeValues.forEach(
        v -> {
          try {
            list.add(handler.toJSONString(obj, v, template));
          } catch (Exception e) {
            log.error("Field [{}] to json string failed: {}", v, e.getMessage());
          }
        });

    if (log.isDebugEnabled()) {
      log.debug("[{}] handler type json string list: {}", handler.getClass().getName(), list);
    }

    return list;
  }
}
