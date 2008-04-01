/*
 * This file is part of the Alitheia system, developed by the SQO-OSS
 * consortium as part of the IST FP6 SQO-OSS project, number 033331.
 *
 * Copyright 2007-2008 by the SQO-OSS consortium members <info@sqo-oss.eu>
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package eu.sqooss.scl;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.axis2.AxisFault;

import eu.sqooss.scl.accessor.WSMetricAccessor;
import eu.sqooss.ws.client.WsStub;
import eu.sqooss.ws.client.datatypes.WSMetric;
import eu.sqooss.ws.client.datatypes.WSMetricMeasurement;
import eu.sqooss.ws.client.ws.GetProjectFileMetricMeasurement;
import eu.sqooss.ws.client.ws.GetProjectFileMetricMeasurementResponse;
import eu.sqooss.ws.client.ws.GetProjectVersionMetricMeasurement;
import eu.sqooss.ws.client.ws.GetProjectVersionMetricMeasurementResponse;
import eu.sqooss.ws.client.ws.RetrieveMetrics4SelectedFiles;
import eu.sqooss.ws.client.ws.RetrieveMetrics4SelectedFilesResponse;
import eu.sqooss.ws.client.ws.RetrieveMetrics4SelectedProject;
import eu.sqooss.ws.client.ws.RetrieveMetrics4SelectedProjectResponse;

class WSMetricAccessorImpl extends WSMetricAccessor {

    private static final String METHOD_NAME_RETRIEVE_METRICS_4_SELECTED_PROJECT  = "retrieveMetrics4SelectedProject";
    
    private static final String METHOD_NAME_RETRIEVE_METRICS_4_SELECTED_FILES    = "retrieveMetrics4SelectedFiles";
    
    private static final String METHOD_NAME_GET_PROJECT_FILE_METRIC_MEASUREMENT     = "getFileMetricMeasurement";
    
    private static final String METHOD_NAME_GET_PROJECT_VERSION_METRIC_MEASUREMENT  = "getVersionMetricMeasurement";
    
    private Map<String, Object> parameters;
    private String userName;
    private String password;
    private WsStub wsStub;
    
    public WSMetricAccessorImpl(String userName, String password, String webServiceUrl) throws WSException {
        this.userName = userName;
        this.password = password;
        parameters = new Hashtable<String, Object>();
        try {
            this.wsStub = new WsStub(webServiceUrl);
        } catch (AxisFault af) {
            throw new WSException(af);
        }
    }
    
    /**
     * @see eu.sqooss.scl.accessor.WSMetricAccessor#retrieveMetrics4SelectedProject(long)
     */
    @Override
    public WSMetric[] retrieveMetrics4SelectedProject(long projectId) throws WSException {
        RetrieveMetrics4SelectedProjectResponse response;
        RetrieveMetrics4SelectedProject params;
        if (!parameters.containsKey(METHOD_NAME_RETRIEVE_METRICS_4_SELECTED_PROJECT)) {
            params = new RetrieveMetrics4SelectedProject();
            params.setPassword(password);
            params.setUserName(userName);
            parameters.put(METHOD_NAME_RETRIEVE_METRICS_4_SELECTED_PROJECT, params);
        } else {
            params = (RetrieveMetrics4SelectedProject) parameters.get(
                    METHOD_NAME_RETRIEVE_METRICS_4_SELECTED_PROJECT);
        }
        synchronized (params) {
            params.setProjectId(projectId);
            try {
                response = wsStub.retrieveMetrics4SelectedProject(params);
            } catch (RemoteException re) {
                throw new WSException(re);
            }
        }
        return (WSMetric[]) parseWSResult(response.get_return());
    }
    
    /**
     * @see eu.sqooss.scl.accessor.WSMetricAccessor#retrieveMetrics4SelectedFiles(long, java.lang.String, java.lang.String)
     */
    @Override
    public WSMetric[] retrieveMetrics4SelectedFiles(long projectId, String folderNames,
            String fileNames) throws WSException {
        String delimiter = ",";
        
        StringTokenizer folderNamesTokenizer = new StringTokenizer(folderNames, delimiter);
        int folderNamesNumber = folderNamesTokenizer.countTokens();
        String[] folderNamesArray;
        if (folderNamesNumber == 0) {
            folderNamesArray = new String[1];
            folderNamesArray[0] = null;
        } else {
            folderNamesArray = new String[folderNamesNumber];
            for (int i = 0; i < folderNamesArray.length; i++) {
                folderNamesArray[i] = folderNamesTokenizer.nextToken().trim();
            }
        }
        
        StringTokenizer fileNamesTokenizer = new StringTokenizer(fileNames, delimiter);
        int fileNamesNumber = fileNamesTokenizer.countTokens();
        String[] fileNamesArray;
        if (fileNamesNumber == 0) {
            fileNamesArray = new String[1];
            fileNamesArray[0] = null;
        } else {
            fileNamesArray = new String[fileNamesNumber];
            for (int i = 0; i < fileNamesArray.length; i++) {
                fileNamesArray[i] = fileNamesTokenizer.nextToken().trim();
            }
        }
        
        RetrieveMetrics4SelectedFilesResponse response;
        RetrieveMetrics4SelectedFiles params;
        if (!parameters.containsKey(METHOD_NAME_RETRIEVE_METRICS_4_SELECTED_FILES)) {
            params = new RetrieveMetrics4SelectedFiles();
            params.setPassword(password);
            params.setUserName(userName);
            parameters.put(METHOD_NAME_RETRIEVE_METRICS_4_SELECTED_FILES, params);
        } else {
            params = (RetrieveMetrics4SelectedFiles) parameters.get(
                    METHOD_NAME_RETRIEVE_METRICS_4_SELECTED_FILES);
        }
        synchronized (params) {
            params.setProjectId(projectId);
            params.setFolders(folderNamesArray);
            params.setFileNames(fileNamesArray);
            try {
                response = wsStub.retrieveMetrics4SelectedFiles(params);
            } catch (RemoteException re) {
                throw new WSException(re);
            }
        }
        return (WSMetric[]) parseWSResult(response.get_return());
    }

    /**
     * @see eu.sqooss.scl.accessor.WSMetricAccessor#getProjectFileMetricMeasurement(long, long)
     */
    @Override
    public WSMetricMeasurement[] getProjectFileMetricMeasurement(
            long metricId, long projectFileId) throws WSException {
        GetProjectFileMetricMeasurement params;
        GetProjectFileMetricMeasurementResponse response;
        if (!parameters.containsKey(METHOD_NAME_GET_PROJECT_FILE_METRIC_MEASUREMENT)) {
            params = new GetProjectFileMetricMeasurement();
            params.setPassword(password);
            params.setUserName(userName);
            parameters.put(METHOD_NAME_GET_PROJECT_FILE_METRIC_MEASUREMENT, params);
        } else {
            params = (GetProjectFileMetricMeasurement) parameters.get(
                    METHOD_NAME_GET_PROJECT_FILE_METRIC_MEASUREMENT);
        }
        synchronized (params) {
            params.setMetricId(metricId);
            params.setProjectFileId(projectFileId);
            try {
                response = wsStub.getProjectFileMetricMeasurement(params);
            } catch (RemoteException re) {
                throw new WSException(re);
            }
        }
        return (WSMetricMeasurement[]) parseWSResult(response.get_return());
    }
    
    /**
     * @see eu.sqooss.scl.accessor.WSMetricAccessor#getProjectVersionMetricMeasurement(long, long)
     */
    @Override
    public WSMetricMeasurement[] getProjectVersionMetricMeasurement(
            long metricId, long projectVersionId) throws WSException {
        GetProjectVersionMetricMeasurement params;
        GetProjectVersionMetricMeasurementResponse response;
        if (!parameters.containsKey(METHOD_NAME_GET_PROJECT_VERSION_METRIC_MEASUREMENT)) {
            params = new GetProjectVersionMetricMeasurement();
            params.setPassword(password);
            params.setUserName(userName);
            parameters.put(METHOD_NAME_GET_PROJECT_VERSION_METRIC_MEASUREMENT, params);
        } else {
            params = (GetProjectVersionMetricMeasurement) parameters.get(
                    METHOD_NAME_GET_PROJECT_VERSION_METRIC_MEASUREMENT);
        }
        synchronized (params) {
            params.setMetricId(metricId);
            params.setProjectVersionId(projectVersionId);
            try {
                response = wsStub.getProjectVersionMetricMeasurement(params);
            } catch (RemoteException re) {
                throw new WSException(re);
            }
        }
        return (WSMetricMeasurement[]) parseWSResult(response.get_return());
    }
    
}

//vi: ai nosi sw=4 ts=4 expandtab
