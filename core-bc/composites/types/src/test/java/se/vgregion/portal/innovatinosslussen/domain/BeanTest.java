package se.vgregion.portal.innovatinosslussen.domain;

import junit.framework.Assert;

import org.apache.commons.collections.BeanMap;
import org.junit.Test;

import se.vgregion.portal.innovationsslussen.domain.BariumResponse;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.json.ApplicationInstance;
import se.vgregion.portal.innovationsslussen.domain.pageiterator.PageIterator;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: portaldev
 * Date: 2013-09-18
 * Time: 07:43
 * To change this template use File | Settings | File Templates.
 */
public class BeanTest {

    Map<Class, Object> defaultPrim = new HashMap<Class, Object>() {
        @Override
        public Object get(Object key) {
            Object result = super.get(key);
            if (result == null) {
                try {
                    result = ((Class)key).newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return result;
        }
    };


    {
        defaultPrim.put(Long.TYPE, 1l);
        defaultPrim.put(Integer.TYPE, 1);
        defaultPrim.put(Float.TYPE, 1f);
        defaultPrim.put(Short.TYPE, (short) 1);
        defaultPrim.put(Long.class, 1l);
        defaultPrim.put(Integer.class, 1);
        defaultPrim.put(Float.class, 1f);
        defaultPrim.put(Short.class, (short) 1);
        defaultPrim.put(Boolean.class, true);
        defaultPrim.put(Boolean.TYPE, true);
        defaultPrim.put(Set.class, new HashSet());
        defaultPrim.put(Collection.class, new ArrayList());
        defaultPrim.put(List.class, new ArrayList());
    }

    @Test
    public void constructors() throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
        for (Class type : getTypes(Idea.class)) {
            if (!type.isEnum()) {
                callConstructors(type);
            }
        }
        for (Class type : getTypes(ApplicationInstance.class)) {
            if (!type.isEnum()) {
                callConstructors(type);
            }
        }
        for (Class type : getTypes(PageIterator.class)) {
            if (!type.isEnum()) {
                if (type.getDeclaredConstructors()[0].getModifiers() == Modifier.PUBLIC) {
                    callConstructors(type);
                }
            }
        }
        for (Class type : getTypes(Idea.class)) {
            if (!type.isEnum()) {
                callConstructors(type);
            }
        }
        for (Class type : getTypes(BariumResponse.class)) {
            if (!type.isEnum()) {
                callConstructors(type);
            }
        }
    }


    public void callConstructors(Class type) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor[] constructors = type.getDeclaredConstructors();
        for (Constructor constructor: constructors) {
            Class[] types = constructor.getParameterTypes();
            Object[] arguments = new Object[types.length];
            for (int i = 0; i < arguments.length; i++) {
                arguments[i] = defaultPrim.get(types[i]);
            }
            constructor.newInstance(arguments);
        }
    }


    @Test
    public void gettersAndSetters() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        for (Class type : getTypes(Idea.class)) {
            if (!type.isEnum()) {
                doGetterSetterValuesMatch(type.newInstance());
            }
        }
        for (Class type : getTypes(ApplicationInstance.class)) {
            if (!type.isEnum()) {
                doGetterSetterValuesMatch(type.newInstance());
            }
        }
        for (Class type : getTypes(PageIterator.class)) {
            if (!type.isEnum()) {
                if (type.getDeclaredConstructors()[0].getModifiers() == Modifier.PUBLIC) {
                    doGetterSetterValuesMatch(type.newInstance());
                }
            }
        }
        for (Class type : getTypes(Idea.class)) {
            if (!type.isEnum()) {
                doGetterSetterValuesMatch(type.newInstance());
            }
        }
        for (Class type : getTypes(BariumResponse.class)) {
            if (!type.isEnum()) {
                doGetterSetterValuesMatch(type.newInstance());
            }
        }
    }

    List<Class> getTypes(Class sampelTypeFromPack) throws ClassNotFoundException {
        Package aPackage = sampelTypeFromPack.getPackage();
        URL url = sampelTypeFromPack.getResource("/");
        File file = new File(url.getFile());
        file = new File(file.getParent());
        String sc = File.separator;
        String path = file.getParent() + sc + "target" + sc + "classes" + sc +
                aPackage.getName().replace('.', sc.charAt(0)) + sc;
        file = new File(path);
        List<Class> result = new ArrayList<Class>();

        for (String clazzFileName : file.list()) {
            if (clazzFileName.endsWith(".class")) {
                clazzFileName = clazzFileName.substring(0, clazzFileName.indexOf(".class"));
                result.add(Class.forName(aPackage.getName() + "." + clazzFileName));
            }
        }

        return result;
    }


    void doGetterSetterValuesMatch(Object o) throws IllegalAccessException, InstantiationException {
        BeanMap bm = new BeanMap(o);

        final String javaLangPackageName = String.class.getPackage().getName();

        for (Object key : bm.keySet()) {
            String name = (String) key;

            if ("ideaContentPrivate".equals(name) || "ideaPerson".equals(name)) {
                continue;
            }

            if (bm.getWriteMethod(name) != null) {
                if (bm.getType(name).equals(String.class)) {
                    bm.put(name, name);
                    Assert.assertTrue(name == bm.get(name));
                } else {
                    Class clazz = bm.getType(name);

                    if (!clazz.getName().startsWith(javaLangPackageName) && !clazz.isEnum()) {
                        Object value = defaultPrim.get(clazz);
                        if (value == null) {
                            value = clazz.newInstance();
                        }
                        bm.put(name, value);
                        Assert.assertTrue("1, " + o.getClass() + "." + key, value.equals(bm.get(name)));
                        Assert.assertTrue("2, " + o.getClass() + "." + key, value.hashCode() == bm.get(name).hashCode());
                    }
                }
            }

        }
    }

}
