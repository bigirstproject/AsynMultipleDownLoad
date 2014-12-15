
package com.duowan.asynmultipledownload;

import com.kugou.download.IConfig;
import com.kugou.download.IOperator;

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
