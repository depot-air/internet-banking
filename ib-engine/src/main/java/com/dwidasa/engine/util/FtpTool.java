package com.dwidasa.engine.util;

import com.dwidasa.engine.model.Ftp;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

/**
 * Simple class used as tool to connect and disconnect to ftp.
 *
 * @author prayugo
 */
public final class FtpTool {
    private static Logger logger = Logger.getLogger(FtpTool.class);

    /**
     * Connect to ftp server and do some preparations
     * 1. login and set local passive mode
     * 2. set local and remote directory
     * 3. set file transfer mode
     *
     * @param f   ftp information model
     * @param ftp instance of ftp client session
     * @return true if all tasks performed successfully and vice versa
     */
    public static boolean connect(Ftp f, FTPClient ftp) {

        try {
            ftp.connect(f.getServerAddress(), f.getServerPort());
            int rpy = ftp.getReplyCode();

            if (FTPReply.isPositiveCompletion(rpy)) {

                if (ftp.login(f.getUsername(), f.getPassword())) {

                    // -- use passive mode as default because most of us are
                    // -- behind firewalls these days.
                    ftp.enterLocalPassiveMode();
                    ftp.changeWorkingDirectory(f.getRemoteFolder());

                    if ("A".equals(f.getTransferMode())) {                            // -- ASCII file
                        ftp.setFileTransferMode(FTPClient.ASCII_FILE_TYPE);
                    } else {                                                                // -- BINARY file
                        ftp.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
                    }

                    return true;
                }
            }

            disconnect(ftp);
        } catch (Exception e) {

            logger.error("error when connecting to ftp server, msg : " + e.getMessage(), e);

            if (ftp.isConnected()) {
                disconnect(ftp);
            }
        }

        return false;
    }

    /**
     * Close the connection to ftp server. it automatically logout from current session
     *
     * @param ftp instance of ftp client session
     */
    public static void disconnect(FTPClient ftp) {
        try {
            ftp.disconnect();
        } catch (Exception e) {
        }
    }

    /**
     * Get all available files on remote directory
     *
     * @param ftp instance of ftp client session
     * @return all available files on remote directory
     */
    public static FTPFile[] listFiles(FTPClient ftp) {
        try {
            return ftp.listFiles();
        } catch (Exception e) {
            logger.error("error when getting directory file list, msg : " + e.getMessage(), e);
        }

        return null;
    }
}
