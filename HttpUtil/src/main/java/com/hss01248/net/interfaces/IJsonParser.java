package com.hss01248.net.interfaces;

import java.util.List;

/**
 * Created by huangshuisheng on 2017/12/25.
 */

public interface IJsonParser {

     String toJsonStr(Object obj);


     <T> T  parseObject(String str,Class<T> clazz);

     <T> T  parse(String str,Class<T> clazz);

      <E> List<E> parseArray(String str, Class<E> clazz);

}
