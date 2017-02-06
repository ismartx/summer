package org.smartx.summer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by binglin on 2016/11/4.
 */
@RestController
@RequestMapping("files")
public class FileController {
    private final static Logger logger = LoggerFactory.getLogger(FileController.class);

    @GetMapping(value = "/jooq")
    public ResponseEntity downFile(HttpServletResponse response, HttpServletRequest request) {
        InputStream inputStream = null;
        ServletOutputStream out = null;
        try {

            File file = null;
            ClassPathResource classPathResource = new ClassPathResource("jOOQ-manual-3.8.pdf");
            if (classPathResource.exists()) {
                file = classPathResource.getFile();
            }

            assert file != null;
            Long fSize = file.length();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/x-download");
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Length", fSize.toString());
            response.setHeader("Content-Disposition", "attachment;fileName=" + file.getName());
            inputStream = new FileInputStream(file);
            long pos = 0;
            if (null != request.getHeader("Range")) {
                // 断点续传
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                try {
                    pos = Long.parseLong(request.getHeader("Range").replaceAll("bytes=", "").replaceAll("-", ""));
                } catch (NumberFormatException e) {
                    pos = 0;
                }
            }
            out = response.getOutputStream();
            String contentRange = "bytes " + pos + "" + "-" + (fSize - 1) + "" + "/" + fSize + "";
            response.setHeader("Content-Range", contentRange);
            long skip = inputStream.skip(pos);
            byte[] buffer = new byte[1024 * 10];
            int length = 0;
            while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, length);
                Thread.sleep(200);
            }
        } catch (Exception e) {
            logger.error("下载异常：" + e);
        } finally {
            try {
                if (null != out) out.flush();
                if (null != out) out.close();
                if (null != inputStream) inputStream.close();
            } catch (IOException e) {
                logger.error("下载异常：" + e);
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}

