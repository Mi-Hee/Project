package org.choongang.file.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.file.entities.FileInfo;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
@RequiredArgsConstructor
public class FileDownloadService {
    private final FileInfoService infoService;
    private final HttpServletResponse response;

    public void download(Long seq) {
        FileInfo data = infoService.get(seq);
        String fileName = data.getFileName();
        String filePath = data.getFilePath();

        // 파일명 -> 2바이트 인코딩으로 변경 (윈도우즈 시스템에서 한글 깨짐 방지)
        
        try (FileInputStream fis = new FileInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            OutputStream out = response.getOutputStream(); // 응답 Body에 출력

            response.setHeader("Content-Disposition", "Attachment; filename =" + fileName);

            while (bis.available() > 0) {
                out.write(bis.read());
            }

            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
