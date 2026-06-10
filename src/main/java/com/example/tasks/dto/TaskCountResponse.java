package com.example.tasks.dto;

public class TaskCountResponse {
    private long gardeningCount;
    private long laundryCount;
    private long readingCount;

	public TaskCountResponse(long gardeningCount, long laundryCount, long readingCount) {
		this.gardeningCount = gardeningCount;
		this.laundryCount = laundryCount;
		this.readingCount = readingCount;
	}

	public long getGardeningCount() {
		return gardeningCount;
	}

	public void setGardeningCount(long gardeningCount) {
		this.gardeningCount = gardeningCount;
	}

	public long getLaundryCount() {
		return laundryCount;
	}

	public void setLaundryCount(long laundryCount) {
		this.laundryCount = laundryCount;
	}

	public long getReadingCount() {
		return readingCount;
	}

	public void setReadingCount(long readingCount) {
		this.readingCount = readingCount;
	}
    
    
}
