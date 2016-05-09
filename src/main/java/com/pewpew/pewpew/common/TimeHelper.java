package com.pewpew.pewpew.common;

public class TimeHelper {
	public static final int TIME_TO_SLEEP = 5000;

	public static void sleep(long period){
		if (period <= 0) {
			return;
		}
		try{
			Thread.sleep(period);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void sleep(){
		try{
			Thread.sleep(TIME_TO_SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
