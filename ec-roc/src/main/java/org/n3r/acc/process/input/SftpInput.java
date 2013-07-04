package org.n3r.acc.process.input;

import org.n3r.config.Configable;
import org.n3r.core.tag.EcRocTag;
import org.n3r.net.sftp.client.SftpDownloader;

import java.io.InputStream;
import java.util.Map;

@EcRocTag("Sftp")
public class SftpInput extends FtpInput {
    public InputStream getInputStream() {
        boolean ok = new SftpDownloader().connect(ftpHost, ftpPort).login(ftpUser, ftpPass)
                .remote(ftpRemote).local(ftpLocal).download();

        if (!ok) throw new RuntimeException("DownFile Error");

        return toInputStream();
    }

    @Override
    public SftpInput fromSpec(Configable config, Map<String, String> context) {
        super.fromSpec(config, context);
        ftpPort = config.getInt("ftp.port", 22);

        return this;
    }
}
