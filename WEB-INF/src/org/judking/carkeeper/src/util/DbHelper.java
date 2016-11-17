package org.judking.carkeeper.src.util;

public class DbHelper {
	/**
	 * ������ݿ�insert��������ֵ�Ƿ����0�����������ܳ�RuntimeException
	 * @param test
	 */
	public static void assertGtZero(int test)		{
		if(test <= 0)		{
			throw new RuntimeException("judking## : DB return value is less than or equal to zero");
		}
	}
}
