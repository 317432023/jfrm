package com.soaringloong.jfrm.module.system.service;

import com.soaringloong.jfrm.framework.test.core.BaseMockitoUnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * 模拟测试框架 mockito 使用示例<br>
 * <a href="https://blog.csdn.net/admans/article/details/139069086">详解</a><br>
 * <a href="https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html">文档</a><br>
 * <a href="https://site.mockito.org/">官网</a>
 */
public class MockitoTest extends BaseMockitoUnitTest {

    /**
     * 综合示例
     */
    @Test
    public void myTest() {
        /* 创建 Mock 对象 */
        List list = mock(List.class);
        /* 设置预期，当调用 get(0) 方法时返回 "111" */
        when(list.get(0)).thenReturn("111");

        assertEquals(list.get(0), "111");
        /* 设置后返回期望的结果 */
        System.out.println(list.get(0));
        /* 没有设置则返回 null */
        System.out.println(list.get(1));

        /* 对 Mock 对象设置无效 */
        list.add("12");
        list.add("123");
        /* 返回之前设置的结果 */
        System.out.println(list.get(0));
        /* 返回 null */
        System.out.println(list.get(1));
        /* size 大小为 0 */
        System.out.println(list.size());

        /* 验证操作，验证 get(0) 调用了 3 次 */
        verify(list, times(3)).get(0);

        /* 验证返回结果 */
        String ret = (String) list.get(0);
        assertEquals(ret, "111");
    }

    /**
     * when...thenReturn 使用
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void with_arguments() {
        // 模拟一个 comparable 对象
        Comparable comparable = mock(Comparable.class);
        // 预设根据不同的参数返回不同的结果
        when(comparable.compareTo("Test")).thenReturn(1);
        when(comparable.compareTo("Omg")).thenReturn(2);
        assertEquals(1, comparable.compareTo("Test"));
        assertEquals(2, comparable.compareTo("Omg"));
        //对于没有预设的情况会返回默认值
        assertEquals(0, comparable.compareTo("Not stub"));
    }

    /**
     * argThat 使用
     */
    @SuppressWarnings({"rawtypes"})
    @Test
    public void with_unspecified_arguments() {
        // 模拟一个 list 对象
        List list = mock(List.class);
        // 预设 list 任意下标 对应值均为 1
        when(list.get(anyInt())).thenReturn(1);
        when(list.contains(argThat(new IsValid()))).thenReturn(true);
        assertEquals(1, list.get(1));
        assertEquals(1, list.get(999));
        assertTrue(list.contains(1));
        assertTrue(list.contains(2));
        assertFalse(list.contains(3));
    }

    private static class IsValid implements ArgumentMatcher<Integer> {
        @Override
        public boolean matches(Integer integer) {
            return integer == 1 || integer == 2;
        }
    }

    /**
     * verify + argThat 使用-检查特定的方法调用情况
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void argumentMatchersTest() {
        // 创建mock对象
        List mock = mock(List.class);

        // argThat(Matches<T> matcher)方法用来应用自定义的规则，可以传入任何实现Matcher接口的实现类。
        when(mock.addAll(argThat(new IsListOfTwoElements()))).thenReturn(true);

        mock.addAll(Arrays.asList("one", "two", "three"));

        // IsListOfTwoElements用来匹配size为2的List，因为例子传入List为三个元素，所以此时将失败。
        verify(mock).addAll(argThat(new IsListOfTwoElements()));
    }

    @SuppressWarnings("rawtypes")
    public static class IsListOfTwoElements implements ArgumentMatcher<List> {
        public boolean matches(List list) {
            return list.size() == 2;
        }
    }

}
