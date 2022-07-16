package org.keedio.flume.source.ftp.client.filters;

import com.jcraft.jsch.ChannelSftp;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.keedio.flume.source.ftp.client.KeedioSource;
import org.keedio.flume.source.ftp.client.sources.SFTPSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by luislazaro on 21/8/17.
 * lalazaro@keedio.com
 * Keedio
 */
public class KeedioFileFilter implements FTPFileFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeedioFileFilter.class);
    private String strMatch = "";

    public KeedioFileFilter(String strMatch) {
        this.strMatch = strMatch;
    }

    @Override
    public boolean accept(FTPFile ftpFile) {
        String match = ".*" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".*.txt";
        if (ftpFile.isDirectory() || (ftpFile.isFile() && ftpFile.getName().matches(match))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param sftpFile
     * @return
     */
    public boolean accept(ChannelSftp.LsEntry sftpFile) {
        if (("").equals(strMatch)) return true;
        if (strMatch == null) return true;
        return (
                sftpFile.getAttrs().isDir() ||
                        (isFile(sftpFile)) && (sftpFile.getFilename().matches(strMatch)));
    }

    /**
     * There is no attribute to check isfile in SftpATTRS for JSCH.
     * Auxiliar function for checking if a ChannelSftp.LsEntry is a file.
     *
     * @param file
     * @return
     */
    public boolean isFile(ChannelSftp.LsEntry file) {
        boolean isfile = false;
        if ((!file.getAttrs().isDir()) && (!file.getAttrs().isLink())) {
            isfile = true;
        } else {
            isfile = false;
        }
        return isfile;
    }

}
