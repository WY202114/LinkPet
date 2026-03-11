package com.wzc.linkpet.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页结果封装
 * 用于包装列表查询的分页数据，与 {@link Result} 配合使用：
 * <pre>
 *   Result.success(new PageResult&lt;&gt;(records, total));
 * </pre>
 *
 * @param <T> 列表数据项的类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    /**
     * 数据列表（当前页记录）
     */
    private List<T> records;

    /**
     * 总记录数
     */
    private long total;
}
