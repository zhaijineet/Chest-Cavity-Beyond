package net.zhaiji.chestcavitybeyond.api;

import java.util.List;
import java.util.Map;

/**
 * simple 与 detailed 描述的动态值映射
 */
public record DynamicValues(
    Map<Integer, List<FormulaValue>> simple,
    Map<Integer, List<FormulaValue>> detailed
) {

    /**
     * simple 与 detailed 共用同一份映射
     */
    public static DynamicValues same(Map<Integer, List<FormulaValue>> values) {
        return new DynamicValues(values, values);
    }

    /**
     * simple 与 detailed 各自一份映射
     */
    public static DynamicValues split(
        Map<Integer, List<FormulaValue>> simple,
        Map<Integer, List<FormulaValue>> detailed
    ) {
        return new DynamicValues(simple, detailed);
    }
}
