package cn.codepod.tool.util;

import cn.codepod.tool.entity.MarkdownImage;
import cn.codepod.tool.entity.SettingConfig;
import cn.codepod.tool.service.SettingStateService;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author zhanglei
 * @date 2022/4/3 13:05
 */
public class OssUtil {

    private static SettingConfig config;

    public static OSS getOssClient() {
        config = SettingStateService.getInstance().getState();
        return new OSSClientBuilder()
                .build(config.getEndpoint(), config.getAccessKeyId(), config.getAccessKeySecret());
    }

    public static String upload(@NotNull File file, @Nullable String markdownFileName) {

        OSS ossClient = getOssClient();

        String fileName = file.getName();

        String filenameAfterFormat = formatFilename(fileName, markdownFileName);

        String uploadPath = filenameAfterFormat;

        if (StringUtil.startsWithChar(uploadPath, '/')) {
            uploadPath = uploadPath.substring(1);
        }

        ossClient.putObject(config.getBucketName(), uploadPath, file);
        MarkdownImage markdownImage = MarkdownImage.of(fileName, generateUrl(filenameAfterFormat));
        return markdownImage.getFull();
    }

    public static String upload(@NotNull InputStream input, @Nullable String markdownFileName) {

        OSS ossClient = getOssClient();

        String fileName = "UNKNOWN" + DateTimeFormatter.ofPattern("_HH_mm_ss").format(LocalDateTime.now()) + ".png";

        String filenameAfterFormat = formatFilename(fileName, markdownFileName);

        String uploadPath = filenameAfterFormat;

        if (StringUtil.startsWithChar(uploadPath, '/')) {
            uploadPath = uploadPath.substring(1);
        }
        ossClient.putObject(config.getBucketName(), uploadPath, input);
        MarkdownImage markdownImage = MarkdownImage.of(fileName, generateUrl(filenameAfterFormat));
        return markdownImage.getFull();
    }

    public static void delete(MarkdownImage image) {
        OSS ossClient = getOssClient();

        String key = image.getUrl();

        ossClient.deleteObject(config.getBucketName(), getOssKey(key));
    }

    public static String getOssKey(String key) {
        config = SettingStateService.getInstance().getState();
        key = key.replace(getPrefix(), "");
        key = key.replace(getSuffix(), "");
        if (StringUtil.startsWithChar(key, '/')) {
            key = key.substring(1);
        }
        return key;
    }


    private static String formatFilename(String fileName, @Nullable String markdownFileName) {
        if (null == markdownFileName) {
            markdownFileName = "";
        }
        String filenameFormat = config.getFilenameFormat();
        filenameFormat = filenameFormat.replace("{DATE}", DateTimeFormatter.ofPattern("yyyy_MM_dd").format(LocalDate.now()));
        filenameFormat = filenameFormat.replace("{MD_NAME}", markdownFileName);
        filenameFormat = filenameFormat.replace("{PIC_NAME}", fileName);
        filenameFormat = filenameFormat.replace("{UUID}", UUID.randomUUID().toString());
        if (!StringUtil.startsWithChar(filenameFormat, '/')) {
            filenameFormat = "/" + filenameFormat;
        }
        return filenameFormat;
    }

    private static String generateUrl(String filenameAfterFormat) {
        StringBuilder url = new StringBuilder();
        url.append(getPrefix());
        if (!StringUtil.startsWithChar(filenameAfterFormat, '/')) {
            url.append("/");
        }
        url.append(filenameAfterFormat);
        url.append(getSuffix());
        return url.toString();
    }

    private static String getPrefix() {
        StringBuilder url = new StringBuilder("https://");
        String endpoint = config.getEndpoint();
        String bucketName = config.getBucketName();
        url.append(bucketName).append('.').append(endpoint);
        return url.toString();
    }

    private static String getSuffix() {
        String url = "";
        String styleSuffix = config.getStyleSuffix();
        if (StringUtil.isNotEmpty(styleSuffix)) {
            url += styleSuffix;
        }
        return url;
    }

    public static String rename(String ossKey, String newName) {
        OSS ossClient = getOssClient();
        int index = ossKey.lastIndexOf('/');
        if (index != -1) {
            newName = ossKey.substring(0, index + 1) + newName;
        }
        ossClient.copyObject(config.getBucketName(), ossKey, config.getBucketName(), newName);
        return generateUrl(newName);
    }
}
