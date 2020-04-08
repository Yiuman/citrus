package com.github.yiuman.citrus.support.crud;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yiuman.citrus.support.model.Tree;
import com.github.yiuman.citrus.support.model.TreeService;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 预排序遍历树形结构的Crud
 *
 * @author yiuman
 * @date 2020/4/7
 */
public abstract class BaseTreeCrudService<M extends BaseMapper<E>, E extends Tree<K>, K>
        extends BaseCrudService<M, E, K>
        implements TreeService<E, K> {

    private final static String UPDATE_ADD_FORMAT = "%s=%s+%s";

    private final static String UPDATE_REDUCTION_FORMAT = "%s=%s-%s";

    @Override
    public E getRoot() {
        return getBaseMapper().selectOne(new QueryWrapper<E>().isNull(getParentField()));
    }

    @Override
    public void beforeSave(E entity) throws Exception {
        this.insert(entity);
    }

    @Override
    public void insert(E current) throws Exception {
        //1.获取当前父节点
        E parent = Optional.ofNullable(get(current.getParentId()))
                .orElse(getRoot());
        //2.更新所有左值大于当前父节点左值的节点左值 +2
        //3.更新所有右值大于当前父节点右值的节点右值 +2
        int rightValue = 1;
        int deep = 1;
        //4.更新父节点
        if (parent != null) {
            beforeSaveOrUpdate(parent);
            rightValue = parent.getRightValue();
            deep = parent.getDeep() + 1;
            parent.setRightValue(rightValue + 2);
            saveOrUpdate(parent);
            current.setParentId(parent.getId());
        } else {
            current.setParentId(null);
        }

        //5.设置当前节点的左右值
        current.setLeftValue(rightValue);
        current.setRightValue(rightValue + 1);
        current.setDeep(deep);
        //Over
    }

    @Override
    public void move(E current, K moveTo) throws Exception {
        //更新树
        beforeDeleteOrMove(current);

        //变更父ID
        current.setParentId(moveTo);
        saveEntity(current);

        List<E> children = children(current);
        //子节点不为空时重新生成左右值
        if (!CollectionUtils.isEmpty(children)) {
            children.forEach(LambdaUtils.consumerWrapper(this::saveEntity));
        }
    }

    /**
     * 重写普通CRUD的删除方法，在删除前做更新节点操作
     *
     * @param key 当前删除的主键
     */
    @SuppressWarnings("unchecked")
    @Override
    public void delete(K key) throws Exception {
        E currentEntity = get(key);
        List<K> collect = children(currentEntity).parallelStream().map(Tree::getId).collect(Collectors.toList());
        removeByIds((Collection<? extends Serializable>) collect);
        delete(currentEntity);
        super.delete(key);
    }

    @Override
    public void delete(E current) {
        beforeDeleteOrMove(current);
    }

    @Override
    public List<E> children(E current) {
        // target.left > this.left  and target.right < this.right
        return list(new QueryWrapper<E>()
                .gt(getLeftField(), current.getRightValue())
                .lt(getRightField(), current.getRightValue()));
    }

    @Override
    public List<E> children(E current, int deep) {
        return list(new QueryWrapper<E>()
                .gt(getLeftField(), current.getRightValue())
                .lt(getRightField(), current.getRightValue())
                .eq(getDeepField(), deep));
    }

    @Override
    public List<E> parents(E current) {
        return list(new QueryWrapper<E>()
                .gt(getRightField(), current.getRightValue())
                .le(getLeftField(), current.getLeftValue()));
    }

    @Override
    public List<E> parents(E current, int high) {
        return list(new QueryWrapper<E>()
                .gt(getRightField(), current.getRightValue())
                .le(getLeftField(), current.getLeftValue())
                .eq(getDeepField(), high));
    }

    @Override
    public List<E> siblings(E current) {
        return list(new QueryWrapper<E>().eq(getParentField(), current.getParentId()));
    }


    /**
     * 当前节点保存前的做的操作
     *
     * @param parent 当前节点的父节点
     */
    private void beforeSaveOrUpdate(E parent) {
        //2.更新所有左值大于当前父节点右值的节点左值 +2
        update(new UpdateWrapper<E>()
                .setSql(String.format(UPDATE_ADD_FORMAT, getLeftField(), getLeftField(), 2))
                .gt(getLeftField(), parent.getRightValue()));

        //3.更新所有右值大于当前父节点右值的节点右值 +2
        update(new UpdateWrapper<E>()
                .setSql(String.format(UPDATE_ADD_FORMAT, getRightField(), getRightField(), 2))
                .gt(getRightField(), parent.getRightValue()));
    }

    /**
     * 删除或移动前做的操作
     *
     * @param current 当前节点
     */
    private void beforeDeleteOrMove(E current) {
        //1.更新所有右值小于当前父节点右值的节点左值 -2
        update(new UpdateWrapper<E>()
                .setSql(String.format(UPDATE_REDUCTION_FORMAT, getLeftField(), getLeftField(), 2))
                .gt(getLeftField(), current.getRightValue()));

        //2.更新所有右值小于当前父节点右值的节点右值 +2
        update(new UpdateWrapper<E>()
                .setSql(String.format(UPDATE_REDUCTION_FORMAT, getRightField(), getRightField(), 2))
                .gt(getRightField(), current.getRightValue()));
    }

}
