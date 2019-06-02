package top.whq6.project.transition;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.whq6.project.bean.ClassBeanInfo;
import top.whq6.project.bean.Configuration;

@Getter
@Setter
@NoArgsConstructor
public class ObjectTemplate implements Cloneable {

  private String className;

  private ClassBeanInfo beanInfo;

  private Configuration configuration;
  /**
   * Key is field name, value is real object field value.
   */
  private Set<String> baseTypeValues;

  /**
   * To save static field value.
   */
  private Set<String> staticModifierValues;

  /**
   * Key is field name, value is #{@link ObjectTemplate} object.
   */
  private HashMap<String, ObjectTemplate> complexTypeValues;

  /**
   * To save enum field.
   */
  private HashMap<String, String> enumNames;

  private Set<String> notSerialized;

  private Set<String> notDeserialize;

  private Set<String> dateNames;

  private Set<String> valueTypeNames;

  private ImmutableMap<String, String> name2Alias;

  private ImmutableMap<String, Class<?>> allFieldCls;

  public ObjectTemplate(String className, ClassBeanInfo beanInfo,
      Configuration configuration) {
    this();
    this.className = className;
    this.beanInfo = beanInfo;
    this.configuration = configuration;

    this.name2Alias = beanInfo.getName2Alias();
    this.allFieldCls = beanInfo.getAllFieldCls();
    this.notDeserialize = beanInfo.getNotDeserialize();
    this.notSerialized = beanInfo.getNotSerialized();

    // init
    baseTypeValues = new HashSet<>();
    complexTypeValues = new HashMap<>();
    staticModifierValues = new HashSet<>();
    dateNames = new HashSet<>();
    valueTypeNames = new HashSet<>();
    enumNames = new HashMap<>();
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    super.clone();

    ObjectTemplate objectTemplate = new ObjectTemplate();
    objectTemplate.setClassName(this.className);
    objectTemplate.setBeanInfo(this.beanInfo);

    objectTemplate.setBaseTypeValues(Sets.newHashSet(this.baseTypeValues));
    objectTemplate.setComplexTypeValues(Maps.newHashMap(this.complexTypeValues));
    objectTemplate.setEnumNames(Maps.newHashMap(this.enumNames));
    objectTemplate.setStaticModifierValues(Sets.newHashSet(this.staticModifierValues));
    objectTemplate.setDateNames(Sets.newHashSet(dateNames));
    objectTemplate.setValueTypeNames(Sets.newHashSet(dateNames));

    objectTemplate.setAllFieldCls(this.allFieldCls);
    objectTemplate.setName2Alias(this.name2Alias);

    objectTemplate.setNotSerialized(Sets.newHashSet(this.beanInfo.getNotSerialized()));
    objectTemplate.setNotDeserialize(Sets.newHashSet(this.beanInfo.getNotDeserialize()));

    objectTemplate.setConfiguration(configuration);

    return objectTemplate;
  }
}
