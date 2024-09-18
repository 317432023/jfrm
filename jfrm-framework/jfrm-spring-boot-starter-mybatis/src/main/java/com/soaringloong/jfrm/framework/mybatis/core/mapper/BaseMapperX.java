package com.soaringloong.jfrm.framework.mybatis.core.mapper;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.comm.pojo.PageParam;
import com.comm.pojo.PageResult;
import com.comm.pojo.SortablePageParam;
import com.comm.pojo.SortingField;
import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.soaringloong.jfrm.framework.mybatis.core.enums.SqlConstants;
import com.soaringloong.jfrm.framework.mybatis.core.util.MyBatisUtils;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public interface BaseMapperX<T> extends MPJBaseMapper<T> {

	default PageResult<T> selectPage(SortablePageParam pageParam, @Param("ew") Wrapper<T> queryWrapper) {
		return selectPage(pageParam, pageParam.getSortingFields(), queryWrapper);
	}

	default PageResult<T> selectPage(PageParam pageParam, @Param("ew") Wrapper<T> queryWrapper) {
		return selectPage(pageParam, null, queryWrapper);
	}

	default PageResult<T> selectPage(PageParam pageParam, List<SortingField> sortingFields,
			@Param("ew") Wrapper<T> queryWrapper) {
		// 特殊：不分页，直接查询全部
		if (PageParam.PAGE_SIZE_NONE.equals(pageParam.getPageSize())) {
			List<T> list = selectList(queryWrapper);
			return new PageResult<>(list, (long) list.size());
		}

		// MyBatis Plus 查询
		IPage<T> mpPage = MyBatisUtils.buildPage(pageParam, sortingFields);
		selectPage(mpPage, queryWrapper);
		// 转换返回
		return new PageResult<>(mpPage.getRecords(), mpPage.getTotal());
	}

	default <D> PageResult<D> selectJoinPage(PageParam pageParam, Class<D> resultTypeClass,
			MPJLambdaWrapper<T> lambdaWrapper) {
		// 特殊：不分页，直接查询全部
		if (PageParam.PAGE_SIZE_NONE.equals(pageParam.getPageSize())) {
			List<D> list = selectJoinList(resultTypeClass, lambdaWrapper);
			return new PageResult<>(list, (long) list.size());
		}

		// MyBatis Plus Join 查询
		IPage<D> mpPage = MyBatisUtils.buildPage(pageParam);
		mpPage = selectJoinPage(mpPage, resultTypeClass, lambdaWrapper);
		// 转换返回
		return new PageResult<>(mpPage.getRecords(), mpPage.getTotal());
	}

	default <D> PageResult<D> selectJoinPage(PageParam pageParam, Class<D> resultTypeClass,
			MPJBaseJoin<T> joinQueryWrapper) {
		IPage<D> mpPage = MyBatisUtils.buildPage(pageParam);
		selectJoinPage(mpPage, resultTypeClass, joinQueryWrapper);
		// 转换返回
		return new PageResult<>(mpPage.getRecords(), mpPage.getTotal());
	}

	default T selectOne(String field, Object value) {
		return selectOne(new QueryWrapper<T>().eq(field, value));
	}

	default T selectOne(SFunction<T, ?> field, Object value) {
		return selectOne(new LambdaQueryWrapper<T>().eq(field, value));
	}

	default T selectOne(String field1, Object value1, String field2, Object value2) {
		return selectOne(new QueryWrapper<T>().eq(field1, value1).eq(field2, value2));
	}

	default T selectOne(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2) {
		return selectOne(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2));
	}

	default T selectOne(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2,
			SFunction<T, ?> field3, Object value3) {
		return selectOne(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2).eq(field3, value3));
	}

	default Long selectCount() {
		return selectCount(new QueryWrapper<>());
	}

	default Long selectCount(String field, Object value) {
		return selectCount(new QueryWrapper<T>().eq(field, value));
	}

	default Long selectCount(SFunction<T, ?> field, Object value) {
		return selectCount(new LambdaQueryWrapper<T>().eq(field, value));
	}

	default List<T> selectList() {
		return selectList(new QueryWrapper<>());
	}

	default List<T> selectList(String field, Object value) {
		return selectList(new QueryWrapper<T>().eq(field, value));
	}

	default List<T> selectList(SFunction<T, ?> field, Object value) {
		return selectList(new LambdaQueryWrapper<T>().eq(field, value));
	}

	default List<T> selectList(String field, Collection<?> values) {
		if (CollUtil.isEmpty(values)) {
			return CollUtil.newArrayList();
		}
		return selectList(new QueryWrapper<T>().in(field, values));
	}

	default List<T> selectList(SFunction<T, ?> field, Collection<?> values) {
		if (CollUtil.isEmpty(values)) {
			return CollUtil.newArrayList();
		}
		return selectList(new LambdaQueryWrapper<T>().in(field, values));
	}

	@Deprecated
	default List<T> selectList(SFunction<T, ?> leField, SFunction<T, ?> geField, Object value) {
		return selectList(new LambdaQueryWrapper<T>().le(leField, value).ge(geField, value));
	}

	default List<T> selectList(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2) {
		return selectList(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2));
	}

	/**
	 * 批量插入，适合大量数据插入
	 * @param entities 实体们
	 */
	default Boolean insertBatch(Collection<T> entities) {
		// 特殊：SQL Server 批量插入后，获取 id 会报错，因此通过循环处理
		if (Objects.equals(SqlConstants.DB_TYPE, DbType.SQL_SERVER)) {
			entities.forEach(this::insert);
			return CollUtil.isNotEmpty(entities);
		}
		return Db.saveBatch(entities);
	}

	/**
	 * 批量插入，适合大量数据插入
	 * @param entities 实体们
	 * @param size 插入数量 Db.saveBatch 默认为 1000
	 */
	default Boolean insertBatch(Collection<T> entities, int size) {
		// 特殊：SQL Server 批量插入后，获取 id 会报错，因此通过循环处理
		if (Objects.equals(SqlConstants.DB_TYPE, DbType.SQL_SERVER)) {
			entities.forEach(this::insert);
			return CollUtil.isNotEmpty(entities);
		}
		return Db.saveBatch(entities, size);
	}

	default int updateBatch(T update) {
		return update(update, new QueryWrapper<>());
	}

	default Boolean updateBatch(Collection<T> entities) {
		return Db.updateBatchById(entities);
	}

	default Boolean updateBatch(Collection<T> entities, int size) {
		return Db.updateBatchById(entities, size);
	}

	default boolean insertOrUpdate(T entity) {
		return Db.saveOrUpdate(entity);
	}

	default Boolean insertOrUpdateBatch(Collection<T> collection) {
		return Db.saveOrUpdateBatch(collection);
	}

	default int delete(String field, String value) {
		return delete(new QueryWrapper<T>().eq(field, value));
	}

	default int delete(SFunction<T, ?> field, Object value) {
		return delete(new LambdaQueryWrapper<T>().eq(field, value));
	}

}
