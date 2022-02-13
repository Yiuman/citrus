package com.github.yiuman.citrus.mda.ddl;

import com.github.yiuman.citrus.mda.ddl.analyzer.MetadataAnalyzer;
import com.github.yiuman.citrus.mda.exception.DdlException;
import com.github.yiuman.citrus.mda.meta.TableMeta;
import com.github.yiuman.citrus.mda.meta.TableMetaRel;
import com.github.yiuman.citrus.support.utils.SpringUtils;

import java.util.Optional;

/**
 * DDL处理器基类
 *
 * @author yiuman
 * @date 2021/4/26
 */
public abstract class BaseDdlProcessor implements DdlProcessor {

    public BaseDdlProcessor() {
    }

    protected MetadataAnalyzer<?> getMappingAnalyze(Class<?> metaClass) {
        try {
            @SuppressWarnings("rawtypes")
            Optional<MetadataAnalyzer> first = SpringUtils.getBeanOfType(MetadataAnalyzer.class)
                    .values()
                    .stream()
                    .filter(analyzer -> analyzer.getMetaType().equals(metaClass))
                    .findFirst();

            return first.orElseThrow(() -> new DdlException(String.format("cannot find MetadataAnalyzer for metaType '%s'", metaClass)));

        } catch (Throwable throwable) {
            throw new DdlException(throwable);
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> void doAction(T metadata, Action action) {
        MetadataContext<T> context = buildContext(metadata);
        ((MetadataAnalyzer<T>) getMappingAnalyze(metadata.getClass()))
                .analyzer(action, context)
                .execute(context);
    }

    @SuppressWarnings("unchecked")
    protected <T> MetadataContext<T> buildContext(T metadata) {
        return (MetadataContext<T>) MetadataContextImpl.builder()
                .metadata(metadata)
                .sqlSessionFactory(getSqlSessionFactory(getMetadataNamespace(metadata)))
                .build();
    }

    /**
     * 获取元数据的命名空间
     *
     * @param metadata 元数据
     * @return 命名空间
     */
    protected String getMetadataNamespace(Object metadata) {
        if (metadata instanceof TableMeta) {
            return ((TableMeta) metadata).getNamespace();
        }

        if (TableMetaRel.class.isAssignableFrom(metadata.getClass())) {
            return ((TableMetaRel) metadata).getTable().getNamespace();
        }
        return "";
    }
}
