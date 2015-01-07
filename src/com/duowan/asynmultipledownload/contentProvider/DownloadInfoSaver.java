
package com.duowan.asynmultipledownload.contentProvider;

import java.util.List;

import com.duowan.asynmultipledownload.operator.DownloadOperator;
import com.duowan.download.DefaultOperator;
import com.duowan.download.DownloadFile;

/**
 * ������Ϣ������
 * 
 */
public class DownloadInfoSaver extends DefaultOperator {

    /**
     * �����ļ���Ϣ
     * 
     * @param file �ļ�
     * @return ���������ļ�ID
     */
    public long insertFile(DownloadFile file) {
        return DownloadOperator.getInstance().insert(file);
    }

    /**
     * �����ļ���Ϣ
     * 
     * @param file �ļ�
     * @return �Ƿ���³ɹ�
     */
    public boolean updateFile(DownloadFile file) {
        return DownloadOperator.getInstance().update(file, file.getKey()) > 0;
    }

    /**
     * ɾ���ļ���Ϣ
     * 
     * @param id �ļ�ID
     * @return �Ƿ�ɾ���ɹ�
     */
    public boolean deleteFile(long id) {
        return DownloadOperator.getInstance().delete(id) > 0;
    }

    /**
     * ��ѯ�ļ���Ϣ
     * 
     * @param id �ļ�ID
     * @return �����ļ���Ϣ
     */
    public DownloadFile queryFile(long id) {
        return DownloadOperator.getInstance().query(id);
    }

    /**
     * ��ѯ�ļ���Ϣ
     * 
     * @param selection ��ѯ����
     * @param selectionArgs ��ѯ������ֵ
     * @return �����ļ���Ϣ����
     */
    public List<DownloadFile> queryFile(String selection, String[] selectionArgs) {
        return DownloadOperator.getInstance().query(selection, selectionArgs, null);
    }

    /**
     * ��ѯ�ļ���Ϣ
     * 
     * @param selection ��ѯ����
     * @param selectionArgs ��ѯ������ֵ
     * @param orderby ��������
     * @return �����ļ���Ϣ����
     */
    public List<DownloadFile> queryFile(String selection, String[] selectionArgs, String orderby) {
        return DownloadOperator.getInstance().query(selection, selectionArgs, orderby);
    }

    /**
     * ��ѯ����
     * 
     * @param selection ��ѯ����
     * @param selectionArgs ��ѯ������ֵ
     * @return ��������
     */
    public int getCount(String selection, String[] selectionArgs) {
        return DownloadOperator.getInstance().getCount(selection, selectionArgs);
    }
}
