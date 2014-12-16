
package com.duowan.asynmultipledownload;

import com.duowan.download.IConfig;
import com.duowan.download.IOperator;

public class DownloadService extends BaseDownloadService {

    @Override
    protected IOperator createOperator() {
        return new DownloadInfoSaver();
    }

    @Override
    protected IConfig createConfig() {
        return new DownloadConfig();
    }

}
