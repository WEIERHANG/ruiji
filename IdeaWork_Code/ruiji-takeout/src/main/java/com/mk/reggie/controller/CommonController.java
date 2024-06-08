package com.mk.reggie.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mk.reggie.common.R;
import com.mk.reggie.common.UploadGiteeImgBed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequestMapping("/common")
@RestController
public class CommonController{
    @Value("${reggie.path}")
    private String basePath;
    /**
     * 文件上传
     * @param
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(@RequestParam("file") MultipartFile multipartFile) {
        try {
            // 获取原始文件名
            String originalFilename = multipartFile.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName=UUID.randomUUID().toString()+suffix;
            String targetURL = UploadGiteeImgBed.createUploadFileUrl(fileName);
            log.info("目标url：" + targetURL);
            Map<String, Object> uploadBodyMap = UploadGiteeImgBed.getUploadBodyMap(multipartFile.getBytes());
            String JSONResult = HttpUtil.post(targetURL, uploadBodyMap);
            JSONObject jsonObj = JSONUtil.parseObj(JSONResult);
            // 请求成功：返回生成的文件名
            return R.success(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return R.error("文件上传失败"); // 捕获异常并返回失败信息
        }
    }


    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void downLoad(String name, HttpServletResponse response) {
        String imageUrl = "https://gitee.com/lucky_h/img/raw/master/" + name;

        HttpURLConnection connection = null;
        InputStream inputStream = null;
        ServletOutputStream outputStream = null;

        try {
            // 创建URL对象
            URL url = new URL(imageUrl);
            // 打开连接
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            // 检查响应代码
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.out.println("Failed to fetch image. HTTP response code: " + responseCode);
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // 获取输入流
            inputStream = connection.getInputStream();

            // 输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            outputStream = response.getOutputStream();

            // 设置响应的格式
            response.setContentType("image/jpeg");

            int len;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            // 关闭资源
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                if (connection != null) connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
