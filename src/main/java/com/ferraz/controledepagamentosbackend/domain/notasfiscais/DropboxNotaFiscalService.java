package com.ferraz.controledepagamentosbackend.domain.notasfiscais;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.oauth.DbxCredential;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.ferraz.controledepagamentosbackend.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

@Service
public class DropboxNotaFiscalService {

    @Value("${dropbox_token}")
    private String accessToken;
    @Value("${dropbox_refresh_token}")
    private String refreshToken;
    @Value("${dropbox_app_key}")
    private String appKey;
    @Value("${dropbox_app_secret}")
    private String appSecret;

    public String upload(MultipartFile multipartFile, User user) throws DbxException, IOException {
        DbxClientV2 client = buildClient();

        String folder = "/%s/%s".formatted(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
        String fileName = getAvailableFileName(client, folder, user);
        String path = "%s/%s".formatted(folder, fileName);

        try (InputStream in = new ByteArrayInputStream(multipartFile.getBytes())) {
            FileMetadata fileMetadata = client.files().uploadBuilder(path).uploadAndFinish(in);
            return fileMetadata.getPathDisplay();
        }
    }

    public InputStream download(NotaFiscal notaFiscal) throws DbxException {
        return buildClient().files().download(notaFiscal.getFilePath()).getInputStream();
    }

    private DbxClientV2 buildClient() throws DbxException {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("controle-de-pagamentos").build();
        DbxCredential credential = new DbxCredential(
                accessToken,
                14400l,
                refreshToken,
                appKey,
                appSecret);
        DbxClientV2 dbxClientV2 = new DbxClientV2(config, credential);

        if (credential.getRefreshToken() != null && credential.aboutToExpire()) {
            dbxClientV2.refreshAccessToken();
        }

        return dbxClientV2;
    }

    private String getAvailableFileName(DbxClientV2 client, String folder, User user) throws DbxException {
        String userName = user.getNome().replace(" ", "_");
        String fileName = "NF_%s_%s_%s.pdf".formatted(userName, LocalDate.now().getMonthValue(), LocalDate.now().getYear());

        ListFolderResult result = client.files().listFolder(folder);
        int contador = 2;
        boolean nomeArquivoDisponivel = false;
        while (!nomeArquivoDisponivel) {
            nomeArquivoDisponivel = true;

            for (Metadata metadata : result.getEntries()) {
                if(metadata.getName().equals(fileName)) {
                    nomeArquivoDisponivel = false;
                    fileName = "NF_%s_%s_%s_%s.pdf".formatted(userName, LocalDate.now().getMonthValue(), LocalDate.now().getYear(), contador++);
                }
            }

            result = client.files().listFolder(folder);
        }
        return fileName;
    }

}
