package com.my.tools.monitor;

import com.my.tools.base.LogUtils;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;

/**
 * @author: xdx
 * @date: 2024/8/28
 * @description: Jvm监控器
 */
public class JvmMonitor extends AbstractMonitor {

	public static final Logger log = LogUtils.get();
	private static final JvmMonitor INSTANCE = new JvmMonitor();

	/**
	 * 私有化构造方法，确保只能通过 getInstance() 获取实例
	 */
	private JvmMonitor() {

	}

	/**
	 * 获取单例实例
	 */
	public static JvmMonitor getInstance() {
		return INSTANCE;
	}


	@Override
	public MonitorType type() {
		return MonitorType.JVM;
	}

	@Override
	public Map<String, Object> collect() {
		Map<String, Object> jvmMap = new LinkedHashMap<>();
		// 获取内存管理 Bean
		MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
		MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
		MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
		jvmMap.put("heap_memory_init", heapMemoryUsage.getInit());
		jvmMap.put("heap_memory_used", heapMemoryUsage.getUsed());
		jvmMap.put("heap_memory_max", heapMemoryUsage.getMax());
		jvmMap.put("non_heap_memory_init", nonHeapMemoryUsage.getInit());
		jvmMap.put("non_heap_memory_used", nonHeapMemoryUsage.getUsed());
		jvmMap.put("non_heap_memory_max", nonHeapMemoryUsage.getMax());
		return jvmMap;
	}
}
