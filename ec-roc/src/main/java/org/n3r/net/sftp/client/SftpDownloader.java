package org.n3r.net.sftp.client;

import com.jcraft.jsch.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Vector;

public class SftpDownloader {
    private int port = 22;
    private String host;
    private String user;
    private String pass;
    private String remoteFile;
    private File localFile;

    public SftpDownloader remote(String remoteFile) {
        this.remoteFile = remoteFile;
        return this;
    }

    public SftpDownloader local(File localFile) {
        this.localFile = localFile;
        return this;
    }

    public SftpDownloader connect(String host) {
        this.host = host;
        return this;
    }

    public SftpDownloader login(String user, String pass) {
        this.user = user;
        this.pass = pass;
        return this;
    }

    public SftpDownloader connect(String host, int port) {
        this.host = host;
        this.port = port;
        return this;
    }

    public boolean download() {
        Session session = null;
        Channel channel = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            session.setPassword(pass);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp c = (ChannelSftp) channel;
            if (!remoteFileExists(c)) return false;

            c.get(remoteFile, localFile.getAbsolutePath());
            return true;
        } catch (JSchException e) {
            throw new RuntimeException(e);
        } catch (SftpException e) {
            throw new RuntimeException(e);
        } finally {
            if (channel != null) channel.disconnect();
            if (session != null) session.disconnect();
        }
    }

    private boolean remoteFileExists(ChannelSftp channelSftp) throws SftpException {
        String path = FilenameUtils.getFullPath(remoteFile);
        path = StringUtils.isEmpty(path) ? "." : path;
        String name = FilenameUtils.getName(remoteFile);
        Vector<ChannelSftp.LsEntry> remoteFiles = channelSftp.ls(path);
        for (ChannelSftp.LsEntry entry : remoteFiles)
            if (entry.getFilename().equals(name)) return true;

        return false;
    }
}
