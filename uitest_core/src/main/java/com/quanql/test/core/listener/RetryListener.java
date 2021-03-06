package com.quanql.test.core.listener;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;

import com.quanql.test.core.utils.LogUtil;
import com.quanql.test.core.utils.ConfigUtil;

/**
 * RetryListener for each test method.
 * 
 * @author kevinkong
 *
 */
public class RetryListener implements IAnnotationTransformer {

	@SuppressWarnings("rawtypes")
	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {

		IRetryAnalyzer retry = annotation.getRetryAnalyzer();
		if (retry == null) {
			annotation.setRetryAnalyzer(TestngRetry.class);
		}

		// 设置 默认循环次数
		ConfigUtil property = ConfigUtil.getInstance();
		int count = Integer.valueOf(property.getProperty("loopCount"));
		LogUtil.info("默认每个方法循环" + count + "次");
		annotation.setInvocationCount(count);

		// 设置 需要特殊处理方法的循环次数
		String excepLoopCount = property.getProperty("excepLoopCount");
		String[] excepCount = excepLoopCount.split(";");
		for (int i = 0; i < excepCount.length; i++) {
			String[] temp = excepCount[i].split(",");
			if (testMethod.getName().equals(temp[0])) {
				LogUtil.info("该方法循环" + temp[1] + "次");

				annotation.setInvocationCount(Integer.valueOf(temp[1]));
			}

		}

	}

}
