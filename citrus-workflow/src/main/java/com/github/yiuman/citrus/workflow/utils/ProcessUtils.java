package com.github.yiuman.citrus.workflow.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import org.activiti.bpmn.model.*;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;

import java.util.*;

/**
 * @author yiuman
 * @date 2022/11/15
 */
public final class ProcessUtils {

    public static final String PREFIX = "activiti";
    public static final String NAMESPACE = "http://activiti.com/modeler";
    private static final String PROPERTIES_EL_NAME = "properties";
    private static final String PROPERTY_EL_NAME = "property";
    private static final String PROPERTY_NAME = "name";
    private static final String PROPERTY_VALUE = "value";

    private ProcessUtils() {
    }

    /**
     * 获取节点的扩展属性
     *
     * @param flowElement 流程节点
     * @return 扩展属性
     */
    public static Map<String, Object> getElementProperties(FlowElement flowElement) {
        Map<String, Object> elementProperties = new HashMap<>();
        Map<String, List<ExtensionElement>> extensionElements = flowElement.getExtensionElements();
        if (MapUtil.isEmpty(extensionElements)) {
            return elementProperties;
        }
        List<ExtensionElement> property = extensionElements.get(PROPERTIES_EL_NAME)
                .stream()
                .map(item -> item.getChildElements().get(PROPERTY_EL_NAME))
                .reduce(CollUtil.newArrayList(), (collect, current) -> {
                    if (CollUtil.isEmpty(current)) {
                        return collect;
                    }
                    collect.addAll(current);
                    return collect;
                });
        property.forEach(item -> elementProperties.put(
                item.getAttributeValue(null, PROPERTY_NAME),
                item.getAttributeValue(null, PROPERTY_VALUE)
        ));
        return elementProperties;
    }

    /**
     * 添加扩展属性
     *
     * @param flowElement 节点
     * @param name        扩展数据名称
     * @param value       扩展数据的值
     * @param <T>         流程节点类型
     */
    public static <T extends BaseElement> void addExtensionElement(T flowElement, String name, Object value) {
        if (StrUtil.isBlank(name) || Objects.isNull(value)) {
            return;
        }
        ExtensionElement propertiesElement;
        boolean isNew = false;
        List<ExtensionElement> extensionElementList = flowElement.getExtensionElements().get(PROPERTIES_EL_NAME);
        if (CollUtil.isNotEmpty(extensionElementList)) {
            propertiesElement = extensionElementList.get(0);
        } else {
            isNew = true;
            propertiesElement = new ExtensionElement();
            propertiesElement.setNamespace(NAMESPACE);
            propertiesElement.setNamespacePrefix(PREFIX);
            propertiesElement.setName(PROPERTIES_EL_NAME);
        }

        ExtensionElement childElements = new ExtensionElement();
        childElements.setNamespace(NAMESPACE);
        childElements.setNamespacePrefix(PREFIX);
        childElements.setName(PROPERTY_EL_NAME);

        ExtensionAttribute nameAttribute = new ExtensionAttribute();
        nameAttribute.setName(PROPERTY_NAME);
        nameAttribute.setValue(name);
        childElements.addAttribute(nameAttribute);

        ExtensionAttribute valueAttribute = new ExtensionAttribute();
        valueAttribute.setName(PROPERTY_VALUE);
        valueAttribute.setValue(value.toString());
        childElements.addAttribute(valueAttribute);

        propertiesElement.addChildElement(childElements);

        if (isNew) {
            flowElement.addExtensionElement(propertiesElement);
        }

    }

    /**
     * 找到上一个活动节点
     *
     * @param executionEntity 当前执行实例
     * @return 上一个活动节点
     */
    public static List<Activity> findPreActivities(ExecutionEntity executionEntity) {
        BpmnModel bpmnModel = ProcessDefinitionUtil.getBpmnModel(executionEntity.getProcessDefinitionId());
        Activity currentFlowElement = (Activity) executionEntity.getCurrentFlowElement();
        List<SequenceFlow> incomingFlows = currentFlowElement.getIncomingFlows();
        List<Activity> activities = new ArrayList<>();
        collectActivity(bpmnModel, incomingFlows, activities);
        return activities;
    }

    private static void collectActivity(BpmnModel bpmnModel, List<SequenceFlow> incomingFlows, List<Activity> collection) {
        for (SequenceFlow incomingFlow : incomingFlows) {
            String sourceRef = incomingFlow.getSourceRef();
            FlowElement sourceFlowElement = bpmnModel.getFlowElement(sourceRef);
            if (sourceFlowElement instanceof UserTask) {
                collection.add((Activity) sourceFlowElement);
            }

            if (sourceFlowElement instanceof ExclusiveGateway) {
                ExclusiveGateway gateway = (ExclusiveGateway) sourceFlowElement;
                collectActivity(bpmnModel, gateway.getIncomingFlows(), collection);
            }
        }
    }

}
