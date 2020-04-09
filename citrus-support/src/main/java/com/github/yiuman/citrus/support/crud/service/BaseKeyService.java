package com.github.yiuman.citrus.support.crud.service;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yiuman.citrus.support.model.Key;
import com.github.yiuman.citrus.support.model.Primary;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

/**
 * 基础主键查找抽象类
 *
 * @author yiuman
 * @date 2020/4/5
 */
@SuppressWarnings("unchecked")
public abstract class BaseKeyService<M extends BaseMapper<E>, E, K> extends ServiceImpl<M, E> implements Key<E, K> {

    private final static String DEFAULT_KEY_NAME = "id";

    protected final Class<K> keyClass = currentKeyClass();

    private Class<K> currentKeyClass() {
        return (Class<K>) ReflectionKit.getSuperClassGenericType(getClass(), 2);
    }

    public Class<E> getEntityClass(){
        return (Class<E>) entityClass;
    }

    /**
     * 此处通过实体类找到主键
     * 规则：
     * 必须条件:1.满足属性类型与此类的E泛型类型匹配的属性
     * 2.满足条件1且有使用@Primary标记的属性
     * 3.满足条件1且有使用@TableId标记的属性
     * 4.满足条件1且属性名称为id的属性
     * <p>
     * 满足 2 或 3 或 4 则返回主键值，否则抛异常
     *
     * @param entity 实体类
     * @return 主键
     */
    @Override
    public K key(E entity) throws IllegalAccessException {
        AtomicReference<K> key = new AtomicReference<>();
        final List<Predicate<Field>> filterPredicate = Arrays
                .asList((field -> field.getType() == keyClass && field.getAnnotation(Primary.class) != null),
                        (field -> field.getType() == keyClass && DEFAULT_KEY_NAME.equals(field.getName())),
                        (field -> field.getType() == keyClass && field.getAnnotation(TableId.class) != null));

        boolean hasPrimaryField = false;
        for (Predicate<Field> predicate : filterPredicate) {
            Optional<Field> optionalField = Arrays.stream(entityClass.getDeclaredFields()).filter(predicate).findFirst();
            if (optionalField.isPresent()) {
                hasPrimaryField = true;
                Field field = optionalField.get();
                field.setAccessible(true);
                key.set((K) field.get(entity));
                break;
            }
        }

        if (!hasPrimaryField) {
            throw new RuntimeException(String.format("Cannot found the entity's primary key from entity's class '%s'," +
                    "you can tagging the annotation [%s or %s] for the field which is the primary key," +
                    "or define a field with the name 'id'", entityClass, Primary.class, TableId.class));
        }

        return key.get();
    }
}
