
package com.duowan.asynmultipledownload.contentProvider;

import java.util.List;

import com.duowan.asynmultipledownload.operator.DownloadOperator;
import com.duowan.download.DefaultOperator;
import com.duowan.download.DownloadFile;

/**
 * 下载信息保存类
 * 
 */
public class DownloadInfoSaver extends DefaultOperator {

    /**
     * 新增文件信息
     * 
     * @param file 文件
     * @return 返回新增文件ID
     */
    public long insertFile(DownloadFile file) {
        return DownloadOperator.getInstance().insert(file);
    }

    /**
     * 更新文件信息
     * 
     * @param file 文件
     * @return 是否更新成功
     */
    public boolean updateFile(DownloadFile file) {
        return DownloadOperator.getInstance().update(file, file.getKey()) > 0;
    }

    /**
     * 删除文件信息
     * 
     * @param id 文件ID
     * @return 是否删除成功
     */
    public boolean deleteFile(long id) {
        return DownloadOperator.getInstance().delete(id) > 0;
    }

    /**
     * 查询文件信息
     * 
     * @param id 文件ID
     * @return 返回文件信息
     */
    public DownloadFile queryFile(long id) {
        return DownloadOperator.getInstance().query(id);
    }

    /**
     * 查询文件信息
     * 
     * @param selection 查询条件
     * @param selectionArgs 查询条件赋值
     * @return 返回文件信息集合
     */
    public List<DownloadFile> queryFile(String selection, String[] selectionArgs) {
        return DownloadOperator.getInstance().query(selection, selectionArgs, null);
    }

    /**
     * 查询文件信息
     * 
     * @param selection 查询条件
     * @param selectionArgs 查询条件赋值
     * @param orderby 排序条件
     * @return 返回文件信息集合
     */
    public List<DownloadFile> queryFile(String selection, String[] selectionArgs, String orderby) {
        return DownloadOperator.getInstance().query(selection, selectionArgs, orderby);
    }

    /**
     * 查询数量
     * 
     * @param selection 查询条件
     * @param selectionArgs 查询条件赋值
     * @return 返回数量
     */
    public int getCount(String selection, String[] selectionArgs) {
        return DownloadOperator.getInstance().getCount(selection, selectionArgs);
    }
}
