package org.n3r.acc.process.input;

import org.n3r.config.Configable;
import org.n3r.core.tag.EcRocTag;
import org.n3r.core.tag.FromSpecConfig;
import org.n3r.net.ftp.client.FtpDownloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

@EcRocTag("Ftp")
public class FtpInput implements DataInput, FromSpecConfig<FtpInput> {
    protected String ftpHost;
    protected int ftpPort = 21;
    protected String ftpPass;
    protected String ftpUser;
    protected String ftpRemote;
    protected File ftpLocal;

    @Override
    public InputStream getInputStream() {
        boolean ok = new FtpDownloader().connect(ftpHost, ftpPort).login(ftpUser, ftpPass)
                .remote(ftpRemote).local(ftpLocal).download();

        if (!ok) throw new RuntimeException("DownFile Error");

        return toInputStream();
    }

    protected FileInputStream toInputStream() {
        try {
            return new FileInputStream(ftpLocal);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FtpInput fromSpec(Configable config, Map<String, String> context) {
        ftpHost = config.getStr("ftp.host");
        ftpUser = config.getStr("ftp.user");
        ftpPort = config.getInt("ftp.port", 21);
        ftpPass = config.getStr("ftp.pass");
        ftpRemote = config.getStr("ftp.remote");
        ftpLocal = new File(config.getStr("ftp.local"));

        return this;
    }
}
