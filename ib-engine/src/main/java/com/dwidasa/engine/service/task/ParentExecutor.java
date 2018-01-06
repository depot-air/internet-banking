package com.dwidasa.engine.service.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.PeriodicTaskDao;
import com.dwidasa.engine.dao.PeriodicTaskHistoryDao;
import com.dwidasa.engine.model.PeriodicProcess;
import com.dwidasa.engine.model.PeriodicTask;
import com.dwidasa.engine.model.PeriodicTaskHistory;
import com.dwidasa.engine.service.PeriodicProcessService;

public class ParentExecutor implements Callable<String>{
	private final String periodType;
	
	public ParentExecutor(String periodType) {
		this.periodType = periodType;
		this.progressInfo = Collections.synchronizedList(new ArrayList<String>());
	}
	
	private Long userId;
	public synchronized Long getUserId() {
		return userId;
	}
	public synchronized void setUserId(Long userId) {
		this.userId = userId;
	}
	
	private Date processDate;
	public synchronized Date getProcessDate() {
		return processDate;
	}
	public synchronized void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}

	private List<String> progressInfo;
    public synchronized List<String> getProgressInfo() {
    	return progressInfo;
    }
    public synchronized void setProgressInfo(List<String> progressInfo) {
    	this.progressInfo = progressInfo;
    }
    
    private boolean ready = true;

	public synchronized boolean isReady() {
		return ready;
	}

	public synchronized void setReady(boolean ready) {
		this.ready = ready;
	}
	
	public void initProcess(Long userId, Date processDate) {
		setUserId(userId);
		setProcessDate(processDate);
	}
	
	@Autowired
	private PeriodicProcessService periodicProcessService;
	
	protected PeriodicProcess process;
	
	private PeriodicProcess prepareProcessData() {
		process = new PeriodicProcess();
		process.setPeriodType(periodType);
		process.setProcessDate(processDate);
		process.setStartTime(new Date());
		process.setFinishTime(null);
		process.setStatus(Constants.PERIODIC_STATUS.PROGRESS);
		process.setCreated(new Date());
		process.setCreatedby(userId);
		process.setUpdated(new Date());
		process.setUpdatedby(userId);
		periodicProcessService.save(process);
		return process;
	}
	
	private void updateProcessData(PeriodicProcess process, String status) {
		process.setStatus(status);
		process.setUpdated(new Date());
		if (com.dwidasa.engine.Constants.PERIODIC_STATUS.SUCCESS.equals(status) ||
			com.dwidasa.engine.Constants.PERIODIC_STATUS.FAIL.equals(status)) {
			process.setFinishTime(process.getUpdated());
		}
		periodicProcessService.save(process);
	}
	
	private List<PeriodicTaskHistory> prepareTaskData(List<PeriodicTask> taskList, Long periodicProcessId) { 
		List<PeriodicTaskHistory> result = new ArrayList<PeriodicTaskHistory>();
		Date currentDate = new Date();
		for (PeriodicTask pt : taskList) {
			PeriodicTaskHistory pth = new PeriodicTaskHistory();
			pth.setPeriodicProcessId(periodicProcessId);
			pth.setTaskName(pt.getTaskName());
		    pth.setClassName(pt.getClassName());
		    pth.setPeriodType(pt.getPeriodType());
		    pth.setExecutionOrder(pt.getExecutionOrder());
		    pth.setStatus(pt.getStatus());
		    pth.setTrace(pt.getTrace());
		    pth.setCreated(currentDate);
		    pth.setCreatedby(userId);
		    pth.setUpdated(currentDate);
		    pth.setUpdatedby(userId);
		    periodicTaskHistoryDao.save(pth);
			result.add(pth);
		}
		return result;
	}
	
	@Autowired
	private PeriodicTaskDao periodicTaskDao;
	
	@Autowired
	private PeriodicTaskHistoryDao periodicTaskHistoryDao;
	
	@Override
	public String call() throws Exception {
		setReady(false);
		PeriodicProcess process = prepareProcessData();
		StringBuilder sb = new StringBuilder();
		try {
	    	progressInfo.clear();
			
			progressInfo.add("Process Started");
			progressInfo.add("");

			List<PeriodicTask> taskList = periodicTaskDao.getAll(periodType);
			List<PeriodicTaskHistory> taskHistoryList = prepareTaskData(taskList, process.getId());
			
			boolean success = true;
            for (PeriodicTaskHistory task : taskHistoryList) {
            	progressInfo.add("Task " + task.getTaskName() + " Started");
            	if (periodicTaskDao.executeSingleTask(processDate, userId, task)) {
	            	progressInfo.add("Task " + task.getTaskName() + " Finished");
	            	progressInfo.add("");
	            	if (task.getTrace() != null) sb.append(task.getTrace()).append("\n");
            	} else {
            		progressInfo.add("Task " + task.getTaskName() + " Failed");
	            	progressInfo.add("");
	            	success = false;
	            	break;
            	}
            }
			
            if (success) {
				updateProcessData(process, Constants.PERIODIC_STATUS.SUCCESS);
				progressInfo.add("Processing Done");
            } else {
            	updateProcessData(process, Constants.PERIODIC_STATUS.FAIL);
    			progressInfo.add("Processing Error");
            }
            return sb.toString();
		} catch (Exception e) {
			updateProcessData(process, Constants.PERIODIC_STATUS.FAIL);
			progressInfo.add("Processing Error: " + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			setReady(true);
		}
	}
	

}

