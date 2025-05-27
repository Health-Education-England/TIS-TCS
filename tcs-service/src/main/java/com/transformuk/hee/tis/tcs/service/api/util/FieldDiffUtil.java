package com.transformuk.hee.tis.tcs.service.api.util;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Utility to compare flat fields of two objects.
 */
public class FieldDiffUtil {

  /**
   * Compare the differences between two objects.
   * 1. Only top-level fields (no nested support)
   * 2. Ignores fields that exist only in the new object
   * 3. Flags fields that are in old object but missing/null in new object
   */
  @SuppressWarnings("java:S3011")
  public static <T1, T2> Map<String, Object[]> diff(T1 newObj, T2 oldObj) {
    if (newObj == null || oldObj == null) {
      throw new IllegalArgumentException("Objects to compare must not be null");
    }

    Map<String, Object[]> diffs = new LinkedHashMap<>();

    Class<?> newClass = newObj.getClass();
    Class<?> oldClass = oldObj.getClass();

    // Only compare fields that exist in old object (to allow detection of missing fields)
    for (Field oldField : oldClass.getDeclaredFields()) {
      oldField.setAccessible(true);
      String fieldName = oldField.getName();

      try {
        Field newField = getFieldByName(newClass, fieldName);

        Object oldValue = oldField.get(oldObj);
        Object newValue = null;

        if (newField != null) {
          newField.setAccessible(true);
          newValue = newField.get(newObj);
        }

        if (!Objects.equals(newValue, oldValue)) {
          diffs.put(fieldName, new Object[]{oldValue, newValue});
        }

      } catch (IllegalAccessException e) {
        throw new IllegalStateException("Unable to access field: " + fieldName, e);
      }
    }

    return diffs;
  }

  private static Field getFieldByName(Class<?> clazz, String name) {
    while (clazz != null) {
      try {
        return clazz.getDeclaredField(name);
      } catch (NoSuchFieldException e) {
        clazz = clazz.getSuperclass();
      }
    }
    return null;
  }

  // Utility class should not have public constructor
  private FieldDiffUtil() {
    throw new UnsupportedOperationException("Utility class");
  }
}
