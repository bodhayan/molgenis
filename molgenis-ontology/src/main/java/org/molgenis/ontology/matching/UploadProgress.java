package org.molgenis.ontology.matching;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class UploadProgress
{
	private final Set<String> currentUsers;
	private final Map<String, String> currentJobs;
	private final Map<String, Integer> userTotalNumber;
	private final Map<String, Integer> userFinishedNumber;
	private final Map<String, Integer> userThreshold;
	private final static Integer ILLEGAL_DENOMINATOR = 0;
	private final static Integer DEFAULT_THRESHOLD = 80;

	public UploadProgress()
	{
		currentUsers = new HashSet<String>();
		currentJobs = new HashMap<String, String>();
		userThreshold = new HashMap<String, Integer>();
		userTotalNumber = new HashMap<String, Integer>();
		userFinishedNumber = new HashMap<String, Integer>();
	}

	public float getPercentage(String userName)
	{
		if (userTotalNumber.containsKey(userName) && userFinishedNumber.containsKey(userName)
				&& userTotalNumber.get(userName) != ILLEGAL_DENOMINATOR)
		{
			return userFinishedNumber.get(userName).floatValue() / userTotalNumber.get(userName).floatValue() * 100;
		}

		return 0;
	}

	public int getThreshold(String userName)
	{
		if (userThreshold.containsKey(userName)) return userThreshold.get(userName);

		return 0;
	}

	public boolean isUserExists(String userName)
	{
		return currentUsers.contains(userName);
	}

	public String getCurrentJob(String userName)
	{
		return currentJobs.containsKey(userName) ? currentJobs.get(userName) : StringUtils.EMPTY;
	}

	public void incrementProgress(String userName)
	{
		if (userFinishedNumber.containsKey(userName))
		{
			userFinishedNumber.put(userName, (userFinishedNumber.get(userName) + 1));
		}
	}

	public void resetUserThreshold(String userName, Integer threshold)
	{
		userThreshold.put(userName, threshold);
	}

	public void registerUser(String userName, String currentJob, Integer totalNumber)
	{
		currentUsers.add(userName);
		userTotalNumber.put(userName, totalNumber);
		currentJobs.put(userName, currentJob);
		userFinishedNumber.put(userName, ILLEGAL_DENOMINATOR);
		userThreshold.put(userName, DEFAULT_THRESHOLD);
	}

	public void registerUser(String userName, String currentJob)
	{
		registerUser(userName, currentJob, ILLEGAL_DENOMINATOR);
	}

	public void removeUser(String userName)
	{
		currentUsers.remove(userName);
		userTotalNumber.remove(userName);
		userFinishedNumber.remove(userName);
		userThreshold.remove(userName);
		currentJobs.remove(userName);
	}
}
