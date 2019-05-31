package top.whq6.project.transition;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.whq6.project.bean.ClassBeanInfo;

@Getter
@Setter
@NoArgsConstructor
public class ObjectTemplate implements Cloneable {

  private String className;

  private ClassBeanInfo beanInfo;
  /**
   * Key is field name, value is real object field value.
   */
  private HashMap<String, Object> baseTypeValues;

  /**
   * To save static field value.
   */
  private HashMap<String, Object> staticModifierValues;

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

  private ImmutableMap<String, String> name2Alias;

  private ImmutableMap<String, Class<?>> allFieldCls;

  @Override
  public Object clone() throws CloneNotSupportedException {
    super.clone();

    ObjectTemplate objectTemplate = new ObjectTemplate();
    objectTemplate.setClassName(this.className);
    objectTemplate.setBeanInfo(this.beanInfo);

    objectTemplate.setBaseTypeValues(Maps.newHashMap(this.baseTypeValues));
    objectTemplate.setComplexTypeValues(Maps.newHashMap(this.complexTypeValues));
    objectTemplate.setEnumNames(Maps.newHashMap(this.enumNames));
    objectTemplate.setStaticModifierValues(Maps.newHashMap(this.staticModifierValues));

    objectTemplate.setAllFieldCls(this.allFieldCls);
    objectTemplate.setName2Alias(this.name2Alias);

    objectTemplate.setNotSerialized(Sets.newHashSet(this.beanInfo.getNotSerialized()));
    objectTemplate.setNotDeserialize(Sets.newHashSet(this.beanInfo.getNotDeserialize()));

    return objectTemplate;
  }
}
