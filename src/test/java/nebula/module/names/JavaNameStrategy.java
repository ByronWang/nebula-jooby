/*
 * Copyright 2000-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nebula.module.names;


import java.util.Arrays;
import java.util.function.Function;

/**
 * This strategy decapitalizes property name, e.g. getXmlElementName() will correspond to xmlElementName
 *
 * @author peter
 */
public class JavaNameStrategy extends DomNameStrategy {
  public static final Function<String,String> DECAPITALIZE_FUNCTION = s -> decapitalize(s);

  @Override
  public final String convertName(String propertyName) {
    return decapitalize(propertyName);
  }

  @Override
  public final String splitIntoWords(final String tagName) {
    return  StringUtil.join(NameUtil.nameToWords(tagName), DECAPITALIZE_FUNCTION, " ");
  }
  
  public static String decapitalize(String name) {
      if (name == null || name.length() == 0) {
          return name;
      }
      if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) &&
                      Character.isUpperCase(name.charAt(0))){
          return name;
      }
      char chars[] = name.toCharArray();
      chars[0] = Character.toLowerCase(chars[0]);
      return new String(chars);
  }



  public static String join( final String[] strings,  final String separator) {
    return join(strings, 0, strings.length, separator);
  }



  public static String join( final String[] strings, int startIndex, int endIndex,  final String separator) {
    final StringBuilder result = new StringBuilder();
    for (int i = startIndex; i < endIndex; i++) {
      if (i > startIndex) result.append(separator);
      result.append(strings[i]);
    }
    return result.toString();
  }


}
