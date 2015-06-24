/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.clinicaintegrada.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Query;

/**
 * http://stackoverflow.com/questions/13012584/jpa-how-to-convert-a-native-query-result-set-to-pojo-class-collection
 *
 */
public class QueryMapping {

    public static <T> T map(Class<T> type, Object[] tuple) {
        List<Class<?>> tupleTypes = new ArrayList<>();
        for (Object field : tuple) {
            tupleTypes.add(field.getClass());
        }
        try {
            Constructor<T> ctor = type.getConstructor(tupleTypes.toArray(new Class<?>[tuple.length]));
            return ctor.newInstance(tuple);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

//Query query = em.createNativeQuery("SELECT name,age FROM jedis_table");
//List<Jedi> jedis = getResultList(query, Jedi.class);    
    public static <T> List<T> addMap(Class<T> type, Object[] records) {
        List<T> result = new LinkedList<>();
        try {
            for(int i = 0; i < records.length; i++) {
                Object[] o = ((List) records[i]).toArray();
                result.add(map(type, o));
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    public static <T> List<T> getResultList(Query query, Class<T> type) {
        @SuppressWarnings("unchecked")
        List list = query.getResultList();
        Object[] records = list.toArray();
        return addMap(type, records);
    }
}
